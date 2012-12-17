package br.apolo.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.apolo.business.model.SearchResult;
import br.apolo.business.service.UserGroupService;
import br.apolo.data.model.UserGroup;
import br.apolo.data.repository.UserGroupRepository;

@Service("userGroupService")
public class UserGroupServiceImpl implements UserGroupService {

	@Autowired
	UserGroupRepository userGroupRepository;

	@Override
	public List<UserGroup> list() {
		return userGroupRepository.findAll();
	}

	@Override
	public UserGroup find(Long id) {
		return userGroupRepository.find(id);
	}

	@Override
	@Transactional
	public UserGroup save(UserGroup userGroup) {
		return userGroupRepository.saveOrUpdate(userGroup);
	}
	
	@Override
	@Transactional
	public void remove(UserGroup userGroup) {
		userGroupRepository.remove(userGroup);
	}
	
	@Override
	public SearchResult<UserGroup> search(String param) {
		SearchResult<UserGroup> result = new SearchResult<UserGroup>();
		
		result.setResults(userGroupRepository.search(param));
		
		return result;
	}
}
