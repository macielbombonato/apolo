package apolo.data.model;

import apolo.data.entitylistener.AuditLogListener;
import apolo.data.enums.Status;
import apolo.data.util.InputLength;
import apolo.security.UserPermission;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(value = AuditLogListener.class)
@Table(name = "application")
@AttributeOverride(name = "id", column = @Column(name = "application_id"))
public class Application extends AuditableBaseEntity {

	private static final long serialVersionUID = -7985007135932159381L;

	@Column(name = "name", length = InputLength.MEDIUM, nullable = false)
	@NotNull
	@Size(min = 1, max = InputLength.MEDIUM)
	private String name;

	@Column(name = "application_key", unique = true, length = InputLength.MEDIUM, nullable = false)
	private String applicationKey;

	@ManyToOne
	private Tenant tenant;

	@Column(name = "status", nullable = false)
	@Type(type = "apolo.data.enums.usertype.StatusUserType")
	private Status status;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "application_in_group",
			joinColumns = { @JoinColumn(name = "application_id", referencedColumnName = "application_id") },
			inverseJoinColumns = { @JoinColumn(name = "group_id", referencedColumnName = "user_group_id") })
	@NotNull
	private Set<UserGroup> groups;

	@Transient
	private Set<UserPermission> permissions;

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

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Set<UserGroup> getGroups() {
		return groups;
	}

	public void setGroups(Set<UserGroup> groups) {
		this.groups = groups;
	}

	public String getApplicationKey() {
		return applicationKey;
	}

	public void setApplicationKey(String applicationKey) {
		this.applicationKey = applicationKey;
	}
}
