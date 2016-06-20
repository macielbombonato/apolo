package apolo.api.controller;

import apolo.api.apimodel.AuthUser;
import apolo.api.apimodel.UserDTO;
import apolo.api.controller.base.BaseAPIController;
import apolo.api.helper.ApoloHelper;
import apolo.api.service.UserAuthenticationProvider;
import apolo.business.service.UserService;
import apolo.common.config.model.ApplicationProperties;
import apolo.data.enums.UserStatus;
import apolo.data.model.User;
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

                user = new User();

                user.setStatus(UserStatus.ADMIN);

                user.setName(entity.getUsername());
                user.setEmail(entity.getUsername());
                user.setPassword(entity.getPassword());

                userService.systemSetup(getServerUrl(request), user);

                result.setMessage("Success");
                response.setStatus(200);

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
