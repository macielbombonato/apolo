package apolo.web.config;


import apolo.business.service.UserService;
import apolo.common.config.model.ApplicationProperties;
import apolo.common.exception.AccessDeniedException;
import apolo.common.util.MessageBundle;
import apolo.common.util.ThreadLocalContextUtil;
import apolo.data.enums.Status;
import apolo.data.model.User;
import apolo.security.CurrentUser;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		User user = userService.loadByUsernameAndPassword(
				authentication.getName(), 
				authentication.getCredentials().toString() 
			);
		
		String tenant = ThreadLocalContextUtil.getTenantId();
		
		if (tenant == null || !tenant.isEmpty()) {
			ThreadLocalContextUtil.setTenantId(applicationProperties.getDefaultTenant());
		}

		if (user != null) {
			if (user.getTenant() != null
					&& user.getTenant().getStatus().equals(Status.LOCKED)) {
				String message = MessageBundle.getMessageBundle("error.403.0");
				throw new BadCredentialsException(message, new AccessDeniedException(0, message));
			}
			
			// Set permission to see only the code screen.
			Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			
			authorities = userService.loadUserAuthorities(user);
			
			ThreadLocalContextUtil.setTenantId(user.getTenant().getUrl());
			
			ThreadLocalContextUtil.setLanguage(user.getLanguage());
			
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
