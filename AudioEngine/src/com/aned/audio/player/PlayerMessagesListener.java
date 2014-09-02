package com.aned.audio.player;

/**
 *--------------------------------------------------------------------------------
 *AudioEngine is an audio engine based on FMOD
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
 * Listeners for player messages
 *
 * @author Alexandre NEDJARI
 * @since 20 juil. 2011
 *--------------------------------------------------------------------------------
 */
public interface PlayerMessagesListener {

	/**
	 * Actions to do on player change state
	 * @param newState the newState
	 */
	void onChangeState(int newState);

	/**
	 * Actions to do on player change state
	 * @param message the message
	 */
	void onInfo(String message);

	/**
	 * Actions to do on player change state
	 * @param errorMessage the error message
	 */
	void onFatalError(String errorMessage);

	/**
	 * Actions to do on player change state
	 * @param errorMessage the error message
	 */
	void onError(String errorMessage);

	/**
	 * Actions to do on player change state
	 * @param message the message
	 */
	void onWarning(String message);
}
