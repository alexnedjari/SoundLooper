package com.soundlooper.gui.fenapropos;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 *-------------------------------------------------------
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
 * Fenetre A propos générique
 *
 * @author Alexandre NEDJARI
 * @since  11 juin 2012
 *-------------------------------------------------------
 */
public class FenAPropos extends JFrame {

	/**
	 * numéro de série pour cette classe
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Content pane
	 */
	private JPanel jContentPane = null;

	/**
	 * Panel contenant les informations diverses
	 */
	private JPanel jPanelAffichages = null;

	/**
	 * Label "Auteur"
	 */
	private JLabel jLabelAuteurLabel = null;

	/**
	 * LAbel contenant la valeur de l'auteur
	 */
	private JLabel jLabelAuteur = null;

	/**
	 * Panel contenant le nom et la version de l'application
	 */
	private JPanel jPanelNomAppli = null;

	/**
	 * Label contenant le nom + version de l'application
	 */
	private JLabel jLabelNomAppli = null;

	/**
	 * Objet contenant toutes les informations sur l'application
	 */
	private InformationLogiciel informationLogiciel;

	/**
	 * Fenêtre à partir de laquelle la fenêtre "à propos" est lancée
	 */
	private JFrame fenetreParent;

	/**
	 * Image à afficher
	 */
	private Image image;

	/**
	 * Label contenant l'image
	 */
	private JLabel labelImage;

	/**
	 * Zone de texte contenant les mentions obligatoires des librairies utilisées
	 */
	private JTextArea textAreaLicence;

	/**
	 * @param informationLogiciel informations sur le logiciel
	 *@param fenetreParent la fenêtre appelante
	 *@param image l'image à afficher
	 * This is the default constructor
	 */
	public FenAPropos(InformationLogiciel informationLogiciel, JFrame fenetreParent, Image image) {
		super();
		this.informationLogiciel = informationLogiciel;
		this.fenetreParent = fenetreParent;
		this.image = image;
		this.initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		//Prévu pour une image de 166*313
		this.setSize(766, 643);
		this.setResizable(false);
		this.setContentPane(this.getJContentPane());
		this.setTitle("A propos de...");
		this.setLocationRelativeTo(null);
		this.setAlwaysOnTop(true);

	}

	/**
	 * This method initializes jContentPane
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (this.jContentPane == null) {
			this.jContentPane = new JPanel();
			this.jContentPane.setLayout(new BorderLayout());
			this.jContentPane.setBackground(this.fenetreParent.getContentPane().getBackground());
			this.jContentPane.add(this.getLabelImage(), java.awt.BorderLayout.WEST);
			this.jContentPane.add(this.getJPanelAffichages(), java.awt.BorderLayout.CENTER);
			this.jContentPane.add(this.getJPanelNomAppli(), java.awt.BorderLayout.NORTH);
			this.jContentPane.add(this.getTextAreaLicence(), BorderLayout.SOUTH);
		}
		return this.jContentPane;
	}

	/**
	 * @return
	 */
	private JTextArea getTextAreaLicence() {
		if (this.textAreaLicence == null) {
			this.textAreaLicence = new JTextArea();
			this.textAreaLicence.setEditable(false);
			this.textAreaLicence.setPreferredSize(new Dimension(0, 300));
			this.textAreaLicence.setBackground(this.fenetreParent.getContentPane().getBackground());
			this.textAreaLicence.setText(this.getLinkedLibrairiesFileContent());
			this.textAreaLicence.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		}
		return this.textAreaLicence;
	}

	/**
	 * @return
	 */
	private String getLinkedLibrairiesFileContent() {
		File file = new File("linkedlibrairies.txt");
		if (!file.exists()) {
			return "";
		}
		BufferedReader reader = null;
		StringBuffer buffer = new StringBuffer();
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null) {
				buffer.append(line);
				buffer.append("\n");
				line = reader.readLine();
			}
		} catch (IOException e) {
			return "";
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					//rien à faire...
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * @return
	 */
	private JLabel getLabelImage() {
		if (this.labelImage == null) {
			ImageIcon image2 = new ImageIcon(this.image);
			this.labelImage = new JLabel(image2);
		}
		return this.labelImage;
	}

	/**
	 * This method initializes jPanelAffichages
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelAffichages() {
		if (this.jPanelAffichages == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(1);
			this.jLabelAuteur = new JLabel();
			this.jLabelAuteur.setText(this.informationLogiciel.getAuteur());
			this.jLabelAuteurLabel = new JLabel();
			this.jLabelAuteurLabel.setText("Auteur : ");
			this.jPanelAffichages = new JPanel();
			this.jPanelAffichages.setLayout(gridLayout);
			this.jPanelAffichages.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			this.jPanelAffichages.add(this.jLabelAuteurLabel, null);
			this.jPanelAffichages.add(this.jLabelAuteur, null);
			this.jPanelAffichages.setOpaque(false);
		}
		return this.jPanelAffichages;
	}

	/**
	 * This method initializes jPanelNomAppli
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelNomAppli() {
		if (this.jPanelNomAppli == null) {
			this.jLabelNomAppli = new JLabel();
			this.jLabelNomAppli.setText(this.informationLogiciel.getNomApplication() + " " + this.informationLogiciel.versionApplication);
			this.jPanelNomAppli = new JPanel();

			this.jPanelNomAppli.setBackground(new java.awt.Color(247, 247, 247));
			this.jPanelNomAppli.add(this.jLabelNomAppli, null);
		}
		return this.jPanelNomAppli;
	}

}
