package apolo.api.apimodel;

import apolo.api.apimodel.base.BaseAPIModel;
import apolo.data.model.PermissionGroup;

import java.util.List;

public class PermissionList extends BaseAPIModel {

    private long totalPages;

    private long totalElements;

    private List<PermissionGroup> groupList;

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

    public List<PermissionGroup> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<PermissionGroup> groupList) {
        this.groupList = groupList;
    }
}
