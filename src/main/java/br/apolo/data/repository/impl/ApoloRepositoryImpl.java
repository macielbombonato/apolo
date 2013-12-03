package br.apolo.data.repository.impl;


import br.apolo.data.model.BaseEntity;
import br.apolo.data.repository.ApoloRepository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;

import java.io.Serializable;
import java.util.List;

/**
 * Basic implementation of the {@link ApoloRepository} class.
 *
 * @param <T> Generic Entity (inherits from {@link BaseEntity}) associated with this Data Access Object Class.
 */
@NoRepositoryBean
public class ApoloRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements ApoloRepository<T, ID> {

    private EntityManager entityManager;
    private Class<T> domainClass;

    public ApoloRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);

        // This is the recommended method for accessing inherited class dependencies.
        this.domainClass = domainClass;
        this.entityManager = entityManager;
    }

    @SuppressWarnings("rawtypes")
	@Override
    public List<T> search(String param, List<SingularAttribute<? extends BaseEntity, String>> fields) {
        CriteriaQuery<T> query = createBasicCriteriaForSearch(param, fields, domainClass, null, null);

        return entityManager.createQuery(query).getResultList();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<T> search(String param, List<SingularAttribute<? extends BaseEntity, String>> fields, Integer pageSize, Integer pageIndex, String sortField, Sort.Direction sortDirection) {
        CriteriaQuery<T> query = createBasicCriteriaForSearch(param, fields, domainClass, sortField, sortDirection);

        TypedQuery<T> result = entityManager.createQuery(query);

        // add paging in case there are elements
        if (pageSize != null) {
            result.setMaxResults(pageSize);
        }
        if (pageIndex != null) {
            result.setFirstResult(pageIndex);
        }

        return result.getResultList();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public int count(String param, List<SingularAttribute<? extends BaseEntity, String>> fields) {
        // create criteria for count
        CriteriaQuery<Long> query = createBasicCriteriaForCount(param, fields, domainClass, null, null);
        // do count
        return entityManager.createQuery(query).getSingleResult().intValue();
    }

    /**
     * Create a basic search query.
     *
     * @param param         Parameter to be filtered in the search.
     * @param fields        Fields which will be filtered by the entered parameter.
     * @param classType     Entity Class Type.
     * @param sortField     Sort field.
     * @param sortDirection Sort Direction.
     * @return The basic search query.
     */
    @SuppressWarnings("rawtypes")
    protected CriteriaQuery<T> createBasicCriteriaForSearch(String param, List<SingularAttribute<? extends BaseEntity, String>> fields,
                                                            Class<T> classType, String sortField, Sort.Direction sortDirection) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(classType);
        Root<T> root = query.from(classType);

        concatCriteria(param, fields, sortField, sortDirection, criteriaBuilder, query, root);

        return query;
    }

    /**
     * Create the criteria for searching, retrieving the count select.
     *
     * @param param         Parameter to be filtered in the search.
     * @param fields        Fields which will be filtered by the entered parameter.
     * @param classType     Entity Class Type.
     * @param sortField     Sort field.
     * @param sortDirection Sort Direction.
     * @return Criteria holding count select query for a basic search.
     */
    @SuppressWarnings("rawtypes")
    protected CriteriaQuery<Long> createBasicCriteriaForCount(String param, List<SingularAttribute<? extends BaseEntity, String>> fields,
                                                              Class<T> classType, String sortField, Sort.Direction sortDirection) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<T> root = query.from(classType);
        CriteriaQuery<Long> select = query.select(criteriaBuilder.count(root));

        concatCriteria(param, fields, sortField, sortDirection, criteriaBuilder, select, root);

        return query;
    }

    /**
     * Add to the query the filtering elements.
     *
     * @param param           Parameter which will be used as the filter for the query.
     * @param fields          Fields which will be filtered.
     * @param sortField       Sort field.
     * @param sortDirection   Sort direction.
     * @param criteriaBuilder Criteria Builder.
     * @param query           query which the filtered will be concatenated on.
     * @param root            Root element of the query.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected <X> void concatCriteria(String param, List<SingularAttribute<? extends BaseEntity, String>> fields, String sortField,
                                      Sort.Direction sortDirection, CriteriaBuilder criteriaBuilder, CriteriaQuery<X> query, Root<T> root) {

        if (StringUtils.isNotEmpty(param)) {
            param = "%" + param.toUpperCase() + "%";

            if (fields != null && !fields.isEmpty()) {
                Predicate[] criteriaLikes = new Predicate[fields.size()];

                for (int i = 0; i < fields.size(); i++) {
                    criteriaLikes[i] = criteriaBuilder
                            .like(criteriaBuilder.upper(root.get((SingularAttribute<T, String>) fields.get(i))), param);
                }

                query.where(criteriaBuilder.or(criteriaLikes));
            }
        }

        concatOrderBy(sortField, sortDirection, criteriaBuilder, query, root);
    }

    /**
     * Add to the query the order by elements.
     *
     * @param sortField       The field to sort by.
     * @param sortDirection   ASC or DESC direction.
     * @param criteriaBuilder The current {@link CriteriaBuilder} instance.
     * @param query           The current {@link CriteriaQuery} instance.
     * @param from            The from path.
     */
    protected <X> void concatOrderBy(String sortField, Sort.Direction sortDirection, CriteriaBuilder criteriaBuilder, CriteriaQuery<X> query,
                                     Path<T> from) {
        // add order by, in case there is any
        if (sortField != null) {
            Path<Object> sortPath = null;

            // handles complex sort field with lazy relationship (example:
            // usergroup.name).
            if (sortField.contains(".")) {
                String[] sortFieldPieces = sortField.split("\\.");
                if (sortFieldPieces.length > 0) {
                    sortPath = from.get(sortFieldPieces[0]);
                    for (int i = 1; i < sortFieldPieces.length; i++) {
                        sortPath = sortPath.get(sortFieldPieces[i]);
                    }
                }
            } else {
                sortPath = from.get(sortField);
            }

            if (sortDirection == null) {
                sortDirection = Sort.Direction.ASC;
            }
            switch (sortDirection) {
                case ASC:
                    query.orderBy(criteriaBuilder.asc(sortPath));
                    break;
                case DESC:
                    query.orderBy(criteriaBuilder.desc(sortPath));
                    break;
            }

        }
    }

    @SuppressWarnings("unchecked")
	@Override
    public <B> List<B> executeQuery(Class<B> clazz, String query, Object... params) {
        // Check query parameters.
        int queryParams = StringUtils.countMatches(query, "?");
        if (queryParams > 0) {
            Assert.isTrue(params != null && queryParams == params.length,
                    "The number of parameters on query has a different length from parameters passed to method.");
        }

        Query jpaQuery = entityManager.createNativeQuery(query);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                jpaQuery.setParameter((i + 1), params[i]);
            }
        }

        // Holds the result before close the entity manager.
        List<B> result = jpaQuery.getResultList();

        // Close the entity manager to avoid keep multiple database connections.
        entityManager.close();

        return result;
    }

    @Override
    public <B> B attach(B entity) {
        Assert.notNull(entity);
        return entityManager.merge(entity);
    }
}
