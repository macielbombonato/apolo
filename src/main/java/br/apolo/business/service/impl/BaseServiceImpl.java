package br.apolo.business.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;

import br.apolo.business.service.BaseService;
import br.apolo.data.model.BaseEntity;
import br.apolo.data.model.User;
import br.apolo.security.CurrentUser;

public abstract class BaseServiceImpl<E extends BaseEntity> implements BaseService<E> {
	
	public User getAuthenticatedUser() {
		CurrentUser currentUser = (CurrentUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		User user = null;
		
		if (currentUser != null) {
			user = currentUser.getSystemUser();
		}
		
		return user;
	}

}
