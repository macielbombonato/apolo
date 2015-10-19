package apolo.data.repository;

import apolo.data.model.Tenant;
import apolo.data.model.UserCustomField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserCustomFieldRepository extends JpaRepository<UserCustomField, Long> {

	Page<UserCustomField> findByTenantAndNameLikeOrderByNameAsc(
			@Param("tenant") Tenant tenant, 
			@Param("name") String name, 
			Pageable page
		);
	
	List<UserCustomField> findByTenantOrderByNameAsc(
			@Param("tenant") Tenant tenant
		);
	
	Page<UserCustomField> findByTenantOrderByNameAsc(
			@Param("tenant") Tenant tenant, 
			Pageable page
		);
	
	UserCustomField findByTenantAndId(
			Tenant tenant,
			Long id
		);
	
}
