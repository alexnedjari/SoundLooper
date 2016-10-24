/**
 *
 */
package com.soundlooper.audio.player;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.soundlooper.audio.player.Player.PlayerState;
import com.soundlooper.exception.PlayerException;

/**
 * AudioEngine is an audio engine based on FMOD Copyright (C) 2014 Alexandre
 * NEDJARI
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author ANEDJARI
 *
 */
public class PlayerActionLoad extends PlayerAction {

	/**
	 * The file to load
	 */
	private File fileToLoad = null;

	/**
	 * Logger for this class
	 */
	private Logger logger = LogManager.getLogger(this.getClass());

	/**
	 * Constructor
	 * 
	 * @param player
	 *            the player
	 * @param fileToLoad
	 *            the file to load
	 */
	public PlayerActionLoad(Player player, File fileToLoad) {
		super(player);
		this.fileToLoad = fileToLoad;
	}

	@Override
	public void run() throws PlayerException {
		try {
			logger.info("Start action : " + this.getClass().getSimpleName());
			this.getPlayer().setState(PlayerState.STATE_LOADING_SONG);
			this.getPlayer().checkSystemInitialized();
			if (this.getPlayer().isSoundInitialized()) {
				new PlayerActionRemoveTimestretch(this.getPlayer()).run();
				new PlayerActionStop(this.getPlayer()).run();
				new PlayerActionDesallocateSong(this.getPlayer()).run();
			}
			new SoundFile(this.fileToLoad, this.getPlayer());
			this.getPlayer().setVolume(this.getPlayer().getVolume());
			this.getPlayer().setTimeStretch(this.getPlayer().getTimeStretch());

			this.getPlayer().setState(PlayerState.STATE_PAUSED);
		} catch (IOException e) {
			this.getPlayer().setCurrentSound(null);
			this.getPlayer().setState(PlayerState.STATE_PLAYER_UNINITIALIZED);
			throw new PlayerException("Impossible de charger le fichier '" + fileToLoad.getAbsolutePath() + "'", e);
		}
	}

}
