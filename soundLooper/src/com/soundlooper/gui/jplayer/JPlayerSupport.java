package com.soundlooper.gui.jplayer;

import java.util.HashSet;
import java.util.Set;

/**
 *
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
 * Gestion des évènements / listeners pour le composant JPlayer
 *
 * @author Alexandre NEDJARI
 * @since  28 août 2014
 *-------------------------------------------------------
 */
public class JPlayerSupport {
	private Set<JPlayerListener> jPlayerListeners = new HashSet<JPlayerListener>();

	/**
	 * Add a listener
	 * @param l  The listener to add
	 */
	public synchronized void addJPlayerListener(JPlayerListener l) {
		this.jPlayerListeners.add(l);
	}

	/**
	 * Remove a listener
	 * @param l  The listener to remove
	 */
	public synchronized void removeJPLayerListener(JPlayerListener l) {
		this.jPlayerListeners.remove(l);
	}

	/**
	 * When the player value is changed
	 * @param valeur
	 */
	public void fireNewValeur(double valeur) {
		// For security, make a synchronized copy to avoid concurents access
		Set<JPlayerListener> listener;
		synchronized (this) {
			listener = new HashSet<JPlayerListener>(this.jPlayerListeners);
		}

		for (JPlayerListener jPlayerListener : listener) {
			jPlayerListener.onNewValeur(valeur);
		}
	}
}
