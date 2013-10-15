package br.apolo.data.model;

import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import br.apolo.common.util.InputLength;
import br.apolo.data.entitylistener.AuditLogListener;
import br.apolo.data.enums.Status;
import br.apolo.data.enums.UserPermission;

@Entity
@EntityListeners(value = AuditLogListener.class)
@Table(name = "user_group")
@AttributeOverride(name = "id", column = @Column(name = "user_group_id"))
public class UserGroup extends AuditableBaseEntity {

	private static final long serialVersionUID = -7985007135932159381L;
	
	@Column(name = "name", length = InputLength.MEDIUM, nullable = false)
	@NotNull
	@Size(min = 1, max = InputLength.MEDIUM)
	private String name;

	@ElementCollection(targetClass = UserPermission.class)
	@LazyCollection(LazyCollectionOption.FALSE)
	@CollectionTable(name = "group_permission", 
			joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "user_group_id", insertable = true, updatable = true, nullable = false), 
			uniqueConstraints = @UniqueConstraint(columnNames = {"group_id", "permission_name" }, name = "uq_group_permission")
	)
	@Type(type = "br.apolo.data.enums.usertype.UserPermissionUserType")
	@Column(name = "permission_name")
	@NotNull
	private Set<UserPermission> permissions;
	
	@Column(name = "status", nullable = false)
	@Type(type = "br.apolo.data.enums.usertype.StatusUserType")
	private Status status;

	@ManyToMany(mappedBy = "groups", cascade = CascadeType.MERGE)
	private Set<User> users;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

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
