package apolo.api.controller;

import apolo.api.apimodel.UserAPI;
import apolo.api.apimodel.UserFormModel;
import apolo.api.apimodel.UserList;
import apolo.api.controller.base.BaseAPIController;
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

    @CrossOrigin(origins = "*")
    @PreAuthorize("permitAll")
    @RequestMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET
    )
    public @ResponseBody
    UserList list(
            @PathVariable("tenant-url") String tenantUrl,
            @RequestParam(required = false) Integer pageNumber,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserList result = new UserList();

        if (checkAccess(result, tenantUrl, request, Permission.USER_LIST)) {
            Page<User> page = userService.list(getDBTenant(tenantUrl), pageNumber);

            if (page != null) {
                result.setTotalPages(page.getTotalPages());
                result.setTotalElements(page.getTotalElements());

                if (page.getContent() != null
                        && !page.getContent().isEmpty()) {
                    result.setUserList(page.getContent());

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
    UserList listFromAllTenants(
            @PathVariable("tenant-url") String tenantUrl,
            @RequestParam(required = false) Integer pageNumber,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserList result = new UserList();

        if (checkAccess(result, tenantUrl, request, Permission.ADMIN)) {
            Page<User> page = userService.listAll(pageNumber);

            if (page != null) {
                result.setTotalPages(page.getTotalPages());
                result.setTotalElements(page.getTotalElements());

                if (page.getContent() != null
                        && !page.getContent().isEmpty()) {
                    result.setUserList(page.getContent());

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
    UserAPI find(
            @PathVariable("tenant-url") String tenantUrl,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserAPI result = new UserAPI();

        if (checkAccess(result, tenantUrl, request, Permission.ADMIN, Permission.USER_LIST)) {
            User requestUser = getUserFromRequest(request);

            User user = null;

            if (requestUser.getPermissions().contains(Permission.ADMIN)) {
                user = userService.find(id);
            } else if (requestUser.getPermissions().contains(Permission.USER_LIST)) {
                user = userService.find(getDBTenant(tenantUrl), id);
            }

            if (user != null) {
                result.setUser(user);

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
    UserAPI create(
            @PathVariable("tenant-url") String tenantUrl,
            @RequestBody UserFormModel entity,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserAPI result = new UserAPI();

        if (checkAccess(result, tenantUrl, request, Permission.USER_CREATE)) {
            if (entity != null
                    && entity.getEmail() != null) {
                User user = userService.findByLogin(getDBTenant(tenantUrl), entity.getEmail());

                User requestUser = getUserFromRequest(request);

                if (user != null) {
                    result.setMessage("User account already exist.");

                    response.setStatus(403);
                } else {
                    user = new User();
                    user.setName(entity.getName());
                    user.setEmail(entity.getEmail());
                    user.setMobile(entity.getMobile());
                    user.setPassword(entity.getPassword());

                    user.setGroupIds(entity.getGroupIds());

                    user.setStatus(UserStatus.ACTIVE);
                    user.setCreatedAt(new Date());

                    user.setCreatedBy(requestUser);
                    user.setEnabled(true);

                    if (entity.getPassword() == null
                            || "".equals(entity.getPassword())) {
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

                    result.setUser(user);
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
    UserAPI update(
            @PathVariable("tenant-url") String tenantUrl,
            @RequestBody UserFormModel entity,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserAPI result = new UserAPI();

        if (checkAccess(result, tenantUrl, request, Permission.USER_EDIT)) {
            Tenant tenant = getDBTenant(tenantUrl);

            boolean hasChangePassword = false;

            if (entity.getPassword() != null
                    && !"".equals(entity.getPassword())) {
                hasChangePassword = true;
            }

            if (entity != null
                    && entity.getId() != null) {
                User dbEntity = userService.find(entity.getId());

                if (dbEntity != null) {
                    dbEntity.setName(entity.getName());
                    dbEntity.setEmail(entity.getEmail());

                    if (entity.getMobile() != null
                            && !"".equals(entity.getMobile())) {
                        dbEntity.setMobile(entity.getMobile());
                    }

                    if (hasChangePassword) {
                        dbEntity.setPassword(entity.getPassword());
                    }

                    User requestUser = getUserFromRequest(request);

                    if (requestUser.getPermissions().contains(Permission.ADMIN)
                            || requestUser.getPermissions().contains(Permission.TENANT_MANAGER)) {
                        dbEntity.setTenant(tenant);
                    }

                    if (entity.getGroupIds() != null
                            && !entity.getGroupIds().isEmpty()) {
                        for (Long groupId : entity.getGroupIds()) {
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
                        result.setUser(dbEntity);

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
    UserAPI delete(
            @PathVariable("tenant-url") String tenantUrl,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserAPI result = new UserAPI();

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

                result.setUser(null);

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
    UserAPI lock(
            @PathVariable("tenant-url") String tenantUrl,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserAPI result = new UserAPI();

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

                result.setUser(user);
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
    UserAPI unlock(
            @PathVariable("tenant-url") String tenantUrl,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserAPI result = new UserAPI();

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

                result.setUser(user);
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
