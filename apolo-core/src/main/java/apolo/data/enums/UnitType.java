package apolo.data.enums;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public enum UnitType {
	UNITS("UNITS") {
		@Override
		public int getDivider() {
			return 1;
		}
	},
	TENS("TENS") {
		@Override
		public int getDivider() {
			return 10;
		}
	},
	HUNDREDS("HUNDREDS") {
		@Override
		public int getDivider() {
			return 100;
		}
	},
	THOUSANDS("THOUSANDS") {
		@Override
		public int getDivider() {
			return 1000;
		}
	},
	
	;
	
	private final String code;
	
	private static final Map<String, UnitType> valueMap;
	
	static {
		Builder<String, UnitType> builder = ImmutableMap.builder();
		for (UnitType tipo : values()) {
			builder.put(tipo.code, tipo);
		}
		valueMap = builder.build();
	}
	
	public static UnitType fromCode(String code) {
		return valueMap.get(code);
	}

	private UnitType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	public abstract int getDivider();
	
}
