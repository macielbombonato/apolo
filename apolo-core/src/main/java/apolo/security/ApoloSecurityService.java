package apolo.security;

import apolo.business.service.UserService;
import apolo.data.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component("apoloSecurity")
public class ApoloSecurityService {

    private static final Logger log = LoggerFactory.getLogger(ApoloSecurityService.class);

    @Inject
    private UserService userService;

    public boolean hasPermission(String...userPermissions) {
        boolean result = false;

        if (userPermissions != null && userPermissions.length > 0) {
            User user = userService.getAuthenticatedUser();

            if (user != null
                    && user.getPermissions() != null
                    && user.getPermissions().contains(UserPermission.ADMIN)) {
                result = true;
            } else {
                for (String userPermission : userPermissions) {
                    try {
                        UserPermission permission = UserPermission.fromCode(userPermission);

                        if (user != null
                                && user.getPermissions() != null
                                && user.getPermissions().contains(permission)) {
                            result = true;
                            break;
                        }
                    } catch(Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }

            if (result
                    && user != null
                    && user.getPermissions() != null
                    && user.getTenant().equals(user.getDbTenant())
                    && !user.getPermissions().contains(UserPermission.ADMIN)
                    && !user.getPermissions().contains(UserPermission.TENANT_MANAGER)
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
