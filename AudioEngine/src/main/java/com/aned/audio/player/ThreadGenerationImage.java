/**
 *
 */
package com.aned.audio.player;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.aned.audio.player.Player.PlayerMessageNotifier;
import com.aned.exception.PlayerException;

/**
 *-------------------------------------------------------
 * AudioEngine is an audio engine based on FMOD
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
 * @since  27 août 2014
 *-------------------------------------------------------
 */
public class ThreadGenerationImage extends Thread {

	private PlayerMessageNotifier notifier;

	/**
	 * Action listener list
	 */
	private List<PlayerActionListener> listPlayerActionListener = new ArrayList<PlayerActionListener>();

	/**
	 * The current player (corresponding to the current song)
	 */
	private SoundFile sound;

	/**
	 * @param notifier
	 * @param listPlayerActionListener
	 * @param sound
	 */
	public ThreadGenerationImage(PlayerMessageNotifier notifier, List<PlayerActionListener> listPlayerActionListener, SoundFile sound) {
		super();
		this.notifier = notifier;
		this.listPlayerActionListener = listPlayerActionListener;
		this.sound = sound;
	}

	@Override
	public void run() {
		try {
			for (PlayerActionListener listener : this.listPlayerActionListener) {
				listener.onBeginGenerateImage();
			}
			BufferedImage image = this.sound.generateImage();
			if (this.isInterrupted()) {
				return;
			}
			for (PlayerActionListener listener : this.listPlayerActionListener) {
				listener.onEndGenerateImage(image);
			}

		} catch (PlayerException e) {
			this.notifier.sendError(e.getMessage());
		} catch (IOException e) {
			this.notifier.sendError(e.getMessage());
		}
	}

}
