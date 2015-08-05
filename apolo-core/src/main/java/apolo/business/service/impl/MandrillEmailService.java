package apolo.business.service.impl;

import apolo.business.service.EmailService;
import apolo.data.model.BaseEntity;
import apolo.data.model.Tenant;
import org.springframework.stereotype.Service;

@Service("mandrillEmailService")
public class MandrillEmailService<E extends BaseEntity> implements EmailService<E> {

    @Override
    public String send(Tenant tenant, String from, String to, String subject, String message) {
        return null;
    }
}
