package apolo.common.config.enums;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import java.util.Locale;
import java.util.Map;

public enum Language {

	BR("pt_BR") {
		@Override
		public String getIcon() {
			return "/resources/app/img/flags/br.png";
		}

		@Override
		public Locale getLocale() {
			return new Locale("pt", "BR");
		}
	},
	
	US("en_US") {
		@Override
		public String getIcon() {
			return "/resources/app/img/flags/us.png";
		}
		
		@Override
		public Locale getLocale() {
			return new Locale("en", "US");
		}
	},
	
	ES("es_ES") {
		@Override
		public String getIcon() {
			return "/resources/app/img/flags/es.png";
		}
		
		@Override
		public Locale getLocale() {
			return new Locale("es", "ES");
		}
	},
	
	;
	
	private final String code;
	
	private static final Map<String, Language> valueMap;
	
	static {
		Builder<String, Language> builder = ImmutableMap.builder();
		for (Language type : values()) {
			builder.put(type.code, type);
		}
		valueMap = builder.build();
	}
	
	public static Language fromCode(String code) {
		return valueMap.get(code);
	}

	private Language(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	public abstract String getIcon();
	
	public abstract Locale getLocale();
	
}
