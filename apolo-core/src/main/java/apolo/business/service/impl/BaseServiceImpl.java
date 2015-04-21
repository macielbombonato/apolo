package apolo.business.service.impl;

import apolo.business.service.BaseService;
import apolo.data.model.BaseEntity;
import apolo.data.model.User;
import apolo.security.CurrentUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

@SuppressWarnings("rawtypes")
public abstract class BaseServiceImpl<E extends BaseEntity> implements BaseService<E> {
	
	protected static final Logger LOG = LoggerFactory.getLogger(BaseService.class);
	
	public User getAuthenticatedUser() {
		User user = null;
		
		CurrentUser currentUser = null;
		
		try {
			if (SecurityContextHolder.getContext().getAuthentication() != null 
					&& SecurityContextHolder.getContext().getAuthentication() instanceof CurrentUser) {
				currentUser = (CurrentUser)SecurityContextHolder.getContext().getAuthentication();				
			}
		} catch(Throwable errorId) {
			currentUser = null;
			LOG.error(errorId.getMessage(), errorId);
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
