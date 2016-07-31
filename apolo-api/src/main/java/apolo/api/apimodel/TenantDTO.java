package apolo.api.apimodel;

import apolo.api.apimodel.base.BaseAPIModel;
import apolo.data.enums.Status;

public class TenantDTO extends BaseAPIModel {

    private Long id;
    private String name;
    private String url;
    private String theme;
    private Status status;
    private Boolean showAds;

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

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getShowAds() {
        return showAds;
    }

    public void setShowAds(Boolean showAds) {
        this.showAds = showAds;
    }
}
