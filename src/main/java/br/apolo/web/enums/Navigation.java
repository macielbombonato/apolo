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
	
	
	AUTH("auth"),
	AUTH_LOGIN("auth/login"),
	AUTH_LOGOUT("auth/logout"),
	
	ERROR_401("error/401"),
	ERROR_403("auth/403"),
	ERROR_500("error/500"),
	
	;
	
	
	private String path;

	private Navigation(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

}
