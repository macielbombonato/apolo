package apolo.data.enums;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public enum Skin {
	SKIN_0("") {
		@Override
		public String getColor() {
			return "#323447";
		}
	},
	SKIN_1("skin-1") {
		@Override
		public String getColor() {
			return "#efefef";
		}
	},
	SKIN_2("skin-2") {
		@Override
		public String getColor() {
			return "#a93922";
		}
	},
	SKIN_3("skin-3") {
		@Override
		public String getColor() {
			return "#3e6b96";
		}
	},
	SKIN_4("skin-4") {
		@Override
		public String getColor() {
			return "#635247";
		}
	},
	SKIN_5("skin-5") {
		@Override
		public String getColor() {
			return "#3a3a3a";
		}
	},
	SKIN_6("skin-6") {
		@Override
		public String getColor() {
			return "#495B6C";
		}
	},
	;
	
	private final String code;
	
	private static final Map<String, Skin> valueMap;
	
	static {
		Builder<String, Skin> builder = ImmutableMap.builder();
		for (Skin tipo : values()) {
			builder.put(tipo.code, tipo);
		}
		valueMap = builder.build();
	}
	
	public static Skin fromCode(String code) {
		return valueMap.get(code);
	}

	private Skin(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	public abstract String getColor();
}
