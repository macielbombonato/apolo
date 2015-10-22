package apolo.data.model;

import apolo.business.service.TenantService;
import apolo.business.service.UserGroupService;
import apolo.data.entitylistener.AuditLogListener;
import apolo.data.enums.UserStatus;
import apolo.data.model.base.AuditableBaseEntity;
import apolo.data.util.InputLength;
import apolo.security.UserPermission;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import org.springframework.context.ApplicationContext;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@EntityListeners(value = AuditLogListener.class)
@Table(name = "users")
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
public class User extends AuditableBaseEntity {

	private static final long serialVersionUID = 5588722501578237833L;
	
	@Column(name = "name", length = InputLength.NAME, nullable = false)
	@NotNull
	@Size(min = 1, max = InputLength.NAME)
	private String name;

	@Column(name = "email", unique = true, length = InputLength.NAME, nullable = false)
	@NotNull
	@Size(min = 1, max = InputLength.NAME)
	private String email;
	
	@Column(name = "encrypted_password", length = InputLength.MEDIUM, nullable = false)
	@JsonIgnore
	private String password;

	@Column(name = "mobile", unique = false, length = InputLength.NAME, nullable = true)
	@Size(max = InputLength.NAME)
	private String mobile;
	
	@Column(name = "status", nullable = false)
	@Type(type = "apolo.data.enums.usertype.UserStatusUserType")
	private UserStatus userStatus;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_in_groups", 
			joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "user_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "group_id", referencedColumnName = "user_group_id") })
	@NotNull
	@JsonIgnore
	private Set<UserGroup> groups;

	@Transient
	@JsonIgnore
	private Set<UserPermission> permissions;
	
	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<UserCustomFieldValue> customFields;
	
	@ManyToOne
	@JsonIgnore
	private Tenant tenant;
	
	@Column(name = "avatar_original_name", length = InputLength.MEDIUM, nullable = true)
	private String avatarOriginalName;

	@Column(name = "avatar_file_name", length = InputLength.MEDIUM, nullable = true)
	private String avatarFileName;

	@Column(name = "avatar_content_type", length = InputLength.MEDIUM, nullable = true)
	private String avatarContentType;

	@Column(name = "avatar_file_size", nullable = true)
	private Long avatarFileSize;

	@Column(name = "avatar_updated_at")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
	private Date avatarUpdatedAt;

	@Column(name = "enabled", nullable = true, columnDefinition = "int default 1")
	private Boolean enabled;
	
	@Transient
	@JsonIgnore
	private List<MultipartFile> picturefiles;
	
	@Transient
	@JsonIgnore
	private Tenant dbTenant;

	@Column(name = "reset_password_token", length = InputLength.MEDIUM, nullable = true)
	@JsonIgnore
	private String resetPasswordToken;

	@Column(name = "reset_password_sent_at")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
	private Date resetPasswordSentAt;

	@Column(name = "remember_created_at")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
	private Date rememberCreatedAt;

	@Column(name = "sign_in_count", nullable = true)
	private Integer signInCount;

	@Column(name = "current_sign_in_at")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
	private Date currentSignInAt;

	@Column(name = "last_sign_in_at")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
	private Date lastSignInAt;

	@Column(name = "current_sign_in_ip", length = InputLength.MEDIUM, nullable = true)
	private String currentSignInIp;

	@Column(name = "last_sign_in_ip", length = InputLength.MEDIUM, nullable = true)
	private String lastSignInIp;

	public Long getTenantId() {
		Long result = null;

		if (this.tenant != null) {
			result = this.tenant.getId();
		}

		return result;
	}

	public void setTenantId(Long id) {
		if (this.tenant == null && id != null) {
			ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
			TenantService tenantService = (TenantService) ctx.getBean("tenantService");

			this.tenant = tenantService.find(id);
		}
	}

	public List<Long> getGroupIds() {
		List<Long> ids = null;

		if (this.groups != null
				&& !this.groups.isEmpty()) {

			ids = new ArrayList<Long>();

			for(UserGroup group : this.groups) {
				ids.add(group.getId());
			}
		}

		return ids;
	}

	public void setGroupIds(List<Long> ids) {
		if (this.groups == null
				&& ids != null
				&& !ids.isEmpty()) {
			ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
			UserGroupService userGroupService = (UserGroupService) ctx.getBean("userGroupService");

			this.groups = new HashSet<UserGroup>();
			for (Long id : ids) {
				this.groups.add(userGroupService.find(id));
			}
		}
	}
	
	public Tenant getDbTenant() {
		Tenant result = null; 
		
		if (this.dbTenant != null) {
			result = this.dbTenant;
		} else {
			this.dbTenant = this.tenant;
			result = this.tenant;
		}
		
		return result;
	}
	
	public Set<UserPermission> getPermissions() {
		if ((permissions == null || permissions.isEmpty()) && groups != null) {
			permissions = new HashSet<UserPermission>();
			for (UserGroup ug : groups) {
				if (ug.getPermissions() != null && !ug.getPermissions().isEmpty()) {
					permissions.addAll(ug.getPermissions());
				}
			}
		}
		return permissions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public UserStatus getStatus() {
		return userStatus;
	}

	public void setStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public Set<UserGroup> getGroups() {
		return groups;
	}

	public void setGroups(Set<UserGroup> groups) {
		this.groups = groups;
	}

	public List<UserCustomFieldValue> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(List<UserCustomFieldValue> customFields) {
		this.customFields = customFields;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public String getAvatarOriginalName() {
		return avatarOriginalName;
	}

	public void setAvatarOriginalName(String avatarOriginalName) {
		this.avatarOriginalName = avatarOriginalName;
	}

	public String getAvatarFileName() {
		return avatarFileName;
	}

	public void setAvatarFileName(String avatarFileName) {
		this.avatarFileName = avatarFileName;
	}

	public String getAvatarContentType() {
		return avatarContentType;
	}

	public void setAvatarContentType(String avatarContentType) {
		this.avatarContentType = avatarContentType;
	}

	public Long getAvatarFileSize() {
		return avatarFileSize;
	}

	public void setAvatarFileSize(Long avatarFileSize) {
		this.avatarFileSize = avatarFileSize;
	}

	public Date getAvatarUpdatedAt() {
		return avatarUpdatedAt;
	}

	public void setAvatarUpdatedAt(Date avatarUpdatedAt) {
		this.avatarUpdatedAt = avatarUpdatedAt;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<MultipartFile> getPicturefiles() {
		return picturefiles;
	}

	public void setPicturefiles(List<MultipartFile> picturefiles) {
		this.picturefiles = picturefiles;
	}

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	public Date getResetPasswordSentAt() {
		return resetPasswordSentAt;
	}

	public void setResetPasswordSentAt(Date resetPasswordSentAt) {
		this.resetPasswordSentAt = resetPasswordSentAt;
	}

	public Date getRememberCreatedAt() {
		return rememberCreatedAt;
	}

	public void setRememberCreatedAt(Date rememberCreatedAt) {
		this.rememberCreatedAt = rememberCreatedAt;
	}

	public Integer getSignInCount() {
		return signInCount;
	}

	public void setSignInCount(Integer signInCount) {
		this.signInCount = signInCount;
	}

	public Date getCurrentSignInAt() {
		return currentSignInAt;
	}

	public void setCurrentSignInAt(Date currentSignInAt) {
		this.currentSignInAt = currentSignInAt;
	}

	public Date getLastSignInAt() {
		return lastSignInAt;
	}

	public void setLastSignInAt(Date lastSignInAt) {
		this.lastSignInAt = lastSignInAt;
	}

	public String getCurrentSignInIp() {
		return currentSignInIp;
	}

	public void setCurrentSignInIp(String currentSignInIp) {
		this.currentSignInIp = currentSignInIp;
	}

	public void setDbTenant(Tenant dbTenant) {
		this.dbTenant = dbTenant;
	}

	public String getLastSignInIp() {
		return lastSignInIp;
	}

	public void setLastSignInIp(String lastSignInIp) {
		this.lastSignInIp = lastSignInIp;
	}
}
