package apolo.common.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.LocaleResolver;

public class MessageBundle {
	
	private static final String MESSAGE_RESOURCES = "messages";
	
	@Autowired
	private LocaleResolver localeResolver;
	
    public static String getMessageBundle(final String key) {
        try {
            return ResourceBundle.getBundle(
            		MESSAGE_RESOURCES, 
            		ThreadLocalContextUtil.getLanguage().getLocale()
            	).getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        } catch (Exception e) {
        	return "problem to load locale " + '[' + key + ']';
        }
    }
    
    public static String getMessageBundle(final String key, Object... params) {
        try {
            return MessageFormat.format(
            		ResourceBundle.getBundle(
            				MESSAGE_RESOURCES, 
            				ThreadLocalContextUtil.getLanguage().getLocale()
            			).getString(key), 
            			params
            	);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        } catch (Exception e) {
        	return "problem to load locale " + '[' + key + ']';
        }
    }
}
