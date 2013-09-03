package br.apolo.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.apolo.data.model.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long>, AuditLogRepositoryCustom {

}
