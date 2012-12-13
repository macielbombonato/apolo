package br.apolo.security;

import org.springframework.security.access.ConfigAttribute;

public enum UserPermission implements ConfigAttribute {
	ADMIN,
	
	USER, 
	USER_CREATE, 
	USER_EDIT,
	USER_REMOVE,
	USER_LIST,
	
	USER_PERMISSION_LIST,
	USER_PERMISSION_VIEW,
	USER_PERMISSION_CREATE,
	USER_PERMISSION_EDIT,
	USER_PERMISSION_REMOVE,
	;
	
	public String getAttribute() {
		return "ROLE_" + name(); // the spring security needs the ROLE prefix
	}
}
