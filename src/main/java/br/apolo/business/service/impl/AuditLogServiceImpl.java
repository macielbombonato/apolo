package br.apolo.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.apolo.business.model.SearchResult;
import br.apolo.business.service.AuditLogService;
import br.apolo.data.model.AuditLog;
import br.apolo.data.model.User;
import br.apolo.data.repository.AuditLogRepository;

@Service(value = "auditLogService")
public class AuditLogServiceImpl implements AuditLogService {

	@Autowired
	AuditLogRepository auditLogRepository;

	@Transactional
	public AuditLog save(AuditLog entity) {
		return auditLogRepository.save(entity);
	}

	@Override
	public List<AuditLog> list() {
		return null;
	}
	
	@Override
	public Page<AuditLog> list(Integer pageNumber) {
		return null;
	}

	@Override
	public AuditLog find(Long id) {
		return null;
	}

	@Override
	public void remove(AuditLog entity) {
	}

	@Override
	public SearchResult<AuditLog> search(String param) {
		return null;
	}

	@Override
	public User getAuthenticatedUser() {
		return null;
	}
}
