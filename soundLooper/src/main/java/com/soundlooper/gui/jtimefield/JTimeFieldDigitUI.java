package com.soundlooper.gui.jtimefield;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

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
