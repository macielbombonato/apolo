package br.apolo.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import br.apolo.data.model.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long>, AuditLogRepositoryCustom {
	
	Page<AuditLog> findByEntityNameLikeOrderByEntityNameAscOperationDateDesc(@Param("entityName") String entityName, Pageable page);

}
