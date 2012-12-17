package br.apolo.data.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.apolo.data.model.UserGroup;
import br.apolo.data.repository.UserGroupRepository;

@Repository(value = "userGroupRepository")
public class UserGroupRepositoryImpl extends JpaRepositoryImpl<UserGroup> implements UserGroupRepository {

	@Override
	public List<UserGroup> search(String param) {
		List<UserGroup> result = null;
		
		StringBuilder queryStr = new StringBuilder();
		
		queryStr.append(" SELECT u ");
		queryStr.append(" FROM UserGroup u ");
		queryStr.append(" WHERE u.name like :param ");
		
		TypedQuery<UserGroup> query = createQuery(queryStr.toString());
		
		query.setParameter("param", "%" + param + "%");
		
		result = query.getResultList();
		
		return result;
	}


}
