package apolo.data.model;

import apolo.data.entitylistener.AuditLogListener;
import apolo.data.enums.Status;
import apolo.data.util.InputLength;
import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

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

	@Column(name = "google_ad_client", length = InputLength.NAME, nullable = true)
	private String googleAdClient;

	@Column(name = "google_ad_slot_one", length = InputLength.NAME, nullable = true)
	private String googleAdSlotOne;

	@Column(name = "google_ad_slot_two", length = InputLength.NAME, nullable = true)
	private String googleAdSlotTwo;

	@Column(name = "google_ad_slot_three", length = InputLength.NAME, nullable = true)
	private String googleAdSlotThree;

	@Column(name = "google_analytics_user", length = InputLength.NAME, nullable = true)
	private String googleAnalyticsUserAccount;

	@Column(name = "auth_email", nullable = true)
	@Type(type="yes_no")
	private Boolean sendAuthEmail;
	
	@Transient
	private List<MultipartFile> logoFile;

    @Transient
    private List<MultipartFile> iconFile;

	public String getGoogleAdSlotThree() {
		return googleAdSlotThree;
	}

	public void setGoogleAdSlotThree(String googleAdSlotThree) {
		this.googleAdSlotThree = googleAdSlotThree;
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

	public String getGoogleAdClient() {
		return googleAdClient;
	}

	public void setGoogleAdClient(String googleAdClient) {
		this.googleAdClient = googleAdClient;
	}

	public String getGoogleAdSlotOne() {
		return googleAdSlotOne;
	}

	public void setGoogleAdSlotOne(String googleAdSlotOne) {
		this.googleAdSlotOne = googleAdSlotOne;
	}

	public String getGoogleAdSlotTwo() {
		return googleAdSlotTwo;
	}

	public void setGoogleAdSlotTwo(String googleAdSlotTwo) {
		this.googleAdSlotTwo = googleAdSlotTwo;
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

	public String getGoogleAnalyticsUserAccount() {
		return googleAnalyticsUserAccount;
	}

	public void setGoogleAnalyticsUserAccount(String googleAnalyticsUserAccount) {
		this.googleAnalyticsUserAccount = googleAnalyticsUserAccount;
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
}
