package apolo.business.service;

import apolo.business.model.EmailStatusReport;
import apolo.data.model.BaseEntity;
import apolo.data.model.Tenant;

import java.util.List;

public interface EmailService<E extends BaseEntity> {

    EmailStatusReport send(
            final Tenant tenant,
            final String fromName,
            final String from,
            final String toName,
            final String to,
            final String subject,
            final String message
    );

    List<EmailStatusReport> send(
            final Tenant tenant,
            final String fromName,
            final String from,
            final List<String> toList,
            final String subject,
            final String message
    );

    EmailStatusReport sendAsync(
            final Tenant tenant,
            final String fromName,
            final String from,
            final String toName,
            final String to,
            final String subject,
            final String message
    );

    List<EmailStatusReport> sendAsync(
            final Tenant tenant,
            final String fromName,
            final String from,
            final List<String> toList,
            final String subject,
            final String message
    );

}
