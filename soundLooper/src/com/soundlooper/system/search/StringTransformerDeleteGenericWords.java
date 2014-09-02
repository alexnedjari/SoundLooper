/**
 *
 */
package com.soundlooper.system.search;

import java.util.ArrayList;

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
 * To delete all the words like Et, And, Le, La, Les, the
 *
 * @author Alexandre NEDJARI
 * @since 7 mars 2011 -------------------------------------------------------
 */
public class StringTransformerDeleteGenericWords extends StringTransformer {

	/**
	 * The list containing the words to delete
	 */
	private static final ArrayList<String> WORDS_TO_DELETE = new ArrayList<String>();

	static {
		// Initialise the map
		StringTransformerDeleteGenericWords.WORDS_TO_DELETE.add("ET");
		StringTransformerDeleteGenericWords.WORDS_TO_DELETE.add("AND");
		StringTransformerDeleteGenericWords.WORDS_TO_DELETE.add("LE");
		StringTransformerDeleteGenericWords.WORDS_TO_DELETE.add("LA");
		StringTransformerDeleteGenericWords.WORDS_TO_DELETE.add("LES");
		StringTransformerDeleteGenericWords.WORDS_TO_DELETE.add("THE");
		StringTransformerDeleteGenericWords.WORDS_TO_DELETE.add("LOS");
	}

	@Override
	public String processTransformation(String stringToProcess) {
		String uppercasedStringToProcess = stringToProcess.toUpperCase();
		String resultString = stringToProcess;
		for (String wordToDelete : StringTransformerDeleteGenericWords.WORDS_TO_DELETE) {
			int searchIndex = 0;
			int indexFound = uppercasedStringToProcess.indexOf(wordToDelete, searchIndex);
			while (indexFound != -1) {
				int indexAfter = indexFound + wordToDelete.length();
				if (indexFound == 0) {
					// word is at the begin, check that there is a space after
					if (uppercasedStringToProcess.charAt(indexAfter) == StringTransformer.SPACE_CHAR
							|| StringTransformer.PONCTUATION_CHAR.indexOf(uppercasedStringToProcess.charAt(indexAfter)) != -1) {
						uppercasedStringToProcess = uppercasedStringToProcess.substring(wordToDelete.length() + 1);
						resultString = resultString.substring(wordToDelete.length() + 1);
					}
					// search must restart from the first character, so no
					// update of indexFound
				} else {
					// search if the previous and next characters are spaces
					if (uppercasedStringToProcess.charAt(indexFound - 1) == StringTransformer.SPACE_CHAR
							|| StringTransformer.PONCTUATION_CHAR.indexOf(uppercasedStringToProcess.charAt(indexFound - 1)) != -1) {
						if (indexAfter >= uppercasedStringToProcess.length()) {
							// it's at the end so cut the end
							uppercasedStringToProcess = uppercasedStringToProcess.substring(0, indexFound - 1);
							resultString = resultString.substring(0, indexFound - 1);
							searchIndex = indexAfter;
							// exit of the while
							break;
						} else if (uppercasedStringToProcess.charAt(indexAfter) == StringTransformer.SPACE_CHAR
								|| StringTransformer.PONCTUATION_CHAR.indexOf(uppercasedStringToProcess.charAt(indexAfter)) != -1) {
							uppercasedStringToProcess = uppercasedStringToProcess.substring(0, indexFound)
									+ uppercasedStringToProcess.substring(indexFound + wordToDelete.length());
							resultString = resultString.substring(0, indexFound) + resultString.substring(indexFound + wordToDelete.length());
							searchIndex = indexFound;
						} else {
							//it's not the entire word, so searching after
							searchIndex = indexAfter;
						}
					} else {
						//it's not the entire word, so searching after
						searchIndex = indexAfter;
					}
				}
				indexFound = uppercasedStringToProcess.indexOf(wordToDelete, searchIndex);
			}
		}

		return resultString.toString();
	}
}
