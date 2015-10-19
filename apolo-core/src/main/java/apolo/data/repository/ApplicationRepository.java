package apolo.data.repository;

import apolo.data.model.Application;
import apolo.data.model.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

	Application findByApplicationKey(
			String applicationKey
	);

	List<Application> findByTenant(
			Tenant tenant,
			Sort page
	);

	Page<Application> findByTenant(
			Tenant tenant,
			Pageable page
	);
	
	List<Application> findByTenantOrName(
			Tenant tenant,
			String name,
			Sort page
	);
	
	Page<Application> findByTenantOrName(
			Tenant tenant,
			String name,
			Pageable page
	);
	
	Page<Application> findByTenantAndNameLikeOrderByNameAsc(
			Tenant tenant,
			String name,
			Pageable page
	);
	
}
