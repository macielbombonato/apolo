package br.apolo.security;

import org.springframework.security.access.ConfigAttribute;

/**
 * UserPermission
 * 
 * IMPORTANT NOTE:
 * All permission inserted here needs the corresponding internationalization term in the i18n file (resources/messages.properties).
 * If the term doesn´t exist, the user will get an error in the GroupPermission page.
 * 
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 */
public enum UserPermission implements ConfigAttribute {
	ADMIN,
	
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
