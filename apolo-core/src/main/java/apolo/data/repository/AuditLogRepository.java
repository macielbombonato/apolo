package apolo.data.repository;

import apolo.data.model.AuditLog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
	
	Page<AuditLog> findByEntityNameLike(
			String entityName, 
			Pageable page
		);
	
	AuditLog findById(Long id);

}
