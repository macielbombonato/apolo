package br.apolo.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CurrentUser extends User {

	private static final long serialVersionUID = 8790909411747968450L;

	private Long id;
	
	private br.apolo.data.model.User systemUser;

	public CurrentUser(Long id, String username, String password, br.apolo.data.model.User systemUser, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.id = id;
		this.systemUser = systemUser;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public br.apolo.data.model.User getSystemUser() {
		return systemUser;
	}

	public void setSystemUser(br.apolo.data.model.User systemUser) {
		this.systemUser = systemUser;
	}

}
