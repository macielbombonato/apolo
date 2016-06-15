package apolo.api.controller;

import apolo.api.apimodel.AuthUser;
import apolo.api.apimodel.UserAPI;
import apolo.api.controller.base.BaseAPIController;
import apolo.business.service.UserService;
import apolo.common.config.model.ApplicationProperties;
import apolo.data.enums.UserStatus;
import apolo.data.model.User;
import apolo.api.service.UserAuthenticationProvider;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/install")
public class InstallController extends BaseAPIController<User> {

    @Inject
    private UserService userService;

    @Inject
    private ApplicationProperties applicationProperties;

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

                user = new User();

                user.setStatus(UserStatus.ADMIN);

                user.setName(entity.getUsername());
                user.setEmail(entity.getUsername());
                user.setPassword(entity.getPassword());

                userService.systemSetup(getServerUrl(request), user);

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
