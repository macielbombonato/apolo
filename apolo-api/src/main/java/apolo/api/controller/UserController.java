package apolo.api.controller;

import apolo.api.apimodel.ModelList;
import apolo.api.apimodel.UserDTO;
import apolo.api.controller.base.BaseAPIController;
import apolo.api.helper.ApoloHelper;
import apolo.business.service.PermissionGroupService;
import apolo.business.service.UserService;
import apolo.common.exception.AccessDeniedException;
import apolo.common.util.MessageBundle;
import apolo.data.enums.UserStatus;
import apolo.data.model.PermissionGroup;
import apolo.data.model.Tenant;
import apolo.data.model.User;
import apolo.security.Permission;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
@RequestMapping(value = "/{tenant-url}/user")
public class UserController extends BaseAPIController<User> {

    @Inject
    private UserService userService;

    @Inject
    private PermissionGroupService permissionGroupService;

    @Inject
    private ApoloHelper<User, UserDTO> userHelper;

    @CrossOrigin(origins = "*")
    @PreAuthorize("permitAll")
    @RequestMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET
    )
    public @ResponseBody
    ModelList<UserDTO> list(
            @PathVariable("tenant-url") String tenantUrl,
            @RequestParam(required = false) Integer pageNumber,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        ModelList<UserDTO> result = new ModelList<UserDTO>();

        if (checkAccess(result, tenantUrl, request, Permission.USER_LIST)) {
            Page<User> page = userService.list(getDBTenant(tenantUrl), pageNumber);

            if (page != null) {
                result.setTotalPages(page.getTotalPages());
                result.setTotalElements(page.getTotalElements());

                if (page.getContent() != null
                        && !page.getContent().isEmpty()) {
                    result.setList(userHelper.toDTOList(page.getContent()));

                    response.setStatus(200);
                } else {
                    response.setStatus(404);
                }
            }
        } else {
            response.setStatus(401);
        }

        return result;
    }

    @CrossOrigin(origins = "*")
    @PreAuthorize("permitAll")
    @RequestMapping(
            value = "list-all",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET
    )
    public @ResponseBody
    ModelList<UserDTO> listFromAllTenants(
            @PathVariable("tenant-url") String tenantUrl,
            @RequestParam(required = false) Integer pageNumber,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        ModelList<UserDTO> result = new ModelList<UserDTO>();

        if (checkAccess(result, tenantUrl, request, Permission.ADMIN)) {
            Page<User> page = userService.listAll(pageNumber);

            if (page != null) {
                result.setTotalPages(page.getTotalPages());
                result.setTotalElements(page.getTotalElements());

                if (page.getContent() != null
                        && !page.getContent().isEmpty()) {
                    result.setList(userHelper.toDTOList(page.getContent()));

                    response.setStatus(200);
                } else {
                    response.setStatus(404);
                }
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
    UserDTO find(
            @PathVariable("tenant-url") String tenantUrl,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserDTO result = new UserDTO();

        if (checkAccess(result, tenantUrl, request, Permission.ADMIN, Permission.USER_LIST)) {
            User requestUser = getUserFromRequest(request);

            User user = null;

            if (requestUser.getPermissions().contains(Permission.ADMIN)) {
                user = userService.find(id);
            } else if (requestUser.getPermissions().contains(Permission.USER_LIST)) {
                user = userService.find(getDBTenant(tenantUrl), id);
            }

            if (user != null) {
                result = userHelper.toDTO(user);

                result.setFilesURL(getServerUrl(request) + "file/");

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
    UserDTO create(
            @PathVariable("tenant-url") String tenantUrl,
            @RequestBody UserDTO dto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        UserDTO result = new UserDTO();

        if (checkAccess(result, tenantUrl, request, Permission.USER_CREATE)) {
            if (dto != null
                    && dto.getEmail() != null) {
                User user = userService.findByLogin(getDBTenant(tenantUrl), dto.getEmail());

                User requestUser = getUserFromRequest(request);

                if (user != null) {
                    result.setMessage("User account already exist.");

                    response.setStatus(403);
                } else {
                    user = userHelper.toEntity(dto);

                    user.setStatus(UserStatus.ACTIVE);
                    user.setCreatedAt(new Date());

                    user.setCreatedBy(requestUser);
                    user.setEnabled(true);

                    if (dto.getPassword() == null
                            || "".equals(dto.getPassword())) {
                        user.setPassword("newUser-this-password-need-to-be-changed-!!!!!");
                    }

                    Tenant tenant = getDBTenant(tenantUrl);

                    user.setTenant(tenant);

                    if (user.getPermissions().contains(Permission.ADMIN)) {
                        if (!requestUser.getPermissions().contains(Permission.ADMIN)) {
                            throw new AccessDeniedException(8, MessageBundle.getMessageBundle("error.403.8"));
                        }
                    }

                    user = userService.save(
                            getServerUrl(request),
                            user,
                            true,
                            null
                    );

                    result = userHelper.toDTO(user);

                    result.setFilesURL(getServerUrl(request) + "file/");

                    response.setStatus(201);
                }


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
    UserDTO update(
            @PathVariable("tenant-url") String tenantUrl,
            @RequestBody UserDTO dto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserDTO result = new UserDTO();

        if (checkAccess(result, tenantUrl, request, Permission.USER_EDIT)) {
            Tenant tenant = getDBTenant(tenantUrl);

            boolean hasChangePassword = false;

            if (dto.getPassword() != null
                    && !"".equals(dto.getPassword())) {
                hasChangePassword = true;
            }

            if (dto != null
                    && dto.getId() != null) {
                User dbEntity = userService.find(dto.getId());

                if (dbEntity != null) {
                    dbEntity = userHelper.toEntity(dto);

                    if (hasChangePassword) {
                        dbEntity.setPassword(dto.getPassword());
                    }

                    User requestUser = getUserFromRequest(request);

                    if (requestUser.getPermissions().contains(Permission.ADMIN)
                            || requestUser.getPermissions().contains(Permission.TENANT_MANAGER)) {
                        dbEntity.setTenant(tenant);
                    }

                    if (dto.getGroupIds() != null
                            && !dto.getGroupIds().isEmpty()) {
                        for (Long groupId : dto.getGroupIds()) {
                            if (!dbEntity.getGroupIds().contains(groupId)) {
                                PermissionGroup permissionGroup = permissionGroupService.find(groupId);

                                dbEntity.getGroups().add(permissionGroup);
                            }
                        }
                    }

                    if (dbEntity.getPermissions().contains(Permission.ADMIN)) {
                        if (!requestUser.getPermissions().contains(Permission.ADMIN)) {
                            throw new AccessDeniedException(8, MessageBundle.getMessageBundle("error.403.8"));
                        }
                    }

                    dbEntity = userService.save(
                            getServerUrl(request, tenantUrl),
                            dbEntity,
                            hasChangePassword,
                            null
                    );

                    if (dbEntity != null) {
                        result = userHelper.toDTO(dbEntity);

                        result.setFilesURL(getServerUrl(request) + "file/");

                        response.setStatus(200);
                    } else {
                        response.setStatus(404);
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

    @CrossOrigin(origins = "*")
    @PreAuthorize("permitAll")
    @RequestMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "{id}",
            method = RequestMethod.DELETE
    )
    public @ResponseBody
    UserDTO delete(
            @PathVariable("tenant-url") String tenantUrl,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserDTO result = new UserDTO();

        if (checkAccess(result, tenantUrl, request, Permission.USER_REMOVE)) {
            User user = userService.find(id);

            if (user != null) {
                if (UserStatus.ADMIN.equals(user.getStatus())) {
                    throw new AccessDeniedException(9, MessageBundle.getMessageBundle("error.403.9"));
                } else if (user.getPermissions().contains(Permission.ADMIN)) {
                    User requestUser = getUserFromRequest(request);

                    if (!requestUser.getPermissions().contains(Permission.ADMIN)) {
                        throw new AccessDeniedException(10, MessageBundle.getMessageBundle("error.403.10"));
                    }
                }

                userService.remove(user);

                result = null;

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
            value = "{id}/lock",
            method = RequestMethod.PATCH
    )
    public @ResponseBody
    UserDTO lock(
            @PathVariable("tenant-url") String tenantUrl,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserDTO result = new UserDTO();

        if (checkAccess(result, tenantUrl, request, Permission.USER_MANAGER)) {
            User requestUser = getUserFromRequest(request);

            User user = null;

            if (requestUser.getPermissions().contains(Permission.ADMIN)) {
                user = userService.find(id);
            } else {
                user = userService.find(getDBTenant(tenantUrl), id);
            }

            if (user != null) {
                user = userService.lock(user);

                result = userHelper.toDTO(user);

                result.setFilesURL(getServerUrl(request) + "file/");

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
            value = "{id}/unlock",
            method = RequestMethod.PATCH
    )
    public @ResponseBody
    UserDTO unlock(
            @PathVariable("tenant-url") String tenantUrl,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserDTO result = new UserDTO();

        if (checkAccess(result, tenantUrl, request, Permission.USER_MANAGER)) {
            User requestUser = getUserFromRequest(request);

            User user = null;

            if (requestUser.getPermissions().contains(Permission.ADMIN)) {
                user = userService.find(id);
            } else {
                user = userService.find(getDBTenant(tenantUrl), id);
            }

            if (user != null) {
                user = userService.unlock(user);

                result = userHelper.toDTO(user);

                result.setFilesURL(getServerUrl(request) + "file/");

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
