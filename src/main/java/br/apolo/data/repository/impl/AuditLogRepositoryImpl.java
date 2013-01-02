package br.apolo.data.repository.impl;

import org.springframework.stereotype.Repository;

import br.apolo.data.model.AuditLog;
import br.apolo.data.repository.AuditLogRepositoryCustom;

@Repository(value = "auditLogRepository")
public class AuditLogRepositoryImpl extends BaseRepotitoryImpl<AuditLog> implements AuditLogRepositoryCustom {

}
