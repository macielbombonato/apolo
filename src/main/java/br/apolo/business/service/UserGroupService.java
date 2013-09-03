package br.apolo.business.service;

import java.util.List;

import br.apolo.data.enums.UserPermission;
import br.apolo.data.model.UserGroup;

public interface UserGroupService extends BaseService<UserGroup> {

	List<UserPermission> getUserPermissionList();
	
	UserGroup findByName(String name);
	
}