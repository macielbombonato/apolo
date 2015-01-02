package apolo.business.service.impl;

import apolo.business.service.AuditLogService;
import apolo.business.service.TenantService;
import apolo.business.service.UserService;
import apolo.data.model.AuditLog;
import apolo.data.model.Tenant;
import apolo.data.repository.AuditLogRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("auditLogService")
public class AuditLogServiceImpl implements AuditLogService {

	@Autowired
	private AuditLogRepository auditLogRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TenantService tenantService;

	@Transactional
	public AuditLog save(Tenant tenant, AuditLog entity) {
		entity.setTenantId(tenant.getId());
		
		return auditLogRepository.saveAndFlush(entity);
	}

	@Override
	public List<AuditLog> list() {
		Page<AuditLog> result = null;
		
		PageRequest request = new PageRequest(1, PAGE_SIZE, Sort.Direction.DESC, "operationDate");
		
		result = auditLogRepository.findAll(
				request
			);
		
		findAllOperationExecutors(result);
		
		return result.getContent();
	}
	
	@Override
	public Page<AuditLog> list(Integer pageNumber) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		
		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.DESC, "operationDate");
		
		Page<AuditLog> result = auditLogRepository.findAll(
				request
			);
		
		findAllOperationExecutors(result);
		
		return result;
	}

	@Override
	public AuditLog find(Long id) {
		AuditLog result = null;
		
		result = auditLogRepository.findById(id);
		
		return result;
	}

	@Override
	public Page<AuditLog> search(Integer pageNumber, String param) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		
		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.DESC, "operationDate");
		
		if (param != null) {
			param = "%" + param + "%";	
		}
		
		Page<AuditLog> result = auditLogRepository.findByEntityNameLike(
				param, 
				request
			);
		
		findAllOperationExecutors(result);
		
		return result;
	}

	private void findAllOperationExecutors(Page<AuditLog> result) {
		if (result != null && result.getContent() != null && !result.getContent().isEmpty()) {
			for (AuditLog auditLog : result.getContent()) {
				auditLog.setExecutedBy(userService.find(auditLog.getExecutedById()));
				auditLog.setTenant(tenantService.find(auditLog.getTenantId()));
			}
		}
	}
}
