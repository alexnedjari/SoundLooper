/**
 *
 */
package com.soundlooper.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JSlider;

import org.apache.log4j.Logger;

import com.aned.exception.PlayerException;
import com.soundlooper.gui.jplayer.JPlayer;
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
public class PanelSliders extends JPanel {

	/**
	 * Serial version for this class
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The slider for the media time
	 */
	protected JSlider sliderMediaTime;

	/**
	 * The slider for ine interval to play
	 */
	protected JPlayer sliderInterval = null;

	/**
	 * True if the media time slider is currently dragged
	 */
	protected boolean isSliderMediaTimePressed = false;

	/**
	 * The player windows
	 */
	protected WindowPlayer windowPlayer;

	/**
	 * The logger for this class
	 */
	protected Logger logger = Logger.getLogger(PanelOtherControl.class);

	/**
	 * Get the sliders panel
	 * @param windowPlayer the principal windows
	 */
	public PanelSliders(WindowPlayer windowPlayer) {
		this.windowPlayer = windowPlayer;
		this.setLayout(new BorderLayout());
		this.add(this.getSliderInterval(), BorderLayout.NORTH);
		this.setOpaque(false);
	}

	/**
	 * Get the interval slider
	 * @return the interval slider
	 */
	private JPlayer getSliderInterval() {
		if (this.sliderInterval == null) {
			this.sliderInterval = new JPlayer();
			this.sliderInterval.setOpaque(false);
			this.sliderInterval.setColor(new Color(36, 168, 206));
			this.sliderInterval.addJPlayerListener(this.windowPlayer);
			this.sliderInterval.setPreferredSize(new Dimension(800, 126));
		}
		return this.sliderInterval;
	}

	/**
	 * Set the pause state
	 */
	public void setStatePaused() {
		//this.sliderMediaTime.setEnabled(true);
		this.sliderInterval.setEnabled(true);
	}

	/**
	 * Set the uninitialized state
	 */
	public void setStateUninitialized() {
		//this.sliderMediaTime.setEnabled(false);
		this.sliderInterval.setEnabled(false);
	}

	/**
	 * Set the stopped state
	 */
	protected void setStateStopped() {
		//this.sliderMediaTime.setEnabled(false);
		this.sliderInterval.setEnabled(true);
	}

	/**
	 * Set the playing state
	 */
	protected void setStatePlaying() {
		//this.sliderMediaTime.setEnabled(true);
		this.sliderInterval.setEnabled(true);
	}

	/**
	 * Initialize the sliders after song load
	 * @throws PlayerException If the player is not initialized
	 */
	public void initializedSlidersFromSong() throws PlayerException {
		long millisecondDuration = SoundLooperPlayer.getInstance().getCurrentSound().getDuration();
		this.sliderInterval.setMaximumValue(millisecondDuration);
		this.sliderInterval.setValeurSliderDroite(this.sliderInterval.getMaximumValue());
		this.sliderInterval.setValeurSliderGauche(0);
		this.sliderInterval.setValeur(0);
		//this.sliderMediaTime.setValue(0);
	}

	/**
	 * Set the play cursor position
	 * @param millisecondPosition ths new millisecond position
	 */
	public void setPlayCursorPosition(int millisecondPosition) {
		try {
			if (!this.isSliderMediaTimePressed) {
				long millisecondDuration = SoundLooperPlayer.getInstance().getCurrentSound().getDuration();
				//long value = millisecondPosition * this.sliderMediaTime.getMaximum() / millisecondDuration;
				//				this.sliderMediaTime.setValue(new Long(value).intValue());

				long value = millisecondPosition * new Double(this.sliderInterval.getMaximumValue()).longValue() / millisecondDuration;
				this.sliderInterval.setValeur(new Long(value).intValue());
			}
		} catch (PlayerException e) {
			this.logger.error(StackTracer.getStackTrace(e));
			this.windowPlayer.onError(e.getMessage());
		}
	}

	/**
	 * Set the loop slider positions
	 * @param beginPoint the loop start point
	 * @param endPoint the loop end point
	 */
	public void setLoopPointsPositions(int beginPoint, int endPoint) {
		this.sliderInterval.setValeurSlider(beginPoint, endPoint);
	}

	/**
	 * The song image generation is started, we must display it in the player component
	 */
	public void startGenerateImage() {
		this.sliderInterval.startGenerateImage();

	}

	/**
	 * @param image
	 */
	public void setGeneratedImage(BufferedImage image) {
		this.sliderInterval.setImage(image);

	}
}
