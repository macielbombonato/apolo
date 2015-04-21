package apolo.data.enums;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public enum ConfigField {
	EMAIL_FROM("EMAIL_FROM"),
	
	EMAIL_PASS("EMAIL_PASS") {
		
		@Override
		public boolean isCryptedValue() {
			return true;
		}
	},
	
	EMAIL_SMTP_HOST("EMAIL_SMTP_HOST"),
	
	EMAIL_SMTP_PORT("EMAIL_SMTP_PORT"),
	
	EMAIL_USE_TLS("EMAIL_USE_TLS"){

		@Override
		public boolean isBooleanValue() {
			return true;
		}
	},
	
	;
	
	private final String code;
	
	private static final Map<String, ConfigField> valueMap;
	
	static {
		Builder<String, ConfigField> builder = ImmutableMap.builder();
		for (ConfigField tipo : values()) {
			builder.put(tipo.code, tipo);
		}
		valueMap = builder.build();
	}
	
	public static ConfigField fromCode(String code) {
		return valueMap.get(code);
	}

	private ConfigField(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	public boolean isBooleanValue() {
		return false;
	}
	
	public boolean isCryptedValue() {
		return false;
	}
	
}
