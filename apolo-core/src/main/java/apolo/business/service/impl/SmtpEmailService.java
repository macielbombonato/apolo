package apolo.business.service.impl;

import apolo.business.enums.EmailStatusType;
import apolo.business.model.EmailStatusReport;
import apolo.business.service.EmailService;
import apolo.common.config.model.ApplicationProperties;
import apolo.common.exception.BusinessException;
import apolo.common.util.MessageBundle;
import apolo.data.model.BaseEntity;
import apolo.data.model.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service("smtpEmailService")
public class SmtpEmailService<E extends BaseEntity> implements EmailService<E> {

    private static final Logger log = LoggerFactory.getLogger(SmtpEmailService.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Override
    public EmailStatusReport send(
            final Tenant tenant,
            final String fromName,
            final String from,
            final String toName,
            final String to,
            final String subject,
            final String message
    ) {
        EmailStatusReport result = new EmailStatusReport();

        Session session = createSession(tenant);

        // compose message
        try {
            MimeMessage mimeMessage = new MimeMessage(session);

            mimeMessage.setFrom(
                    new InternetAddress(
                            from,
                            fromName
                        )
            );

            mimeMessage.addRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(
                            to,
                            toName
                        )
            );

            mimeMessage.setSubject(subject);

            mimeMessage.setContent(message, "text/html; charset=utf-8");

            // send message
            Transport.send(mimeMessage);

            result.setStatus(EmailStatusType.SENT);

        } catch (BusinessException e) {
            log.error(e.getMessage(), e);

            result.setEmail(to);
            result.setStatus(EmailStatusType.ERROR);
            result.setRejectReason(e.getMessage());
        } catch (AddressException e) {
            log.error(e.getMessage(), e);

            result.setEmail(to);
            result.setStatus(EmailStatusType.ERROR);
            result.setRejectReason(e.getMessage());
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);

            result.setEmail(to);
            result.setStatus(EmailStatusType.ERROR);
            result.setRejectReason(e.getMessage());
        } catch (Throwable e) {
            log.error(e.getMessage(), e);

            result.setEmail(to);
            result.setStatus(EmailStatusType.ERROR);
            result.setRejectReason(e.getMessage());
        }

        return result;
    }

    @Override
    public List<EmailStatusReport> send(
            final Tenant tenant,
            final String fromName,
            final String from,
            final List<String> toList,
            final String subject,
            final String message
    ) {
        List<EmailStatusReport> resultList = new ArrayList<EmailStatusReport>();

        Session session = createSession(tenant);

        // compose message
        try {
            MimeMessage mimeMessage = new MimeMessage(session);

            mimeMessage.setFrom(
                    new InternetAddress(
                            from,
                            fromName
                        )
            );

            if (toList != null && !toList.isEmpty()) {
                List<Address> addresses = new ArrayList<Address>();
                for (String to : toList) {
                    if (to != null && !"".equals(to)) {
                        addresses.add(new InternetAddress(to));
                    }
                }

                mimeMessage.addRecipients(
                        Message.RecipientType.TO,
                        (Address[]) addresses.toArray()
                    );

                mimeMessage.setSubject(subject);

                mimeMessage.setContent(message, "text/html; charset=utf-8");

                // send message
                Transport.send(mimeMessage);

                EmailStatusReport result = new EmailStatusReport();
                result.setStatus(EmailStatusType.SENT);

                resultList.add(result);
            } else {
                EmailStatusReport result = new EmailStatusReport();
                result.setStatus(EmailStatusType.ERROR);
                result.setRejectReason(MessageBundle.getMessageBundle("mail.message.error.to"));

                resultList.add(result);
            }

        } catch (BusinessException e) {
            log.error(e.getMessage(), e);

            EmailStatusReport result = new EmailStatusReport();
            result.setStatus(EmailStatusType.ERROR);
            result.setRejectReason(e.getMessage());

            resultList.add(result);
        } catch (AddressException e) {
            log.error(e.getMessage(), e);

            EmailStatusReport result = new EmailStatusReport();
            result.setStatus(EmailStatusType.ERROR);
            result.setRejectReason(e.getMessage());

            resultList.add(result);
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);

            EmailStatusReport result = new EmailStatusReport();
            result.setStatus(EmailStatusType.ERROR);
            result.setRejectReason(e.getMessage());

            resultList.add(result);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);

            EmailStatusReport result = new EmailStatusReport();
            result.setStatus(EmailStatusType.ERROR);
            result.setRejectReason(e.getMessage());

            resultList.add(result);
        }

        return resultList;
    }

    @Override
    @Async
    public EmailStatusReport sendAsync(Tenant tenant, String fromName, String from, String toName, String to, String subject, String message) {
        return this.send(tenant, fromName, from, toName, to, subject, message);
    }

    @Override
    @Async
    public List<EmailStatusReport> sendAsync(Tenant tenant, String fromName, String from, List<String> toList, String subject, String message) {
        return this.send(tenant, fromName, from, toList, subject, message);
    }

    private Session createSession(Tenant tenant) {
        // Get the session object
        Properties props = new Properties();

        if (tenant != null && tenant.isUseTLS() != null && Boolean.TRUE.equals(tenant.isUseTLS())) {
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

        String emailUsername = tenant.getEmailUsername();

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

        return Session.getDefaultInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                USERNAME,
                                PASSWORD
                        );
                    }
                }
        );
    }
}