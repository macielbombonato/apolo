package apolo.data.enums;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public enum DatabaseTransactionType {
	CREATE("create"),
	UPDATE("update"), 
	DELETE("delete");
	
	private final String code;
	
	private static final Map<String, DatabaseTransactionType> valueMap;
	
	static {
		Builder<String, DatabaseTransactionType> builder = ImmutableMap.builder();
		for (DatabaseTransactionType tipo : values()) {
			builder.put(tipo.code, tipo);
		}
		valueMap = builder.build();
	}
	
	public static DatabaseTransactionType fromCode(String code) {
		return valueMap.get(code);
	}

	private DatabaseTransactionType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
