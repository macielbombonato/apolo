package apolo.api.controller;

import apolo.api.apimodel.PermissionList;
import apolo.api.apimodel.StatusList;
import apolo.api.apimodel.UserStatusList;
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
@RequestMapping(value = "/{tenant-url}/static")
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
    PermissionList permissions(
            @PathVariable("tenant-url") String tenantUrl,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        PermissionList result = new PermissionList();

        if (isAutheticated(result, tenantUrl, request)) {
            result.setPermissions(new ArrayList<Permission>());
            for(Permission permission : Permission.values()) {
                if (permission.isListable()) {
                    result.getPermissions().add(permission);
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
    UserStatusList userStatus(
            @PathVariable("tenant-url") String tenantUrl,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserStatusList result = new UserStatusList();

        if (isAutheticated(result, tenantUrl, request)) {
            result.setStatuses(new ArrayList<UserStatus>());
            for(UserStatus status : UserStatus.values()) {
                result.getStatuses().add(status);
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
    StatusList status(
            @PathVariable("tenant-url") String tenantUrl,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        StatusList result = new StatusList();

        if (isAutheticated(result, tenantUrl, request)) {
            result.setStatuses(new ArrayList<Status>());
            for(Status status : Status.values()) {
                result.getStatuses().add(status);
            }

            response.setStatus(200);

        } else {
            response.setStatus(401);
        }

        return result;
    }

}
