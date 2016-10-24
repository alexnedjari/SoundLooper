/**
 *
 */
package com.soundlooper.system.search;

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
 *
 *
 * @author Alexandre NEDJARI
 * @since  7 mars 2011
 *-------------------------------------------------------
 */
public abstract class StringTransformer {

	//TODO complete with list
	protected static final String PONCTUATION_CHAR = "!:;.,?'";
	protected static final char[] PONCTUATION_CHAR_ARRAY = StringTransformer.PONCTUATION_CHAR.toCharArray();

	protected static final String SPACE_STRING = " ";
	protected static final char SPACE_CHAR = ' ';

	/**
	 * Process the tranformation
	 * @param stringToProcess the string to process
	 * @return the processed String
	 */
	public abstract String processTransformation(String stringToProcess);
}
