package br.apolo.business.service;

import java.util.List;

import br.apolo.data.model.UserGroup;

public interface UserGroupService {

	List<UserGroup> list();

	UserGroup find(Long id);

	UserGroup save(UserGroup userGroup);
	
	void remove(UserGroup userGroup);
}