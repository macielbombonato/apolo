package apolo.api.apimodel;

import apolo.api.apimodel.base.BaseAPIModel;

import java.util.List;

public class UserList extends BaseAPIModel {

    private long totalPages;

    private long totalElements;

    private List<UserDTO> userList;

    public List<UserDTO> getUserList() {
        return userList;
    }

    public void setUserList(List<UserDTO> userList) {
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
