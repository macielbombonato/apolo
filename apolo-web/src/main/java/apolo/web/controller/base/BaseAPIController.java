package apolo.web.controller.base;

import apolo.business.service.ApplicationService;
import apolo.common.exception.AccessDeniedException;
import apolo.data.model.Application;
import apolo.data.model.base.BaseEntity;
import apolo.security.ApoloSecurityService;
import apolo.security.UserPermission;
import apolo.web.apimodel.base.BaseAPIModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.social.ResourceNotFoundException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

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

            throw new AccessDeniedException(403, "Access Denied");
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

    @ExceptionHandler(Exception.class)
    public @ResponseBody
    BaseAPIModel handleException(
            Exception ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        log.error(ex.getMessage(), ex);

        BaseAPIModel model = new BaseAPIModel();

        model.setSuccess(false);
        model.setMessage("Internal Server Error");

        response.setStatus(500);

        return model;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public @ResponseBody
    BaseAPIModel handleExceptionResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        log.error(ex.getMessage(), ex);

        BaseAPIModel model = new BaseAPIModel();

        model.setSuccess(false);
        model.setMessage("Resource Not Found");

        response.setStatus(404);

        return model;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public @ResponseBody
    BaseAPIModel handleExceptionAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        log.error(ex.getMessage(), ex);

        BaseAPIModel model = new BaseAPIModel();

        model.setSuccess(false);
        model.setMessage(ex.getCustomMsg());

        response.setStatus(ex.getPrincipalCode());

        return model;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        //Create a custom binder that will convert a String with pattern dd/MM/yyyy to an appropriate Date object.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

        binder.registerCustomEditor(Date.class, "createdAt", new CustomDateEditor(dateFormat, false));
        binder.registerCustomEditor(Date.class, "updatedAt", new CustomDateEditor(dateFormat, false));

        binder.registerCustomEditor(Date.class, "resetPasswordSentAt", new CustomDateEditor(dateFormat, false));

        binder.registerCustomEditor(Date.class, "currentSignInAt", new CustomDateEditor(dateFormat, false));
        binder.registerCustomEditor(Date.class, "lastSignInAt", new CustomDateEditor(dateFormat, false));

    }

}
