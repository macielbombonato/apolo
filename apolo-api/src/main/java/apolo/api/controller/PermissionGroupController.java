package apolo.api.controller;

import apolo.api.apimodel.ModelList;
import apolo.api.apimodel.PermissionGroupDTO;
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
@RequestMapping(value = "/permission-group")
public class PermissionGroupController extends BaseAPIController<PermissionGroup> {

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
    ModelList<PermissionGroupDTO> list(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        ModelList<PermissionGroupDTO> result = new ModelList<PermissionGroupDTO>();

        if (checkAccess(result, null, request, Permission.PERMISSION_GROUP_LIST)) {
            User user = getUserFromRequest(request);

            List<PermissionGroup> entities = permissionGroupService.list(user);

            if (entities != null
                    && entities.size() > 0) {
                result.setList(permissionGroupHelper.toDTOList(entities));

                response.setStatus(200);

            } else {
                response.setStatus(404);
            }
        } else {
            response.setStatus(403);
        }

        return result;
    }

    @CrossOrigin(origins = "*")
    @PreAuthorize("permitAll")
    @RequestMapping(
            value = "list",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET
    )
    public @ResponseBody
    ModelList<PermissionGroupDTO> listAll(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        ModelList<PermissionGroupDTO> result = new ModelList<PermissionGroupDTO>();

        if (request.getUserPrincipal() != null) {
            User user = getUserFromRequest(request);

            List<PermissionGroup> entities = permissionGroupService.list(user);

            if (entities != null
                    && entities.size() > 0) {
                result.setList(permissionGroupHelper.toDTOList(entities));

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
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        PermissionGroupDTO result = null;

        if (checkAccess(
                result,
                null,
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
            @RequestBody PermissionGroupDTO dto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        PermissionGroupDTO result = null;

        if (checkAccess(
                result,
                null,
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
            @RequestBody PermissionGroupDTO dto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        PermissionGroupDTO result = null;

        if (checkAccess(
                result,
                null,
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
