package apolo.web.service;


import apolo.business.service.EmailService;
import apolo.business.service.TenantService;
import apolo.business.service.UserService;
import apolo.common.config.model.ApplicationProperties;
import apolo.common.exception.AccessDeniedException;
import apolo.common.util.MessageBundle;
import apolo.common.util.ThreadLocalContextUtil;
import apolo.data.enums.Status;
import apolo.data.enums.UserStatus;
import apolo.data.model.Tenant;
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
import org.springframework.security.web.authentication.WebAuthenticationDetails;
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
	private TenantService tenantService;

	@Inject
	@Named("smtpEmailService")
	private EmailService emailService;

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		User user = null;
		Tenant tenant = null;
		String tenantUrl = null;

		String[] userdata = authentication.getName().split("/");

		if (userdata != null
				&& userdata.length > 1) {

			tenantUrl = userdata[0];

			tenant = tenantService.getValidatedTenant(tenantUrl);

			user = userService.loadByUsernameAndPassword(
					tenant,
					userdata[1],
					authentication.getCredentials().toString()
			);
		}

		if (user != null
				&& user.getId() != null) {
			if (UserStatus.LOCKED.equals(user.getStatus())) {
				String message = MessageBundle.getMessageBundle("error.403.2");
				throw new BadCredentialsException(message, new AccessDeniedException(0, message));
			} else if (user.getTenant() != null
					&& Status.LOCKED.equals(user.getTenant().getStatus())) {
				String message = MessageBundle.getMessageBundle("error.403.0");
				throw new BadCredentialsException(message, new AccessDeniedException(0, message));
			}

			// Increase the access count
			try {
				WebAuthenticationDetails wad = null;
				String userIPAddress         = null;
				boolean isAuthenticatedByIP  = false;

				// Get the IP address of the user tyring to use the site
				wad = (WebAuthenticationDetails) authentication.getDetails();
				userIPAddress = wad.getRemoteAddress();

				user.setSignInCount(userService.increaseSignInCounter(user, userIPAddress));
			} catch (Throwable e) {
				log.error("********* => Error when try to count the login sequence");
				log.error(e.getMessage(), e);
			}

			// Set permission to see only the code screen.
			Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

			authorities = userService.loadUserAuthorities(user);

			ThreadLocalContextUtil.setTenantId(user.getTenant().getUrl());

			if (Boolean.TRUE.equals(tenant.getSendAuthEmail())) {
				try {
					emailService.sendAsync(
							tenant,
							tenant.getName(),
							tenant.getEmailFrom(),
							user.getName(),
							user.getEmail(),
							user.getTenant().getName() + ": " + MessageBundle.getMessageBundle("mail.auth.subject"),
							buildAuthenticationMessage(user).toString()
					);
				} catch (Throwable e) {
					log.error("********* => Error when try to send authentication email");
					log.error(e.getMessage(), e);
				}
			}

			if (user.getDbTenant() != null
					&& user.getDbTenant().getUrl() != null
					&& !"".equals(user.getDbTenant().getUrl())) {
				ThreadLocalContextUtil.setTenantId(user.getDbTenant().getUrl());
			} else if (tenantUrl == null || !tenantUrl.isEmpty()) {
				ThreadLocalContextUtil.setTenantId(applicationProperties.getDefaultTenant());
			}

			return new CurrentUser(
					user.getId(),
					user.getEmail(),
					user.getPassword().toLowerCase(),
					user,
					authorities
			);
		} else {
			throw new BadCredentialsException(MessageBundle.getMessageBundle("error.403.1"));
		}

	}

	@SuppressWarnings("rawtypes")
	public boolean supports(Class authentication) {
		return (UsernamePasswordAuthenticationToken.class
				.isAssignableFrom(authentication));
	}

	private StringBuilder buildAuthenticationMessage(User user) {
		StringBuilder result = new StringBuilder();

		result.append("<html>");
		result.append("<body>");

		result.append("<h1>");
		result.append(user.getTenant().getName() + ": " + MessageBundle.getMessageBundle("mail.auth.subject"));
		result.append("</h1>");

		result.append("<p>");
		result.append(MessageBundle.getMessageBundle("mail.auth.message", user.getName(), user.getSignInCount()));
		result.append("</p>");

		result.append("</body>");
		result.append("</html>");

		return result;
	}
}
