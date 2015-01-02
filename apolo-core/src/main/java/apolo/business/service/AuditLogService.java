package apolo.business.service;

import apolo.data.model.AuditLog;
import apolo.data.model.Tenant;

import java.util.List;

import org.springframework.data.domain.Page;

public interface AuditLogService {
	
	final int PAGE_SIZE = 50;
	
	Page<AuditLog> search(Integer pageNumber, String param);
	
	List<AuditLog> list();
	
	Page<AuditLog> list(Integer pageNumber);
	
	AuditLog find(Long id);
	
	AuditLog save(Tenant tenant, AuditLog entity);
	
}