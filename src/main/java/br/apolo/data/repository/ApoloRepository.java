package br.apolo.data.repository;

import br.apolo.data.model.BaseEntity;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.metamodel.SingularAttribute;

import java.io.Serializable;
import java.util.List;

/**
 * Provides extended functionality for {@link JpaRepository}.
 * User: rmberne@gmail.com
 * Date: 27/11/13
 */
@NoRepositoryBean
public interface ApoloRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    /**
     * Execute basic search (no paging).
     *
     * @param param  Search Parameter which will be used as a filter to the query.
     * @param fields Fields where the search will be performed.
     * @return List of entities resulted from the executed search.
     */
    @SuppressWarnings("rawtypes")
	List<T> search(String param, List<SingularAttribute<? extends BaseEntity, String>> fields);

    /**
     * Execute basic search with paging and sorting.
     *
     * @param param         Search Parameter which will be used as a filter to the query.
     * @param fields        Fields where the search will be performed.
     * @param pageSize      Number of records to be retrieved.
     * @param pageIndex     The record index which will be the first to be fetched.
     * @param sortField     Name of the field which the query will be sorted. In case this element is <code>null</code>, no sorting is performed.
     * @param sortDirection The sort direction of the sortField parameter. In case <code>null</code> is set, the Ascending sort will be executed.
     * @return List of entities resulted from the executed search.
     */
    @SuppressWarnings("rawtypes")
	List<T> search(String param, List<SingularAttribute<? extends BaseEntity, String>> fields, Integer pageSize, Integer pageIndex,
                   String sortField, Sort.Direction sortDirection);

    /**
     * Count the number of records which would be retrieved by a basic search using the entered parameters and fields.
     *
     * @param param  Search Parameter which will be used as a filter to the query.
     * @param fields Fields where the search will be performed.
     * @return Number of records which would be retrieved by a basic search using filters.
     */
    @SuppressWarnings("rawtypes")
	int count(String param, List<SingularAttribute<? extends BaseEntity, String>> fields);

    /**
     * Execute a native query on database.
     *
     * @param clazz  The return class type.
     * @param query  The query to be executed.
     * @param params The query parameters to be set.
     * @return The query result.
     */
    <B> List<B> executeQuery(Class<B> clazz, String query, Object... params);

    /**
     * Merge the state of the given entity into the current persistence context.
     *
     * @param entity The referred entity.
     */
    <B> B attach(B entity);

}
