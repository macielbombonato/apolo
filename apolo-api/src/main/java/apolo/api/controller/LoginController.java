package apolo.api.controller;

import apolo.api.apimodel.AuthUser;
import apolo.api.apimodel.UserAPI;
import apolo.api.controller.base.BaseAPIController;
import apolo.business.service.UserService;
import apolo.data.model.User;
import apolo.security.CurrentUser;
import apolo.api.service.UserAuthenticationProvider;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/login")
public class LoginController extends BaseAPIController<User> {

    @Inject
    private UserService userService;

    @Inject
    private UserAuthenticationProvider userAuthenticationProvider;

    @CrossOrigin(origins = "*")
    @RequestMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            value = "",
            method = RequestMethod.POST
    )
    public @ResponseBody
    UserAPI login(
            @RequestBody AuthUser entity,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            UserAPI result = new UserAPI();

            User user = null;

            if (entity != null
                    && entity.getUsername() != null) {

                String sessionId = null;
                if (request.getSession() != null) {
                    sessionId = request.getSession().getId();
                }

                String ipAddress = request.getHeader("X-FORWARDED-FOR");
                if (ipAddress == null) {
                    ipAddress = request.getRemoteAddr();
                }

                Authentication authentication = new CurrentUser(
                        null,
                        entity.getUsername(),
                        entity.getPassword(),
                        null,
                        null
                );

                authentication = userAuthenticationProvider.authenticate(
                        authentication,
                        sessionId,
                        ipAddress
                    );

                if (authentication != null) {
                    user = (User) authentication.getPrincipal();

                    result.setUser(user);
                    response.setStatus(200);
                } else {
                    response.setStatus(204);
                }
            } else {
                result.setMessage("Username can't be null.");
                response.setStatus(403);
            }

            return result;
        } catch (Throwable th) {
            log.error(th.getMessage(), th);

            UserAPI result = new UserAPI();
            result.setMessage(th.getMessage());

            response.setStatus(500);
            return result;
        }
    }

}
