package br.apolo.data.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import br.apolo.data.model.UserGroup;
import br.apolo.data.model.UserGroup_;
import br.apolo.data.repository.UserGroupRepositoryCustom;

@Repository
public class UserGroupRepositoryImpl implements UserGroupRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<UserGroup> search(String param) {
		try {
			param = "%" + param + "%";

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<UserGroup> query = cb.createQuery(UserGroup.class);
			Root<UserGroup> root = query.from(UserGroup.class);
			query.where(cb.like(root.get(UserGroup_.name), param));

			return em.createQuery(query).getResultList();
		} catch (PersistenceException e) {
			return null;
		}
	}

}
