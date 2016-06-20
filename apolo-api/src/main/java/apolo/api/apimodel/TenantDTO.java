package apolo.api.apimodel;

import apolo.api.apimodel.base.BaseAPIModel;
import apolo.data.enums.Status;

public class TenantDTO extends BaseAPIModel {

    private Long id;
    private String name;
    private String url;
    private String logo;
    private String icon;
    private Integer logoWidth;
    private Integer logoHeight;

    private Status status;
    private Boolean showName;
    private Boolean showAdds;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getLogoWidth() {
        return logoWidth;
    }

    public void setLogoWidth(Integer logoWidth) {
        this.logoWidth = logoWidth;
    }

    public Integer getLogoHeight() {
        return logoHeight;
    }

    public void setLogoHeight(Integer logoHeight) {
        this.logoHeight = logoHeight;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getShowName() {
        return showName;
    }

    public void setShowName(Boolean showName) {
        this.showName = showName;
    }

    public Boolean getShowAdds() {
        return showAdds;
    }

    public void setShowAdds(Boolean showAdds) {
        this.showAdds = showAdds;
    }
}
