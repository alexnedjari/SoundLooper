/**
 *
 */
package com.soundlooper.system.search;

import java.util.Locale;

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
 * To uppercase a String. Accentuations characters are not deleted
 *
 * @author Alexandre NEDJARI
 * @since 7 mars 2011
 *  -------------------------------------------------------
 */
public class StringTransformerNoCase extends StringTransformer {

	@Override
	public String processTransformation(String stringToProcess) {
		return stringToProcess.toUpperCase(Locale.getDefault());
	}
}
