/**
 *
 */
package com.soundlooper.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.aned.exception.PlayerException;
import com.soundlooper.gui.jsoundlooperslider.JSoundLooperSlider;
import com.soundlooper.gui.jsoundlooperslider.JSoundLooperSliderListener;
import com.soundlooper.gui.jsoundlooperslider.JSoundLooperSliderModel;
import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.system.util.StackTracer;

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
public class PanelOtherControl extends JPanel {

	/**
	 * Serial number for this class
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the volume slider
	 */
	//protected JSlider sliderVolume;
	protected JSoundLooperSlider sliderVolume;

	/**
	 * the rate slider
	 */
	protected PanelTimestretch panelTimestretch;

	/**
	 * the window player
	 */
	protected WindowPlayer windowPlayer;

	/**
	 * Logger for this class
	 */
	protected Logger logger = Logger.getLogger(this.getClass());

	/**
	 * Get the control panel
	 * @param windowPlayer the player windows
	 */
	public PanelOtherControl(WindowPlayer windowPlayer) {
		this.windowPlayer = windowPlayer;
		this.setOpaque(false);
		this.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 0));

		JRoundedPanel panelVolume = new JRoundedPanel();
		panelVolume.add(new JLabel(ImageGetter.getImageIcon(ImageGetter.ICONE_VOLUME_16)));
		panelVolume.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 0));
		panelVolume.add(this.getSliderVolume());
		panelVolume.setBackground(new Color(220, 220, 220));
		//panelVolume.setOpaque(true);

		this.add(panelVolume);
		this.add(this.getPanelTimestretch());

		this.setPreferredSize(new Dimension(300, 28));
	}

	/**
	 * Get the volume slider
	 * @return the volume slider
	 */
//	private JSlider getSliderVolume() {
//		if (this.sliderVolume == null) {
//			this.sliderVolume = new JSlider();
//			this.sliderVolume.setOpaque(false);
//			this.sliderVolume.setValue(100);
//			this.sliderVolume.setMaximum(100);
//			this.sliderVolume.setValueIsAdjusting(false);
//			this.sliderVolume.setPreferredSize(new Dimension(50, 28));
//			this.sliderVolume.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0), "none");
//			this.sliderVolume.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0), "none");
//
//			this.sliderVolume.addMouseMotionListener(new MouseMotionAdapter() {
//
//				@Override
//				public void mouseDragged(MouseEvent e) {
//					try {
//						SoundLooperPlayer.getInstance().setVolume(PanelOtherControl.this.sliderVolume.getValue());
//					} catch (PlayerException e1) {
//						PanelOtherControl.this.logger.error(StackTracer.getStackTrace(e1));
//						PanelOtherControl.this.windowPlayer.onError(e1.getMessage());
//					}
//				}
//			});
//		}
//		return this.sliderVolume;
//	}
	
	private JSoundLooperSlider getSliderVolume() {
		if (this.sliderVolume == null) {
			this.sliderVolume = new JSoundLooperSlider();
			this.sliderVolume.setModel(new JSoundLooperSliderModel(this));
			this.sliderVolume.setMaxValue(100);
			this.sliderVolume.setMinValue(0);
			this.sliderVolume.addDisplayedValue(0);
			this.sliderVolume.addDisplayedValue(100);
			this.sliderVolume.setPreferredSize(new Dimension(50, 28));
			sliderVolume.addJSoundLooperSliderListener(new JSoundLooperSliderListener() {
				
				@Override
				public void onValueChange(int newValue) {
					try {
						SoundLooperPlayer.getInstance().setVolume(newValue);
					} catch (PlayerException e1) {
						PanelOtherControl.this.logger.error(StackTracer.getStackTrace(e1));
						PanelOtherControl.this.windowPlayer.onError(e1.getMessage());
					}
				}
			});
		}
		return this.sliderVolume;
	}

	/**
	 * Get the volume slider
	 * @return the volume slider
	 */
	public PanelTimestretch getPanelTimestretch() {
		if (this.panelTimestretch == null) {
			this.panelTimestretch = new PanelTimestretch();
		}
		return this.panelTimestretch;
	}

	/**
	 * Update the volume position of the slider
	 * @param percent the player volume
	 */
	public void setVolumePosition(int percent) {
		this.sliderVolume.setValue(percent);
		
	}

	/**
	 * @param percent the new timestretch percent
	 */
	public void setTimeStretchPosition(int percent) {
		this.panelTimestretch.setTimestrechValue(percent);
	}
}
