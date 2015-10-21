package apolo.business.service;

import apolo.business.service.base.BaseService;
import apolo.data.model.Tenant;
import apolo.data.model.UserGroup;
import apolo.security.UserPermission;

import java.util.List;

import org.springframework.data.domain.Page;

public interface UserGroupService extends BaseService<UserGroup> {
	
	final int PAGE_SIZE = 10;

	List<UserPermission> getUserPermissionList();
	
	Page<UserGroup> search(Tenant tenant, Integer pageNumber, String param);
	
	UserGroup find(Long id);
	
	UserGroup lock(UserGroup entity);
	
	UserGroup unlock(UserGroup entity);
	
}