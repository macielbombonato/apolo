package apolo.business.model;

import apolo.business.enums.EmailStatusType;

public class EmailStatusReport {

    private String id;
    private String email;
    private EmailStatusType status;
    private String rejectReason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EmailStatusType getStatus() {
        return status;
    }

    public void setStatus(EmailStatusType status) {
        this.status = status;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
}
