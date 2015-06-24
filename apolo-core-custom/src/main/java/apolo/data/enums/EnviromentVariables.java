package apolo.data.enums;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import java.util.Map;

public enum EnviromentVariables {

	DATASOURCE_DRIVER_CLASS("APOLO_DATASOURCE_DRIVER_CLASS"),
	DATASOURCE_URL("BEATRIX_DATASOURCE_URL"),
	DATASOURCE_USERNAME("APOLO_DATASOURCE_USERNAME"),
	DATASOURCE_PASSWORD("APOLO_DATASOURCE_PASSWORD"),

	HIBERNATE_DIALECT("APOLO_HIBERNATE_DIALECT"),
	HIBERNATE_HBM2DDL("APOLO_HIBERNATE_HBM2DDL"),
	HIBERNATE_SHOW_AND_FORMAT_SQL("APOLO_HIBERNATE_SHOW_AND_FORMAT_SQL"),

	DEFAULT_TENANT("APOLO_DEFAULT_TENANT"),

	UPLOADED_FILES("APOLO_UPLOADED_FILES"),

	DEFAULT_emailFrom("APOLO_DEFAULT_emailFrom"),
	DEFAULT_emailPassword("APOLO_DEFAULT_emailPassword"),
	DEFAULT_smtpHost("APOLO_DEFAULT_smtpHost"),
	DEFAULT_smtpPort("APOLO_DEFAULT_smtpPort"),
	DEFAULT_useTLS("APOLO_DEFAULT_useTLS"),
	DEFAULT_GOOGLE_ADCLIENT("APOLO_DEFAULT_GOOGLE_ADCLIENT"),
	DEFAULT_GOOGLE_ADSLOT_ONE("APOLO_DEFAULT_GOOGLE_ADSLOT_ONE"),
	DEFAULT_GOOGLE_ADSLOT_TWO("APOLO_DEFAULT_GOOGLE_ADSLOT_TWO"),
	DEFAULT_GOOGLE_ADSLOT_THREE("APOLO_DEFAULT_GOOGLE_ADSLOT_THREE"),
	DEFAULT_GOOGLE_ANALYTICS_USER_ACCOUNT("APOLO_DEFAULT_GOOGLE_ANALYTICS_USER_ACCOUNT"),

	SEND_AUTH_EMAIL("APOLO_SEND_AUTH_EMAIL"),

	SECRET_KEY("APOLO_SECRET_KEY"),
	IV_KEY("APOLO_IV_KEY"),

	;

	private final String code;

	private static final Map<String, EnviromentVariables> valueMap;

	static {
		Builder<String, EnviromentVariables> builder = ImmutableMap.builder();
		for (EnviromentVariables type : values()) {
			builder.put(type.code, type);
		}
		valueMap = builder.build();
	}

	public static EnviromentVariables fromCode(String code) {
		return valueMap.get(code);
	}

	private EnviromentVariables(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
