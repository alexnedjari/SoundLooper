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
 * Converter for the time
 *
 * @author ANEDJARI
 */
public final class TimeConverter {

	/**
	 * Private constructor
	 */
	private TimeConverter() {
		// to avoid construction
	}

	/**
	 * Millisecond per hours
	 */
	private static final int MILLISECOND_PER_HOUR = 3600000;

	/**
	 * Millisecond per minutes
	 */
	private static final int MILLISECOND_PER_MINUTS = 60000;

	/**
	 * Milliseconds per seconds
	 */
	private static final int MILLISECOND_PER_SECONDS = 1000;

	/**
	 * convert milliseconds to nanoseconds
	 *
	 * @param milliSeconds
	 *            the milliseconds time
	 * @return the nanosecond time
	 */
	public static long toNanoSeconds(long milliSeconds) {
		return milliSeconds * (1000 * 1000);
	}

	/**
	 * convert nanoseconds to milliseconds
	 *
	 * @param nanoSeconds
	 *            the nanoSeconds time
	 * @return the millisecond time
	 */
	public static long toMilliSeconds(long nanoSeconds) {
		return nanoSeconds / (1000 * 1000);
	}

	/**
	 * get the time information a string
	 *
	 * @param millisecondTime
	 *            the time to convert
	 * @return the formatted string
	 */
	public static String getTimeInformationString(long millisecondTime) {
		long remainingMillisecondTime = millisecondTime;
		long hours = remainingMillisecondTime / TimeConverter.MILLISECOND_PER_HOUR;
		remainingMillisecondTime -= hours * TimeConverter.MILLISECOND_PER_HOUR;
		long minuts = remainingMillisecondTime / TimeConverter.MILLISECOND_PER_MINUTS;
		remainingMillisecondTime -= minuts * TimeConverter.MILLISECOND_PER_MINUTS;
		long seconds = remainingMillisecondTime / TimeConverter.MILLISECOND_PER_SECONDS;
		remainingMillisecondTime -= seconds * TimeConverter.MILLISECOND_PER_SECONDS;

		return TimeConverter.complete(hours, 2) + ":" + TimeConverter.complete(minuts, 2) + ":" + TimeConverter.complete(seconds, 2) + "."
				+ TimeConverter.complete(remainingMillisecondTime, 3);
	}

	/**
	 * get the time information a string
	 *
	 * @param millisecondTime
	 *            the time to convert
	 * @return the formatted string
	 */
	public static String getTimeInformationStringMMSS(long millisecondTime) {
		long remainingMillisecondTime = millisecondTime;
		long minuts = remainingMillisecondTime / TimeConverter.MILLISECOND_PER_MINUTS;
		remainingMillisecondTime -= minuts * TimeConverter.MILLISECOND_PER_MINUTS;
		long seconds = remainingMillisecondTime / TimeConverter.MILLISECOND_PER_SECONDS;
		remainingMillisecondTime -= seconds * TimeConverter.MILLISECOND_PER_SECONDS;

		return TimeConverter.complete(minuts, 2) + ":" + TimeConverter.complete(seconds, 2);
	}

	/**
	 * Complete a numeric value, if initial value is too long, it s not
	 * truncated
	 *
	 * @param valueToComplete
	 *            the value to complete
	 * @param numberChar
	 *            the number of character to have
	 * @return the completed string
	 */
	private static String complete(long valueToComplete, int numberChar) {
		return TimeConverter.complete(String.valueOf(valueToComplete), numberChar);
	}

	/**
	 * Complete a numeric value (with '0'), if initial value is too long, it s
	 * not truncated
	 *
	 * @param stringToComplete
	 *            the value to complete
	 * @param numberChar
	 *            the number of character to have
	 * @return the completed string
	 */
	private static String complete(String stringToComplete, int numberChar) {
		String completeString = stringToComplete;
		while (completeString.length() < numberChar) {
			completeString = "0" + completeString;
		}
		return completeString;
	}
}
