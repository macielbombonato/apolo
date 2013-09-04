package br.apolo.business.service;

import org.springframework.data.domain.Page;

import br.apolo.data.model.AuditLog;

public interface AuditLogService extends BaseService<AuditLog> {
	
	final int PAGE_SIZE = 50;
	
	Page<AuditLog> search(Integer pageNumber, String param);

}