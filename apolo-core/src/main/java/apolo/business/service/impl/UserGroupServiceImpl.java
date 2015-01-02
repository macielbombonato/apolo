package apolo.business.service.impl;

import apolo.business.service.UserGroupService;
import apolo.common.exception.AccessDeniedException;
import apolo.common.util.MessageBundle;
import apolo.data.enums.UserStatus;
import apolo.data.model.Tenant;
import apolo.data.model.User;
import apolo.data.model.UserGroup;
import apolo.data.repository.UserGroupRepository;
import apolo.security.UserPermission;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userGroupService")
public class UserGroupServiceImpl extends BaseServiceImpl<UserGroup> implements UserGroupService {

	@Autowired
	private UserGroupRepository userGroupRepository;
	
	@Override
	public List<UserGroup> list(Tenant tenant) {
		Sort request = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
		
		List<UserGroup> result = null;
		
		if (getAuthenticatedUser().getPermissions().contains(UserPermission.ADMIN)) {
			result = userGroupRepository.findByTenantOrName(
						tenant, 
						MessageBundle.getMessageBundle("user.permission.ADMIN"),
						request
					);
		} else {
			result = userGroupRepository.findByTenantAndNameNot(
					tenant, 
					MessageBundle.getMessageBundle("user.permission.ADMIN"),
					request
				);
		}
		
		return result;
	}
	
	@Override
	public Page<UserGroup> list(Tenant tenant, Integer pageNumber) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		
		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "name");
		
		Page<UserGroup> result = null;
		
		if (getAuthenticatedUser().getPermissions().contains(UserPermission.ADMIN)) {
			result = userGroupRepository.findByTenantOrName(
						tenant, 
						MessageBundle.getMessageBundle("user.permission.ADMIN"),
						request
					);
		} else {
			result = userGroupRepository.findByTenantAndNameNot(
					tenant, 
					MessageBundle.getMessageBundle("user.permission.ADMIN"),
					request
				);
		}
		
		return result;
	}

	@Override
	public UserGroup find(Long id) {
		return userGroupRepository.findOne(id);
	}
	
	@Override
	public UserGroup find(Tenant tenant, Long id) {
		return userGroupRepository.findByTenantAndId(tenant, id);
	}

	@Override
	@Transactional
	public UserGroup save(UserGroup userGroup) throws AccessDeniedException {
		if (userGroup != null && UserStatus.ADMIN.equals(userGroup.getStatus()) && userGroup.getId() != null) {
			throw new AccessDeniedException(MessageBundle.getMessageBundle("user.group.msg.access.denied"));
		} else {
			if (!UserStatus.ADMIN.equals(userGroup.getStatus())) {
				userGroup.setStatus(UserStatus.ACTIVE);
			}
			
			userGroup.setLastUpdatedBy(getAuthenticatedUser());
			userGroup.setLastUpdateDate(new Date());
			
			return userGroupRepository.saveAndFlush(userGroup);	
		}
	}

	@Override
	@Transactional
	public void remove(UserGroup userGroup) throws AccessDeniedException {
		if (userGroup != null && UserStatus.ADMIN.equals(userGroup.getStatus()) && userGroup.getId() != null) {
			throw new AccessDeniedException(MessageBundle.getMessageBundle("user.group.msg.access.denied"));
		} else {
			userGroupRepository.delete(userGroup);	
		}
	}

	@Override
	public Page<UserGroup> search(Tenant tenant, Integer pageNumber, String param) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		
		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "name");
		
		if (param != null) {
			param = "%" + param + "%";	
		}
		
		Page<UserGroup> result = userGroupRepository.findByTenantAndNameLikeOrderByNameAsc(tenant, param, request);
		
		return result;
	}

	@Override
	public List<UserPermission> getUserPermissionList() {
		List<UserPermission> permissions = new ArrayList<UserPermission>();
		
		User user = getAuthenticatedUser();
		
		for (UserPermission permission : UserPermission.values()) {
			if (UserPermission.ADMIN.equals(permission) 
					&& user.getPermissions().contains(UserPermission.ADMIN)
					&& permission.isListable()) {
				permissions.add(permission);
			} else if (!UserPermission.ADMIN.equals(permission)
					&& permission.isListable()) {
				permissions.add(permission);
			}
		}
		
		return permissions;
	}
	
	@Override
	public UserGroup lock(UserGroup entity) {
		if (!entity.getStatus().isChangeable()) {
			String message = MessageBundle.getMessageBundle("error.403.4");
			throw new AccessDeniedException(4, message);
		} 
		
		entity.setStatus(UserStatus.LOCKED);
		return userGroupRepository.save(entity);
	}

	@Override
	public UserGroup unlock(UserGroup entity) {
		if (!entity.getStatus().isChangeable()) {
			String message = MessageBundle.getMessageBundle("error.403.4");
			throw new AccessDeniedException(4, message);
		} 
		
		entity.setStatus(UserStatus.ACTIVE);
		return userGroupRepository.save(entity);
	}

}
