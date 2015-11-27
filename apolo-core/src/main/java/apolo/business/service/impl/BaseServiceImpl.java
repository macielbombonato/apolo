package apolo.business.service.impl;

import apolo.business.service.base.BaseService;
import apolo.data.model.base.BaseEntity;
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
			if (SecurityContextHolder.getContext() != null
					&&SecurityContextHolder.getContext().getAuthentication() != null
					&& SecurityContextHolder.getContext().getAuthentication() instanceof CurrentUser) {
				currentUser = (CurrentUser)SecurityContextHolder.getContext().getAuthentication();
			}
		} catch(Throwable errorId) {
			currentUser = null;
			LOG.error(errorId.getMessage(), errorId);
		}

		if (currentUser != null) {
			user = currentUser.getSystemUser();

			if (user != null
					&& user.getId() == null) {
				user = null;
			}
		}

		return user;
	}

}
