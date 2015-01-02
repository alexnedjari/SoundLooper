/**
 *
 */
package com.soundlooper.gui.jplayer;

import java.text.DecimalFormat;
import java.text.FieldPosition;

/**
 *-------------------------------------------------------
 *
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
 * @author Alexandre NEDJARI
 * @since  22 août 2014
 *-------------------------------------------------------
 */
public class TimeDecimalFormat extends DecimalFormat {

	/**
	 * Serial version for the class
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public StringBuffer format(double number, StringBuffer result, FieldPosition fieldPosition) {
		int milliSeconds = new Double(number).intValue();
		return this.formatFromInt(result, milliSeconds);
	}

	/**
	 * Format time from millisecond int
	 * @param result the result
	 * @param milliSeconds the millisecond time
	 * @return the filled result
	 */
	private StringBuffer formatFromInt(StringBuffer result, int milliSeconds) {
		int seconds = milliSeconds / 1000;
		int minuts = seconds / 60;
		int remainingSeconds = seconds % 60;
		result.append(minuts);
		result.append(":");
		if (remainingSeconds < 10) {
			result.append("0");
		}
		result.append(remainingSeconds);

		return result;
	}

	@Override
	public StringBuffer format(long number, StringBuffer result, FieldPosition fieldPosition) {
		int seconds = new Long(number).intValue();
		return this.formatFromInt(result, seconds);
	}

}
