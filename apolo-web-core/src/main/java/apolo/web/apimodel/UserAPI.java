package apolo.web.apimodel;

import apolo.data.model.User;
import apolo.web.apimodel.base.BaseAPIModel;

public class UserAPI extends BaseAPIModel {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
