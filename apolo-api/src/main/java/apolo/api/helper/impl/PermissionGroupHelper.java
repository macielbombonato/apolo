package apolo.api.helper.impl;

import apolo.api.apimodel.PermissionGroupDTO;
import apolo.api.helper.ApoloHelper;
import apolo.data.model.PermissionGroup;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("permissionGroupHelper")
public class PermissionGroupHelper implements ApoloHelper<PermissionGroup, PermissionGroupDTO> {

    public PermissionGroupDTO toDTO(PermissionGroup from) {
        PermissionGroupDTO to = new PermissionGroupDTO();

        if (from != null) {
            to.setId(from.getId());
            to.setName(from.getName());
            to.setPermissions(from.getPermissions());
            to.setStatus(from.getStatus());
        }

        return to;
    }

    public List<PermissionGroupDTO> toDTOList(List<PermissionGroup> from) {
        List<PermissionGroupDTO> to = new ArrayList<PermissionGroupDTO>();

        if (from != null) {
            for (PermissionGroup entity : from) {
                to.add(toDTO(entity));
            }
        }

        return to;
    }

    public PermissionGroup toEntity(PermissionGroupDTO from) {
        PermissionGroup to = new PermissionGroup();

        if (from != null) {
            to.setId(from.getId());
            to.setName(from.getName());
            to.setPermissions(from.getPermissions());
            to.setStatus(from.getStatus());
        }

        return to;
    }
}
