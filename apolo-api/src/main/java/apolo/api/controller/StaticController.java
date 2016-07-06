package apolo.api.controller;

import apolo.api.apimodel.ModelList;
import apolo.api.controller.base.BaseAPIController;
import apolo.business.service.PermissionGroupService;
import apolo.data.enums.Status;
import apolo.data.enums.UserStatus;
import apolo.data.model.User;
import apolo.security.Permission;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "/static")
public class StaticController extends BaseAPIController<User> {

    @Inject
    private PermissionGroupService permissionGroupService;

    @CrossOrigin(origins = "*")
    @PreAuthorize("permitAll")
    @RequestMapping(
            value = "permission",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET
    )
    public @ResponseBody
    ModelList<Permission> permissions(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        ModelList<Permission> result = new ModelList<Permission>();

        if (isAutheticated(request)) {
            result.setList(new ArrayList<Permission>());
            for(Permission permission : Permission.values()) {
                if (permission.isListable()) {
                    result.getList().add(permission);
                }
            }

            response.setStatus(200);

        } else {
            response.setStatus(401);
        }

        return result;
    }

    @CrossOrigin(origins = "*")
    @PreAuthorize("permitAll")
    @RequestMapping(
            value = "user-status",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET
    )
    public @ResponseBody
    ModelList<UserStatus> userStatus(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        ModelList<UserStatus> result = new ModelList<UserStatus>();

        if (isAutheticated(request)) {
            result.setList(new ArrayList<UserStatus>());
            for(UserStatus status : UserStatus.values()) {
                result.getList().add(status);
            }

            response.setStatus(200);

        } else {
            response.setStatus(401);
        }

        return result;
    }

    @CrossOrigin(origins = "*")
    @PreAuthorize("permitAll")
    @RequestMapping(
            value = "status",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET
    )
    public @ResponseBody
    ModelList<Status> status(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        ModelList<Status> result = new ModelList<Status>();

        if (isAutheticated(request)) {
            result.setList(new ArrayList<Status>());
            for(Status status : Status.values()) {
                result.getList().add(status);
            }

            response.setStatus(200);

        } else {
            response.setStatus(401);
        }

        return result;
    }

}
