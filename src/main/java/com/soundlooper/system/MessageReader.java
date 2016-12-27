package com.soundlooper.system;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class MessageReader {

	private static MessageReader instance;

	private Map<Locale, ResourceBundle> cache = new HashMap<Locale, ResourceBundle>();

	private Locale locale = Locale.FRENCH;

	private MessageReader() {
		// to avoid construction
	}

	public static MessageReader getInstance() {
		if (instance == null) {
			instance = new MessageReader();
		}
		return instance;
	}

	public ResourceBundle getBundle() {
		if (cache.get(locale) == null) {
			cache.put(locale,
					ResourceBundle.getBundle("properties/messages", locale));
		}
		return cache.get(locale);
	}

	public String getMessage(String key) {
		return getBundle().getString(key);
	}
}
