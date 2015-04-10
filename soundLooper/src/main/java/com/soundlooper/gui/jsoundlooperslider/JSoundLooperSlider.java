package com.soundlooper.gui.jsoundlooperslider;

import java.util.List;

import javax.swing.JComponent;

public class JSoundLooperSlider extends JComponent {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JSoundLooperSliderModel model;

	
	public JSoundLooperSlider() {
		super();
		this.setUI(new JSoundLooperSliderUI(this));
	}

	public JSoundLooperSliderModel getModel() {
		return model;
	}

	public void setModel(JSoundLooperSliderModel model) {
		this.model = model;
	}

	public int getMinValue() {
		return model.getMinValue();
	}

	public void setMinValue(int minValue) {
		model.setMinValue(minValue);
	}

	public int getMaxValue() {
		return model.getMaxValue();
	}

	public void setMaxValue(int maxValue) {
		model.setMaxValue(maxValue);
	}

	public List<Integer> getListDisplayedValue() {
		return model.getListDisplayedValue();
	}
	
	public int getValue() {
		return model.getValue();
	}

	public void addJSoundLooperSliderListener(JSoundLooperSliderListener listener) {
		model.addJSoundLooperSliderListener(listener);
	}
	
	public void removeJSoundLooperSliderListener(JSoundLooperSliderListener listener) {
		model.removeJSoundLooperSliderListener(listener);
	}

	public void setValue(int value) {
		model.setValue(value);
	}
	
	public void changeValue(int value) {
		model.changeValue(value);
	}
	

	
	
	
	
	
	
}
