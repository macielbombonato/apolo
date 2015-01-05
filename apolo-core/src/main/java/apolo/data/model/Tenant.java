package apolo.data.model;

import apolo.data.entitylistener.AuditLogListener;
import apolo.data.enums.Language;
import apolo.data.enums.Skin;
import apolo.data.enums.Status;
import apolo.data.util.InputLength;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

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
	
	@Column(name = "language", nullable = false)
	@Type(type = "apolo.data.enums.usertype.LanguageUserType")
	@NotNull
	private Language language;
	
	@Column(name = "status", nullable = false)
	@Type(type = "apolo.data.enums.usertype.StatusUserType")
	@NotNull
	private Status status;
	
	@Transient
	private List<MultipartFile> logoFile;

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

	public Skin getSkin() {
		return this.skin;
	}

	public void setSkin(Skin skin) {
		this.skin = skin;
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

	public Language getLanguage() {
		return this.language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}
	

}