package br.apolo.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.apolo.business.service.AuditLogService;
import br.apolo.business.service.UserService;
import br.apolo.data.model.AuditLog;
import br.apolo.data.repository.AuditLogRepository;

@Service("auditLogService")
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
		
		findAllOperationExecutors(result);
		
		return result;
	}

	@Override
	public AuditLog find(Long id) {
		return auditLogRepository.findOne(id);
	}

	@Override
	public Page<AuditLog> search(Integer pageNumber, String param) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		
		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "entityName");
		
		if (param != null) {
			param = "%" + param + "%";	
		}
		
		Page<AuditLog> result = auditLogRepository.findByEntityNameLikeOrderByEntityNameAscOperationDateDesc(param, request);
		
		findAllOperationExecutors(result);
		
		return result;
	}

	private void findAllOperationExecutors(Page<AuditLog> result) {
		if (result != null && result.getContent() != null && !result.getContent().isEmpty()) {
			for (AuditLog auditLog : result.getContent()) {
				auditLog.setExecutedBy(userService.find(auditLog.getExecutedById()));
			}
		}
	}
}
