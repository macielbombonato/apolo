package br.apolo.common.util;

import java.util.ResourceBundle;

public class AppConfig {
	
	private static final String CONFIG_RESOURCES = "config";
	
	private static final String OBJECT_FILES_PATH = "objectfiles.path";
	
	private static final String GHOST_SCRIPT_EXECUTABLE = "ghostscript.executable";
	
	public static String getConfigBundle(final String key) {
		return ResourceBundle.getBundle(CONFIG_RESOURCES).getString(key);
	}
	
	public static String getObjectFilesPath() {
		return getConfigBundle(OBJECT_FILES_PATH);
	}

	public static String getGhostScriptExecutable() {
		return getConfigBundle(GHOST_SCRIPT_EXECUTABLE);
	}

}
