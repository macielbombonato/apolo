package apolo.business.service;

import apolo.business.model.FileContent;
import apolo.data.model.Tenant;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TenantService extends BaseService<Tenant> {
	
	final int PAGE_SIZE = 10;

	Page<Tenant> search(Integer pageNumber, String param);
	
	Tenant find(Long id);
	
	Tenant findByUrl(String url);
	
	Page<Tenant> list(Integer pageNumber);
	
	List<Tenant> list();
	
	Tenant save(Tenant tenant, FileContent logo, FileContent icon);
	
	Tenant lock(Tenant entity);
	
	Tenant unlock(Tenant entity);

	Tenant getValidatedTenant(String tenantUrl);

	long count();
	
}