package apolo.data.model;

import apolo.common.config.model.ApplicationProperties;
import apolo.data.entitylistener.AuditLogListener;
import apolo.data.enums.Skin;
import apolo.data.enums.Status;
import apolo.data.model.base.AuditableBaseEntity;
import apolo.data.util.InputLength;
import org.hibernate.annotations.Type;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

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

	@Column(name = "logo", length = InputLength.MEDIUM, nullable = true)
	private String logo;

	@Column(name = "icon", length = InputLength.MEDIUM, nullable = true)
	private String icon;

	@Column(name = "logo_width", nullable = true)
	@Max(160)
	private Integer logoWidth;

	@Column(name = "logo_height", nullable = true)
	@Max(40)
	private Integer logoHeight;

	@Column(name = "skin", nullable = false)
	@Type(type = "apolo.data.enums.usertype.SkinUserType")
	@NotNull
	private Skin skin;

	@Column(name = "status", nullable = false)
	@Type(type = "apolo.data.enums.usertype.StatusUserType")
	@NotNull
	private Status status;

	@Column(name = "has_show_name", nullable = true)
	@Type(type="yes_no")
	private Boolean showName;

	@Column(name = "has_show_adds", nullable = true)
	@Type(type="yes_no")
	private Boolean showAdds;

	@Column(name = "email_from", length = InputLength.NAME, nullable = true)
	private String emailFrom;

	@Column(name = "email_username", length = InputLength.NAME, nullable = true)
	private String emailUsername;

	@Column(name = "email_password", length = InputLength.NAME, nullable = true)
	private String emailPassword;

	@Column(name = "smtp_host", length = InputLength.NAME, nullable = true)
	private String smtpHost;

	@Column(name = "smtp_port", length = InputLength.NAME, nullable = true)
	private String smtpPort;

	@Column(name = "use_tls", nullable = true)
	@Type(type="yes_no")
	private Boolean useTLS;

	@Column(name = "auth_email", nullable = true)
	@Type(type="yes_no")
	private Boolean sendAuthEmail;

	@Column(name = "email_use_tenant_config", nullable = true)
	@Type(type="yes_no")
	private Boolean emailUseTenantConfig;

	@Transient
	private List<MultipartFile> logoFile;

	@Transient
	private List<MultipartFile> iconFile;

	@Transient
	@Inject
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

	public String getGoogleAnalyticsUserAccount() {
		return getApplicationProperties().getGoogleAnalyticsUserAccount();
	}

	public String getGoogleAdClient() {
		return getApplicationProperties().getGoogleAdClient();
	}

	public String getGoogleAdSlotOne() {
		return getApplicationProperties().getGoogleAdSlotOne();
	}

	public String getGoogleAdSlotTwo() {
		return getApplicationProperties().getGoogleAdSlotTwo();
	}

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

	public Boolean isUseTLS() {
		return useTLS;
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

	public String getLogo() {
		return this.logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Integer getLogoWidth() {
		return this.logoWidth;
	}

	public void setLogoWidth(Integer logoWidth) {
		this.logoWidth = logoWidth;
	}

	public Integer getLogoHeight() {
		return this.logoHeight;
	}

	public void setLogoHeight(Integer logoHeight) {
		this.logoHeight = logoHeight;
	}

	public List<MultipartFile> getLogoFile() {
		return this.logoFile;
	}

	public void setLogoFile(List<MultipartFile> logoFile) {
		this.logoFile = logoFile;
	}

	public Boolean isShowName() {
		return this.showName;
	}

	public Boolean getShowName() {
		return this.showName;
	}

	public void setShowName(Boolean showName) {
		this.showName = showName;
	}

	public Boolean isShowAdds() {
		return this.showAdds;
	}

	public Boolean getShowAdds() {
		return this.showAdds;
	}

	public void setShowAdds(Boolean showAdds) {
		this.showAdds = showAdds;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<MultipartFile> getIconFile() {
		return iconFile;
	}

	public void setIconFile(List<MultipartFile> iconFile) {
		this.iconFile = iconFile;
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

	public Skin getSkin() {
		return skin;
	}

	public void setSkin(Skin skin) {
		this.skin = skin;
	}

	public Boolean getEmailUseTenantConfig() {
		return emailUseTenantConfig;
	}

	public void setEmailUseTenantConfig(Boolean emailUseTenantConfig) {
		this.emailUseTenantConfig = emailUseTenantConfig;
	}
}
