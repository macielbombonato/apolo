package apolo.api.apimodel;

import apolo.api.apimodel.base.BaseAPIModel;

import java.util.List;

public class TenantList extends BaseAPIModel {

    private long totalPages;

    private long totalElements;

    private List<TenantDTO> tenants;

    public List<TenantDTO> getTenants() {
        return tenants;
    }

    public void setTenants(List<TenantDTO> tenants) {
        this.tenants = tenants;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}
