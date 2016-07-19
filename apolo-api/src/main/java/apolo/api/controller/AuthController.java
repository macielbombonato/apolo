package apolo.api.controller;

import apolo.api.apimodel.AuthUser;
import apolo.api.apimodel.UserDTO;
import apolo.api.controller.base.BaseAPIController;
import apolo.api.helper.ApoloHelper;
import apolo.api.service.UserAuthenticationProvider;
import apolo.business.service.UserService;
import apolo.data.model.User;
import apolo.security.CurrentUser;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/login")
public class AuthController extends BaseAPIController<User> {

    @Inject
    private UserService userService;

    @Inject
    private UserAuthenticationProvider userAuthenticationProvider;

    @Inject
    private ApoloHelper<User, UserDTO> userHelper;

    @CrossOrigin(origins = "*")
    @RequestMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            value = "",
            method = RequestMethod.POST
    )
    public @ResponseBody
    UserDTO login(
            @RequestBody AuthUser entity,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            UserDTO result = new UserDTO();

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

                    result = userHelper.toDTO(user);

                    result.setToken(user.getToken());

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

            UserDTO result = new UserDTO();
            result.setMessage(th.getMessage());

            response.setStatus(500);
            return result;
        }
    }

}
