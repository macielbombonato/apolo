package apolo.web.config;

import apolo.security.UserPermission;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Inject
    private UserAuthenticationProvider userAuthenticationProvider;

    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userAuthenticationProvider);
    }
    
    @Bean @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.csrf()
        		.disable()
        	.authorizeRequests()
                .antMatchers(
                		"/resources/**", 
                        "/error/**",
                        "/install/**",
                        "/change-locale/**",
                        "/auth/login",
                        "/auth/loginfailed",
                        "/auth/validate-token",
                        "/auth/logout",
                        "/index",
                        "/",
                        "/login").permitAll()
                .anyRequest()
                	.hasAnyRole(
                			UserPermission.ADMIN.getCode(), 
                			UserPermission.AFTER_AUTH_USER.getCode()
                		)
            .and()
	            .exceptionHandling()
	                .accessDeniedPage("/error/403")
            .and()
	            .formLogin()
	                .usernameParameter("username")
	                .passwordParameter("password")
	                .loginPage("/auth/login")
	                .loginProcessingUrl("/login")
                	.defaultSuccessUrl("/user", true)
                	.failureUrl("/auth/loginfailed")
                		.permitAll()
            .and()
	            .logout()
	            	.logoutUrl("/auth/logout")
	            		.permitAll();
    }
}