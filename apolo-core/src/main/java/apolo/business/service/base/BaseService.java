package apolo.business.service.base;

import apolo.data.model.base.BaseEntity;
import apolo.data.model.Tenant;
import apolo.data.model.User;

import java.util.List;

import org.springframework.data.domain.Page;

@SuppressWarnings("rawtypes")
public interface BaseService<E extends BaseEntity> {
	
	E find(Tenant tenant, Long id);
	
	E save(E entity);
	
	void remove(E entity);
	
	User getAuthenticatedUser();
	
	List<E> list(Tenant tenant);
	
	Page<E> list(Tenant tenant, Integer pageNumber);
	
}
