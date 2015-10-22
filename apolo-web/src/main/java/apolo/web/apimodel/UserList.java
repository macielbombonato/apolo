package apolo.web.apimodel;

import apolo.data.model.User;
import apolo.web.apimodel.base.BaseAPIModel;

import java.util.List;

public class UserList extends BaseAPIModel {

    private long totalPages;

    private long totalElements;

    private List<User> userList;

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
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
