package apolo.api.apimodel;

import apolo.api.apimodel.base.BaseAPIModel;
import apolo.data.enums.UserStatus;
import apolo.security.Permission;

import java.util.List;
import java.util.Set;

public class UserDTO extends BaseAPIModel {

    private Long id;
    private String name;
    private String email;
    private String mobile;
    private String password;
    private List<Long> groupIds;
    private Set<Permission> permissions;

    private String token;
    private UserStatus status;

    private TenantDTO tenant;

    private String avatarFileName;
    private String avatarContentType;

    private String filesURL;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatarFileName() {
        return avatarFileName;
    }

    public void setAvatarFileName(String avatarFileName) {
        this.avatarFileName = avatarFileName;
    }

    public String getAvatarContentType() {
        return avatarContentType;
    }

    public void setAvatarContentType(String avatarContentType) {
        this.avatarContentType = avatarContentType;
    }

    public TenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(TenantDTO tenant) {
        this.tenant = tenant;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public String getFilesURL() {
        return filesURL;
    }

    public void setFilesURL(String filesURL) {
        this.filesURL = filesURL;
    }

}
