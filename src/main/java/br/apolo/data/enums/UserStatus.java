package br.apolo.data.enums;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public enum UserStatus {
	ACTIVE("active"),
	LOCKED("locked");
	
	private final String code;
	
	private static final Map<String, UserStatus> valueMap;
	
	static {
		Builder<String, UserStatus> builder = ImmutableMap.builder();
		for (UserStatus tipo : values()) {
			builder.put(tipo.code, tipo);
		}
		valueMap = builder.build();
	}
	
	public static UserStatus fromCode(String code) {
		return valueMap.get(code);
	}

	private UserStatus(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
