package apolo.web.controller;

import apolo.business.service.UserGroupService;
import apolo.common.exception.AccessDeniedException;
import apolo.common.util.MessageBundle;
import apolo.data.enums.UserStatus;
import apolo.data.model.Application;
import apolo.data.model.Tenant;
import apolo.data.model.User;
import apolo.data.model.UserGroup;
import apolo.security.UserPermission;
import apolo.web.apimodel.UserAPI;
import apolo.web.apimodel.UserList;
import apolo.web.controller.base.BaseAPIController;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
@RequestMapping(value = "/api/{tenant-url}/user")
public class UserAPIController extends BaseAPIController<User> {

    @Inject
    private UserGroupService userGroupService;

    @PreAuthorize("permitAll")
    @RequestMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            value = "list",
            method = RequestMethod.GET
    )
    public @ResponseBody
    UserList listFirstPage(
            @PathVariable("tenant-url") String tenant,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return this.list(tenant, 1, request, response);
    }

    @PreAuthorize("permitAll")
    @RequestMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            value = "list/{pageNumber}",
            method = RequestMethod.GET
    )
    public @ResponseBody
    UserList listSomePage(
            @PathVariable("tenant-url") String tenant,
            @PathVariable Integer pageNumber,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return this.list(tenant, pageNumber, request, response);
    }

    private UserList list(
            String tenant,
            Integer pageNumber,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        UserList result = new UserList();

        if (checkAccess(result, tenant, request, UserPermission.USER_LIST)) {
            Page<User> page = userService.list(getDBTenant(tenant), pageNumber);

            if (page != null) {
                result.setTotalPages(page.getTotalPages());
                result.setTotalElements(page.getTotalElements());

                if (page.getContent() != null
                        && !page.getContent().isEmpty()) {
                    result.setUserList(page.getContent());

                    response.setStatus(200);
                } else {
                    response.setStatus(204);
                }
            }
        }

        return result;
    }

    @PreAuthorize("permitAll")
    @RequestMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            value = "list-all",
            method = RequestMethod.GET
    )
    public @ResponseBody
    UserList listAllFirstPage(
            @PathVariable("tenant-url") String tenant,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return this.listAll(tenant, 1, request, response);
    }

    @PreAuthorize("permitAll")
    @RequestMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            value = "list-all/{pageNumber}",
            method = RequestMethod.GET
    )
    public @ResponseBody
    UserList listAllSomePage(
            @PathVariable("tenant-url") String tenant,
            @PathVariable Integer pageNumber,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return this.list(tenant, pageNumber, request, response);
    }

    private UserList listAll(
            String tenant,
            Integer pageNumber,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        UserList result = new UserList();

        if (checkAccess(result, tenant, request, UserPermission.ADMIN)) {
            Page<User> page = userService.listAll(pageNumber);

            if (page != null) {
                result.setTotalPages(page.getTotalPages());
                result.setTotalElements(page.getTotalElements());

                if (page.getContent() != null
                        && !page.getContent().isEmpty()) {
                    result.setUserList(page.getContent());

                    response.setStatus(200);
                } else {
                    response.setStatus(204);
                }
            }
        }

        return result;
    }

    @PreAuthorize("permitAll")
    @RequestMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            value = "{id}",
            method = RequestMethod.GET
    )
    public @ResponseBody
    UserAPI find(
            @PathVariable("tenant-url") String tenant,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserAPI result = new UserAPI();

        if (checkAccess(result, tenant, request, UserPermission.ADMIN, UserPermission.USER_LIST)) {
            Application app = getApplication(request);

            User user = null;

            if (app.getPermissions().contains(UserPermission.ADMIN)) {
                user = userService.find(id);
            } else if (app.getPermissions().contains(UserPermission.USER_LIST)) {
                user = userService.find(getDBTenant(tenant), id);
            }

            if (user != null) {
                result.setUser(user);

                response.setStatus(200);
            } else {
                response.setStatus(204);
            }
        }

        return result;
    }

    @PreAuthorize("permitAll")
    @RequestMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            value = "",
            method = RequestMethod.POST
    )
    public @ResponseBody
    UserAPI create(
            @PathVariable("tenant-url") String tenantUrl,
            @RequestBody User entity,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserAPI result = new UserAPI();

        if (checkAccess(result, tenantUrl, request, UserPermission.USER_CREATE)) {
            if (entity != null
                    && entity.getEmail() != null) {
                User dbEntity = userService.findByLogin(entity.getEmail());

                if (dbEntity != null) {
                    result.setMessage(MessageBundle.getMessageBundle("error.203.email.already.used", getDBTenant(tenantUrl).getName()));

                    response.setStatus(203);
                }
            } else {
                Tenant tenant = getDBTenant(tenantUrl);

                entity.setTenant(tenant);

                boolean hasChangePassword = true;

                entity.setPassword("newUser-this-password-need-to-be-changed-!!!!!");

                entity.setCreatedAt(new Date());

                Application app = getApplication(request);

                entity.setCreatedBy(app.getCreatedBy());

                entity.setStatus(UserStatus.ACTIVE);
                entity.setEnabled(true);

                if (entity.getPermissions().contains(UserPermission.ADMIN)) {
                    if (!app.getPermissions().contains(UserPermission.ADMIN)) {
                        throw new AccessDeniedException(8, MessageBundle.getMessageBundle("error.403.8"));
                    }
                }

                entity = userService.save(
                        getServerUrl(request, tenantUrl),
                        entity,
                        hasChangePassword,
                        null
                );

                if (entity != null) {
                    result.setUser(entity);

                    response.setStatus(201);
                } else {
                    response.setStatus(203);
                }
            }
        }

        return result;
    }

    @PreAuthorize("permitAll")
    @RequestMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            value = "",
            method = RequestMethod.PUT
    )
    public @ResponseBody
    UserAPI update(
            @PathVariable("tenant-url") String tenantUrl,
            @RequestBody User entity,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserAPI result = new UserAPI();

        if (checkAccess(result, tenantUrl, request, UserPermission.USER_CREATE)) {
            Tenant tenant = getDBTenant(tenantUrl);

            entity.setTenant(tenant);

            boolean hasChangePassword = true;

            if (entity != null
                    && entity.getId() != null) {
                hasChangePassword = false;

                User dbEntity = userService.find(entity.getId());

                if (dbEntity != null) {
                    dbEntity.setName(entity.getName());
                    dbEntity.setEmail(entity.getEmail());

                    Application app = getApplication(request);

                    if (app.getPermissions().contains(UserPermission.ADMIN)
                            || app.getPermissions().contains(UserPermission.TENANT_MANAGER)) {
                        dbEntity.setTenant(entity.getTenant());
                    }

                    for (Long groupId : entity.getGroupIds()) {
                        if (!dbEntity.getGroupIds().contains(groupId)) {
                            UserGroup userGroup = userGroupService.find(groupId);

                            dbEntity.getGroups().add(userGroup);
                        }
                    }

                    if (dbEntity.getPermissions().contains(UserPermission.ADMIN)) {
                        if (!app.getPermissions().contains(UserPermission.ADMIN)) {
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
                        response.setStatus(203);
                    }
                } else {
                    response.setStatus(204);
                }
            } else {
                response.setStatus(204);
            }
        }

        return result;
    }

    @PreAuthorize("permitAll")
    @RequestMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            value = "{id}",
            method = RequestMethod.DELETE
    )
    public @ResponseBody
    UserAPI delete(
            @PathVariable("tenant-url") String tenant,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserAPI result = new UserAPI();

        if (checkAccess(result, tenant, request, UserPermission.USER_PERMISSION_REMOVE)) {
            User user = userService.find(id);

            if (user != null) {
                if (UserStatus.ADMIN.equals(user.getStatus())) {
                    throw new AccessDeniedException(9, MessageBundle.getMessageBundle("error.403.9"));
                } else if (user.getPermissions().contains(UserPermission.ADMIN)) {
                    Application app = getApplication(request);

                    if (!app.getPermissions().contains(UserPermission.ADMIN)) {
                        throw new AccessDeniedException(10, MessageBundle.getMessageBundle("error.403.10"));
                    }
                }

                userService.remove(user);

                result.setUser(null);

                response.setStatus(200);
            } else {
                result.setMessage("Not deleted");

                response.setStatus(203);
            }
        }

        return result;
    }

}
