/**
 *
 */
package com.soundlooper.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.soundlooper.gui.action.player.PlayPauseAction;
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
 * Class containing all the players controls
 * @author ANEDJARI
 */
public class PanelPlayerControls extends JPanel {

	/**
	 * Serial for this class
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The start button
	 */
	protected JButton boutonStart;

	/**
	 * The pause button
	 */
	protected JButton boutonPause;

	/**
	 * Create the player control panel
	 */
	public PanelPlayerControls() {
		this.setOpaque(false);
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		this.setLayout(flowLayout);

		this.boutonStart = SoundLooperGUIHelper.getBouton(new PlayPauseAction(PlayPauseAction.PLAY_ONLY),"lecture", "Lecture", false);
		this.add(this.boutonStart);

		this.boutonPause = SoundLooperGUIHelper.getBouton(new PlayPauseAction(PlayPauseAction.PAUSE_ONLY), "pause", "Pause", false);
		this.add(this.boutonPause);
		this.setPreferredSize(new Dimension(400, 40));
	}

	/**
	 * Set the interface paused state
	 */
	protected void setStatePaused() {
		this.boutonPause.setEnabled(false);
		this.boutonStart.setEnabled(true);
	}

	/**
	 * set the interface state for uninitialized player
	 */
	protected void setStateUninitialized() {
		this.boutonPause.setEnabled(false);
		this.boutonStart.setEnabled(false);
	}

	/**
	 * Set the stopped state
	 */
	protected void setStateStopped() {
		this.boutonPause.setEnabled(false);
		this.boutonStart.setEnabled(true);
	}

	/**
	 * Set the playing state
	 */
	protected void setStatePlaying() {
		this.boutonPause.setEnabled(true);
		this.boutonStart.setEnabled(false);
	}
}
