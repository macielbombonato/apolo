package br.apolo.data.repository.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import br.apolo.data.model.BaseEntity;
import br.apolo.data.repository.BaseRepository;

public abstract class BaseRepotitoryImpl<E extends BaseEntity> implements BaseRepository<E> {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<E> search(String param, List<SingularAttribute<E, String>> fields) {
		try {
			@SuppressWarnings("unchecked")
			Class<E> classType = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			
			param = "%" + param + "%";

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<E> query = cb.createQuery(classType);
			Root<E> root = query.from(classType);
			
			if (fields != null && !fields.isEmpty()) {
				Predicate[] criteriaLikes = new Predicate[fields.size()];
				
				for (int i = 0; i < fields.size(); i++) {
					criteriaLikes[i] = cb.like(root.get(fields.get(i)), param);
				}
				
				query.where(cb.or(criteriaLikes));
			}
			
			return em.createQuery(query).getResultList();
		} catch (PersistenceException e) {
			return null;
		}
	}

}
