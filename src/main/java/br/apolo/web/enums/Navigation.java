package br.apolo.web.enums;

public enum Navigation {
	
	HOME("/"), 
	INDEX("index"),
	
	USER("user"),
	USER_INDEX("user/index"),
	USER_NEW("user/new"),
	USER_EDIT("user/edit"),
	USER_LIST("user/list"),
	USER_VIEW("user/view"),
	USER_CHANGE_PASSWORD("user/change-password"),
	
	USER_PERMISSION_LIST("user-group/list"),
	USER_PERMISSION_CREATE("user-group/new"),
	USER_PERMISSION_EDIT("user-group/edit"),
	
	
	AUTH("auth"),
	AUTH_LOGIN("auth/login"),
	AUTH_LOGOUT("auth/logout"),
	
	ERROR("error/error"),
	
	;
	
	
	private String path;

	private Navigation(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

}
