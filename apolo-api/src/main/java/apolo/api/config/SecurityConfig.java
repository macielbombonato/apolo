package apolo.api.config;

import apolo.common.config.model.ApplicationProperties;
import apolo.security.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.ContextLoader;

import javax.inject.Inject;
import javax.inject.Named;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Inject
    @Named("userAuthenticationProvider")
    private AuthenticationProvider userAuthenticationProvider;

    @Inject
    @Named("apoloCrypt")
    private PasswordEncoder apoloCrypt;

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
		/*
		 * This class is created by jpa engine and because this is necessary get dependencies in spring context.
		 */
        if (apoloCrypt == null) {
            ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();

            apoloCrypt = (PasswordEncoder) ctx.getBean("apoloCrypt");
        }

        return apoloCrypt;
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
                            Permission.ADMIN.getCode()
                        );

        http.authorizeRequests()
                .antMatchers(
                        "/install/**",
                        "/install",
                        "/web/login",
                        "/web/loginfailed",
                        "/web/logout",
                        "/web/index",
                        "/web/",
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
                .loginPage("/web/login")
                .loginProcessingUrl("/web/login")
                    .defaultSuccessUrl("/web/", false)
                            .failureUrl("/web/loginfailed")
                    .permitAll()
            .and()
                .logout()
                .logoutUrl("/web/logout")
                    .permitAll();
    }
}