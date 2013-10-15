package br.apolo.data.enums;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public enum Status {
	ADMIN("admin") {
		@Override
		public boolean isChangeable() {
			return false;
		}
	},
	ACTIVE("active") {
		@Override
		public boolean isChangeable() {
			return true;
		}
	},
	LOCKED("locked") {
		@Override
		public boolean isChangeable() {
			return true;
		}
	};
	
	private final String code;
	
	private static final Map<String, Status> valueMap;
	
	static {
		Builder<String, Status> builder = ImmutableMap.builder();
		for (Status tipo : values()) {
			builder.put(tipo.code, tipo);
		}
		valueMap = builder.build();
	}
	
	public static Status fromCode(String code) {
		return valueMap.get(code);
	}

	private Status(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	public abstract boolean isChangeable();
}
