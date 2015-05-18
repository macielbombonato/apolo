package apolo.web.config;


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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private TenantService tenantService;

	@Autowired
	private EmailService emailService;
	
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		User user = userService.loadByUsernameAndPassword(
				authentication.getName(), 
				authentication.getCredentials().toString() 
			);
		
		String tenantUrl = ThreadLocalContextUtil.getTenantId();
		
		if (tenantUrl == null || !tenantUrl.isEmpty()) {
			ThreadLocalContextUtil.setTenantId(applicationProperties.getDefaultTenant());
		}

		if (user != null) {
			if (UserStatus.LOCKED.equals(user.getStatus())) {
				String message = MessageBundle.getMessageBundle("error.403.2");
				throw new BadCredentialsException(message, new AccessDeniedException(0, message));
			} else if (user.getTenant() != null
					&& Status.LOCKED.equals(user.getTenant().getStatus())) {
				String message = MessageBundle.getMessageBundle("error.403.0");
				throw new BadCredentialsException(message, new AccessDeniedException(0, message));
			}
			
			// Set permission to see only the code screen.
			Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			
			authorities = userService.loadUserAuthorities(user);
			
			ThreadLocalContextUtil.setTenantId(user.getTenant().getUrl());

			Tenant tenant = tenantService.getValidatedTenant(user.getTenant().getUrl());

			if (Boolean.TRUE.equals(tenant.getSendAuthEmail())) {
				emailService.send(
						tenant,
						user.getTenant().getName() + ": " + MessageBundle.getMessageBundle("mail.auth.subject"),
						user.getEmail(),
						MessageBundle.getMessageBundle("mail.auth.message")
				);
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
}
