package br.apolo.data.entitylistener;

import java.util.Date;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.ContextLoader;

import br.apolo.business.service.AuditLogService;
import br.apolo.business.service.impl.AuditLogServiceImpl;
import br.apolo.data.enums.DatabaseTransactionType;
import br.apolo.data.model.AuditLog;
import br.apolo.data.model.AuditableBaseEntity;
import br.apolo.data.model.User;
import br.apolo.security.CurrentUser;

public class AuditLogListener {

	@Autowired
	AuditLogService auditLogService;

	@PostRemove
	void postDelete(AuditableBaseEntity e) {
		createLog(DatabaseTransactionType.DELETE, e);
	}

	@PostPersist
	void postPersist(AuditableBaseEntity e) {
		createLog(DatabaseTransactionType.CREATE, e);
	}

	@PostUpdate
	void postUpdate(AuditableBaseEntity e) {
		createLog(DatabaseTransactionType.UPDATE, e);
	}

	private void createLog(DatabaseTransactionType transactionType, AuditableBaseEntity e) {
		/*
		 * This class is created by jpa engine and because this is necessary get dependencies in spring context.
		 */
		if (auditLogService == null) {
			ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();

			auditLogService = (AuditLogService) ctx.getBean("auditLogService");
		}

		User user = new User();
		
		if (SecurityContextHolder.getContext() != null
				&& SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
			user.setId(((CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
		} else {
			user.setId(1L);
		}
		
		AuditLog auditLog = new AuditLog();

		/*
		 * If class has Table annotation, the system will use this data, otherwise, we´ll use the class name.
		 */
		String entityName = e.getClass().getAnnotation(Table.class).name();
		if (entityName == null || entityName.isEmpty()) {
			entityName = e.getClass().getSimpleName();
		}

		auditLog.setTransactionType(transactionType);
		auditLog.setEntityName(entityName);
		auditLog.setRegistryId(e.getId());
		auditLog.setExecutedById(user.getId());
		auditLog.setOperationDate(new Date());

		auditLogService.save(auditLog);
	}

	public void setAuditLogService(AuditLogServiceImpl auditLogService1) {
		this.auditLogService = auditLogService1;
	}

}