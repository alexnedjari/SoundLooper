package com.soundlooper.system.util;


/**
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
 * String utils methods
 *
 * @author ANEDJARI
 */
public class StringUtil {
	
	/**
	 * the instance
	 */
	private static StringUtil instance;
	
	private StringUtil() {
		//to avoid construction
	}
	
	/**
	 * get the instance
	 * @return the instance
	 */
	public static synchronized StringUtil getInstance() {
		if (instance == null) {
			instance = new StringUtil();
		}
		return instance;
	}
	
	/**
	 * get the name and the increment of a mark name
	 * @param nom the name + increment
	 * @return a tab containing the name (index 0) and the increment (index 1)
	 */
	public String[] getNomEtIncrement(String nom) {
		String increment = "";
		String prefixe = nom;
		try {
			while (prefixe.length() > 0) {
				String incrementATester = prefixe.substring(prefixe.length() - 1);
				Integer numericIncrement = Integer.valueOf(incrementATester);
				increment = numericIncrement.toString() + increment;
				prefixe = prefixe.substring(0, prefixe.length() - 1);
			}
		} catch (NumberFormatException e) {
			//trouvé la fin de l'incrément
		}
		if (increment.equals("")) {
			increment = "1";
		}
		return new String[] { prefixe, increment };
	}
}
