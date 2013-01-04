package br.apolo.common.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageBundle {
	
	private static final String MESSAGE_RESOURCES = "messages";
	
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(MESSAGE_RESOURCES, Locale.getDefault());
	
    public static String getMessageBundle(final String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
    
    public static String getMessageBundle(final String key, Object... params  ) {
        try {
            return MessageFormat.format(RESOURCE_BUNDLE.getString(key), params);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
