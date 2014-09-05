/**
 *
 */
package com.soundlooper.system.preferences;

/**--------------------------------------------------------------------------------
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
 * Listener for preferences change
 *
 * @author Alexandre NEDJARI
 * @since 26 juil. 2011
 *--------------------------------------------------------------------------------
 */
public interface PreferencesListener {

	/**
	 * Even launched when a preference is changed
	 * @param preferenceKey the key
	 * @param value the new value
	 */
	void onPreferenceChange(String preferenceKey, Object value);
}
