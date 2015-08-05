package apolo.business.service;

import apolo.data.model.BaseEntity;
import apolo.data.model.Tenant;

public interface EmailService<E extends BaseEntity> {

    String send(
            final Tenant tenant,
            final String from,
            final String to,
            final String subject,
            final String message
    );

}
