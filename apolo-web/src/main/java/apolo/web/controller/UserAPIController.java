package apolo.web.controller;

import apolo.data.enums.UserStatus;
import apolo.data.model.Application;
import apolo.data.model.Tenant;
import apolo.data.model.User;
import apolo.security.UserPermission;
import apolo.web.apimodel.UserAPI;
import apolo.web.apimodel.UserList;
import apolo.web.controller.base.BaseAPIController;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@RequestMapping(value = "/api/{tenant-url}/user")
public class UserAPIController extends BaseAPIController<User> {

    @PreAuthorize("permitAll")
    @RequestMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            value = "list",
            method = RequestMethod.GET
    )
    public @ResponseBody
    UserList listFirstPage(
            @PathVariable("tenant-url") String tenant,
            HttpServletRequest request
    ) {
        return this.list(tenant, 1, request);
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
            HttpServletRequest request
    ) {
        return this.list(tenant, pageNumber, request);
    }

    private UserList list(
            String tenant,
            Integer pageNumber,
            HttpServletRequest request
    ) {

        UserList result = new UserList();

        if (checkAccess(result, tenant, request, UserPermission.USER_LIST)) {
            Page<User> page = userService.list(getDBTenant(tenant), pageNumber);

            result.setSuccess(true);
            result.setTotalPages(page.getTotalPages());
            result.setTotalElements(page.getTotalElements());

            result.setUserList(page.getContent());
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
            HttpServletRequest request
    ) {
        return this.listAll(tenant, 1, request);
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
            HttpServletRequest request
    ) {
        return this.list(tenant, pageNumber, request);
    }

    private UserList listAll(
            String tenant,
            Integer pageNumber,
            HttpServletRequest request
    ) {

        UserList result = new UserList();

        if (checkAccess(result, tenant, request, UserPermission.ADMIN)) {
            Page<User> page = userService.listAll(pageNumber);

            result.setSuccess(true);
            result.setTotalPages(page.getTotalPages());
            result.setTotalElements(page.getTotalElements());

            result.setUserList(page.getContent());
        }

        return result;
    }

    @PreAuthorize("permitAll")
    @RequestMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            value = "find/{id}",
            method = RequestMethod.GET
    )
    public @ResponseBody
    UserAPI find(
            @PathVariable("tenant-url") String tenant,
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        UserAPI result = new UserAPI();

        if (checkAccess(result, tenant, request, UserPermission.USER_LIST)) {
            User user = userService.find(getDBTenant(tenant), id);

            result.setSuccess(true);
            result.setUser(user);
        }

        return result;
    }

    @PreAuthorize("permitAll")
    @RequestMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            value = "find-all/{id}",
            method = RequestMethod.GET
    )
    public @ResponseBody
    UserAPI findRoot(
            @PathVariable("tenant-url") String tenant,
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        UserAPI result = new UserAPI();

        if (checkAccess(result, tenant, request, UserPermission.ADMIN)) {
            User user = userService.find(id);

            result.setSuccess(true);
            result.setUser(user);
        }

        return result;
    }

    @PreAuthorize("permitAll")
    @RequestMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            value = "save",
            method = RequestMethod.POST
    )
    public @ResponseBody
    UserAPI save(
            @PathVariable("tenant-url") String tenantUrl,
            @RequestBody User entity,
            HttpServletRequest request
    ) {
        UserAPI result = new UserAPI();

        if (checkAccess(result, tenantUrl, request, UserPermission.USER_CREATE)) {
            Tenant tenant = getDBTenant(tenantUrl);

            entity.setTenant(tenant);

            boolean hasChangePassword = true;

            if (entity != null
                    && entity.getId() != null) {
                hasChangePassword = false;
            } else {
                hasChangePassword = true;
                entity.setPassword("newUser-this-password-need-to-be-changed-!!!!!");

                entity.setCreatedAt(new Date());

                Application app = getApplication(request);

                entity.setCreatedBy(app.getCreatedBy());

                entity.setStatus(UserStatus.ACTIVE);
                entity.setEnabled(true);
            }

            entity = userService.save(getServerUrl(request, tenantUrl), entity, hasChangePassword, null);

            result.setSuccess(true);
            result.setUser(entity);
        }

        return result;
    }

    @PreAuthorize("permitAll")
    @RequestMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            value = "delete/{id}",
            method = RequestMethod.DELETE
    )
    public @ResponseBody
    UserAPI delete(
            @PathVariable("tenant-url") String tenant,
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        UserAPI result = new UserAPI();

        if (checkAccess(result, tenant, request, UserPermission.ADMIN)) {
            User user = userService.find(id);

            if (user != null) {
                userService.remove(user);

                result.setSuccess(true);
                result.setUser(null);
            } else {
                result.setSuccess(false);
                result.setMessage("Not deleted");
            }
        }

        return result;
    }

}
