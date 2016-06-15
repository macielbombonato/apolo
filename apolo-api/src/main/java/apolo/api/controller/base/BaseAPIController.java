package apolo.api.controller.base;

import apolo.api.apimodel.base.BaseAPIModel;
import apolo.business.service.UserService;
import apolo.common.exception.AccessDeniedException;
import apolo.common.util.MessageBundle;
import apolo.data.model.User;
import apolo.data.model.base.BaseEntity;
import apolo.security.ApoloSecurityService;
import apolo.security.CurrentUser;
import apolo.security.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.ResourceNotFoundException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public abstract class BaseAPIController<E extends BaseEntity> extends BaseController<E> {

    protected static final Logger log = LoggerFactory.getLogger(BaseAPIController.class);

    @Inject
    private ApoloSecurityService apoloSecurity;

    @Inject
    private UserService userService;

    protected boolean checkAccess(
            BaseAPIModel model,
            String tenantUrl,
            HttpServletRequest request,
            Permission... permission) {

        boolean hasAccess = false;

        User user = getUserFromRequest(request);

        if (user != null) {

            if (user != null) {
                if (apoloSecurity.hasPermission(user.getPermissions(), permission)) {
                    hasAccess = true;
                } else {
                    throw new AccessDeniedException(MessageBundle.getMessageBundle("error.403"));
                }
            }

            if (hasAccess) {
                boolean isSameTenant = user.getTenant() != null && user.getTenant().getUrl().equals(tenantUrl);

                if (isSameTenant ||
                        (apoloSecurity.hasPermission(
                                user.getPermissions(),
                                Permission.ADMIN,
                                Permission.TENANT_MANAGER
                        ))
                        ) {
                    hasAccess = true;
                } else {
                    hasAccess = false;

                    throw new AccessDeniedException(MessageBundle.getMessageBundle("error.403"));
                }
            }
        }

        if (!hasAccess) {
            model.setMessage(MessageBundle.getMessageBundle("error.403"));

            throw new AccessDeniedException(MessageBundle.getMessageBundle("error.403"));
        }

        return hasAccess;
    }

    protected User getUserFromRequest(HttpServletRequest request) {
        String apiKey = request.getHeader("key");

        User result = null;

        if (apiKey != null) {
            result = userService.findByToken(apiKey);

            if (result != null) {
                // Set permission to see only the code screen.
                Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

                authorities = userService.loadUserAuthorities(result);

                Authentication authentication = new CurrentUser(
                        result.getId(),
                        result.getEmail(),
                        result.getPassword().toLowerCase(),
                        result,
                        authorities
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
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

        model.setMessage("Internal Server Error");

        response.setStatus(500);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

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

        model.setMessage("Resource Not Found");

        response.setStatus(404);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

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

        model.setMessage(ex.getCustomMsg());

        response.setStatus(ex.getPrincipalCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

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
