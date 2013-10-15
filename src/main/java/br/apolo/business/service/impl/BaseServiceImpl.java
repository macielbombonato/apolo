package br.apolo.business.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import br.apolo.business.service.BaseService;
import br.apolo.data.model.BaseEntity;
import br.apolo.data.model.User;
import br.apolo.security.CurrentUser;

public abstract class BaseServiceImpl<E extends BaseEntity> implements BaseService<E> {
	
	protected static final Logger log = LoggerFactory.getLogger(BaseService.class);
	
	public User getAuthenticatedUser() {
		User user = null;
		
		CurrentUser currentUser = null;
		
		try {
			currentUser = (CurrentUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch(Throwable errorId) {
			currentUser = null;
			log.error(errorId.getMessage(), errorId);
		}
		
		if (currentUser != null) {
			user = currentUser.getSystemUser();
			
			if (user.getId() == null) {
				user = null;
			}
		}
		
		return user;
	}

}
