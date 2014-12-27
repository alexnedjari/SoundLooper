package com.soundlooper.gui.jtimefield;

import java.awt.Dimension;

import javax.swing.JComponent;

public class JTimeFieldDigit extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The digit value
	 */
	private char value;

	public JTimeFieldDigit() {
		super();
		setPreferredSize(new Dimension(JTimeFieldDigitUI.LARGEUR_DIGIT, JTimeFieldDigitUI.HAUTEUR_DIGIT));
		setSize(new Dimension(JTimeFieldDigitUI.LARGEUR_DIGIT, JTimeFieldDigitUI.HAUTEUR_DIGIT));
		setUI(new JTimeFieldDigitUI(this));
	}

	public char getValue() {
		return value;
	}

	public void setValue(char value) {
		this.value = value;
	}
}
