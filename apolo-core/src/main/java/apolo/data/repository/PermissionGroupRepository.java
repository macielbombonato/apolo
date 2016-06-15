package apolo.data.repository;

import apolo.data.model.PermissionGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PermissionGroupRepository extends JpaRepository<PermissionGroup, Long> {

	PermissionGroup findByName(
			String name
		);
	
	List<PermissionGroup> findByName(
			String name,
			Sort page
		);

	List<PermissionGroup> findAll(
			Sort page
	);
	
	Page<PermissionGroup> findByName(
			String name,
			Pageable page
		);
	
	List<PermissionGroup> findByNameNot(
			String name,
			Sort page
		);
	
	Page<PermissionGroup> findByNameNot(
			String name,
			Pageable page
		);
	
	Page<PermissionGroup> findByNameLikeOrderByNameAsc(
			@Param("name") String name,
			Pageable page
		);
	
}
