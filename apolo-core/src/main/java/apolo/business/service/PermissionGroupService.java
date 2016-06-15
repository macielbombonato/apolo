package apolo.business.service;

import apolo.business.service.base.BaseService;
import apolo.data.model.PermissionGroup;
import apolo.data.model.Tenant;
import apolo.security.Permission;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PermissionGroupService extends BaseService<PermissionGroup> {
	
	final int PAGE_SIZE = 10;

	List<Permission> getUserPermissionList();

	List<PermissionGroup> list();
	
	Page<PermissionGroup> search(Tenant tenant, Integer pageNumber, String param);
	
	PermissionGroup find(Long id);
	
	PermissionGroup lock(PermissionGroup entity);
	
	PermissionGroup unlock(PermissionGroup entity);
	
}