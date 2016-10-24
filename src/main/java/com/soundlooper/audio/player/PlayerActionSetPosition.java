/**
 *
 */
package com.soundlooper.audio.player;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.soundlooper.exception.PlayerException;
import com.soundlooper.model.SoundLooperPlayer;

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
public class PlayerActionSetPosition extends PlayerAction {

	/**
	 * Logger for this class
	 */
	private Logger logger = LogManager.getLogger(this.getClass());

	/**
	 * The millisecond time
	 */
	private int millisecondTime;

	/**
	 * Constructor
	 * 
	 * @param player
	 *            the player
	 * @param millisecondTime
	 *            the millisecond time
	 */
	public PlayerActionSetPosition(Player player, int millisecondTime) {
		super(player);
		this.millisecondTime = millisecondTime;
	}

	@Override
	public void run() throws PlayerException {
		logger.info("Start action : " + this.getClass().getSimpleName());
		if (!this.getPlayer().isSoundInitialized()) {
			return;
		}
		int beginPoint = SoundLooperPlayer.getInstance().getLoopPointBeginMillisecond();
		int endPoint = SoundLooperPlayer.getInstance().getLoopPointEndMillisecond();
		if (this.millisecondTime >= beginPoint && this.millisecondTime <= endPoint) {
			this.getPlayer().getCurrentSound().setMediaTime(this.millisecondTime);
		} else {
			this.getPlayer().getCurrentSound().setMediaTime(beginPoint);
		}
	}
}
