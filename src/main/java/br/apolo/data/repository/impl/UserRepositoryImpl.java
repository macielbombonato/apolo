package br.apolo.data.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import br.apolo.data.model.User;
import br.apolo.data.model.User_;
import br.apolo.data.repository.UserRepositoryCustom;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<User> search(String param) {
		try {
			param = "%" + param + "%";

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<User> query = cb.createQuery(User.class);
			Root<User> root = query.from(User.class);
			query.where(cb.or(cb.like(root.get(User_.name), param),
					cb.like(root.get(User_.email), param)));

			return em.createQuery(query).getResultList();
		} catch (PersistenceException e) {
			return null;
		}
	}
}