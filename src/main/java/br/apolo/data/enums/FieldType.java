package br.apolo.data.enums;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public enum FieldType {
	TEXT("text") {
		@Override
		public boolean hasOptions() {
			return false;
		}
	},
	TEXT_AREA("text_area") {
		@Override
		public boolean hasOptions() {
			return false;
		}
	},
	SELECT("select") {
		@Override
		public boolean hasOptions() {
			return true;
		}
	},
	SELECT_MULTIPLE("select_multiple") {
		@Override
		public boolean hasOptions() {
			return true;
		}
	},
	RADIO("radio") {
		@Override
		public boolean hasOptions() {
			return true;
		}
	},
	
	;
	
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
	
	public abstract boolean hasOptions();
	
	public boolean getShowOptions() {
		return hasOptions();
	}
}
