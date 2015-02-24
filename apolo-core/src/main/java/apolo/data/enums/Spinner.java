package apolo.data.enums;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import java.util.Map;

public enum Spinner {
	COGS("spinner-cogs") {
		@Override
		public String getImage() {
			return "spinner-cogs.gif";
		}
	},
	OPEN("spinner-open") {
		@Override
		public String getImage() {
			return "spinner-open.gif";
		}
	},
	TWO_CIRCLES("spinner-two-circles") {
		@Override
		public String getImage() {
			return "spinner-two-circles.gif";
		}
	},
	ALL_CIRCLES("spinner-all-circles") {
		@Override
		public String getImage() {
			return "spinner-all-circles.gif";
		}
	},
	;

	private final String code;

	private static final Map<String, Spinner> valueMap;

	static {
		Builder<String, Spinner> builder = ImmutableMap.builder();
		for (Spinner tipo : values()) {
			builder.put(tipo.code, tipo);
		}
		valueMap = builder.build();
	}

	public static Spinner fromCode(String code) {
		return valueMap.get(code);
	}

	private Spinner(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	public abstract String getImage();
}
