package apolo.api.apimodel;

import apolo.api.apimodel.base.BaseAPIModel;
import apolo.data.enums.UserStatus;

import java.util.List;

public class UserStatusList extends BaseAPIModel {

    private List<UserStatus> statuses;

    public List<UserStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<UserStatus> statuses) {
        this.statuses = statuses;
    }
}
