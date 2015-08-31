package apolo.business.enums;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public enum EmailStatusType {

    SENT("sent"),
    REJECTED("rejected"),
    ERROR("error"),
    NULL("null"),
    ;

    private final String code;

    private static final Map<String, EmailStatusType> valueMap;

    static {
        ImmutableMap.Builder<String, EmailStatusType> builder = ImmutableMap.builder();
        for (EmailStatusType type : values()) {
            builder.put(type.code, type);
        }
        valueMap = builder.build();
    }

    public static EmailStatusType fromCode(String code) {
        return valueMap.get(code);
    }

    private EmailStatusType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
