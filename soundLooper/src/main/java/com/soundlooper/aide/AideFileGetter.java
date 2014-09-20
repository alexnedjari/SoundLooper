package com.soundlooper.aide;

import java.io.File;
import java.net.URISyntaxException;

public class AideFileGetter {
	public static final String HELP_FILE_SHORTCUT_LIST ="/aide/listeRaccourciClavier.html";
	public static final String HELP_FILE_HELP ="/aide/aide.html";
	
	public static File getHelpFile(String fileName) {
		try {
			return new File(AideFileGetter.class.getResource(fileName).toURI());
		} catch (URISyntaxException e) {
			return null;
		}
	}
}
