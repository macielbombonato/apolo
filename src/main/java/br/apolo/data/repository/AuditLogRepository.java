package br.apolo.data.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.apolo.data.model.AuditLog;

public interface AuditLogRepository extends PagingAndSortingRepository<AuditLog, Long>, AuditLogRepositoryCustom {

}
