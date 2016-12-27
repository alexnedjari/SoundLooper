package com.soundlooper.exception;

/**
 *
 * ====================================================================
 * AudioEngine is an audio engine based on FMOD Copyright (C) 2014 Alexandre
 * NEDJARI
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * Parent of all exception of the player
 *
 * @author Alexandre NEDJARI
 * @since 1 juil. 2011
 *
 *        ====================================================================
 */
public class PlayerNotInitializedException extends PlayerException {

	/**
	 * the serial version
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 *
	 * @param message
	 *            the error message
	 * @param cause
	 *            the error cause
	 */
	public PlayerNotInitializedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor
	 *
	 * @param message
	 *            the error message
	 */
	public PlayerNotInitializedException(String message) {
		super(message);
	}
}
