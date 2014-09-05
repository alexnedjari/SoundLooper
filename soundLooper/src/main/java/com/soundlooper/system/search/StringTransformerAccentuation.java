/**
 *
 */
package com.soundlooper.system.search;

import java.util.HashMap;
import java.util.Set;

/**
 *-------------------------------------------------------
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
 * To change all the accetuated character of a string by
 * non accentuated character
 *
 * @author Alexandre NEDJARI
 * @since  7 mars 2011
 *-------------------------------------------------------
 */
public class StringTransformerAccentuation extends StringTransformer {

	/**
	 * The map containing all the transformations
	 * <remplacable characters, remplacement caracter>
	 */
	private static final HashMap<String, Character> CARACTS = new HashMap<String, Character>();

	static {
		//Initialise the map
		StringTransformerAccentuation.CARACTS.put("ÈËÎÍ", Character.valueOf('e'));
		StringTransformerAccentuation.CARACTS.put("…»À ", Character.valueOf('E'));
		StringTransformerAccentuation.CARACTS.put("‡·‚„‰", Character.valueOf('a'));
		StringTransformerAccentuation.CARACTS.put("¿¡¬√ƒ", Character.valueOf('A'));
		StringTransformerAccentuation.CARACTS.put("ÚÛÙıˆ", Character.valueOf('o'));
		StringTransformerAccentuation.CARACTS.put("“”‘’÷", Character.valueOf('O'));
		StringTransformerAccentuation.CARACTS.put("˘˙˚¸", Character.valueOf('u'));
		StringTransformerAccentuation.CARACTS.put("Ÿ⁄€‹", Character.valueOf('U'));
		StringTransformerAccentuation.CARACTS.put("ÏÌÓÔ", Character.valueOf('i'));
		StringTransformerAccentuation.CARACTS.put("ÃÕŒœ", Character.valueOf('I'));
		StringTransformerAccentuation.CARACTS.put("Ò", Character.valueOf('n'));
		StringTransformerAccentuation.CARACTS.put("—", Character.valueOf('N'));
		StringTransformerAccentuation.CARACTS.put("Á", Character.valueOf('c'));
		StringTransformerAccentuation.CARACTS.put("«", Character.valueOf('C'));
		StringTransformerAccentuation.CARACTS.put("˝ˇ", Character.valueOf('y'));
		StringTransformerAccentuation.CARACTS.put("›ü", Character.valueOf('Y'));
	}

	@Override
	public String processTransformation(String stringToProcess) {
		StringBuffer resultString = new StringBuffer();
		int lastIndexSavedInBuffer = 0;
		for (int i = 0; i < stringToProcess.length(); i++) {
			char caractToReplace = stringToProcess.charAt(i);
			Set<String> replacableCaractsList = StringTransformerAccentuation.CARACTS.keySet();
			for (String replacableCaract : replacableCaractsList) {
				for (int j = 0; j < replacableCaract.length(); j++) {
					char charToTry = replacableCaract.charAt(j);
					if (charToTry == caractToReplace) {
						resultString.append(stringToProcess.substring(lastIndexSavedInBuffer, i));
						resultString.append(StringTransformerAccentuation.CARACTS.get(replacableCaract));
						lastIndexSavedInBuffer = i + 1;
						break;
					}
				}
			}
		}
		resultString.append(stringToProcess.substring(lastIndexSavedInBuffer));
		return resultString.toString();
	}
}
