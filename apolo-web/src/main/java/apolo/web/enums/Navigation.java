package apolo.web.enums;


public enum Navigation {

	INDEX("/index"),
	VERSION("/version"),
	INSTALL("/install"),
	INSTALL_NEW("/install/new"),

	USER_INDEX("user"),
	USER_NEW("user/new"),
	USER_EDIT("user/edit"),
	USER_LIST("user/list"),
	USER_SEARCH("user/search"),
	USER_VIEW("user/view"),
	USER_CHANGE_PASSWORD("user/change-password"),
	USER_FORGOT_PASSWORD("user/forgot-password"),

	USER_RESET_PASSWORD("user/reset-password"),

	USER_PERMISSION_LIST("user-group/list"),
	USER_PERMISSION_SEARCH("user-group/search"),
	USER_PERMISSION_NEW("user-group/new"),
	USER_PERMISSION_EDIT("user-group/edit"),
	USER_PERMISSION_VIEW("user-group/view"),

	USER_CUSTOM_FIELD_LIST("user-custom-field/list"),
	USER_CUSTOM_FIELD_SEARCH("user-custom-field/search"),
	USER_CUSTOM_FIELD_NEW("user-custom-field/new"),
	USER_CUSTOM_FIELD_EDIT("user-custom-field/edit"),
	USER_CUSTOM_FIELD_VIEW("user-custom-field/view"),

	CONFIGURATION_LIST("configuration/list"),
	CONFIGURATION_NEW("configuration/new"),
	CONFIGURATION_EDIT("configuration/edit"),
	CONFIGURATION_VIEW("configuration/view"),

	AUTH_LOGIN("login"),
	AUTH_LOGOUT("logout"),

	AUDIT_LOG_LIST("auditlog/list"),

	ERROR("error/error"),

	TENANT_LIST("tenant/list"),
	TENANT_SEARCH("tenant/search"),
	TENANT_NEW("tenant/new"),
	TENANT_EDIT("tenant/edit"),
	TENANT_VIEW("tenant/view"),
	
	;
	
	protected String path;

	private Navigation(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
	
}
