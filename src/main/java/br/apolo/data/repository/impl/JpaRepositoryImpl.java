package br.apolo.data.repository.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hibernate.Session;

import br.apolo.data.model.BaseEntity;
import br.apolo.data.repository.JpaRepository;

public abstract class JpaRepositoryImpl<T extends BaseEntity> implements JpaRepository<T> {
	protected Class<T> entityClass;

	protected EntityManager em;

	private Session session = null;

	public Session getSession() {
		if (this.session == null || !this.session.isOpen()) {
			synchronized (this) {
				this.session = em.unwrap(Session.class);
			}
		}

		return this.session;
	}

	@SuppressWarnings("unchecked")
	public JpaRepositoryImpl() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.em = entityManager;

		// it can't be initialized here - an exception is thrown
		// this.session = em.unwrap(Session.class);
	}

	public T find(Long id) {
		return em.find(entityClass, id);
	}

	public T saveOrUpdate(T entity) {
		Long id = entity.getId();
		if (id == null || id.equals(0L)) {
			em.persist(entity);
		} else {
			entity = em.merge(entity);
		}
		return entity;
	}

	public void remove(T entity) {
		if (entity != null && entity.getId() != null) {
			T retrieved = find(entity.getId());
			if (retrieved != null) {
				em.remove(retrieved);
			}
		}
	}

	public List<T> findAll() {
		String q = String.format("FROM %s", entityClass.getName());
		return em.createQuery(q, entityClass).getResultList();
	}

	/**
	 * Create a typed query based on the repository entityClass
	 * 
	 * @param q The query string
	 * @return A TypedQuery of the entity class
	 */
	protected TypedQuery<T> createQuery(String query) {
		return em.createQuery(query, entityClass);
	}

	protected TypedQuery<Long> createQueryForCount(String query) {
		return em.createQuery(query, Long.class);
	}

	protected TypedQuery<T> createNamedQuery(String queryName) {
		return em.createNamedQuery(queryName, entityClass);
	}
}
