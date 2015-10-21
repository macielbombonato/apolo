package apolo.business.service;

import apolo.business.service.base.BaseService;
import apolo.data.model.Application;
import apolo.data.model.Tenant;
import org.springframework.data.domain.Page;

public interface ApplicationService extends BaseService<Application> {
	
	final int PAGE_SIZE = 10;

	Page<Application> search(Tenant tenant, Integer pageNumber, String param);

	Application find(Long id);

	Application find(String key);

	Application lock(Application entity);

	Application unlock(Application entity);
	
}