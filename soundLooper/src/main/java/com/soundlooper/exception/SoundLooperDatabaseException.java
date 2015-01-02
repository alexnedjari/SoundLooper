/**
 *
 */
package com.soundlooper.exception;

/**
 * ====================================================================
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
 * @since 29 juin 2011
 *
 *====================================================================
 */
public class SoundLooperDatabaseException extends SoundLooperRuntimeException {

	/**
	 * Serial number
	 */
	private static final long serialVersionUID = 1L;

	// codes for database exception
	/**
	 * Code for database exception : song path already exists in database
	 */
	public static final int ERROR_CODE_SONG_PATH_ALREADY_EXISTS_IN_DATABASE = 1000;
	/**
	 * Code for database exception : song ID does not exists in database
	 */
	public static final int ERROR_CODE_SONG_ID_DOES_NOT_EXISTS_IN_DATABASE = 1001;
	/**
	 * Code for database exception : No ID is generated for this song
	 */
	public static final int ERROR_CODE_NO_ID_GENERATED_FOR_NEW_SONG = 1002;

	/**
	 * Code for database exception : mark name already exists in database
	 */
	public static final int ERROR_CODE_MARK_NAME_ALREADY_EXISTS_FOR_THIS_SONG = 2000;
	/**
	 * Code for database exception : mark ID does not exists in database
	 */
	public static final int ERROR_CODE_MARK_ID_DOES_NOT_EXISTS_IN_DATABASE = 2001;
	/**
	 * Code for database exception : No id is generated for this mark
	 */
	public static final int ERROR_CODE_NO_ID_GENERATED_FOR_NEW_MARK = 2002;

	/**
	 * Constructor
	 *
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public SoundLooperDatabaseException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor
	 *
	 * @param message
	 *            the message
	 */
	public SoundLooperDatabaseException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 *
	 * @param message
	 *            the message
	 * @param errorCode
	 *            the error code
	 */
	public SoundLooperDatabaseException(String message, int errorCode) {
		super(message, errorCode);
	}

	/**
	 * Constructor
	 *
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 * @param errorCode
	 *            the error code
	 */
	public SoundLooperDatabaseException(String message, Throwable cause, int errorCode) {
		super(message, cause, errorCode);
	}
}
