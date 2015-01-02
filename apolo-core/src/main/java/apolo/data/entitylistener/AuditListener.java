package apolo.data.entitylistener;

import apolo.data.model.User;
import apolo.data.model.interfaces.IAuditableEntity;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditListener {

	@PrePersist
	void onCreate(IAuditableEntity e) {
		User user = new User();
		
		Object principal = null;
		
		if (SecurityContextHolder.getContext() != null 
				&& SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
			principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
		}
		
		if (principal != null && !"anonymousUser".equals(principal)) {
			User currentUser = (User) principal;
			
			if (currentUser != null) {
				user.setId(currentUser.getId());
				
				e.setCreatedBy(user);
				e.setLastUpdatedBy(user);
			}
		} else {
			e.setCreatedBy(null);
			e.setLastUpdatedBy(null);
		}

		e.setCreationDate(new Date());
		e.setLastUpdateDate(new Date());
	}

	@PreUpdate
	void onPersist(IAuditableEntity e) {
		User user = new User();
		
		Object principal = null;
				
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication != null) {
			principal = authentication.getPrincipal();
			
			if (principal != null && !"anonymousUser".equals(principal)) {
				User currentUser = (User) principal;
				
				if (currentUser != null) {
					user.setId(currentUser.getId());
					
					e.setLastUpdatedBy(user);
				}
			} else {
				e.setLastUpdatedBy(null);
			}

			e.setLastUpdateDate(new Date());
		}
	}
}
