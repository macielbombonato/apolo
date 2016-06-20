package apolo.api.apimodel;

import apolo.api.apimodel.base.BaseAPIModel;
import apolo.data.enums.UserStatus;
import apolo.security.Permission;

import java.util.Set;

public class PermissionGroupDTO extends BaseAPIModel {

    private Long id;
    private String name;
    private Set<Permission> permissions;
    private UserStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
