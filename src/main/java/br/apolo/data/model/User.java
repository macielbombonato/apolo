package br.apolo.data.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

import br.apolo.common.util.InputLength;
import br.apolo.data.entitylistener.AuditLogListener;
import br.apolo.data.enums.Status;
import br.apolo.data.enums.UserPermission;

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

	@Column(name = "password", length = InputLength.MEDIUM, nullable = false)
	private String password;
	
	@Column(name = "status", nullable = false)
	@Type(type = "br.apolo.data.enums.usertype.StatusUserType")
	private Status status;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_in_groups", 
			joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "user_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "group_id", referencedColumnName = "user_group_id") })
	@NotNull
	private Set<UserGroup> groups;

	@Transient
	private Set<UserPermission> permissions;
	
	@Column(name = "pic_original_name", length = InputLength.MEDIUM, nullable = true)
	private String pictureOriginalName;

	@Column(name = "pic_generated_name", length = InputLength.MEDIUM, nullable = true)
	private String pictureGeneratedName;
	
	@Transient
	private List<MultipartFile> picturefiles;

	public String getName() {
		return name;
	}

	public void setName(String value) {
		name = value;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password1) {
		this.password = password1;
	}

	public Set<UserGroup> getGroups() {
		return groups;
	}

	public void setGroups(Set<UserGroup> groups1) {
		this.groups = groups1;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPictureOriginalName() {
		return pictureOriginalName;
	}

	public void setPictureOriginalName(String pictureOriginalName) {
		this.pictureOriginalName = pictureOriginalName;
	}

	public String getPictureGeneratedName() {
		return pictureGeneratedName;
	}

	public void setPictureGeneratedName(String pictureGeneratedName) {
		this.pictureGeneratedName = pictureGeneratedName;
	}

	public List<MultipartFile> getPicturefiles() {
		return picturefiles;
	}

	public void setPicturefiles(List<MultipartFile> picturefiles) {
		this.picturefiles = picturefiles;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
