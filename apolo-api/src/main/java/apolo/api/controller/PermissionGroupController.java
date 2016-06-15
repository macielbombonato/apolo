package apolo.api.controller;

import apolo.api.apimodel.PermissionGroupList;
import apolo.api.controller.base.BaseAPIController;
import apolo.business.service.PermissionGroupService;
import apolo.data.model.PermissionGroup;
import apolo.data.model.User;
import apolo.security.Permission;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "/{tenant-url}/permission-group")
public class PermissionGroupController extends BaseAPIController<User> {

    @Inject
    private PermissionGroupService permissionGroupService;

    @CrossOrigin(origins = "*")
    @PreAuthorize("permitAll")
    @RequestMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET
    )
    public @ResponseBody
    PermissionGroupList list(
            @PathVariable("tenant-url") String tenantUrl,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        PermissionGroupList result = new PermissionGroupList();

        if (checkAccess(result, tenantUrl, request, Permission.USER_LIST)) {
            List<PermissionGroup> groups = permissionGroupService.list();

            if (groups != null
                    && groups.size() > 0) {
                result.setGroupList(groups);

                response.setStatus(200);

            } else {
                response.setStatus(404);
            }
        } else {
            response.setStatus(401);
        }

        return result;
    }


}
