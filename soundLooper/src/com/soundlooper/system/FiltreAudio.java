/**
 *
 */
package com.soundlooper.system;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * -------------------------------------------------------
 * Sound Looper is an audio player that allow user to loop between two points
 * Copyright (C) 2014 Alexandre NEDJARI
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Create audio filter for file selection
 *
 * @author Alexandre NEDJARI
 * @since 6 avr. 2011 -------------------------------------------------------
 */
public class FiltreAudio extends FileFilter {

	/**
	 * Suffix of the filtered types
	 */
	public static String[] SUFFIX_LIST = new String[] { "mp3", "wav", "ogg", "wma", "mpc" };

	/**
	 * Description
	 */
	private String descriptions;

	/**
	 * Constructor
	 */
	public FiltreAudio() {
		FiltreAudio.SUFFIX_LIST = new String[] { "mp3", "wav", "ogg", "wma", "mpc" };
		this.descriptions = "Fichiers audio pris en compte";
	}

	/**
	 * Check if a suffix is include in the list
	 *
	 * @param suffix
	 *            the suffix to check
	 * @return true if the suffix is include
	 */
	private boolean isInclude(String suffix) {
		for (int i = 0; i < FiltreAudio.SUFFIX_LIST.length; ++i) {
			if (suffix.equals(FiltreAudio.SUFFIX_LIST[i])) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		String suffixe = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1) {
			suffixe = s.substring(i + 1).toLowerCase();
		}
		return suffixe != null && this.isInclude(suffixe);
	}

	@Override
	public String getDescription() {
		return this.descriptions;
	}
}
