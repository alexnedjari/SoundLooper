/**
 *
 */
package com.aned.audio.player;

import org.apache.log4j.Logger;

import com.aned.exception.PlayerException;
import com.aned.exception.StackTracer;

/**--------------------------------------------------------------------------------
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
 * @since 9 sept. 2011
 *--------------------------------------------------------------------------------
 */
public class PlayerActionSetLoopPoint extends PlayerAction {

	/**
	 * Logger for this class
	 */
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * The begin point
	 */
	int beginPoint;

	/**
	 * The end point
	 */
	int endPoint;

	/**
	 * constructor
	 * @param player the player
	 * @param beginPoint the begin point in milliseconds
	 * @param endPoint the end point in milliseconds
	 */
	public PlayerActionSetLoopPoint(Player player, int beginPoint, int endPoint) {
		super(player);
		this.beginPoint = beginPoint;
		this.endPoint = endPoint;
	}

	@Override
	public void run() {
		try {
			this.getPlayer().getCurrentSound().setLoopPoints(this.beginPoint, this.endPoint);

			int mediaTime = this.getPlayer().getCurrentSound().getMediaTime();
			if (mediaTime < this.beginPoint || mediaTime > this.endPoint) {
				//If current mefia time is out of new positions, put it on the begin of the loop
				this.getPlayer().setMediaTime(this.beginPoint);
			}
		} catch (PlayerException e) {
			this.logger.error(StackTracer.getStackTrace(e));
			this.getPlayer().getMessageNotifier().sendError(e.getMessage());
		}
	}
}
