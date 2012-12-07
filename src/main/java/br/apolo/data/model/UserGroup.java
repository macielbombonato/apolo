package br.apolo.data.model;

import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import br.apolo.common.util.InputLength;
import br.apolo.security.UserPermission;

@Entity
@Table(name = "user_group")
@AttributeOverride(name = "id", column = @Column(name = "user_group_id"))
public class UserGroup extends BaseEntity {

	private static final long serialVersionUID = -7985007135932159381L;

	@Column(name = "name", length = InputLength.MEDIUM, nullable = false)
	private String name;

	@ElementCollection(targetClass = UserPermission.class)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "group_permission", joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "user_group_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
			"group_id", "permission_name" }, name = "uq_group_permission"))
	@Column(name = "permission_name")
	// Column name in the table
	private Set<UserPermission> permissions;

	@ManyToMany(mappedBy = "groups")
	private Set<User> users;

	public String getName() {
		return name;
	}

	public void setName(String value) {
		name = value;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users1) {
		this.users = users1;
	}

	public Set<UserPermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<UserPermission> perms) {
		this.permissions = perms;
	}

}