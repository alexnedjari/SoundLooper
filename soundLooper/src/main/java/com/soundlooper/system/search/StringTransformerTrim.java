/**
 *
 */
package com.soundlooper.system.search;

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
 * To delete all the spaces before and after world
 * delete double spaces too between the words
 *
 * @author Alexandre NEDJARI
 * @since 7 mars 2011
 *  -------------------------------------------------------
 */
public class StringTransformerTrim extends StringTransformer {

	@Override
	public String processTransformation(String stringToProcess) {
		StringBuffer resultString = new StringBuffer(stringToProcess.trim());

		int indexOfLastFoundSpace = resultString.indexOf(StringTransformer.SPACE_STRING);
		while (indexOfLastFoundSpace != -1) {
			while (resultString.charAt(indexOfLastFoundSpace + 1) == StringTransformer.SPACE_CHAR) {
				resultString.deleteCharAt(indexOfLastFoundSpace + 1);
			}
			indexOfLastFoundSpace = resultString.indexOf(StringTransformer.SPACE_STRING, indexOfLastFoundSpace + 1);
		}

		return resultString.toString();
	}
}
