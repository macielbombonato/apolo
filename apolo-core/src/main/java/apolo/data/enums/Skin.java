package apolo.data.enums;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import java.util.Map;

public enum Skin {

	SKIN_DEFAULT("smart-style-0") {
		@Override
		public String getBackgroundMenuBody() {
			return "#4E463F";
		}
	},
	SKIN_DARK_ELEGANCE("smart-style-1") {
		@Override
		public String getBackgroundMenuBody() {
			return "#3A4558";
		}
	},
	SKIN_ULTRA_LIGHT("smart-style-2") {
		@Override
		public String getBackgroundMenuBody() {
			return "#fff";
		}
	},
	SKIN_GOOGLE_SKIN("smart-style-3") {
		@Override
		public String getBackgroundMenuBody() {
			return "#f78c40";
		}
	},
	SKIN_PIXEL_SMASH("smart-style-4") {
		@Override
		public String getBackgroundMenuBody() {
			return "#bbc0cf";
		}
	},
	SKIN_GLASS("smart-style-5") {
		@Override
		public String getBackgroundMenuBody() {
			return "#17273D";
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
	
	public abstract String getBackgroundMenuBody();
}
