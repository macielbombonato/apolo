package apolo.api.helper.impl;

import apolo.api.apimodel.TenantDTO;
import apolo.api.apimodel.UserDTO;
import apolo.api.helper.ApoloHelper;
import apolo.data.model.Tenant;
import apolo.data.model.User;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Component("userHelper")
public class UserHelper implements ApoloHelper<User, UserDTO> {

    @Inject
    private ApoloHelper<Tenant, TenantDTO> tenantHelper;

    public UserDTO toDTO(User from) {
        UserDTO to = new UserDTO();

        if (from != null) {
            to.setId(from.getId());
            to.setName(from.getName());
            to.setAvatarContentType(from.getAvatarContentType());
            to.setAvatarFileName(from.getAvatarFileName());
            to.setEmail(from.getEmail());
            to.setGroupIds(from.getGroupIds());
            to.setMobile(from.getMobile());
            to.setStatus(from.getStatus());
            to.setPermissions(from.getPermissions());

            to.setTenant(tenantHelper.toDTO(from.getTenant()));
        }

        return to;
    }

    public List<UserDTO> toDTOList(List<User> from) {
        List<UserDTO> to = new ArrayList<UserDTO>();

        if (from != null) {
            for (User entity : from) {
                to.add(toDTO(entity));
            }
        }

        return to;
    }

    public User toEntity(UserDTO from) {
        User to = new User();

        if (from != null) {
            to.setId(from.getId());
            to.setName(from.getName());
            to.setAvatarContentType(from.getAvatarContentType());
            to.setAvatarFileName(from.getAvatarFileName());
            to.setEmail(from.getEmail());
            to.setGroupIds(from.getGroupIds());
            to.setMobile(from.getMobile());
            to.setStatus(from.getStatus());

            to.setToken(from.getToken());

            to.setTenant(tenantHelper.toEntity(from.getTenant()));
        }

        return to;
    }
}
