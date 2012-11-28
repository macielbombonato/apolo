package br.apolo.common.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class ApoloUtils {
	
	private static final String MESSAGE_RESOURCES = "i18n";

	public static String getMessageBundle(final String key) {
		Locale loc = new Locale("pt", "BR");
		String bundleString = MESSAGE_RESOURCES;
		
		ResourceBundle bundle = ResourceBundle.getBundle(bundleString, loc, getCurrentClassLoader(null));

		return bundle.getString(key);
	}
	
	public static String getMessageBundle(final String key, Object[] params) {
		Locale loc = new Locale("pt_BR");
		String bundleString = MESSAGE_RESOURCES;
		
		ResourceBundle bundle = ResourceBundle.getBundle(bundleString, loc, getCurrentClassLoader(params));

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
