package apolo.web.config;

import apolo.common.config.model.ApplicationProperties;
import apolo.common.util.ApoloCrypt;
import apolo.security.UserPermission;
import apolo.web.service.UserAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.ContextLoader;

import javax.inject.Inject;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Inject
    private UserAuthenticationProvider userAuthenticationProvider;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userAuthenticationProvider);
    }

    @Bean @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new ApoloCrypt();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
		/*
		 * This class is created by jpa engine and because this is necessary get dependencies in spring context.
		 */
        if (applicationProperties == null) {
            ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();

            applicationProperties = (ApplicationProperties) ctx.getBean("applicationProperties");
        }

    	http.csrf().disable();

        http.authorizeRequests()
                .antMatchers(
                        "/version/**",
                        "/version"
                    ).hasRole(
                            UserPermission.ADMIN.getCode()
                        );

        http.authorizeRequests()
                .antMatchers(
                        "/install/**",
                        "/install",
                        "/login",
                        "/loginfailed",
                        "/logout",
                        "/index",
                        "/",
                        "/login",
                        "/resources/**",
                        "/**/uploadedfiles/**",
                        "/error/**"
                    ).permitAll();

        http.authorizeRequests()
            .and()
                .exceptionHandling()
                .accessDeniedPage("/error/403")
            .and()
                .formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginPage("/login")
                .loginProcessingUrl("/login")
                        //.defaultSuccessUrl("/", true)
                            .failureUrl("/loginfailed")
                    .permitAll()
            .and()
                .logout()
                .logoutUrl("/logout")
                    .permitAll();
    }
}