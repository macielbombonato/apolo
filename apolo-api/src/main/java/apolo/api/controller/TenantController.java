package apolo.api.controller;

import apolo.api.apimodel.ModelList;
import apolo.api.apimodel.TenantDTO;
import apolo.api.controller.base.BaseAPIController;
import apolo.api.helper.ApoloHelper;
import apolo.business.service.TenantService;
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

@RestController
@RequestMapping(value = "/tenant")
public class TenantController extends BaseAPIController<Tenant> {

    @Inject
    private TenantService tenantService;

    @Inject
    private ApoloHelper<Tenant, TenantDTO> tenantHelper;

    @CrossOrigin(origins = "*")
    @PreAuthorize("permitAll")
    @RequestMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET
    )
    public @ResponseBody
    ModelList<TenantDTO> list(
            @RequestParam(required = false) Integer pageNumber,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        ModelList<TenantDTO> result = new ModelList<TenantDTO>();

        if (checkAccess(result, null, request, Permission.TENANT_LIST)) {
            Page<Tenant> page = tenantService.list(pageNumber);

            if (page != null) {
                result.setTotalPages(page.getTotalPages());
                result.setTotalElements(page.getTotalElements());

                if (page.getContent() != null
                        && !page.getContent().isEmpty()) {
                    result.setList(tenantHelper.toDTOList(page.getContent()));

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
    TenantDTO find(
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        TenantDTO result = null;

        if (checkAccess(
                result,
                null,
                request,
                Permission.ADMIN,
                Permission.TENANT_LIST)
                ) {
            User requestUser = getUserFromRequest(request);

            Tenant entity = tenantService.find(id);

            if (entity != null) {
                result = tenantHelper.toDTO(entity);

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
    TenantDTO create(
            @RequestBody TenantDTO dto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        TenantDTO result = null;

        if (checkAccess(
                result,
                null,
                request,
                Permission.TENANT_CREATE)
                ) {
            if (dto != null) {
                Tenant entity = tenantHelper.toEntity(dto);

                entity = tenantService.save(entity);

                result = tenantHelper.toDTO(entity);

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
    TenantDTO update(
            @RequestBody TenantDTO dto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        TenantDTO result = null;

        if (checkAccess(
                result,
                null,
                request,
                Permission.TENANT_EDIT)
                ) {
            if (dto != null) {
                Tenant entity = tenantService.find(dto.getId());

                if (entity != null) {
                    entity = tenantService.save(tenantHelper.toEntity(dto));

                    result = tenantHelper.toDTO(entity);

                    response.setStatus(200);
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
            value = "changeTenant/{id}",
            method = RequestMethod.GET
    )
    public @ResponseBody
    TenantDTO changeTenant(
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        TenantDTO result = null;

        if (checkAccess(
                result,
                null,
                request,
                Permission.ADMIN,
                Permission.TENANT_MANAGER)
                ) {
            User requestUser = getUserFromRequest(request);

            Tenant entity = tenantService.find(id);

            if (entity != null) {
                result = tenantHelper.toDTO(entity);

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
