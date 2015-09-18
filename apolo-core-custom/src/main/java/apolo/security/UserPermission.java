package apolo.security;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import org.springframework.security.access.ConfigAttribute;

import java.util.Map;

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
	ADMIN("ADMIN") {
		@Override
		public boolean isListable() {
			return false;
		}
	},
	
	TENANT_CREATE("TENANT_CREATE"),
	TENANT_EDIT("TENANT_EDIT"),
	TENANT_REMOVE("TENANT_REMOVE"),
	TENANT_LIST("TENANT_LIST"),
	TENANT_MANAGER("TENANT_MANAGER"),
	
	USER_CREATE("USER_CREATE"), 
	USER_EDIT("USER_EDIT"),
	USER_REMOVE("USER_REMOVE"),
	USER_LIST("USER_LIST"),
	USER_MANAGER("USER_MANAGER"),
	USER_VIEW_ALL_DATA("USER_VIEW_ALL_DATA"),
	
	USER_PERMISSION_LIST("USER_PERMISSION_LIST"),
	USER_PERMISSION_VIEW("USER_PERMISSION_VIEW"),
	USER_PERMISSION_CREATE("USER_PERMISSION_CREATE"),
	USER_PERMISSION_EDIT("USER_PERMISSION_EDIT"),
	USER_PERMISSION_REMOVE("USER_PERMISSION_REMOVE"),
	
	CONFIGURATION_LIST("CONFIGURATION_LIST"),
	CONFIGURATION_VIEW("CONFIGURATION_VIEW"),
	CONFIGURATION_CREATE("CONFIGURATION_CREATE"),
	CONFIGURATION_EDIT("CONFIGURATION_EDIT"),
	CONFIGURATION_REMOVE("CONFIGURATION_REMOVE"),

	;
	
	public String getAttribute() {
		return "ROLE_" + getCode(); // the spring security needs the ROLE prefix
	}
	
	private final String code;
	
	private static final Map<String, UserPermission> valueMap;
	
	static {
		Builder<String, UserPermission> builder = ImmutableMap.builder();
		for (UserPermission type : values()) {
			builder.put(type.code, type);
		}
		valueMap = builder.build();
	}
	
	public static UserPermission fromCode(String code) {
		return valueMap.get(code);
	}

	private UserPermission(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	/**
	 * Define if a Enum can be listed or not.
	 * @return boolean
	 */
	public boolean isListable() {
		return true;
	}
}
