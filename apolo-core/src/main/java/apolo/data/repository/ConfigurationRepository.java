package apolo.data.repository;

import apolo.data.model.Configuration;
import apolo.data.model.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long>, ConfigurationRepositoryCustom {
	
	List<Configuration> findByTenant(
			@Param("tenant") Tenant tenant
		);
	
	Page<Configuration> findByTenant(
			@Param("tenant") Tenant tenant, 
			Pageable page
		);

	Configuration findByTenantAndId(
			Tenant tenant,
			Long id
		);

}
