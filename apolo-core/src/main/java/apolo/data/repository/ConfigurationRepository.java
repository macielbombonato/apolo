package apolo.data.repository;

import apolo.data.enums.ConfigField;
import apolo.data.model.Configuration;
import apolo.data.model.Tenant;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long>, ConfigurationRepositoryCustom {
	
	List<Configuration> findByTenantOrderByFieldAsc(
			@Param("tenant") Tenant tenant
		);
	
	Page<Configuration> findByTenantOrderByFieldAsc(
			@Param("tenant") Tenant tenant, 
			Pageable page
		);
	
	Configuration findByTenantAndId(
			Tenant tenant,
			Long id
		);
	
	Configuration findByTenantAndField(
			Tenant tenant,
			ConfigField field
		);

}
