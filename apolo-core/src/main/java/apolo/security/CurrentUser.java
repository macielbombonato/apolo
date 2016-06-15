package apolo.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CurrentUser extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 8790909411747968450L;

	private Long id;

	private String name;
	
	private apolo.data.model.User systemUser;

	public CurrentUser(
				Long id, 
				String username, 
				String password, 
				apolo.data.model.User systemUser, 
				Collection<? extends GrantedAuthority> authorities
			) {
		
		super(systemUser, password, authorities);
		this.id = id;
		this.name = username;
		this.systemUser = systemUser;
		super.setDetails(systemUser);
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

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
