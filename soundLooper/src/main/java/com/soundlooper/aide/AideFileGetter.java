package com.soundlooper.aide;

import java.io.InputStream;

public class AideFileGetter {
	public static final String HELP_FILE_SHORTCUT_LIST ="/aide/listeRaccourciClavier.html";
	public static final String HELP_FILE_HELP ="/aide/aide.html";
	
	public static InputStream getHelpFile(String fileName) {
		InputStream stream = AideFileGetter.class.getResourceAsStream(fileName);
		return stream;
	}
}
