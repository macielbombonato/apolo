package apolo.api.apimodel;

import apolo.api.apimodel.base.BaseAPIModel;
import apolo.data.enums.Status;

import java.util.List;

public class StatusList extends BaseAPIModel {

    private List<Status> statuses;

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }
}
