package apolo.web.controller.base;

import apolo.business.service.ApplicationService;
import apolo.data.model.Application;
import apolo.data.model.base.BaseEntity;
import apolo.security.ApoloSecurityService;
import apolo.security.UserPermission;
import apolo.web.apimodel.base.BaseAPIModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

public abstract class BaseAPIController<E extends BaseEntity> extends BaseController<E> {

    protected static final Logger log = LoggerFactory.getLogger(BaseAPIController.class);

    @Inject
    private ApoloSecurityService apoloSecurity;

    @Inject
    private ApplicationService applicationService;

    protected boolean checkAccess(BaseAPIModel model, String tenant, HttpServletRequest request, UserPermission...userPermission) {
        boolean hasAccess = false;

        Application app = getApplication(request);

        if (app != null) {

            if (app != null) {
                if (apoloSecurity.hasPermission(app.getPermissions(), userPermission)) {
                    hasAccess = true;
                }
            }

            if (hasAccess) {
                boolean isSameTenant = app.getTenant() != null && app.getTenant().getUrl().equals(tenant);

                if (isSameTenant ||
                        (apoloSecurity.hasPermission(
                                app.getPermissions(),
                                UserPermission.ADMIN,
                                UserPermission.TENANT_MANAGER
                        ))
                ) {
                    hasAccess = true;
                } else {
                    hasAccess = false;
                }
            }
        }

        if (!hasAccess) {
            model.setSuccess(false);
            model.setMessage("Access Denied");
        }

        return hasAccess;
    }

    protected Application getApplication(HttpServletRequest request) {
        String apiKey = request.getHeader("key");

        Application result = null;

        if (apiKey != null) {
            result = applicationService.find(apiKey);
        }

        return result;
    }

}
