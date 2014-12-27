package com.soundlooper.gui.jtimefield;



public class JTimeFieldNumericDigit extends JTimeFieldDigit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int multiplicateur;
	
	private JTimeField jTimeField;
	
	private int valeurMax;
	

	private JTimeFieldNumericDigit digitSuperieur;
	private JTimeFieldNumericDigit digitInferieur;
	
	public JTimeField getJTimeField() {
		return jTimeField;
	}
	
	public void setjTimeField(JTimeField jTimeField) {
		this.jTimeField = jTimeField;
	}

	public JTimeFieldNumericDigit getDigitSuperieur() {
		return digitSuperieur;
	}

	public void setDigitSuperieur(JTimeFieldNumericDigit digitSuperieur) {
		this.digitSuperieur = digitSuperieur;
	}

	public int getMultiplicateur() {
		return multiplicateur;
	}

	public void setMultiplicateur(int multiplicateur) {
		this.multiplicateur = multiplicateur;
	}

	public JTimeFieldNumericDigit(int multiplicateur, JTimeFieldNumericDigit digitSuperieur, final JTimeField jTimeField, int valeurMax) {
		super();
		setUI(new JTimeFieldNumericDigitUI(this));
		this.jTimeField = jTimeField;
		setValue('0');
		this.multiplicateur = multiplicateur;
		this.digitSuperieur = digitSuperieur;
		this.valeurMax = valeurMax;
	}
	
	public int getNumericValue() {
		return Integer.valueOf(new Character(getValue()).toString());
	}
	
	public boolean incrementValue() {
		int newValue = getNumericValue() + 1;
		
		if (newValue > valeurMax) {
			if (digitSuperieur == null || !digitSuperieur.incrementValue()) {
				return false;
			}
			newValue -= valeurMax+1;
		}
		
		changeValue(String.valueOf(newValue).charAt(0));
		return true;
	}
	public boolean decrementValue() {
		int newValue = getNumericValue() - 1;
		
		if (newValue < 0) {
			if (digitSuperieur == null || !digitSuperieur.decrementValue()) {
				return false;
			}
			newValue = valeurMax;
		}
		
		changeValue(String.valueOf(newValue).charAt(0));
		return true;
	}

	public JTimeFieldNumericDigit getDigitInferieur() {
		return digitInferieur;
	}

	public void setDigitInferieur(JTimeFieldNumericDigit digitInferieur) {
		this.digitInferieur = digitInferieur;
	}
	
	public void setNumericValue(int value) {
		if (value <= valeurMax) {
			changeValue(String.valueOf(value).charAt(0));
			
		}
	}

	public void changeValue(char value) {
		super.setValue(value);
		jTimeField.refresh();
		jTimeField.onValueChanged();
	}
	
	
	
	
	
}
