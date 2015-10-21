package apolo.data.model;

import apolo.data.model.base.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "userconnection")
@AttributeOverride(name = "id", column = @Column(name = "userconnection_id"))
public class UserConnection extends BaseEntity<Long> {

    @Column(name = "userId", nullable = false)
    private String userId;

    @Column(name = "providerId", nullable = false)
    private String providerId;

    @Column(name = "providerUserId", nullable = true)
    private String providerUserId;


    @Column(name = "rank", nullable = true)
    private Long rank;

    @Column(name = "displayName", nullable = true)
    private String displayName;

    @Column(name = "profileUrl", nullable = true)
    private String profileUrl;

    @Column(name = "imageUrl", nullable = true)
    private String imageUrl;

    @Column(name = "accessToken", nullable = false)
    private String accessToken;

    @Column(name = "secret", nullable = true)
    private String secret;

    @Column(name = "refreshToken", nullable = true)
    private String refreshToken;

    @Column(name = "expireTime", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expireTime;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}
