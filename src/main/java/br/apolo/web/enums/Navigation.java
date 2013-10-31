package br.apolo.web.enums;

import javax.servlet.http.HttpServletRequest;

import br.apolo.common.util.MessageBundle;

public enum Navigation {
	
	HOME("/") {
		@Override
		public String getNodeName() {
			return MessageBundle.getMessageBundle("breadcrumb.home");
		}

		@Override
		public int getNodeLevel() {
			return 0;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			return getDefaultUrl(request);
		}
		
		@Override
		public String getPath() {
			return path + "index";
		}
	}, 
	ABOUT("/about") {
		@Override
		public String getNodeName() {
			return MessageBundle.getMessageBundle("breadcrumb.about");
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			return getDefaultUrl(request);
		}
		
		@Override
		public String getPath() {
			return path + "/index";
		}
	},
	INSTALL("/install") {
		@Override
		public String getNodeName() {
			return null;
		}

		@Override
		public int getNodeLevel() {
			return 0;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			return getDefaultUrl(request);
		}
	},
	INSTALL_NEW("/install/new") {
		@Override
		public String getNodeName() {
			return null;
		}

		@Override
		public int getNodeLevel() {
			return 0;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			return getDefaultUrl(request);
		}
	},
	
	USER_INDEX("user") {
		@Override
		public String getNodeName() {
			return MessageBundle.getMessageBundle("breadcrumb.user.panel");
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			return getRefererUrl(request);
		}
		
		@Override
		public String getPath() {
			return this.path + "/index";
		}
	},
	USER_NEW("user/new") {
		@Override
		public String getNodeName() {
			return MessageBundle.getMessageBundle("breadcrumb.user.new");
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			return getDefaultUrl(request);
		}
	},
	USER_EDIT("user/edit") {
		@Override
		public String getNodeName() {
			return MessageBundle.getMessageBundle("breadcrumb.user.edit");
		}

		@Override
		public int getNodeLevel() {
			return 2;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			// TODO tratar save
			return getRefererUrl(request);
		}
	},
	USER_LIST("user/list") {
		@Override
		public String getNodeName() {
			return MessageBundle.getMessageBundle("breadcrumb.user.list");
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			return getDefaultUrl(request);
		}
	},
	USER_SEARCH("user/search") {
		@Override
		public String getNodeName() {
			return MessageBundle.getMessageBundle("breadcrumb.user.search");
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			return getDefaultUrl(request);
		}
	},
	USER_VIEW("user/view") {
		@Override
		public String getNodeName() {
			return MessageBundle.getMessageBundle("breadcrumb.user");
		}

		@Override
		public int getNodeLevel() {
			return 2;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			// TODO tratar save
			return getRefererUrl(request);
		}
	},
	USER_CHANGE_PASSWORD("user/change-password") {
		@Override
		public String getNodeName() {
			return MessageBundle.getMessageBundle("breadcrumb.user.changepassword");
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			return getDefaultUrl(request);
		}
	},
	
	USER_PERMISSION_LIST("user-group/list") {
		@Override
		public String getNodeName() {
			return MessageBundle.getMessageBundle("breadcrumb.usergroup.list");
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			return getDefaultUrl(request);
		}
	},
	USER_PERMISSION_SEARCH("user-group/search") {
		@Override
		public String getNodeName() {
			return MessageBundle.getMessageBundle("breadcrumb.usergroup.search");
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			return getDefaultUrl(request);
		}
	},
	USER_PERMISSION_NEW("user-group/new") {
		@Override
		public String getNodeName() {
			return MessageBundle.getMessageBundle("breadcrumb.usergroup.new");
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			return getDefaultUrl(request);
		}
	},
	USER_PERMISSION_EDIT("user-group/edit") {
		@Override
		public String getNodeName() {
			return MessageBundle.getMessageBundle("breadcrumb.usergroup.edit");
		}

		@Override
		public int getNodeLevel() {
			return 2;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			// TODO tratar save
			return getRefererUrl(request);
		}
	},
	USER_PERMISSION_VIEW("user-group/view") {
		@Override
		public String getNodeName() {
			return MessageBundle.getMessageBundle("breadcrumb.usergroup");
		}

		@Override
		public int getNodeLevel() {
			return 2;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			// TODO tratar save
			return getRefererUrl(request);
		}
	},
	
	USER_CUSTOM_FIELD_LIST("user-custom-field/list") {
		@Override
		public String getNodeName() {
			return MessageBundle.getMessageBundle("breadcrumb.userCustomField.list");
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			return getDefaultUrl(request);
		}
	},
	USER_CUSTOM_FIELD_SEARCH("user-custom-field/search") {
		@Override
		public String getNodeName() {
			return MessageBundle.getMessageBundle("breadcrumb.userCustomField.search");
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			return getDefaultUrl(request);
		}
	},
	USER_CUSTOM_FIELD_NEW("user-custom-field/new") {
		@Override
		public String getNodeName() {
			return MessageBundle.getMessageBundle("breadcrumb.userCustomField.new");
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			return getDefaultUrl(request);
		}
	},
	USER_CUSTOM_FIELD_EDIT("user-custom-field/edit") {
		@Override
		public String getNodeName() {
			return MessageBundle.getMessageBundle("breadcrumb.userCustomField.edit");
		}

		@Override
		public int getNodeLevel() {
			return 2;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			// TODO tratar save
			return getRefererUrl(request);
		}
	},
	USER_CUSTOM_FIELD_VIEW("user-custom-field/view") {
		@Override
		public String getNodeName() {
			return MessageBundle.getMessageBundle("breadcrumb.userCustomField");
		}

		@Override
		public int getNodeLevel() {
			return 2;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			// TODO tratar save
			return getRefererUrl(request);
		}
	},
	
	AUTH_LOGIN("auth/login") {
		@Override
		public String getNodeName() {
			return null;
		}

		@Override
		public int getNodeLevel() {
			return 0;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			return getDefaultUrl(request);
		}
	},
	AUTH_LOGOUT("auth/logout") {
		@Override
		public String getNodeName() {
			return null;
		}

		@Override
		public int getNodeLevel() {
			return 0;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			return getDefaultUrl(request);
		}
	},
	
	AUDIT_LOG_LIST("auditlog/list") {
		@Override
		public String getNodeName() {
			return MessageBundle.getMessageBundle("breadcrumb.auditlog.list");
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			return getDefaultUrl(request);
		}
	},
	
	ERROR("error/error") {
		@Override
		public String getNodeName() {
			return null;
		}

		@Override
		public int getNodeLevel() {
			return 0;
		}

		@Override
		public String getUrl(HttpServletRequest request) {
			return getDefaultUrl(request);
		}
	},
	
	;
	
	protected String path;

	private Navigation(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
	
	protected String getDefaultUrl(HttpServletRequest request) {
        String referer = "";
		
		String protocol = "";
        if (request.getProtocol().startsWith("HTTPS")) {
        	protocol = "https://";
        } else {
        	protocol = "http://";
        }
        
        String appPath = request.getHeader("host") + request.getContextPath();
		
		if (appPath.endsWith("/")) {
			referer = protocol + appPath + this.getPath();
		} else {
			referer = protocol + appPath + "/" + this.getPath();
		}
		
		return referer;
	}
	
	protected String getRefererUrl(HttpServletRequest request) {
		String referer = "";
		
		referer = request.getHeader("referer");
		
		return referer;
	}
	
	public abstract String getNodeName();
	
	public abstract int getNodeLevel();
	
	public abstract String getUrl(HttpServletRequest request);

}
