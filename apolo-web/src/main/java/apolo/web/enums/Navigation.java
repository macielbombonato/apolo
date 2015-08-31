package apolo.web.enums;


public enum Navigation {
	
	INDEX("/index") {
		@Override
		public String getNodeName() {
			return "view.index";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
	},
	VERSION("/version") {
		@Override
		public String getNodeName() {
			return "view.version";
		}

		@Override
		public int getNodeLevel() {
			return 1;
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
	},
	
	USER_INDEX("user") {
		@Override
		public String getNodeName() {
			return "user.dashboard";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
		
		@Override
		public String getPath() {
			return this.path + "/index";
		}
	},
	USER_NEW("user/new") {
		@Override
		public String getNodeName() {
			return "user.new";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
	},
	USER_EDIT("user/edit") {
		@Override
		public String getNodeName() {
			return "user.edit";
		}

		@Override
		public int getNodeLevel() {
			return 2;
		}
	},
	USER_LIST("user/list") {
		@Override
		public String getNodeName() {
			return "user.list";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
	},
	USER_SEARCH("user/search") {
		@Override
		public String getNodeName() {
			return "user.search";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
	},
	USER_VIEW("user/view") {
		@Override
		public String getNodeName() {
			return "user";
		}

		@Override
		public int getNodeLevel() {
			return 2;
		}
	},
	USER_CHANGE_PASSWORD("user/change-password") {
		@Override
		public String getNodeName() {
			return "user.changepassword";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
	},
	
	USER_PERMISSION_LIST("user-group/list") {
		@Override
		public String getNodeName() {
			return "user.group.list";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
	},
	USER_PERMISSION_SEARCH("user-group/search") {
		@Override
		public String getNodeName() {
			return "user.group.search.title";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
	},
	USER_PERMISSION_NEW("user-group/new") {
		@Override
		public String getNodeName() {
			return "user.group.new";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
	},
	USER_PERMISSION_EDIT("user-group/edit") {
		@Override
		public String getNodeName() {
			return "user.group.edit.title";
		}

		@Override
		public int getNodeLevel() {
			return 2;
		}
	},
	USER_PERMISSION_VIEW("user-group/view") {
		@Override
		public String getNodeName() {
			return "user.group";
		}

		@Override
		public int getNodeLevel() {
			return 2;
		}
	},
	
	USER_CUSTOM_FIELD_LIST("user-custom-field/list") {
		@Override
		public String getNodeName() {
			return "user.custom.field.list";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
	},
	USER_CUSTOM_FIELD_SEARCH("user-custom-field/search") {
		@Override
		public String getNodeName() {
			return "user.custom.field.search";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
	},
	USER_CUSTOM_FIELD_NEW("user-custom-field/new") {
		@Override
		public String getNodeName() {
			return "user.custom.field.new";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
	},
	USER_CUSTOM_FIELD_EDIT("user-custom-field/edit") {
		@Override
		public String getNodeName() {
			return "user.custom.field.edit";
		}

		@Override
		public int getNodeLevel() {
			return 2;
		}
	},
	USER_CUSTOM_FIELD_VIEW("user-custom-field/view") {
		@Override
		public String getNodeName() {
			return "user.custom.field";
		}

		@Override
		public int getNodeLevel() {
			return 2;
		}
	},
	
	CONFIGURATION_LIST("configuration/list") {
		@Override
		public String getNodeName() {
			return "configuration.list";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
	},
	CONFIGURATION_NEW("configuration/new") {
		@Override
		public String getNodeName() {
			return "configuration.new";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
	},
	CONFIGURATION_EDIT("configuration/edit") {
		@Override
		public String getNodeName() {
			return "configuration.edit";
		}

		@Override
		public int getNodeLevel() {
			return 2;
		}
	},
	CONFIGURATION_VIEW("configuration/view") {
		@Override
		public String getNodeName() {
			return "configuration";
		}

		@Override
		public int getNodeLevel() {
			return 2;
		}
	},
	
	AUTH_LOGIN("login") {
		@Override
		public String getNodeName() {
			return null;
		}

		@Override
		public int getNodeLevel() {
			return 0;
		}
	},
	AUTH_LOGOUT("logout") {
		@Override
		public String getNodeName() {
			return null;
		}

		@Override
		public int getNodeLevel() {
			return 0;
		}
	},
	
	AUDIT_LOG_LIST("auditlog/list") {
		@Override
		public String getNodeName() {
			return "auditlog.list";
		}

		@Override
		public int getNodeLevel() {
			return 1;
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
	},
	
	TENANT_LIST("tenant/list") {
		@Override
		public String getNodeName() {
			return "tenant.list";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
	},
	TENANT_SEARCH("tenant/search") {
		@Override
		public String getNodeName() {
			return "tenant.search.title";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
	},
	TENANT_NEW("tenant/new") {
		@Override
		public String getNodeName() {
			return "tenant.create";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
	},
	TENANT_EDIT("tenant/edit") {
		@Override
		public String getNodeName() {
			return "tenant.edit.title";
		}

		@Override
		public int getNodeLevel() {
			return 2;
		}
	},
	TENANT_VIEW("tenant/view") {
		@Override
		public String getNodeName() {
			return "tenant";
		}

		@Override
		public int getNodeLevel() {
			return 2;
		}
	},

	DISTRIBUTION_LIST("distribution/list") {
		@Override
		public String getNodeName() {
			return "distribution.list";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
	},
	DISTRIBUTION_SEARCH("distribution/search") {
		@Override
		public String getNodeName() {
			return "distribution.search.title";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
	},
	DISTRIBUTION_NEW("distribution/new") {
		@Override
		public String getNodeName() {
			return "distribution.create";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
	},
	DISTRIBUTION_EDIT("distribution/edit") {
		@Override
		public String getNodeName() {
			return "distribution.edit";
		}

		@Override
		public int getNodeLevel() {
			return 2;
		}
	},
	DISTRIBUTION_VIEW("distribution/view") {
		@Override
		public String getNodeName() {
			return "distribution";
		}

		@Override
		public int getNodeLevel() {
			return 2;
		}
	},

	CONTACT_LIST("distribution/contact/list") {
		@Override
		public String getNodeName() {
			return "contact.list";
		}

		@Override
		public int getNodeLevel() {
			return 2;
		}
	},
	CONTACT_SEARCH("distribution/contact/search") {
		@Override
		public String getNodeName() {
			return "contact.search.title";
		}

		@Override
		public int getNodeLevel() {
			return 2;
		}
	},
	CONTACT_IMPORT("distribution/contact/import") {
		@Override
		public String getNodeName() {
			return "contact.import";
		}

		@Override
		public int getNodeLevel() {
			return 2;
		}
	},
	CONTACT_IMPORT_RESULT("distribution/contact/import-result") {
		@Override
		public String getNodeName() {
			return "contact.import.result";
		}

		@Override
		public int getNodeLevel() {
			return 3;
		}
	},
	CONTACT_UNSUBSCRIBE("distribution/contact/unsubscribe") {
		@Override
		public String getNodeName() {
			return "contact.unsubscribe";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
	},

	CAMPAIGN_LIST("campaign/list") {
		@Override
		public String getNodeName() {
			return "campaign.list";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
	},
	CAMPAIGN_SEARCH("campaign/search") {
		@Override
		public String getNodeName() {
			return "campaign.search.title";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
	},
	CAMPAIGN_NEW("campaign/new") {
		@Override
		public String getNodeName() {
			return "campaign.create";
		}

		@Override
		public int getNodeLevel() {
			return 1;
		}
	},
	CAMPAIGN_EDIT("campaign/edit") {
		@Override
		public String getNodeName() {
			return "campaign.edit";
		}

		@Override
		public int getNodeLevel() {
			return 2;
		}
	},
	CAMPAIGN_VIEW("campaign/view") {
		@Override
		public String getNodeName() {
			return "campaign";
		}

		@Override
		public int getNodeLevel() {
			return 2;
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
	
	public abstract String getNodeName();
	
	public abstract int getNodeLevel();
	
	

}
