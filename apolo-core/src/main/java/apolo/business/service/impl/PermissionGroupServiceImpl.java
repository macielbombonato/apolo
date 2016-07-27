package apolo.business.service.impl;

import apolo.business.service.PermissionGroupService;
import apolo.common.exception.AccessDeniedException;
import apolo.common.util.MessageBundle;
import apolo.data.enums.UserStatus;
import apolo.data.model.PermissionGroup;
import apolo.data.model.Tenant;
import apolo.data.model.User;
import apolo.data.repository.PermissionGroupRepository;
import apolo.security.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("permissionGroupService")
public class PermissionGroupServiceImpl extends BaseServiceImpl<PermissionGroup> implements PermissionGroupService {

	@Autowired
	private PermissionGroupRepository permissionGroupRepository;

	@Override
	@Deprecated
	public List<PermissionGroup> list(Tenant tenant) {
		return null;
	}

	@Override
	public List<PermissionGroup> list(User user) {
		Sort request = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));

		List<PermissionGroup> result = null;

		if (user != null) {
			if (user.getPermissions().contains(Permission.ADMIN)) {
				result = permissionGroupRepository.findAll(
						request
				);
			} else {
				result = permissionGroupRepository.findByNameNot(
						MessageBundle.getMessageBundle("user.permission.ADMIN"),
						request
				);

				// A user only can see permissions that he has, otherwise, he can improve your own access
				List<PermissionGroup> permissionToRemove = new ArrayList<PermissionGroup>();

				for(PermissionGroup permGroup : result) {
					for(Permission perm : permGroup.getPermissions()) {
						if (!user.getPermissions().contains(perm)) {
							permissionToRemove.add(permGroup);
						}
					}
				}

				if (!permissionToRemove.isEmpty()) {
					for (PermissionGroup perm : permissionToRemove) {
						result.remove(perm);
					}
				}
			}
		}

		return result;
	}

	public Page<PermissionGroup> list(Tenant tenant, Integer pageNumber) {
		if (pageNumber == null || pageNumber < 1) {
			pageNumber = 1;
		}

		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "name");

		Page<PermissionGroup> result = null;

		if (getAuthenticatedUser().getPermissions().contains(Permission.ADMIN)) {
			result = permissionGroupRepository.findByName(
					MessageBundle.getMessageBundle("user.permission.ADMIN"),
					request
			);
		} else {
			result = permissionGroupRepository.findByNameNot(
					MessageBundle.getMessageBundle("user.permission.ADMIN"),
					request
			);
		}

		return result;
	}

	public PermissionGroup find(Long id) {
		return permissionGroupRepository.findOne(id);
	}

	public PermissionGroup find(Tenant tenant, Long id) {
		return permissionGroupRepository.findOne(id);
	}

	@Transactional
	public PermissionGroup save(PermissionGroup permissionGroup) throws AccessDeniedException {
		if (permissionGroup != null
				&& UserStatus.ADMIN.equals(permissionGroup.getStatus()) && permissionGroup.getId() != null) {
			throw new AccessDeniedException(MessageBundle.getMessageBundle("user.group.msg.access.denied"));
		} else if (permissionGroup != null
				&& getAuthenticatedUser() != null
				&& (!getAuthenticatedUser().getPermissions().contains(Permission.ADMIN)
				&& !getAuthenticatedUser().getPermissions().contains(Permission.TENANT_MANAGER)
		)
				) {
			throw new AccessDeniedException(MessageBundle.getMessageBundle("user.group.msg.access.denied.multitenant"));
		} else {
			if (!UserStatus.ADMIN.equals(permissionGroup.getStatus())) {
				permissionGroup.setStatus(UserStatus.ACTIVE);
			}

			permissionGroup.setUpdatedBy(getAuthenticatedUser());
			permissionGroup.setUpdatedAt(new Date());

			return permissionGroupRepository.saveAndFlush(permissionGroup);
		}
	}

	@Transactional
	public void remove(PermissionGroup permissionGroup) throws AccessDeniedException {
		if (permissionGroup != null && UserStatus.ADMIN.equals(permissionGroup.getStatus()) && permissionGroup.getId() != null) {
			throw new AccessDeniedException(MessageBundle.getMessageBundle("user.group.msg.access.denied"));
		} else {
			permissionGroupRepository.delete(permissionGroup);
		}
	}

	public Page<PermissionGroup> search(Tenant tenant, Integer pageNumber, String param) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}

		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "name");

		if (param != null) {
			param = "%" + param + "%";
		}

		Page<PermissionGroup> result = permissionGroupRepository.findByNameLikeOrderByNameAsc(param, request);

		return result;
	}

	public List<Permission> getUserPermissionList() {
		List<Permission> permissions = new ArrayList<Permission>();

		User user = getAuthenticatedUser();

		for (Permission permission : Permission.values()) {
			if (Permission.ADMIN.equals(permission)
					&& user.getPermissions().contains(Permission.ADMIN)
					&& permission.isListable()) {
				permissions.add(permission);
			} else if (!Permission.ADMIN.equals(permission)
					&& permission.isListable()) {
				permissions.add(permission);
			}
		}

		return permissions;
	}

	public PermissionGroup lock(PermissionGroup entity) {
		if (!entity.getStatus().isChangeable()) {
			String message = MessageBundle.getMessageBundle("error.403.4");
			throw new AccessDeniedException(4, message);
		}

		entity.setStatus(UserStatus.LOCKED);
		return permissionGroupRepository.save(entity);
	}

	public PermissionGroup unlock(PermissionGroup entity) {
		if (!entity.getStatus().isChangeable()) {
			String message = MessageBundle.getMessageBundle("error.403.4");
			throw new AccessDeniedException(4, message);
		}

		entity.setStatus(UserStatus.ACTIVE);
		return permissionGroupRepository.save(entity);
	}

}
