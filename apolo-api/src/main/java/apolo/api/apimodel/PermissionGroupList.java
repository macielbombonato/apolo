package apolo.api.apimodel;

import apolo.api.apimodel.base.BaseAPIModel;

import java.util.List;

public class PermissionGroupList extends BaseAPIModel {

    private List<PermissionGroupDTO> groupList;

    public List<PermissionGroupDTO> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<PermissionGroupDTO> groupList) {
        this.groupList = groupList;
    }
}
