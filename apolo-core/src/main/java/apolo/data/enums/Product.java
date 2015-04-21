package apolo.data.enums;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public enum Product {
	ID_PROTECTION("ID_PROTECTION"),
	
	;
	
	private final String code;
	
	private static final Map<String, Product> valueMap;
	
	static {
		Builder<String, Product> builder = ImmutableMap.builder();
		for (Product tipo : values()) {
			builder.put(tipo.code, tipo);
		}
		valueMap = builder.build();
	}
	
	public static Product fromCode(String code) {
		return valueMap.get(code);
	}

	private Product(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
}
