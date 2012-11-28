package br.apolo.web.enums;

public enum Navigation {
	
	HOME("/"), 
	INDEX("index"),
	
	USER_INDEX("user/index"),
	
	;
	
	
	private String path;

	private Navigation(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

}
