package br.apolo.data.entitylistener;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.security.core.context.SecurityContextHolder;

import br.apolo.data.model.User;
import br.apolo.data.model.interfaces.IAuditableEntity;
import br.apolo.security.CurrentUser;

public class AuditListener {

	@PrePersist
	void onCreate(IAuditableEntity e) {
		User user = new User();
		user.setId(((CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());

		e.setCreationDate(new Date());
		e.setCreatedBy(user);
		e.setLastUpdateDate(new Date());
		e.setLastUpdatedBy(user);
	}

	@PreUpdate
	void onPersist(IAuditableEntity e) {
		User user = new User();
		user.setId(((CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());

		e.setLastUpdateDate(new Date());
		e.setLastUpdatedBy(user);
	}

}
