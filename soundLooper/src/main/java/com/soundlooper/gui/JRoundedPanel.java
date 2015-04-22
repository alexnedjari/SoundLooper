package com.soundlooper.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class JRoundedPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JRoundedPanel() {
		setOpaque(false);
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D graphics2d = (Graphics2D) g;
		RenderingHints renderingHints = new RenderingHints(null);
		renderingHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		graphics2d.setRenderingHints(renderingHints);
		g.setColor(this.getBackground());
		g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 30, 30);
		super.paint(g);
	}

}
