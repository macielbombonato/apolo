package apolo.data.repository;

import apolo.data.enums.Status;
import apolo.data.model.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

	Page<Tenant> findByUrlLikeOrNameLikeOrderByNameAsc(
			@Param("url") String url,
			@Param("name") String name,
			Pageable page
	);

	Tenant findByUrl(String url);

	Page<Tenant> findByStatusOrderByNameAsc(
			Status status,
			Pageable page
	);

	Page<Tenant> findByStatusNotOrderByNameAsc(
			Status status,
			Pageable page
	);

	List<Tenant> findByStatusNotOrderByNameAsc(
			Status status
	);

	long count();

}
