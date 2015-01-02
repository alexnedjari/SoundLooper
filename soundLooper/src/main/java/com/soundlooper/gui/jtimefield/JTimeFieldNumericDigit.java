package com.soundlooper.gui.jtimefield;


/**
 *-------------------------------------------------------
 * Composent of JTimeField, used to display numeric digits 
 * 
 * Copyright (C) 2015 Alexandre NEDJARI
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
	
	public boolean incrementValue(boolean notify) {
		int newValue = getNumericValue() + 1;
		
		if (newValue > valeurMax) {
			if (digitSuperieur == null || !digitSuperieur.incrementValue(false)) {
				return false;
			}
			newValue -= valeurMax+1;
		}
		
		changeValue(String.valueOf(newValue).charAt(0), notify);
		return true;
	}
	public boolean decrementValue(boolean notify) {
		int newValue = getNumericValue() - 1;
		
		if (newValue < 0) {
			if (digitSuperieur == null || !digitSuperieur.decrementValue(false)) {
				return false;
			}
			newValue = valeurMax;
		}
		
		changeValue(String.valueOf(newValue).charAt(0), notify);
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
			changeValue(String.valueOf(value).charAt(0), true);
			
		}
	}

	public void changeValue(char value, boolean notify) {
		super.setValue(value);
		jTimeField.refresh();
		if (notify) {
			jTimeField.onValueChanged();
		}
	}
	
	
	
	
	
}
