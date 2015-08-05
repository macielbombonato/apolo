package apolo.security;

import apolo.business.enums.SocialMediaServiceType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.social.security.SocialUser;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UserDetails extends SocialUser {

    private static final long serialVersionUID = 8790909411747968450L;

    private Long id;

    private String firstName;

    private String lastName;

    private UserPermission role;

    private apolo.data.model.User systemUser;

    private SocialMediaServiceType socialSignInProvider;

    public UserDetails(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(username, password, authorities);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public apolo.data.model.User getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(apolo.data.model.User systemUser) {
        this.systemUser = systemUser;
    }

    public static class Builder {

        private Long id;

        private String username;

        private String firstName;

        private String lastName;

        private String password;

        private UserPermission role;

        private SocialMediaServiceType socialSignInProvider;

        private Set<GrantedAuthority> authorities;

        public Builder() {
            this.authorities = new HashSet<GrantedAuthority>();
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder password(String password) {
            if (password == null) {
                password = "SocialUser";
            }

            this.password = password;
            return this;
        }

        public Builder role(UserPermission role) {
            this.role = role;

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getCode());
            this.authorities.add(authority);

            return this;
        }

        public Builder socialSignInProvider(SocialMediaServiceType socialSignInProvider) {
            this.socialSignInProvider = socialSignInProvider;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public UserDetails build() {
            UserDetails user = new UserDetails(username, password, authorities);

            user.id = id;
            user.firstName = firstName;
            user.lastName = lastName;
            user.role = role;
            user.socialSignInProvider = socialSignInProvider;

            return user;
        }
    }

}
