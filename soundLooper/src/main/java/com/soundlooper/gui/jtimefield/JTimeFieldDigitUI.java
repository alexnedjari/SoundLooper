package com.soundlooper.gui.jtimefield;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/**
 *-------------------------------------------------------
 * Composent UI of JTimeField, used to display digit 
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
public class JTimeFieldDigitUI extends ComponentUI {
	
	private JTimeFieldDigit digit;
	
	public static final int LARGEUR_DIGIT = 7;
	public static final int HAUTEUR_DIGIT = 20;
	public static final int HAUTEUR_CARACTERE = 9;
	
	@Override
	public void update(Graphics graphics_Arg, JComponent comp) {
		this.paint(graphics_Arg);
	}
	
	public void paint(Graphics g) {
		Font font = this.digit.getFont();
		g.setFont(font);
		
		g.setColor(Color.BLACK);
		String string = String.valueOf(digit.getValue());
		int haut = getCoordonneeHaut();
		int gauche = getCoordonneeGauche(g, font, string);
		g.drawString(string, gauche, haut);
	}

	protected int getCoordonneeGauche(Graphics g, Font font, String string) {
		return (LARGEUR_DIGIT - g.getFontMetrics(font).stringWidth(string)) / 2;
	}

	protected int getCoordonneeHaut() {
		int haut = HAUTEUR_DIGIT - ((HAUTEUR_DIGIT-HAUTEUR_CARACTERE)/2);
		return haut;
	}

	public JTimeFieldDigitUI(JTimeFieldDigit digit) {
		super();
		this.digit = digit;
	}
}
