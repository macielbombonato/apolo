package apolo.data.repository;

import apolo.data.model.Tenant;
import apolo.data.model.UserGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

	UserGroup findByName(
			String name
		);
	
	UserGroup findByTenantAndName(
			Tenant tenant, 
			String name
		);
	
	UserGroup findByTenantAndId(
			Tenant tenant, 
			Long id
		);
	
	List<UserGroup> findByTenant(
			Tenant tenant,
			Sort page
		);
	
	List<UserGroup> findByTenantOrName(
			Tenant tenant,
			String name,
			Sort page
		);
	
	Page<UserGroup> findByTenantOrName(
			Tenant tenant,
			String name,
			Pageable page
		);
	
	List<UserGroup> findByTenantAndNameNot(
			Tenant tenant,
			String name,
			Sort page
		);
	
	Page<UserGroup> findByTenantAndNameNot(
			Tenant tenant,
			String name,
			Pageable page
		);
	
	Page<UserGroup> findByTenant(
			Tenant tenant,
			Pageable page
		);
	
	Page<UserGroup> findByTenantAndNameLikeOrderByNameAsc(
			@Param("tenant") Tenant tenant,
			@Param("name") String name, 
			Pageable page
		);
	
}
