package apolo.api.controller.advice;

import apolo.business.service.TenantService;
import apolo.business.service.UserService;
import apolo.common.config.model.ApplicationProperties;
import apolo.data.model.Tenant;
import apolo.data.model.User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Inject
    private UserService userService;

    @Inject
    private TenantService tenantService;

    @Inject
    private ApplicationProperties applicationProperties;

    @ModelAttribute
    public void validateTenant(HttpServletRequest request, HttpServletResponse response) {
        String[] pathArray = request.getServletPath().split("/");

        if (pathArray != null
                && pathArray.length > 1
                && ("web".equals(pathArray[1]) || "api".equals(pathArray[1]))
                ) {
            User user = userService.getAuthenticatedUser();

            if (user != null) {
                request.getSession().setAttribute("tenant", user.getTenant());
            } else {
                String tenantUrl = null;

                if (pathArray.length >= 3
                        && ("web".equals(pathArray[1]) || "api".equals(pathArray[1]))
                        ) {
                    tenantUrl = pathArray[2];

                    if ("login".equals(tenantUrl)
                            || "error".equals(tenantUrl)) {
                        tenantUrl = null;
                    }
                }

                Tenant sessionTenant = (Tenant) request.getSession().getAttribute("tenant");

                if (sessionTenant == null || !sessionTenant.getUrl().equals(tenantUrl)) {
                    if (tenantUrl == null) {
                        tenantUrl = applicationProperties.getDefaultTenant();
                    }

                    sessionTenant = tenantService.getValidatedTenant(tenantUrl);

                    request.getSession().setAttribute("tenant", sessionTenant);
                }
            }
        }
    }

}
