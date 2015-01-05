package apolo.business.service.impl;

import apolo.business.model.FileContent;
import apolo.business.service.FileService;
import apolo.business.service.TenantService;
import apolo.common.config.model.ApplicationProperties;
import apolo.common.exception.AccessDeniedException;
import apolo.common.exception.BusinessException;
import apolo.common.util.MessageBundle;
import apolo.data.enums.Status;
import apolo.data.model.Tenant;
import apolo.data.repository.TenantRepository;
import apolo.security.UserPermission;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("tenantService")
public class TenantServiceImpl extends BaseServiceImpl<Tenant> implements TenantService {

	@Autowired
	private TenantRepository tenantRepository;
	
	@Autowired
	private FileService<Tenant> fileService;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	public List<Tenant> list() {
		return tenantRepository.findAll();
	}
	
	public Page<Tenant> list(Integer pageNumber) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		
		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE);
		
		return tenantRepository.findAll(request);
	}

	public Tenant find(Long id) {
		Tenant result = null;
		
		// Only users with permission can find anothers tenants, unless, they only can view your own tenant.
		if (getAuthenticatedUser() != null) {
			if (getAuthenticatedUser().getPermissions().contains(UserPermission.ADMIN)
					|| getAuthenticatedUser().getPermissions().contains(UserPermission.TENANT_MANAGER)) {
				result = tenantRepository.findOne(id);
			} else {
				result = getAuthenticatedUser().getTenant();
			}
		}
		
		return result;
	}
	
	public Tenant find(Tenant tenant, Long id) {
		return find(id);
	}

	@Transactional
	public Tenant save(Tenant entity) {
		return save(entity, null);
	}
	
	@Transactional
	public Tenant save(Tenant tenant, FileContent file) {
		
		if (file != null) {
			if (file != null 
					&& file.getFile() != null 
					&& file.getFile().getOriginalFilename() != null 
					&& !file.getFile().getOriginalFilename().isEmpty()) {
				
				if (tenant.getId() == null) {
					tenantRepository.saveAndFlush(tenant);	
				}
				
				try {
					tenant.setLogo(
							fileService.uploadFile(
									tenant, 
									tenant, 
									file, 
									file.getFile().getInputStream()
								)
						);
				} catch (IOException e) {
					String message = MessageBundle.getMessageBundle("error.500.5");
					throw new BusinessException(5, message);
				}
			}
		}
		
		return tenantRepository.saveAndFlush(tenant);
	}

	@Transactional
	public void remove(Tenant entity) {
		tenantRepository.delete(entity);
	}

	public Page<Tenant> search(Integer pageNumber, String param) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		
		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "name");
		
		if (param != null) {
			param = "%" + param + "%";	
		}
		
		return tenantRepository.findByUrlLikeOrNameLikeOrderByNameAsc(param, param, request);
	}
	
	public Tenant findByUrl(String url) {
		Tenant result = null;
		
		// Only users with permission can find anothers tenants, unless, they only can view your own tenant.
		if (getAuthenticatedUser() != null) {
			if (getAuthenticatedUser().getTenant().getUrl().equals(url)) {
				
				result = getAuthenticatedUser().getTenant();
			
			} else if (getAuthenticatedUser().getPermissions().contains(UserPermission.ADMIN)
					|| getAuthenticatedUser().getPermissions().contains(UserPermission.TENANT_MANAGER)) {
				
				result = tenantRepository.findByUrl(url);
				
				if (result!= null 
						&& Status.LOCKED.equals(result.getStatus())) {
					String message = MessageBundle.getMessageBundle("error.403.2");
					throw new AccessDeniedException(2, message);
				}
			} else {
				String message = MessageBundle.getMessageBundle("error.403.msg");
				throw new AccessDeniedException(message);
			}
		} else {
			
			// when the query is originated by web service, the session don't hava an authenticated user
			// in this case, the query can be done with permission check
			result = tenantRepository.findByUrl(url);
		}
		
		return result;
	}

	public List<Tenant> list(Tenant tenant) {
		// not used in this service
		return list();
	}

	public Page<Tenant> list(Tenant tenant, Integer pageNumber) {
		// not used in this service
		return list(pageNumber);
	}

	public Tenant lock(Tenant entity) {
		if (getAuthenticatedUser().getTenant().equals(entity)) {
			String message = MessageBundle.getMessageBundle("error.403.6");
			throw new AccessDeniedException(6, message);
		} else if (entity.getUrl().equals(applicationProperties.getDefaultTenant())) {
			String message = MessageBundle.getMessageBundle("error.403.7");
			throw new AccessDeniedException(7, message);
		} else {
			entity.setStatus(Status.LOCKED);
			return tenantRepository.save(entity);			
		}
	}

	public Tenant unlock(Tenant entity) {
		entity.setStatus(Status.ACTIVE);
		return tenantRepository.save(entity);
	}

}