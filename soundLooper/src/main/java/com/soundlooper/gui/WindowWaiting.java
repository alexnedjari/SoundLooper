/**
 *
 */
package com.soundlooper.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Window;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/**--------------------------------------------------------------------------------
 * Sound Looper is an audio player that allow user to loop between two points
 * Copyright (C) 2014 Alexandre NEDJARI
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
 *
 * @author Alexandre NEDJARI
 * @since 21 juil. 2011
 *--------------------------------------------------------------------------------
 */
public class WindowWaiting extends JDialog {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	String text = null;

	public WindowWaiting(String text) {
		super();
		this.text = text;
		this.initialize();
	}

	public WindowWaiting(String text, Image icon) {
		super();
		this.text = text;
		this.setIconImage(icon);
		this.initialize();
	}

	private void initialize() {
		//this.setUndecorated(true);
		this.setSize(new Dimension(300, 100));
		this.setResizable(true);
		this.setContentPane(this.getJContentPane());
		this.setLocationRelativeTo(null);
		//this.setModal(false);
		if (this.getParent() instanceof Window) {
			List<Image> iconImages = ((Window) this.getParent()).getIconImages();
			if (iconImages.size() > 0) {
				this.setIconImage(iconImages.get(0));
			}
		}
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}

	/**
	 * @return
	 */
	private Container getJContentPane() {
		if (this.jContentPane == null) {
			this.jContentPane = new JPanel();
			this.jContentPane.setBackground(Color.WHITE);
			this.jContentPane.setLayout(new BorderLayout());
			JLabel label = new JLabel();
			label.setText(this.text);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			JLabel labelIcon = new JLabel();

			labelIcon.setIcon(new ImageIcon("wait-32.gif"));
			labelIcon.setHorizontalAlignment(SwingConstants.CENTER);

			this.jContentPane.add(label, BorderLayout.CENTER);
			this.jContentPane.add(labelIcon, BorderLayout.SOUTH);
		}
		return this.jContentPane;
	}
}
