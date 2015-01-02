/**
 *
 */
package com.soundlooper.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.model.song.Song;
import com.soundlooper.system.util.GUIHelper;
import com.soundlooper.system.util.StringUtil;

/**
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
 * @author Alexandre NEDJARI
 * @since  14 nov. 2012
 *-------------------------------------------------------
 */
public class WindowAddMark extends JDialog {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String nomSaisi = null;
	private Song song;
	private JLabel labelNouveauNom = new JLabel();
	/**
	 * Labels combo box
	 */
	protected JComboBox<String> comboBoxLabel;

	protected JButton boutonValider;

	public WindowAddMark(WindowPlayer windowPlayer, Song song) {
		super(windowPlayer, true);
		this.song = song;

		BorderLayout mgr = new BorderLayout(3, 3);

		this.getContentPane().setBackground(new Color(249, 248, 208));
		this.getContentPane().setLayout(mgr);
		((JPanel) this.getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));

		this.getContentPane().add(this.getComboBoxLabel(), BorderLayout.NORTH);

		JPanel panelSud = new JPanel();
		panelSud.setLayout(new BorderLayout(3, 3));
		panelSud.add(this.labelNouveauNom, BorderLayout.CENTER);

		panelSud.add(this.getBoutonValider(), BorderLayout.EAST);
		panelSud.setOpaque(false);
		this.getContentPane().add(panelSud, BorderLayout.SOUTH);
		this.updateLabelNouveauNom();
		this.setTitle("Nom du nouveau marqueur");

		GUIHelper.addShortcut(this.getContentPane(), new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER && !WindowAddMark.this.getComboBoxLabel().isPopupVisible()) {
					WindowAddMark.this.getComboBoxLabel().isPopupVisible();
					WindowAddMark.this.getComboBoxLabel().actionPerformed(null);

					WindowAddMark.this.valider();
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !WindowAddMark.this.getComboBoxLabel().isPopupVisible()) {
					WindowAddMark.this.dispose();

				}
			}
		});

		this.setPreferredSize(new Dimension(300, 95));
		this.pack();
		this.setLocationRelativeTo(null);
	}

	private void updateLabelNouveauNom() {
		this.labelNouveauNom.setText("Nom final : " + SoundLooperPlayer.getInstance().getNomValideForMark(song, this.comboBoxLabel.getSelectedItem().toString()));
	}

	/**
	 * Get the labels combo box
	 * @return the combo box
	 */
	protected JComboBox<String> getComboBoxLabel() {
		if (this.comboBoxLabel == null) {
			List<String> listeLabel = new ArrayList<String>(Arrays.asList(new String[] { "Intro", "Couplet", "Refrain", "Solo", "Pont", "Break", "Outtro" }));
			for (String markName : this.song.getMarks().keySet()) {
				if (!listeLabel.contains(StringUtil.getInstance().getNomEtIncrement(markName)[0]) && this.song.getMarks().get(markName).isEditable()) {
					listeLabel.add(markName);
				}
			}

			//listeLabel.addAll(this.song.getMarks().keySet());
			this.comboBoxLabel = new JComboBox<String>(listeLabel.toArray(new String[listeLabel.size()]));
			SoundLooperComboBoxEditor anEditor = new SoundLooperComboBoxEditor();
			anEditor.setMaxLength(50);
			this.comboBoxLabel.setEditor(anEditor);

			this.comboBoxLabel.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					WindowAddMark.this.updateLabelNouveauNom();

				}
			});

			this.comboBoxLabel.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() != KeyEvent.VK_DOWN && e.getKeyCode() != KeyEvent.VK_UP && e.getKeyCode() != KeyEvent.VK_LEFT && e.getKeyCode() != KeyEvent.VK_RIGHT
							&& e.getKeyCode() != KeyEvent.VK_ESCAPE && e.getKeyCode() != KeyEvent.VK_ENTER && e.getKeyCode() != KeyEvent.VK_CONTROL
							&& e.getKeyCode() != KeyEvent.VK_CAPS_LOCK) {
						WindowAddMark.this.comboBoxLabel.actionPerformed(null);
						WindowAddMark.this.updateLabelNouveauNom();
					}
				}
			});

			this.comboBoxLabel.setPreferredSize(new Dimension(200, 20));
			this.comboBoxLabel.setEditable(true);
		}
		return this.comboBoxLabel;
	}

	protected JButton getBoutonValider() {
		if (this.boutonValider == null) {
			this.boutonValider = SoundLooperGUIHelper.getBouton("ok", "Valider le nom", true, 16);
			//this.boutonValider = new JButton("Valider");
			this.boutonValider.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					WindowAddMark.this.valider();

				}
			});
		}
		this.boutonValider.setPreferredSize(new Dimension(50, 25));
		this.boutonValider.setFocusable(false);
		this.boutonValider.setBorderPainted(true);
		this.boutonValider.setContentAreaFilled(true);
		return this.boutonValider;
	}

	

	public String getNomSaisi() {
		return this.nomSaisi;
	}

	private void valider() {
		WindowAddMark.this.nomSaisi = SoundLooperPlayer.getInstance().getNomValideForMark(song, this.comboBoxLabel.getSelectedItem().toString());
		WindowAddMark.this.dispose();
	}

}
