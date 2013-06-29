package br.apolo.data.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.apolo.common.util.InputLength;
import br.apolo.data.entitylistener.AuditLogListener;
import br.apolo.security.UserPermission;

@Entity
@EntityListeners(value = AuditLogListener.class)
@Table(name = "user")
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
@NamedQueries({ @NamedQuery(name = "User.findByLogin", query = " FROM User WHERE email = :login") })
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

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_in_groups", 
			joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "user_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "group_id", referencedColumnName = "user_group_id") })
	@NotNull
	private Set<UserGroup> groups;

	@Transient
	private Set<UserPermission> permissions;

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
}
