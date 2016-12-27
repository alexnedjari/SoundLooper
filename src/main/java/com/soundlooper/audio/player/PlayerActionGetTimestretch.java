/**
 *
 */
package com.soundlooper.audio.player;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
public class PlayerActionGetTimestretch extends PlayerAction {

	/**
	 * Logger for this class
	 */
	private Logger logger = LogManager.getLogger(this.getClass());

	/**
	 * The millisecond time
	 */
	private int timestretch = -1;

	/**
	 * Constructor
	 * 
	 * @param player
	 *            the player
	 */
	public PlayerActionGetTimestretch(Player player) {
		super(player);
	}

	@Override
	public void run() throws PlayerException {
		logger.info("Start action : " + this.getClass().getSimpleName());
		this.getPlayer().checkCurrentSoundInitialized();
		this.timestretch = this.getPlayer().getCurrentSound().getCurrentTimeStretch();

	}

	/**
	 * Get the result
	 * 
	 * @return the result
	 * @throws PlayerException
	 */
	public int getResult() throws PlayerException {
		if (this.timestretch == -1) {
			throw new PlayerException("Timestretch is not initialized");
		}
		return this.timestretch;
	}
}
