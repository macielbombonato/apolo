package br.apolo.common.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class ApoloUtils {
	
	private static final String MESSAGE_RESOURCES = "messages";

	public static String getMessageBundle(final String key) {
		ResourceBundle bundle = ResourceBundle.getBundle(MESSAGE_RESOURCES, Locale.getDefault(), getCurrentClassLoader(null));
		return bundle.getString(key);
	}
	
	public static String getMessageBundle(final String key, Object[] params) {
		ResourceBundle bundle = ResourceBundle.getBundle(MESSAGE_RESOURCES, Locale.getDefault(), getCurrentClassLoader(params));
		return bundle.getString(key);
	}
	
	private static ClassLoader getCurrentClassLoader(Object defaultObject){
		
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		
		if(loader == null){
			loader = defaultObject.getClass().getClassLoader();
		}	
		return loader;
	}


}
