/**
 *
 */
package com.soundlooper.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import com.soundlooper.gui.jsoundlooperslider.JSoundLooperSliderListener;
import com.soundlooper.gui.jsoundlooperslider.JSoundLooperTextSlider;
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
public class PanelTimestretch extends JRoundedPanel {

	/**
	 * Default serial for this class
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The panel that contain the slider
	 */
	protected JPanel panelSilderTimestretch;
	
	protected JSoundLooperTextSlider soundLooperSlider;

	/**
	 * Logger for this class
	 */
	protected Logger logger = Logger.getLogger(this.getClass());

	/**
	 * Get the control panel
	 */
	public PanelTimestretch() {
		BorderLayout mgr = new BorderLayout();
		mgr.setHgap(5);
		this.setLayout(mgr);
		this.setBorder(new EmptyBorder(0, 5, 0, 5));
		this.add(new JLabel("%"), BorderLayout.WEST);
		this.add(this.getSoundLooperSlider(), BorderLayout.CENTER);
		this.setPreferredSize(new Dimension(175, 30));
		this.setBackground(new Color(220, 225, 230));
		
		this.setOpaque(false);
	}
	
	
	public JSoundLooperTextSlider getSoundLooperSlider() {
		if (soundLooperSlider == null) {
			soundLooperSlider = new JSoundLooperTextSlider();
			soundLooperSlider.setMinValue(50);
			soundLooperSlider.setMaxValue(200);
			soundLooperSlider.addDisplayedValue(50);
			soundLooperSlider.addDisplayedValue(100);
			soundLooperSlider.addDisplayedValue(200);
			soundLooperSlider.addJSoundLooperSliderListener(new JSoundLooperSliderListener() {
				
				@Override
				public void onValueChange(int newValue) {
					SoundLooperPlayer.getInstance().setTimestretch(newValue);
				}
			});
		}
		return soundLooperSlider;
	}

	/**
	 * set the time stretch slider value (does not apply real timestretch
	 * @param percent the percent to apply to the slider
	 */
	public void setTimestrechValue(int percent) {
		getSoundLooperSlider().setValue(percent);
	}
}
