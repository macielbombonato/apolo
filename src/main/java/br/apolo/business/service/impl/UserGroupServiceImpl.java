package br.apolo.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.apolo.business.model.SearchResult;
import br.apolo.business.service.UserGroupService;
import br.apolo.data.model.UserGroup;
import br.apolo.data.model.UserGroup_;
import br.apolo.data.repository.UserGroupRepository;

@Service("userGroupService")
public class UserGroupServiceImpl implements UserGroupService {

	@Autowired
	UserGroupRepository userGroupRepository;
	
	@Override
	public List<UserGroup> list() {
		return (List<UserGroup>) userGroupRepository.findAll();
	}

	@Override
	public UserGroup find(Long id) {
		return userGroupRepository.findOne(id);
	}

	@Override
	@Transactional
	public UserGroup save(UserGroup userGroup) {
		return userGroupRepository.save(userGroup);
	}

	@Override
	@Transactional
	public void remove(UserGroup userGroup) {
		userGroupRepository.delete(userGroup);
	}

	@Override
	public SearchResult<UserGroup> search(String param) {
		SearchResult<UserGroup> result = new SearchResult<UserGroup>();
		
		List<SingularAttribute<UserGroup, String>> fields = new ArrayList<SingularAttribute<UserGroup,String>>();
		fields.add(UserGroup_.name);
		
		result.setResults(userGroupRepository.search(param, fields));

		return result;
	}
}
