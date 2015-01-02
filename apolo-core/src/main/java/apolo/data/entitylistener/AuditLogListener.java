package apolo.data.entitylistener;

import apolo.business.service.AuditLogService;
import apolo.business.service.UserService;
import apolo.business.service.impl.AuditLogServiceImpl;
import apolo.data.enums.DatabaseTransactionType;
import apolo.data.model.AuditLog;
import apolo.data.model.AuditableBaseEntity;
import apolo.data.model.User;

import java.util.Date;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.ContextLoader;

public class AuditLogListener {
	
	protected static final Logger LOG = LoggerFactory.getLogger(AuditLogListener.class);

	@Autowired
	private AuditLogService auditLogService;
	
	@Autowired
	private UserService userService;

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
		
		if (userService == null) {
			ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();

			userService = (UserService) ctx.getBean("userService");
		}

		User user = null;
		
		Long executorId = null;
		
		try {
			executorId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
		} catch(Throwable errorId) {
			executorId = null;
		}
		
		if (executorId != null) {
			user = userService.find(executorId);
		} 
		
		if (user == null) {
			user = userService.find(1L);
		}
		
		AuditLog auditLog = new AuditLog();

		/*
		 * If class has Table annotation, the system will use this data, otherwise, we´ll use the class name.
		 */
		String entityName = e.getClass().getAnnotation(Table.class).name();
		if (entityName == null || entityName.isEmpty()) {
			entityName = e.getClass().getSimpleName();
		}

		if (user != null) {
			auditLog.setTransactionType(transactionType);
			auditLog.setTenantId(user.getTenant().getId());
			auditLog.setEntityName(entityName);
			auditLog.setRegistryId(e.getId());
			auditLog.setExecutedById(user.getId());
			auditLog.setOperationDate(new Date());

			try {
				auditLogService.save(user.getTenant(), auditLog);
			} catch (Throwable e1) {
				LOG.error("===> Error on auditing log method.", e1);
			}			
		}
	}

	public void setAuditLogService(AuditLogServiceImpl auditLogService1) {
		this.auditLogService = auditLogService1;
	}

}