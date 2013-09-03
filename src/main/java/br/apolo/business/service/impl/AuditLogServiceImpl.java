package br.apolo.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.apolo.business.model.SearchResult;
import br.apolo.business.service.AuditLogService;
import br.apolo.business.service.UserService;
import br.apolo.data.model.AuditLog;
import br.apolo.data.model.AuditLog_;
import br.apolo.data.model.User;
import br.apolo.data.repository.AuditLogRepository;

@Service(value = "auditLogService")
public class AuditLogServiceImpl implements AuditLogService {

	@Autowired
	private AuditLogRepository auditLogRepository;
	
	@Autowired
	private UserService userService;

	@Transactional
	public AuditLog save(AuditLog entity) {
		return auditLogRepository.save(entity);
	}

	@Override
	public List<AuditLog> list() {
		List<AuditLog> result = new ArrayList<AuditLog>();
		
		result = auditLogRepository.findAll(new Sort(new Sort.Order(Sort.Direction.DESC, "operationDate")));
		
		if (!result.isEmpty()) {
			for (AuditLog auditLog : result) {
				auditLog.setExecutedBy(userService.find(auditLog.getExecutedById()));
			}
		}
		
		return result;
	}
	
	@Override
	public Page<AuditLog> list(Integer pageNumber) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		
		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.DESC, "operationDate");
		
		Page<AuditLog> result = auditLogRepository.findAll(request);
		
		if (result != null && result.getContent() != null && !result.getContent().isEmpty()) {
			for (AuditLog auditLog : result.getContent()) {
				auditLog.setExecutedBy(userService.find(auditLog.getExecutedById()));
			}
		}
		
		return result;
	}

	@Override
	public AuditLog find(Long id) {
		return auditLogRepository.findOne(id);
	}

	@Override
	public void remove(AuditLog entity) {
	}

	@Override
	public SearchResult<AuditLog> search(String param) {
		SearchResult<AuditLog> result = new SearchResult<AuditLog>();
		
		List<SingularAttribute<AuditLog, String>> fields = new ArrayList<SingularAttribute<AuditLog,String>>();
		fields.add(AuditLog_.entityName);
		
		result.setResults(auditLogRepository.search(param, fields));
		
		return result;
	}

	@Override
	public User getAuthenticatedUser() {
		return null;
	}
}
