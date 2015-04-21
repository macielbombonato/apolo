package apolo.data.repository.impl;

import apolo.data.model.AuditLog;
import apolo.data.repository.AuditLogRepositoryCustom;

import org.springframework.stereotype.Repository;

@Repository(value = "auditLogRepository")
public class AuditLogRepositoryImpl extends BaseRepotitoryImpl<AuditLog> implements AuditLogRepositoryCustom {

}
