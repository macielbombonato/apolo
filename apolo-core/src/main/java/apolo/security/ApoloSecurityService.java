package apolo.security;

import apolo.business.service.UserService;
import apolo.data.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Set;

@Component("apoloSecurity")
public class ApoloSecurityService {

    private static final Logger log = LoggerFactory.getLogger(ApoloSecurityService.class);

    @Inject
    private UserService userService;

    public boolean hasPermission(Set<Permission> permissionList, String...userPermissions) {
        boolean result = false;

        if (userPermissions != null && userPermissions.length > 0) {
            if (permissionList != null
                    && permissionList.contains(Permission.ADMIN)) {
                result = true;
            } else {
                for (String userPermission : userPermissions) {
                    try {
                        Permission permission = Permission.fromCode(userPermission);

                        if (permissionList != null
                                && permissionList.contains(permission)) {
                            result = true;
                            break;
                        }
                    } catch(Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }

        return result;
    }

    public boolean hasPermission(Set<Permission> permissionList, Permission... permissions) {
        boolean result = false;

        if (permissions != null && permissions.length > 0) {
            if (permissionList != null
                    && permissionList.contains(Permission.ADMIN)) {
                result = true;
            } else {
                for (Permission permission : permissions) {
                    try {
                        if (permissionList != null
                                && permissionList.contains(permission)) {
                            result = true;
                            break;
                        }
                    } catch(Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }

        return result;
    }

    public boolean hasPermission(String...userPermissions) {
        boolean result = false;

        if (userPermissions != null && userPermissions.length > 0) {
            User user = userService.getAuthenticatedUser();

            if (user != null) {
                result = this.hasPermission(user.getPermissions(), userPermissions);
            }

            if (result
                    && user != null
                    && user.getPermissions() != null
                    && user.getTenant().equals(user.getDbTenant())
                    && !user.getPermissions().contains(Permission.ADMIN)
                    && !user.getPermissions().contains(Permission.TENANT_MANAGER)
                    && !user.getTenant().equals(user.getDbTenant())) {
                result = false;
            }
        }

        return result;
    }

    public boolean hasPermission(Permission... permissions) {
        boolean result = false;

        if (permissions != null && permissions.length > 0) {
            User user = userService.getAuthenticatedUser();

            if (user != null) {
                result = this.hasPermission(user.getPermissions(), permissions);
            }

            if (result
                    && user != null
                    && user.getPermissions() != null
                    && user.getTenant().equals(user.getDbTenant())
                    && !user.getPermissions().contains(Permission.ADMIN)
                    && !user.getPermissions().contains(Permission.TENANT_MANAGER)
                    && !user.getTenant().equals(user.getDbTenant())) {
                result = false;
            }
        }

        return result;
    }

    public boolean isAuthenticated() {
        boolean result = false;

        if (userService.getAuthenticatedUser() != null) {
            result = true;
        }

        return result;
    }

}
