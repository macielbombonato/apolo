package br.apolo.data.enums;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public enum FieldType {
	TEXT("text");
	
	private final String code;
	
	private static final Map<String, FieldType> valueMap;
	
	static {
		Builder<String, FieldType> builder = ImmutableMap.builder();
		for (FieldType tipo : values()) {
			builder.put(tipo.code, tipo);
		}
		valueMap = builder.build();
	}
	
	public static FieldType fromCode(String code) {
		return valueMap.get(code);
	}

	private FieldType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
