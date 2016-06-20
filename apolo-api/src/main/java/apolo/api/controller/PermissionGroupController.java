package apolo.api.controller;

import apolo.api.apimodel.PermissionGroupDTO;
import apolo.api.apimodel.PermissionGroupList;
import apolo.api.controller.base.BaseAPIController;
import apolo.api.helper.ApoloHelper;
import apolo.business.service.PermissionGroupService;
import apolo.common.exception.AccessDeniedException;
import apolo.common.util.MessageBundle;
import apolo.data.enums.UserStatus;
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

    @Inject
    private ApoloHelper<PermissionGroup, PermissionGroupDTO> permissionGroupHelper;

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

        if (checkAccess(result, tenantUrl, request, Permission.PERMISSION_GROUP_LIST)) {
            List<PermissionGroup> entities = permissionGroupService.list();

            if (entities != null
                    && entities.size() > 0) {
                result.setGroupList(permissionGroupHelper.toDTOList(entities));

                response.setStatus(200);

            } else {
                response.setStatus(404);
            }
        } else {
            response.setStatus(401);
        }

        return result;
    }

    @CrossOrigin(origins = "*")
    @PreAuthorize("permitAll")
    @RequestMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "{id}",
            method = RequestMethod.GET
    )
    public @ResponseBody
    PermissionGroupDTO find(
            @PathVariable("tenant-url") String tenantUrl,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        PermissionGroupDTO result = null;

        if (checkAccess(
                result,
                tenantUrl,
                request,
                Permission.ADMIN,
                Permission.PERMISSION_GROUP_LIST,
                Permission.PERMISSION_GROUP_VIEW)
                ) {
            User requestUser = getUserFromRequest(request);

            PermissionGroup entity = permissionGroupService.find(id);

            if (entity != null) {
                result = permissionGroupHelper.toDTO(entity);

                response.setStatus(200);
            } else {
                response.setStatus(404);
            }
        } else {
            response.setStatus(401);
        }

        return result;
    }

    @CrossOrigin(origins = "*")
    @PreAuthorize("permitAll")
    @RequestMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            value = "",
            method = RequestMethod.POST
    )
    public @ResponseBody
    PermissionGroupDTO create(
            @PathVariable("tenant-url") String tenantUrl,
            @RequestBody PermissionGroupDTO dto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        PermissionGroupDTO result = null;

        if (checkAccess(
                result,
                tenantUrl,
                request,
                Permission.PERMISSION_GROUP_CREATE)
                ) {
            if (dto != null) {
                PermissionGroup entity = permissionGroupHelper.toEntity(dto);

                entity = permissionGroupService.save(entity);

                result = permissionGroupHelper.toDTO(entity);

                response.setStatus(201);

            } else {
                response.setStatus(404);
            }
        } else {
            response.setStatus(401);
        }

        return result;
    }

    @CrossOrigin(origins = "*")
    @PreAuthorize("permitAll")
    @RequestMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            value = "",
            method = RequestMethod.PUT
    )
    public @ResponseBody
    PermissionGroupDTO update(
            @PathVariable("tenant-url") String tenantUrl,
            @RequestBody PermissionGroupDTO dto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        PermissionGroupDTO result = null;

        if (checkAccess(
                result,
                tenantUrl,
                request,
                Permission.PERMISSION_GROUP_EDIT)
                ) {
            if (dto != null) {
                PermissionGroup permission = permissionGroupService.find(dto.getId());

                if (permission != null) {
                    if (UserStatus.ADMIN.equals(permission.getStatus())) {
                        throw new AccessDeniedException(8, MessageBundle.getMessageBundle("error.403.8"));
                    } else {
                        permission = permissionGroupService.save(permissionGroupHelper.toEntity(dto));

                        result = permissionGroupHelper.toDTO(permission);

                        response.setStatus(200);
                    }
                } else {
                    response.setStatus(404);
                }

            } else {
                response.setStatus(404);
            }
        } else {
            response.setStatus(401);
        }

        return result;
    }


}
