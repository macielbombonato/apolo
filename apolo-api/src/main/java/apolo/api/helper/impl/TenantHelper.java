package apolo.api.helper.impl;

import apolo.api.apimodel.TenantDTO;
import apolo.api.helper.ApoloHelper;
import apolo.data.model.Tenant;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("tenantHelper")
public class TenantHelper implements ApoloHelper<Tenant, TenantDTO> {

    public TenantDTO toDTO(Tenant from) {
        TenantDTO to = new TenantDTO();

        if (from != null) {
            to.setId(from.getId());
            to.setName(from.getName());
            to.setUrl(from.getUrl());
            to.setShowAds(from.getShowAds());
            to.setStatus(from.getStatus());
            to.setTheme(from.getTheme());
        }

        return to;
    }

    public List<TenantDTO> toDTOList(List<Tenant> from) {
        List<TenantDTO> to = new ArrayList<TenantDTO>();

        if (from != null) {
            for (Tenant entity : from) {
                to.add(toDTO(entity));
            }
        }

        return to;
    }

    public Tenant toEntity(TenantDTO from) {
        Tenant to = new Tenant();

        if (from != null) {
            to.setId(from.getId());
            to.setName(from.getName());
            to.setUrl(from.getUrl());
            to.setShowAds(from.getShowAds());
            to.setStatus(from.getStatus());
            to.setTheme(from.getTheme());
        }

        return to;
    }
}
