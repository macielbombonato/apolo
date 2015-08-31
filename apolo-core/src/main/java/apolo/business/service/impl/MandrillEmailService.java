package apolo.business.service.impl;

import apolo.business.enums.EmailStatusType;
import apolo.business.model.EmailStatusReport;
import apolo.business.service.EmailService;
import apolo.common.config.model.ApplicationProperties;
import apolo.common.exception.BusinessException;
import apolo.common.util.MessageBundle;
import apolo.data.model.BaseEntity;
import apolo.data.model.Tenant;
import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.mail.Address;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service("mandrillEmailService")
public class MandrillEmailService<E extends BaseEntity> implements EmailService<E> {

    private static final Logger log = LoggerFactory.getLogger(MandrillEmailService.class);

    @Inject
    private ApplicationProperties applicationProperties;

    @Override
    public EmailStatusReport send(
                Tenant tenant,
                String fromName,
                String from,
                String toName,
                String to,
                String subject,
                String message) {
        EmailStatusReport result = new EmailStatusReport();

        String mandrillApiKey = createSession(tenant);

        MandrillApi mandrillApi = new MandrillApi(mandrillApiKey);

        MandrillMessage mandrillMessage = new MandrillMessage();
        mandrillMessage.setSubject(subject);

        mandrillMessage.setHtml(message);

        mandrillMessage.setAutoText(true);
        mandrillMessage.setFromName(fromName);
        mandrillMessage.setFromEmail(from);

        List<MandrillMessage.Recipient> recipients = new ArrayList<MandrillMessage.Recipient>();
        MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
        recipient.setName(toName);
        recipient.setEmail(to);

        recipients.add(recipient);

        mandrillMessage.setTo(recipients);
        mandrillMessage.setPreserveRecipients(true);

        try {
            MandrillMessageStatus[] messageStatusReports = mandrillApi.messages().send(mandrillMessage, false);

            if (messageStatusReports != null
                    && messageStatusReports.length > 0) {
                result.setId(messageStatusReports[0].getId());
                result.setEmail(messageStatusReports[0].getEmail());

                try {
                    // If the status is not mapped, the system will consider the status ERROR
                    result.setStatus(EmailStatusType.fromCode(messageStatusReports[0].getStatus()));
                } catch(Throwable emailStatusException) {
                    log.error(emailStatusException.getMessage(), emailStatusException);

                    result.setStatus(EmailStatusType.ERROR);
                }


                result.setRejectReason(messageStatusReports[0].getRejectReason());
            }
        } catch (MandrillApiError mandrillApiError) {
            log.error(mandrillApiError.getMessage(), mandrillApiError);

            result.setEmail(to);
            result.setStatus(EmailStatusType.ERROR);
            result.setRejectReason(mandrillApiError.getMandrillErrorMessage());
        } catch (IOException e) {
            log.error(e.getMessage(), e);

            result.setEmail(to);
            result.setStatus(EmailStatusType.ERROR);
            result.setRejectReason(e.getMessage());
        }

        return result;
    }

    private String createSession(Tenant tenant) {
        String mandrillApiKey = "";

        if (tenant != null
                && tenant.getEmailPassword() != null) {
            mandrillApiKey = tenant.getEmailPassword();
        } else {
            mandrillApiKey = applicationProperties.getEmailPassword();
        }

        if (mandrillApiKey == null || "".equals(mandrillApiKey)) {
            String errorMessage = MessageBundle.getMessageBundle("error.500.11");
            throw new BusinessException(11, errorMessage);
        }
        return mandrillApiKey;
    }

    @Override
    public List<EmailStatusReport> send(
                Tenant tenant,
                String fromName,
                String from,
                List<String> toList,
                String subject,
                String message) {
        List<EmailStatusReport> resultList = new ArrayList<EmailStatusReport>();

        String mandrillApiKey = createSession(tenant);

        MandrillApi mandrillApi = new MandrillApi(mandrillApiKey);

        MandrillMessage mandrillMessage = new MandrillMessage();
        mandrillMessage.setSubject(subject);

        mandrillMessage.setHtml(message);

        mandrillMessage.setAutoText(true);
        mandrillMessage.setFromName(fromName);
        mandrillMessage.setFromEmail(from);


        List<MandrillMessage.Recipient> recipients = new ArrayList<MandrillMessage.Recipient>();

        if (toList != null && !toList.isEmpty()) {
            List<Address> addresses = new ArrayList<Address>();
            for (String to : toList) {
                if (to != null && !"".equals(to)) {
                    MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
                    recipient.setEmail(to);
                    //recipient.setName("Claire Annette");

                    recipients.add(recipient);
                }
            }

            mandrillMessage.setTo(recipients);
            mandrillMessage.setPreserveRecipients(true);

            try {
                MandrillMessageStatus[] messageStatusReports = mandrillApi.messages().send(mandrillMessage, false);

                if (messageStatusReports != null
                        && messageStatusReports.length > 0) {
                    EmailStatusReport result = null;

                    for (int i = 0; i < messageStatusReports.length; i++) {
                        result = new EmailStatusReport();

                        result.setId(messageStatusReports[i].getId());
                        result.setEmail(messageStatusReports[i].getEmail());

                        try {
                            // If the status is not mapped, the system will consider the status ERROR
                            result.setStatus(EmailStatusType.fromCode(messageStatusReports[i].getStatus()));
                        } catch(Throwable emailStatusException) {
                            log.error(emailStatusException.getMessage(), emailStatusException);

                            result.setStatus(EmailStatusType.ERROR);
                        }

                        result.setRejectReason(messageStatusReports[i].getRejectReason());

                        resultList.add(result);
                    }

                } else {
                    EmailStatusReport result = new EmailStatusReport();
                    result.setStatus(EmailStatusType.ERROR);

                    resultList.add(result);
                }
            } catch (MandrillApiError mandrillApiError) {
                log.error(mandrillApiError.getMessage(), mandrillApiError);

                EmailStatusReport result = new EmailStatusReport();
                result.setStatus(EmailStatusType.ERROR);
                result.setRejectReason(mandrillApiError.getMandrillErrorMessage());

                resultList.add(result);
            } catch (IOException e) {
                log.error(e.getMessage(), e);

                EmailStatusReport result = new EmailStatusReport();
                result.setStatus(EmailStatusType.ERROR);
                result.setRejectReason(e.getMessage());

                resultList.add(result);
            }
        } else {
            EmailStatusReport result = new EmailStatusReport();
            result.setStatus(EmailStatusType.ERROR);
            result.setRejectReason(MessageBundle.getMessageBundle("mail.message.error.to"));

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
}
