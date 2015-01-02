package com.soundlooper.gui.jtimefield;

import java.util.HashSet;
import java.util.Set;

import com.soundlooper.gui.jplayer.JPlayerListener;

/**
 *-------------------------------------------------------
 * Mange timeField listeners
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
 * @since  02 jan. 2015
 *-------------------------------------------------------
 */
public class JTimeFieldSupport {

	
	private static JTimeFieldSupport instance;
	
	private JTimeFieldSupport() {
		//to avoid construction
	}
	
	public static synchronized JTimeFieldSupport getInstance() {
		if (instance == null) {
			instance = new JTimeFieldSupport();
		}
		return instance;
	}
	
	private Set<JTimeFieldListener> jTimeFieldListeners = new HashSet<JTimeFieldListener>();

	/**
	 * Add a listener
	 * @param l  The listener to add
	 */
	public synchronized void addJPlayerListener(JTimeFieldListener l) {
		this.jTimeFieldListeners.add(l);
	}

	/**
	 * Remove a listener
	 * @param l  The listener to remove
	 */
	public synchronized void removeJPLayerListener(JTimeFieldListener l) {
		this.jTimeFieldListeners.remove(l);
	}
	
	public void fireValueChanged(int newValeur, JTimeField jTimeField) {
		// For security, make a synchronized copy to avoid concurents access
		Set<JTimeFieldListener> listener;
		synchronized (this) {
			listener = new HashSet<JTimeFieldListener>(this.jTimeFieldListeners);
		}

		for (JTimeFieldListener jTimeFieldListener : listener) {
			jTimeFieldListener.onValueChanged(newValeur, jTimeField);
		}
	}
}
