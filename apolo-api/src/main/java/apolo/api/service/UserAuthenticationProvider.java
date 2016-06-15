package apolo.api.service;

import apolo.business.service.EmailService;
import apolo.business.service.UserService;
import apolo.common.MessageBuilder;
import apolo.common.config.model.ApplicationProperties;
import apolo.common.exception.AccessDeniedException;
import apolo.common.util.MessageBundle;
import apolo.data.enums.UserStatus;
import apolo.data.model.User;
import apolo.security.CurrentUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;

@Component("userAuthenticationProvider")
public class UserAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(UserAuthenticationProvider.class);

    @Inject
    private UserService userService;

    @Inject
    private ApplicationProperties applicationProperties;

    @Inject
    @Named("smtpEmailService")
    private EmailService emailService;

    @Inject
    private MessageBuilder messageBuilder;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return this.authenticate(
                authentication,
                null,
                null
        );
    }

    public Authentication authenticate(
            Authentication authentication,
            String sessionId,
            String ipAddress
                ) throws AuthenticationException {

        User user = null;

        String username = authentication.getName();

        if (username != null
                && !"".equals(username)) {

            user = userService.loadByUsernameAndPassword(
                    username,
                    authentication.getCredentials().toString()
            );
        }

        if (user != null
                && user.getId() != null) {
            if (UserStatus.LOCKED.equals(user.getStatus())) {
                String message = "This user is locked. Please, contact system adminstrator.";
                throw new BadCredentialsException(message, new AccessDeniedException(0, message));
            }

            // Increase the access count
            try {
                user.setSignInCount(
                        userService.increaseSignInCounter(
                                user,
                                sessionId,
                                ipAddress
                        )
                );
            } catch (Throwable e) {
                log.error("********* => Error when try to count the login sequence");
                log.error(e.getMessage(), e);
            }

            // Set permission to see only the code screen.
            Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

            authorities = userService.loadUserAuthorities(user);

            if (user != null
                    && user.getTenant() != null
                    && Boolean.TRUE.equals(user.getTenant().getSendAuthEmail())) {
                try {
                    emailService.sendAsync(
                            user.getTenant(),
                            user.getTenant().getName(),
                            user.getTenant().getEmailFrom(),
                            user.getName(),
                            user.getEmail(),
                            user.getTenant().getName() + ": " + MessageBundle.getMessageBundle("mail.auth.subject"),
                            messageBuilder.buildAuthenticationMessage(
                                    user.getTenant().getName(),
                                    MessageBundle.getMessageBundle("mail.auth.subject"),
                                    MessageBundle.getMessageBundle("mail.auth.message", user.getName(), user.getSignInCount())
                            )
                    );
                } catch (Throwable e) {
                    log.error("********* => Error when try to send authentication email");
                    log.error(e.getMessage(), e);
                }
            }

            authentication = new CurrentUser(
                    user.getId(),
                    user.getEmail(),
                    user.getPassword().toLowerCase(),
                    user,
                    authorities
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return authentication;
        } else {
            throw new BadCredentialsException("Access Denied");
        }
    }

    @SuppressWarnings("rawtypes")
    public boolean supports(Class authentication) {
        return (UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(authentication));
    }
}
