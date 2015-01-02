/**
 *
 */
package com.aned.audio.player;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.aned.audio.player.Player.PlayerState;
import com.aned.exception.PlayerException;
import com.aned.exception.StackTracer;

/**
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
	protected Logger logger = Logger.getLogger(this.getClass());

	/**
	 * Constructor
	 * @param player the player
	 * @param fileToLoad the file to load
	 */
	public PlayerActionLoad(Player player, File fileToLoad) {
		super(player);
		this.fileToLoad = fileToLoad;
	}

	@Override
	public void run() {
		try {
			this.getPlayer().setState(PlayerState.STATE_LOADING_SONG);
			this.getPlayer().checkSystemInitialized();
			if (this.getPlayer().isSoundInitialized()) {
				new PlayerActionRemoveTimestretch(this.getPlayer()).run();
				new PlayerActionStop(this.getPlayer()).run();
				new PlayerActionDesallocateSong(this.getPlayer()).run();
			}
			this.getPlayer().setCurrentSound(new SoundFile(this.fileToLoad));
			this.getPlayer().setVolume(this.getPlayer().getCurrentVolume());

			//Notifie les observeur comme quoi le chargement est terminé
			for (PlayerActionListener playerActionListener : this.getPlayer().getListPlayerActionListener()) {
				playerActionListener.onSongLoaded(this.fileToLoad);
			}
			this.getPlayer().setState(PlayerState.STATE_SONG_LOADED);
			this.getPlayer().getMessageNotifier().sendInfo("Fichier '" + this.fileToLoad.getName() + "' chargé.");
			return;
		} catch (IOException e) {
			this.logger.error(StackTracer.getStackTrace(e));
			this.getPlayer().getMessageNotifier().sendError(e.getMessage());
		} catch (PlayerException e) {
			this.logger.error(StackTracer.getStackTrace(e));
			this.getPlayer().getMessageNotifier().sendError(e.getMessage());
		}
		this.getPlayer().setCurrentSound(null);
		this.getPlayer().setState(PlayerState.STATE_PLAYER_UNINITIALIZED);
	}
}
