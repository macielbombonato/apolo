package br.apolo.business.service;

import java.util.List;

import org.springframework.data.domain.Page;

import br.apolo.data.model.AuditLog;

public interface AuditLogService {
	
	final int PAGE_SIZE = 50;
	
	Page<AuditLog> search(Integer pageNumber, String param);
	
	List<AuditLog> list();
	
	Page<AuditLog> list(Integer pageNumber);
	
	AuditLog find(Long id);
	
	AuditLog save(AuditLog entity);
	
}