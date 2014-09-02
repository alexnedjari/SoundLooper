/**
 *
 */
package com.soundlooper.exception;

/**
 * ====================================================================
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
 * @author Alexandre NEDJARI
 * @since 29 juin 2011
 *
 *====================================================================
 */
public class SoundLooperRuntimeException extends RuntimeException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The error code
	 */
	int errorCode;

	/**
	 * Constructor
	 *
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause Exception
	 * @param errorCode
	 *            the error code
	 */
	public SoundLooperRuntimeException(String message, Throwable cause, int errorCode) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	/**
	 * Constructor
	 *
	 * @param message
	 *            the message
	 * @param errorCode
	 *            the error code
	 */
	public SoundLooperRuntimeException(String message, int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * Get the error code of the exception
	 *
	 * @return the error code
	 */
	public int getErrorCode() {
		return this.errorCode;
	}

	/**
	 * Constructor
	 *
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public SoundLooperRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor
	 *
	 * @param message
	 *            the message
	 */
	public SoundLooperRuntimeException(String message) {
		super(message);
	}

}
