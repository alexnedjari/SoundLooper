package com.soundlooper.gui.jsoundlooperslider;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

public class JSoundLooperSliderModel {
	/**
	 * Contains all the values displayed
	 */
	private List<Integer> listDisplayedValue = new ArrayList<Integer>();
	
	/**
	 * the min value
	 */
	private int minValue = 0;
	
	/**
	 * The max value
	 */
	private int maxValue = 100;
	
	/**
	 * The value
	 */
	private int value = 0;
	
	private JSoundLooperSliderSupport jSoundLooperSliderSupport = JSoundLooperSliderSupport.getInstance();
	
	private JComponent component;
	
	
	
	
	public JSoundLooperSliderModel(JComponent component) {
		super();
		this.component = component;
	}



	/**
	 * Add a new displayed values
	 * @param newValue the new value to display
	 */
	public void addDisplayedValue(int newValue) {
		listDisplayedValue.add(newValue);
	}



	public int getMinValue() {
		return minValue;
	}



	public void setMinValue(int minValue) {
		this.minValue = minValue;
		if (value < minValue) {
			value = minValue;
		}
	}



	public int getMaxValue() {
		return maxValue;
	}



	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
		if (value > maxValue) {
			value = maxValue;
		}
	}



	public List<Integer> getListDisplayedValue() {
		return listDisplayedValue;
	}



	public int getValue() {
		return value;
	}

	public void addJSoundLooperSliderListener(JSoundLooperSliderListener listener) {
		this.jSoundLooperSliderSupport.addJSoundLooperSliderListener(listener);
	}
	
	public void removeJSoundLooperSliderListener(JSoundLooperSliderListener listener) {
		jSoundLooperSliderSupport.removeJSoundLooperSliderListener(listener);
	}

	public void setValue(int value) {
		this.value = value;
		this.component.repaint();
	}
	
	public void changeValue(int value) {
		this.value = value;
		jSoundLooperSliderSupport.fireValueChanged(value);
	}

}
