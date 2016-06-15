package apolo.api.apimodel;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class UserFormModel {

    private Long id;
    private String name;
    private String email;
    private String mobile;
    private String password;
    private List<Long> groupIds;
    private MultipartFile picturefile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Long> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<Long> groupIds) {
        this.groupIds = groupIds;
    }

    public MultipartFile getPicturefile() {
        return picturefile;
    }

    public void setPicturefile(MultipartFile picturefile) {
        this.picturefile = picturefile;
    }
}
