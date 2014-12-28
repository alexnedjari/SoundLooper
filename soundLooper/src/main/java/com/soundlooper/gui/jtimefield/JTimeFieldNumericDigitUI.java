package com.soundlooper.gui.jtimefield;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class JTimeFieldNumericDigitUI extends JTimeFieldDigitUI implements MouseListener, KeyListener, FocusListener {
	
	private JTimeFieldNumericDigit digit;
	
	
	public void paint(Graphics g) {
		Font font = this.digit.getFont();
		g.setFont(font);
		
		if (digit == digit.getJTimeField().getSelectedDigit()) {
			g.setColor(new Color(36, 168, 206));
			g.fillRect(0, 0, LARGEUR_DIGIT, HAUTEUR_DIGIT);
			g.setColor(Color.WHITE);
//			g.fillPolygon(new int[]{LARGEUR_DIGIT / 2, LARGEUR_DIGIT -1, 0}, 
//					new int[]{1,5,5}, 3);
//			
//			g.fillPolygon(new int[]{LARGEUR_DIGIT / 2, LARGEUR_DIGIT -1, 1}, 
//					new int[]{HAUTEUR_DIGIT-1,HAUTEUR_DIGIT-4,HAUTEUR_DIGIT-4}, 3);
		} else {
			g.setColor(Color.BLACK);
		}
		String string = String.valueOf(digit.getValue());
		int haut = getCoordonneeHaut();
		int gauche = getCoordonneeGauche(g, font, string);
		g.drawString(string, gauche, haut);
	}

	
	protected JTimeFieldNumericDigitUI(JTimeFieldNumericDigit digit) {
		super(digit);
		this.digit = digit;
		digit.addMouseListener(this);
		digit.addKeyListener(this);
		digit.addFocusListener(this);
	}


	@Override
	public void mouseClicked(MouseEvent e) { // non géré
	}


	@Override
	public void mousePressed(MouseEvent e) {
		final JTimeField jTimeField = digit.getJTimeField();
		jTimeField.selectDigit(digit);
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {// non géré
	}


	@Override
	public void mouseEntered(MouseEvent e) {// non géré
	}


	@Override
	public void mouseExited(MouseEvent e) {// non géré
	}


	@Override
	public void keyTyped(KeyEvent e) {// non géré
	}


	@Override
	public void keyPressed(KeyEvent e) {// non géré
	}


	@Override
	public void keyReleased(KeyEvent e) {
		
		if (!e.isControlDown()) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				digit.incrementValue(true);
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				digit.decrementValue(true);
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				if (digit.getDigitSuperieur() != null) {
					digit.getJTimeField().selectDigit(digit.getDigitSuperieur());
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if (digit.getDigitInferieur() != null) {
					digit.getJTimeField().selectDigit(digit.getDigitInferieur());
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_0 || e.getKeyCode() == KeyEvent.VK_NUMPAD0 ) {
				digit.setNumericValue(0);
			}
			if (e.getKeyCode() == KeyEvent.VK_1 || e.getKeyCode() == KeyEvent.VK_NUMPAD1) {
				digit.setNumericValue(1);
			}
			if (e.getKeyCode() == KeyEvent.VK_2 || e.getKeyCode() == KeyEvent.VK_NUMPAD2) {
				digit.setNumericValue(2);
			}
			if (e.getKeyCode() == KeyEvent.VK_3 || e.getKeyCode() == KeyEvent.VK_NUMPAD3) {
				digit.setNumericValue(3);
			}
			if (e.getKeyCode() == KeyEvent.VK_4 || e.getKeyCode() == KeyEvent.VK_NUMPAD4) {
				digit.setNumericValue(4);
			}
			if (e.getKeyCode() == KeyEvent.VK_5 || e.getKeyCode() == KeyEvent.VK_NUMPAD5) {
				digit.setNumericValue(5);
			}
			if (e.getKeyCode() == KeyEvent.VK_6 || e.getKeyCode() == KeyEvent.VK_NUMPAD6) {
				digit.setNumericValue(6);
			}
			if (e.getKeyCode() == KeyEvent.VK_7 || e.getKeyCode() == KeyEvent.VK_NUMPAD7) {
				digit.setNumericValue(7);
			}
			if (e.getKeyCode() == KeyEvent.VK_8 || e.getKeyCode() == KeyEvent.VK_NUMPAD8) {
				digit.setNumericValue(8);
			}
			if (e.getKeyCode() == KeyEvent.VK_9 || e.getKeyCode() == KeyEvent.VK_NUMPAD9) {
				digit.setNumericValue(9);
			}
		}
		
		e.consume();
	}


	@Override
	public void focusGained(FocusEvent e) {//Non géré
	}


	@Override
	public void focusLost(FocusEvent e) {
		if (digit == digit.getJTimeField().getSelectedDigit()) {
			digit.getJTimeField().selectDigit(null);
		}
	}
}
