package com.soundlooper.gui.jtimefield;

import java.util.HashSet;
import java.util.Set;

import com.soundlooper.gui.jplayer.JPlayerListener;

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
