package br.apolo.data.repository;

import org.springframework.data.repository.CrudRepository;

import br.apolo.data.model.AuditLog;

public interface AuditLogRepository extends CrudRepository<AuditLog, Long>, AuditLogRepositoryCustom {

}
