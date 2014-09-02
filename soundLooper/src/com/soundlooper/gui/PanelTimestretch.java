/**
 *
 */
package com.soundlooper.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

import org.apache.log4j.Logger;

import com.soundlooper.model.SoundLooperPlayer;

/**
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
 * @author ANEDJARI
 *
 */
public class PanelTimestretch extends JPanel {

	/**
	 * Default serial for this class
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the rate slider
	 */
	protected JSlider sliderTimestretch;

	/**
	 * the button to apply a 50% timeStretch
	 */
	protected JButton buttonTimestretch50;

	/**
	 * the button to apply a 75% timeStretch
	 */
	protected JButton buttonTimestretch75;

	/**
	 * the button to apply a 100% timeStretch
	 */
	protected JButton buttonTimestretch100;

	/**
	 * the button to apply a 150% timeStretch
	 */
	protected JButton buttonTimestretch150;

	/**
	 * the button to apply a 200% timeStretch
	 */
	protected JButton buttonTimestretch200;

	/**
	 * Speed combo box
	 */
	protected JComboBox<Integer> comboBoxVitesse;

	/**
	 * The panel that contain the buttons to change timestretch
	 */
	protected JPanel panelButtonTimestretch;

	/**
	 * The panel that contain the slider
	 */
	protected JPanel panelSilderTimestretch;

	/**
	 * Logger for this class
	 */
	protected Logger logger = Logger.getLogger(this.getClass());

	/**
	 * Get the control panel
	 */
	public PanelTimestretch() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		this.add(new JLabel("%"));
		this.add(this.getSliderTimestretch());
		this.add(this.getComboBoxVitesse());
		this.setPreferredSize(new Dimension(175, 30));
		this.setBorder(new LineBorder(Color.BLACK, 1, true));
		this.setBackground(new Color(253, 253, 234));
		this.setOpaque(true);
	}

	/**
	 * Get the volume slider
	 * @return the volume slider
	 */
	private JSlider getSliderTimestretch() {
		if (this.sliderTimestretch == null) {
			this.sliderTimestretch = new JSlider();
			this.sliderTimestretch.setOpaque(false);
			this.sliderTimestretch.setValue(100);
			this.sliderTimestretch.setMaximum(200);
			this.sliderTimestretch.setMinimum(50);
			this.sliderTimestretch.setValueIsAdjusting(false);
			this.sliderTimestretch.setPreferredSize(new Dimension(100, 30));
			this.sliderTimestretch.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0), "none");
			this.sliderTimestretch.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0), "none");
			this.sliderTimestretch.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					SoundLooperPlayer.getInstance().setTimestretch(PanelTimestretch.this.sliderTimestretch.getValue());
				}
			});

			this.sliderTimestretch.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					SoundLooperPlayer.getInstance().setTimestretch(PanelTimestretch.this.sliderTimestretch.getValue());
				}
			});
		}
		return this.sliderTimestretch;
	}

	/**
	 * set the time stretch slider value (does not apply real timestretch
	 * @param percent the percent to apply to the slider
	 */
	public void setTimestrechValue(int percent) {
		PanelTimestretch.this.getSliderTimestretch().setValue(percent);
		this.comboBoxVitesse.getEditor().setItem(new Integer(percent));
	}

	/**
	 * Get the speed combo box
	 * @return the combo box
	 */
	protected JComboBox<Integer> getComboBoxVitesse() {
		if (this.comboBoxVitesse == null) {
			this.comboBoxVitesse = new JComboBox<Integer>(new Integer[] { Integer.valueOf(50), Integer.valueOf(75), Integer.valueOf(100), Integer.valueOf(150),
					Integer.valueOf(200) });
			SoundLooperComboBoxEditor anEditor = new SoundLooperComboBoxEditor();
			anEditor.setMaxLength(3);
			anEditor.setNumericOnly(true);
			this.comboBoxVitesse.setEditor(anEditor);
			this.comboBoxVitesse.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						try {
							SoundLooperPlayer.getInstance().setTimestretch((new Integer(e.getItem().toString())).intValue());
						} catch (NumberFormatException e2) {
							//La valeur saisie n'est pas un nombre, on ignore
						}
					}
				}
			});

			this.comboBoxVitesse.setPreferredSize(new Dimension(50, 20));
			this.comboBoxVitesse.setEditable(true);
			this.comboBoxVitesse.setSelectedItem(new Integer(100));
		}
		return this.comboBoxVitesse;
	}
}
