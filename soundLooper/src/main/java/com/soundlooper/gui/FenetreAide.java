/**
 *
 */
package com.soundlooper.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

/**
 *-------------------------------------------------------
 *
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
 * @author Alexandre NEDJARI
 * @since  18 nov. 2012
 *-------------------------------------------------------
 */
public class FenetreAide extends JDialog {

	private JTextPane textPane;

	private File file;

	public FenetreAide(Window window, File file, String title) {
		super(window);
		this.file = file;
		this.setModal(false);
		this.setTitle(title);

		this.setIconImage(ImageGetter.getImage(ImageGetter.ICONE_AIDE_16));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		scrollPane.setViewportView(this.getTextPane());
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);

		this.setPreferredSize(new Dimension(800, 600));
		this.pack();
		this.setLocationRelativeTo(null);
	}

	private JTextPane getTextPane() {
		if (this.textPane == null) {
			this.textPane = new JTextPane();
			this.textPane.setEditable(false);
			this.textPane.setContentType("text/html");
			this.textPane.setText(this.getContent());
			this.textPane.setCaretPosition(0);
			this.textPane.setBackground(Color.WHITE);
		}
		return this.textPane;
	}

	/**
	 * @return file content
	 */
	private String getContent() {
		BufferedReader reader =null;
		try {
			reader = new BufferedReader(new FileReader(this.file));
			StringBuffer stringBuffer = new StringBuffer();
			String line = reader.readLine();
			while (line != null) {
				stringBuffer.append(line + "\n");
				line = reader.readLine();
			}
			return stringBuffer.toString();
		} catch (IOException e) {
			return "Fichier d'aide introuvable";
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// buffer will not be closed
				}
			}
		}
	}
}
