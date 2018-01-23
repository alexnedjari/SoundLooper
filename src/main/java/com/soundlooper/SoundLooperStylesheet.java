package com.soundlooper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.sun.javafx.css.Stylesheet;
import com.sun.javafx.css.parser.CSSParser;

public class SoundLooperStylesheet {
	private static Map<String, Stylesheet> mapStylesheet = new HashMap<>();
	private static final CSSParser parser;
	private static Logger logger;

	static {
		parser = new CSSParser();
	}

	private SoundLooperStylesheet() {
		// avoid construction
	}

	public static synchronized Stylesheet getApplicationStylesheet() {
		String stylesheetName = "application";
		if (mapStylesheet.containsKey(stylesheetName)) {
			return mapStylesheet.get(stylesheetName);
		}

		try {
			Stylesheet css = parser
					.parse(SoundLooperStylesheet.class.getResource("/style/application.css").toURI().toURL());
			mapStylesheet.put(stylesheetName, css);
			return css;
		} catch (URISyntaxException | IOException e) {
			logger.error("Error initializing application stylesheet", e);
			throw new RuntimeException("Error initializing application stylesheet", e);
		}

	}
}
