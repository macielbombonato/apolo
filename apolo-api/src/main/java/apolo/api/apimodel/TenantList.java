package apolo.api.apimodel;

import apolo.api.apimodel.base.BaseAPIModel;

import java.util.List;

public class TenantList extends BaseAPIModel {

    private List<TenantDTO> tenants;

    public List<TenantDTO> getTenants() {
        return tenants;
    }

    public void setTenants(List<TenantDTO> tenants) {
        this.tenants = tenants;
    }
}
