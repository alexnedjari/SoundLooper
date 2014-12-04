/**
 *
 */
package com.aned.audio.player;

import org.apache.log4j.Logger;

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
public class PlayerActionSetPosition extends PlayerAction {

	/**
	 * Logger for this class
	 */
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * The millisecond time
	 */
	private int millisecondTime;

	/**
	 * Constructor
	 * @param player the player
	 * @param millisecondTime the millisecond time
	 */
	public PlayerActionSetPosition(Player player, int millisecondTime) {
		super(player);
		this.millisecondTime = millisecondTime;
	}

	@Override
	public void run() {
		try {
			this.getPlayer().checkCurrentSoundInitialized();
			int beginPoint = this.getPlayer().getCurrentSound().getLoopPointBegin();
			int endPoint = this.getPlayer().getCurrentSound().getLoopPointEnd();
			if (this.millisecondTime >= beginPoint && this.millisecondTime <= endPoint) {
				this.getPlayer().getCurrentSound().setMediaTime(this.millisecondTime);
			} else {
				this.getPlayer().getCurrentSound().setMediaTime(beginPoint);
			}
		} catch (PlayerException e) {
			this.logger.error(StackTracer.getStackTrace(e));
			this.getPlayer().getMessageNotifier().sendError(e.getMessage());
		}
	}
}
