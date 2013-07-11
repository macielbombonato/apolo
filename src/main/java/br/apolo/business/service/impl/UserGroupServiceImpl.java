package br.apolo.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.apolo.business.model.SearchResult;
import br.apolo.business.service.UserGroupService;
import br.apolo.common.exception.AccessDeniedException;
import br.apolo.common.util.MessageBundle;
import br.apolo.data.model.User;
import br.apolo.data.model.UserGroup;
import br.apolo.data.model.UserGroup_;
import br.apolo.data.repository.UserGroupRepository;
import br.apolo.security.UserPermission;

@Service("userGroupService")
public class UserGroupServiceImpl extends BaseServiceImpl<UserGroup> implements UserGroupService {

	@Autowired
	UserGroupRepository userGroupRepository;
	
	@Override
	public List<UserGroup> list() {
		return (List<UserGroup>) userGroupRepository.findAllUserGroups();
	}

	@Override
	public UserGroup find(Long id) {
		return userGroupRepository.findOne(id);
	}

	@Override
	@Transactional
	public UserGroup save(UserGroup userGroup) throws AccessDeniedException {
		if (userGroup != null && new Long(1L).equals(userGroup.getId())) {
			throw new AccessDeniedException(MessageBundle.getMessageBundle("user.group.msg.access.denied"));
		} else {
			userGroup.setLastUpdatedBy(getAuthenticatedUser());
			userGroup.setLastUpdateDate(new Date());
			
			return userGroupRepository.save(userGroup);	
		}
	}

	@Override
	@Transactional
	public void remove(UserGroup userGroup) throws AccessDeniedException {
		if (userGroup != null && new Long(1L).equals(userGroup.getId())) {
			throw new AccessDeniedException(MessageBundle.getMessageBundle("user.group.msg.access.denied"));
		} else {
			userGroupRepository.delete(userGroup);	
		}
	}

	@Override
	public SearchResult<UserGroup> search(String param) {
		SearchResult<UserGroup> result = new SearchResult<UserGroup>();
		
		List<SingularAttribute<UserGroup, String>> fields = new ArrayList<SingularAttribute<UserGroup,String>>();
		fields.add(UserGroup_.name);
		
		result.setResults(userGroupRepository.search(param, fields));

		return result;
	}

	@Override
	public List<UserPermission> getUserPermissionList() {
		List<UserPermission> permissions = new ArrayList<UserPermission>();
		
		User user = getAuthenticatedUser();
		
		for (UserPermission permission : UserPermission.values()) {
			if (UserPermission.ADMIN.equals(permission) && user.getPermissions().contains(UserPermission.ADMIN)) {
				permissions.add(permission);
			} else if (!UserPermission.ADMIN.equals(permission)) {
				permissions.add(permission);
			}
		}
		
		return permissions;
	}
	
	@Override
	public UserGroup findByName(String name) {
		return userGroupRepository.findByName(name);
	}
}
