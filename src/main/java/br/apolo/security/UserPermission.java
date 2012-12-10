package br.apolo.security;

import org.springframework.security.access.ConfigAttribute;

public enum UserPermission implements ConfigAttribute {
	ADMIN,
	
	USER, 
	USER_CREATE, 
	USER_EDIT, 
	USER_LIST;
	
	public String getAttribute() {
		return "ROLE_" + name(); // the spring security needs the ROLE prefix
	}
}
