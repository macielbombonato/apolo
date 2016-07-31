package apolo.data.model;

import apolo.common.config.model.ApplicationProperties;
import apolo.data.entitylistener.AuditLogListener;
import apolo.data.enums.Status;
import apolo.data.model.base.AuditableBaseEntity;
import apolo.data.util.InputLength;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import javax.inject.Inject;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@EntityListeners(value = AuditLogListener.class)
@Table(name = "tenant")
@AttributeOverride(name = "id", column = @Column(name = "tenant_id"))
public class Tenant extends AuditableBaseEntity {

	private static final long serialVersionUID = 7470619953861313459L;

	@Column(name = "url", length = InputLength.TINY, nullable = false, unique = true)
	@NotNull
	@Size(min = 1, max = InputLength.TINY)
	private String url;

	@Column(name = "name", length = InputLength.NAME, nullable = false)
	@NotNull
	@Size(min = 1, max = InputLength.NAME)
	private String name;

	@Column(name = "theme", length = InputLength.MEDIUM, nullable = true)
	private String theme;

	@Column(name = "status", nullable = false)
	@Type(type = "apolo.data.enums.usertype.StatusUserType")
	@NotNull
	private Status status;

	@Column(name = "has_show_ads", nullable = true)
	@Type(type="yes_no")
	private Boolean showAds;

	@Column(name = "email_from", length = InputLength.NAME, nullable = true)
	private String emailFrom;

	@Column(name = "email_username", length = InputLength.NAME, nullable = true)
	@JsonIgnore
	private String emailUsername;

	@Column(name = "email_password", length = InputLength.NAME, nullable = true)
	@JsonIgnore
	private String emailPassword;

	@Column(name = "smtp_host", length = InputLength.NAME, nullable = true)
	@JsonIgnore
	private String smtpHost;

	@Column(name = "smtp_port", length = InputLength.NAME, nullable = true)
	@JsonIgnore
	private String smtpPort;

	@Column(name = "use_tls", nullable = true)
	@Type(type="yes_no")
	@JsonIgnore
	private Boolean useTLS;

	@Column(name = "auth_email", nullable = true)
	@Type(type="yes_no")
	private Boolean sendAuthEmail;

	@Column(name = "email_use_tenant_config", nullable = true)
	@Type(type="yes_no")
	@JsonIgnore
	private Boolean emailUseTenantConfig;

	@Transient
	@Inject
	@JsonIgnore
	private ApplicationProperties applicationProperties;

	public ApplicationProperties getApplicationProperties() {
		ApplicationProperties result = null;

		if (applicationProperties != null) {
			result = applicationProperties;
		} else {
			ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();

			applicationProperties = (ApplicationProperties) ctx.getBean("applicationProperties");

			result = applicationProperties;
		}

		return result;
	}

	@JsonIgnore
	public String getGoogleAnalyticsUserAccount() {
		return getApplicationProperties().getGoogleAnalyticsUserAccount();
	}

	@JsonIgnore
	public String getGoogleAdClient() {
		return getApplicationProperties().getGoogleAdClient();
	}

	@JsonIgnore
	public String getGoogleAdSlotOne() {
		return getApplicationProperties().getGoogleAdSlotOne();
	}

	@JsonIgnore
	public String getGoogleAdSlotTwo() {
		return getApplicationProperties().getGoogleAdSlotTwo();
	}

	@JsonIgnore
	public String getGoogleAdSlotThree() {
		return getApplicationProperties().getGoogleAdSlotThree();
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

	public Boolean getUseTLS() {
		return useTLS;
	}

	public void setUseTLS(Boolean useTLS) {
		this.useTLS = useTLS;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Boolean getShowAds() {
		return showAds;
	}

	public void setShowAds(Boolean showAds) {
		this.showAds = showAds;
	}

	public Boolean getSendAuthEmail() {
		return sendAuthEmail;
	}

	public void setSendAuthEmail(Boolean sendAuthEmail) {
		this.sendAuthEmail = sendAuthEmail;
	}

	public String getEmailUsername() {
		return emailUsername;
	}

	public void setEmailUsername(String emailUsername) {
		this.emailUsername = emailUsername;
	}

	public Boolean getEmailUseTenantConfig() {
		return emailUseTenantConfig;
	}

	public void setEmailUseTenantConfig(Boolean emailUseTenantConfig) {
		this.emailUseTenantConfig = emailUseTenantConfig;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}
}
