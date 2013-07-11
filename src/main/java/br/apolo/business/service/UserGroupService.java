package br.apolo.business.service;

import java.util.List;

import br.apolo.data.model.UserGroup;
import br.apolo.security.UserPermission;

public interface UserGroupService extends BaseService<UserGroup> {

	List<UserPermission> getUserPermissionList();
	
	UserGroup findByName(String name);
	
}