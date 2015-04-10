package com.soundlooper.gui.jsoundlooperslider;

import java.util.ArrayList;
import java.util.List;

public class JSoundLooperSliderSupport {
	
	public static JSoundLooperSliderSupport instance;
	
	private List<JSoundLooperSliderListener> soundLooperListenerList = new ArrayList<JSoundLooperSliderListener>(); 
	
	public synchronized static JSoundLooperSliderSupport getInstance() {
		if (instance == null) {
			instance = new JSoundLooperSliderSupport();
		}
		return instance;
	}
	
	public void addJSoundLooperSliderListener(JSoundLooperSliderListener listener) {
		this.soundLooperListenerList.add(listener);
	}
	
	public void removeJSoundLooperSliderListener(JSoundLooperSliderListener listener) {
		this.soundLooperListenerList.remove(listener);
	}
	
	public void fireValueChanged(int newValue) {
		for (JSoundLooperSliderListener jSoundLooperSliderListener : soundLooperListenerList) {
			jSoundLooperSliderListener.onValueChange(newValue);
		}
	}
}
