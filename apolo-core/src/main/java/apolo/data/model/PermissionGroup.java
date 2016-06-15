package apolo.data.model;

import apolo.data.entitylistener.AuditLogListener;
import apolo.data.enums.UserStatus;
import apolo.data.model.base.AuditableBaseEntity;
import apolo.data.util.InputLength;
import apolo.security.Permission;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@EntityListeners(value = AuditLogListener.class)
@Table(name = "permission_group")
@AttributeOverride(name = "id", column = @Column(name = "group_id"))
public class PermissionGroup extends AuditableBaseEntity {

	private static final long serialVersionUID = -7985007135932159381L;
	
	@Column(name = "name", length = InputLength.MEDIUM, nullable = false)
	@NotNull
	@Size(min = 1, max = InputLength.MEDIUM)
	private String name;
	
	@ElementCollection(targetClass = Permission.class)
	@LazyCollection(LazyCollectionOption.FALSE)
	@CollectionTable(name = "permission_group_value",
			joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "group_id", insertable = true, updatable = true, nullable = false),
			uniqueConstraints = @UniqueConstraint(columnNames = {"group_id", "permission_name" })
	)
	@Type(type = "apolo.data.enums.usertype.PermissionUserType")
	@Column(name = "permission_name")
	@NotNull
	private Set<Permission> permissions;
	
	@Column(name = "userStatus", nullable = false)
	@Type(type = "apolo.data.enums.usertype.UserStatusUserType")
	private UserStatus userStatus;

	@ManyToMany(mappedBy = "groups", cascade = CascadeType.MERGE)
	@JsonIgnore
	private Set<User> users;

	public UserStatus getStatus() {
		return userStatus;
	}

	public void setStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
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

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> perms) {
		this.permissions = perms;
	}

}
