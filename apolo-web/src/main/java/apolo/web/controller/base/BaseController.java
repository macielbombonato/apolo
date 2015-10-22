package apolo.web.controller.base;

import apolo.business.service.TenantService;
import apolo.business.service.UserService;
import apolo.common.util.MessageBundle;
import apolo.data.model.Tenant;
import apolo.data.model.User;
import apolo.data.model.base.BaseEntity;
import apolo.security.CurrentUser;
import apolo.security.UserPermission;
import com.google.gson.annotations.SerializedName;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;

public abstract class BaseController<E extends BaseEntity> {

    protected static final Logger log = LoggerFactory.getLogger(BaseController.class);

    @Inject
    protected UserService userService;

    @Inject
    protected TenantService tenantService;

    protected Tenant getDBTenant(String url) {
        Tenant tenant = null;

        tenant = tenantService.getValidatedTenant(url);

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

                reconstructAuthenticatedUser(user);
            } else {
                String message = MessageBundle.getMessageBundle("error.403.msg");
                throw new AccessDeniedException(message);
            }
        }

        return tenant;
    }

    protected void reconstructAuthenticatedUser(User user) {
        Collection<GrantedAuthority> authorities =
                userService.loadUserAuthorities(
                        userService.getAuthenticatedUser()
                );

        Authentication newAuth = new CurrentUser(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user,
                authorities
        );

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    protected JSONObject jsonParser(Object obj) throws IllegalArgumentException, IllegalAccessException, JSONException {
        JSONObject object = new JSONObject();

        Class<?> objClass = obj.getClass();
        Field[] fields = objClass.getDeclaredFields();
        for(Field field : fields) {
            field.setAccessible(true);
            Annotation[] annotations = field.getDeclaredAnnotations();
            for(Annotation annotation : annotations){
                if(annotation instanceof SerializedName){
                    SerializedName myAnnotation = (SerializedName) annotation;
                    String name = myAnnotation.value();
                    Object value = field.get(obj);

                    if(value == null)
                        value = new String("");

                    object.put(name, value);
                }
            }
        }

        return object;
    }

    protected String getServerUrl(HttpServletRequest request, String tenant) {
        String serverUrl = "";

        try {
            URL url = new URL(request.getRequestURL().toString());

            String host  = url.getHost();
            String userInfo = url.getUserInfo();
            String scheme = url.getProtocol();
            int port = url.getPort();
            String path = (String) request.getAttribute("javax.servlet.forward.request_uri");
            String query = (String) request.getAttribute("javax.servlet.forward.query_string");
            URI uri = new URI(scheme,userInfo,host,port,path,query,null);

            serverUrl = uri.toString() + "/web/" + tenant + "/";

        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
        } catch (URISyntaxException e) {
            log.error(e.getMessage(), e);
        }

        return serverUrl;
    }

}
