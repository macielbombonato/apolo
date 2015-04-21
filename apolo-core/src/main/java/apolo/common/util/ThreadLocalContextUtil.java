package apolo.common.util;

import apolo.data.enums.Language;

import org.springframework.util.Assert;

public class ThreadLocalContextUtil {
	private static final ThreadLocal<String> tenantContextHolder = new ThreadLocal<String>();
	
	private static final ThreadLocal<String> languageContextHolder = new ThreadLocal<String>();
	
	public static final void setLanguage(Language language) {
		Language lang = Language.BR;
		
		if (language != null) {
			lang = language;
		}
		
		languageContextHolder.set(lang.getCode());
	}
	
	public static final Language getLanguage() {
		Language result = null;
		
		String langCode = languageContextHolder.get();
		
		if (langCode != null) {
			result = Language.fromCode(langCode);
		} else {
			result = Language.BR;
		}
		
		return result;
	}

	public static final void setTenantId(String tenantId) {
		Assert.notNull(tenantId, "customerType cannot be null");
		tenantContextHolder.set(tenantId);
	}

	public static final String getTenantId() {
		return (String) tenantContextHolder.get();
	}

	public static final void clearTenant() {
		tenantContextHolder.remove();
	}
}
