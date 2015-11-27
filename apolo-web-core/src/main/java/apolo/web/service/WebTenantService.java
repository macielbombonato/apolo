package apolo.web.service;

import apolo.business.service.TenantService;
import apolo.business.service.UserService;
import apolo.common.exception.AccessDeniedException;
import apolo.common.util.MessageBundle;
import apolo.data.model.Tenant;
import apolo.data.model.User;
import apolo.security.UserPermission;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component("webTenantService")
public class WebTenantService {

    @Inject
    protected UserService userService;

    @Inject
    protected TenantService tenantService;

    public Tenant validateTenant(HttpServletRequest request, HttpServletResponse response) {
        Tenant tenant = null;

        String sessionTenantUrl = null;

        if (request.getCookies() != null
                && request.getCookies().length > 0) {
            for (Cookie cookie : request.getCookies()) {
                if ("tenantUrl".equals(cookie.getName())) {
                    sessionTenantUrl = cookie.getValue();
                    cookie.setMaxAge(1);
                    break;
                }
            }
        }

        String tenantUrl = null;
        String[] pathArray = request.getServletPath().split("/");

        if (pathArray.length >= 3
                && ("web".equals(pathArray[1]) || "api".equals(pathArray[1]))
                ) {
            tenantUrl = pathArray[2];

            if ("login".equals(tenantUrl)
                    || "error".equals(tenantUrl)) {
                tenantUrl = null;
            }
        }

        if (sessionTenantUrl == null
                && tenantUrl == null
                ||
                (sessionTenantUrl != null
                        && !sessionTenantUrl.equals(tenantUrl)
                )
                ) {

            tenant = tenantService.getValidatedTenant(tenantUrl);
            User user = userService.getAuthenticatedUser();

            if (user != null
                    && !user.getTenant().equals(tenant)) {

                if (user.getPermissions() != null
                        && !user.getPermissions().isEmpty()
                        && (
                        user.getPermissions().contains(UserPermission.ADMIN)
                                || user.getPermissions().contains(UserPermission.TENANT_MANAGER)
                )) {
                    user.getDbTenant();
                    user.setTenant(tenant);

                    userService.reconstructAuthenticatedUser(user);
                } else {
                    String message = MessageBundle.getMessageBundle("error.403.msg");
                    throw new AccessDeniedException(message);
                }
            }

            putValuesOnSession(request, response, tenant, tenantUrl);
        }

        if (tenant == null || !tenant.getUrl().equals(tenantUrl)) {
            tenant = (Tenant) request.getSession().getAttribute("tenant");

            if (tenant == null || !tenant.getUrl().equals(tenantUrl)) {
                tenant = tenantService.getValidatedTenant(tenantUrl);
            }

            putValuesOnSession(request, response, tenant, tenantUrl);
        }

        return tenant;
    }

    private void putValuesOnSession(
            HttpServletRequest request,
            HttpServletResponse response,
            Tenant tenant,
            String tenantUrl
    ) {
        if (request.getCookies() != null
                && request.getCookies().length > 0) {
            for (Cookie cookie : request.getCookies()) {
                if ("tenantUrl".equals(cookie.getName())) {
                    cookie.setValue(tenantUrl);
                    response.addCookie(cookie);
                    break;
                }
            }
        } else {
            Cookie cookie = new Cookie("tenantUrl", tenantUrl);
            cookie.setMaxAge(1);
            response.addCookie(cookie);
        }

        request.getSession().setAttribute("tenant", tenant);
    }
}
