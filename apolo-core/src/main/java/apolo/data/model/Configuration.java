package apolo.data.model;

import apolo.data.entitylistener.AuditLogListener;
import apolo.data.util.InputLength;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@EntityListeners(value = AuditLogListener.class)
@Table(name = "configuration")
@AttributeOverride(name = "id", column = @Column(name = "configuration_id"))
public class Configuration extends AuditableBaseEntity {

	private static final long serialVersionUID = 7470619953861313459L;

	@ManyToOne
	private Tenant tenant;

	@Column(name = "email_from", length = InputLength.NAME, nullable = true)
	private String emailFrom;

	@Column(name = "email_password", length = InputLength.NAME, nullable = true)
	private String emailPassword;

	@Column(name = "smtp_host", length = InputLength.NAME, nullable = true)
	private String smtpHost;

	@Column(name = "smtp_port", length = InputLength.NAME, nullable = true)
	private String smtpPort;

	@Column(name = "use_tls", nullable = true)
	@Type(type="yes_no")
	private boolean useTLS;

	public Tenant getTenant() {
		return this.tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public String getEmailFrom() {
		return emailFrom;
	}

	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	public boolean isUseTLS() {
		return useTLS;
	}

	public void setUseTLS(boolean useTLS) {
		this.useTLS = useTLS;
	}
}
