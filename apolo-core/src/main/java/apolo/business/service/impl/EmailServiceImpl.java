package apolo.business.service.impl;

import apolo.business.service.EmailService;
import apolo.common.config.model.ApplicationProperties;
import apolo.common.exception.BusinessException;
import apolo.common.util.MessageBundle;
import apolo.data.model.BaseEntity;
import apolo.data.model.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@SuppressWarnings("rawtypes")
@Service("emailService")
public class EmailServiceImpl<E extends BaseEntity> implements EmailService<E> {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Override
    public String send(
            final Tenant tenant,
            final String subject,
            final String target,
            final String message
    ) {
        String result = "";

        // Get the session object
        Properties props = new Properties();

        if (tenant.isUseTLS()) {
            props.put(
                    "mail.smtp.starttls.enable",
                    "true"
            );
        }

        String smtpHost = tenant.getSmtpHost();

        if (smtpHost == null || "".equals(smtpHost)) {
            smtpHost = applicationProperties.getSmtpHost();
        }

        if (smtpHost != null) {
            props.put(
                    "mail.smtp.host",
                    smtpHost
            );
        } else {
            throw new BusinessException(1, MessageBundle.getMessageBundle("error.500.7"));
        }

        String smtpPort = tenant.getSmtpPort();

        if (smtpPort == null || "".equals(smtpPort)) {
            smtpPort = applicationProperties.getSmtpPort();
        }

        if (smtpPort != null) {
            props.put(
                    "mail.smtp.port",
                    smtpPort
            );
            props.put(
                    "mail.smtp.socketFactory.port",
                    smtpPort
            );
        } else {
            throw new BusinessException(2, MessageBundle.getMessageBundle("error.500.8"));
        }

        props.put(
                "mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory"
        );

        props.put(
                "mail.smtp.auth",
                "true"
        );

        String emailUsername = tenant.getEmailFrom();

        if (emailUsername == null || "".equals(emailUsername)) {
            emailUsername = applicationProperties.getEmailFrom();
        }

        String emailPassword = tenant.getEmailPassword();

        if (emailPassword == null || "".equals(emailPassword)) {
            emailPassword = applicationProperties.getEmailPassword();
        }

        if (emailUsername == null) {
            throw new BusinessException(3, MessageBundle.getMessageBundle("error.500.9"));
        } else if (emailPassword == null) {
            throw new BusinessException(4, MessageBundle.getMessageBundle("error.500.10"));
        }

        final String USERNAME = emailUsername;
        final String PASSWORD = emailPassword;

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                USERNAME,
                                PASSWORD
                        );
                    }
                }
        );

        // compose message
        try {
            MimeMessage mimeMessage = new MimeMessage(session);

            mimeMessage.setFrom(
                    new InternetAddress(
                            USERNAME
                    )
            );

            mimeMessage.addRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(target)
            );

            mimeMessage.setSubject(subject);

            mimeMessage.setText(message);

            // send message
            Transport.send(mimeMessage);

            result = "Success";

        } catch (BusinessException e) {
            log.error(e.getMessage(), e);

            result = "Error: " + e.getMessage();
        } catch (AddressException e) {
            log.error(e.getMessage(), e);

            result = "Error: " + e.getMessage();
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);

            result = "Error: " + e.getMessage();
        }

        return result;
    }

}
