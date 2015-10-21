package apolo.business.service;

import apolo.business.model.EmailStatusReport;
import apolo.data.model.base.BaseEntity;
import apolo.data.model.Tenant;

import java.util.List;

public interface EmailService<E extends BaseEntity> {

    EmailStatusReport send(
            Tenant tenant,
            String fromName,
            String from,
            String toName,
            String to,
            String subject,
            String message
    );

    List<EmailStatusReport> send(
            Tenant tenant,
            String fromName,
            String from,
            List<String> toList,
            String subject,
            String message
    );

    EmailStatusReport sendAsync(
            Tenant tenant,
            String fromName,
            String from,
            String toName,
            String to,
            String subject,
            String message
    );

    List<EmailStatusReport> sendAsync(
            Tenant tenant,
            String fromName,
            String from,
            List<String> toList,
            String subject,
            String message
    );

}
