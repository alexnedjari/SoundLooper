package com.soundlooper.gui.jsoundlooperslider;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.soundlooper.gui.UpperCaseDocument;

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
		this.setOpaque(false);
		model = new JSoundLooperSliderModel(this);
		model.addJSoundLooperSliderListener(this);
		
		this.setLayout(new BorderLayout());
		this.add(getJSoundLooperSlider(), BorderLayout.CENTER);
		this.add(getLabelValue(), BorderLayout.EAST);
		
	}
	private JLabel getLabelValue() {
		if (labelValue == null) {
			labelValue = new JLabel(String.valueOf(model.getValue()));
			labelValue.setOpaque(false);
			labelValue.setPreferredSize(new Dimension(25,0));
			labelValue.setHorizontalAlignment(SwingConstants.RIGHT);
			labelValue.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					setEditionMode();
				}
			});
		}
		return labelValue;
	}
	
	private void setEditionMode() {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				JSoundLooperTextSlider.this.remove(getLabelValue());
				getTextFieldValue().setText(String.valueOf(model.getValue()));
				JSoundLooperTextSlider.this.add(getTextFieldValue(), BorderLayout.EAST);
				JSoundLooperTextSlider.this.updateUI();
				getTextFieldValue().requestFocusInWindow();
				getTextFieldValue().selectAll();
			}
		});
	}
	
private void setConsultationMode() {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				JSoundLooperTextSlider.this.remove(getTextFieldValue());
				getLabelValue().setText(String.valueOf(model.getValue()));
				JSoundLooperTextSlider.this.add(getLabelValue(), BorderLayout.EAST);
				JSoundLooperTextSlider.this.updateUI();
			}
		});
	}

protected Integer getValueBetweenMinAndMax() {
	String newTextFieldValue = getTextFieldValue().getText();
	if (newTextFieldValue.equals("")) {
		newTextFieldValue = getLabelValue().getText();
	}
	Integer newValue = Integer.valueOf(newTextFieldValue);
	if (newValue < model.getMinValue()) {
		newValue = model.getMinValue();
	}
	if (newValue > model.getMaxValue()) {
		newValue = model.getMaxValue();
	}
	return newValue;
}
	
	private JTextField getTextFieldValue() {
		if (textFieldValue == null) {
			textFieldValue = new JTextField(String.valueOf(model.getValue()));
			textFieldValue.setPreferredSize(new Dimension(25,0));
			textFieldValue.setHorizontalAlignment(SwingConstants.RIGHT);
			UpperCaseDocument doc = new UpperCaseDocument();
			doc.setNumericOnly(true);
			doc.setMaxLength(3);
			textFieldValue.setDocument(doc);
			textFieldValue.addFocusListener(new FocusAdapter() {
				
				@Override
				public void focusLost(FocusEvent e) {
					Integer newValue = getValueBetweenMinAndMax();
					model.changeValue(newValue);
					setConsultationMode();
					
				}

				
			});
			textFieldValue.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						model.changeValue(getValueBetweenMinAndMax());
						setConsultationMode();
					}
					
					if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						getTextFieldValue().setText(String.valueOf(model.getValue()));
						setConsultationMode();
					}
				}
			});
		}
		return textFieldValue;
	}

	private JSoundLooperSlider getJSoundLooperSlider() {
		if (jSoundLooperSlider == null) {
			jSoundLooperSlider = new JSoundLooperSlider();
			jSoundLooperSlider.setModel(model);
			jSoundLooperSlider.setOpaque(false);
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
		this.getLabelValue().setText(String.valueOf(value));
		this.getTextFieldValue().setText(String.valueOf(value));
	}
	
	public boolean isDisplayEditableValue() {
		return model.isDisplayEditableValue();
	}

	public void setDisplayEditableValue(final boolean displayEditableValue) {
		model.setDisplayEditableValue(displayEditableValue);
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				if (displayEditableValue) {
					JSoundLooperTextSlider.this.add(getLabelValue(), BorderLayout.EAST);
				} else {
					JSoundLooperTextSlider.this.remove(getLabelValue());
					JSoundLooperTextSlider.this.remove(getTextFieldValue());
				}
				updateUI();
			}
		});
	}

	@Override
	public void onValueChange(int newValue) {
		this.getLabelValue().setText(String.valueOf(newValue));
		this.getTextFieldValue().setText(String.valueOf(newValue));
	}
}
