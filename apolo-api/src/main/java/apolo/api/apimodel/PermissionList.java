package apolo.api.apimodel;

import apolo.api.apimodel.base.BaseAPIModel;
import apolo.security.Permission;

import java.util.List;

public class PermissionList extends BaseAPIModel {

    private List<Permission> permissions;

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
