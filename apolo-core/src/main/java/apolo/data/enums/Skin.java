package apolo.data.enums;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import java.util.Map;

public enum Skin {
	SKIN_BLACK("skin-black") {
		@Override
		public String getBackgroundTitleBar() {
			return "bg-black";
		}

		@Override
		public String getBackgroundMenuBody() {
			return "#222d32";
		}
	},
	SKIN_BLUE("skin-blue") {
		@Override
		public String getBackgroundTitleBar() {
			return "bg-blue";
		}

		@Override
		public String getBackgroundMenuBody() {
			return "#222d32";
		}
	},
	SKIN_PURPLE("skin-purple") {
		@Override
		public String getBackgroundTitleBar() {
			return "bg-purple";
		}

		@Override
		public String getBackgroundMenuBody() {
			return "#222d32";
		}
	},
	SKIN_YELLOW("skin-yellow") {
		@Override
		public String getBackgroundTitleBar() {
			return "bg-yellow";
		}

		@Override
		public String getBackgroundMenuBody() {
			return "#222d32";
		}
	},
	SKIN_RED("skin-red") {
		@Override
		public String getBackgroundTitleBar() {
			return "bg-red";
		}

		@Override
		public String getBackgroundMenuBody() {
			return "#222d32";
		}
	},
	SKIN_GREEN("skin-green") {
		@Override
		public String getBackgroundTitleBar() {
			return "bg-green";
		}

		@Override
		public String getBackgroundMenuBody() {
			return "#222d32";
		}
	},

	SKIN_BLACK_LIGHT("skin-black-light") {
		@Override
		public String getBackgroundTitleBar() {
			return "bg-light-black";
		}

		@Override
		public String getBackgroundMenuBody() {
			return "#f9fafc";
		}
	},
	SKIN_BLUE_LIGHT("skin-blue-light") {
		@Override
		public String getBackgroundTitleBar() {
			return "bg-blue";
		}

		@Override
		public String getBackgroundMenuBody() {
			return "#f9fafc";
		}
	},
	SKIN_PURPLE_LIGHT("skin-purple-light") {
		@Override
		public String getBackgroundTitleBar() {
			return "bg-purple";
		}

		@Override
		public String getBackgroundMenuBody() {
			return "#f9fafc";
		}
	},
	SKIN_YELLOW_LIGHT("skin-yellow-light") {
		@Override
		public String getBackgroundTitleBar() {
			return "bg-yellow";
		}

		@Override
		public String getBackgroundMenuBody() {
			return "#f9fafc";
		}
	},
	SKIN_RED_LIGHT("skin-red-light") {
		@Override
		public String getBackgroundTitleBar() {
			return "bg-red";
		}

		@Override
		public String getBackgroundMenuBody() {
			return "#f9fafc";
		}
	},
	SKIN_GREEN_LIGHT("skin-green-light") {
		@Override
		public String getBackgroundTitleBar() {
			return "bg-green";
		}

		@Override
		public String getBackgroundMenuBody() {
			return "#f9fafc";
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
	
	public abstract String getBackgroundTitleBar();
	public abstract String getBackgroundMenuBody();
}
