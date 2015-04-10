package com.soundlooper.gui.jsoundlooperslider;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class JSoundLooperTextSlider extends JPanel implements JSoundLooperSliderListener{

	private JSoundLooperSlider jSoundLooperSlider;
	
	private JTextField textFieldValue;
	
	private JLabel labelValue;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JSoundLooperSliderModel model;
	
		
	public JSoundLooperTextSlider() {
		model = new JSoundLooperSliderModel(this);
		
		this.setLayout(new BorderLayout());
		this.add(getJSoundLooperSlider(), BorderLayout.CENTER);
		this.add(getLabelValue(), BorderLayout.EAST);
		JSoundLooperSliderSupport.getInstance().addJSoundLooperSliderListener(this);
	}
	private JLabel getLabelValue() {
		if (labelValue == null) {
			labelValue = new JLabel(String.valueOf(model.getValue()));
			labelValue.setPreferredSize(new Dimension(25,0));
			labelValue.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return labelValue;
	}
	
	private JLabel getTextFieldValue() {
		if (textFieldValue == null) {
			textFieldValue = new JTextField(String.valueOf(model.getValue()));
			textFieldValue.setPreferredSize(new Dimension(100,0));
		}
		return labelValue;
	}

	private JSoundLooperSlider getJSoundLooperSlider() {
		if (jSoundLooperSlider == null) {
			jSoundLooperSlider = new JSoundLooperSlider();
			jSoundLooperSlider.setModel(model);
		}
		return jSoundLooperSlider;
	}
	
	public void addDisplayedValue(int newValue) {
		model.addDisplayedValue(newValue);
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

	@Override
	public void onValueChange(int newValue) {
		System.out.println("changement");
		this.getLabelValue().setText(String.valueOf(newValue));
		this.getTextFieldValue().setText(String.valueOf(newValue));
		
	}
}
