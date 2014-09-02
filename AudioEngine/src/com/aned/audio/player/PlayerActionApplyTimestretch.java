/**
 *
 */
package com.aned.audio.player;

import org.apache.log4j.Logger;

import com.aned.exception.PlayerException;
import com.aned.exception.StackTracer;

/**--------------------------------------------------------------------------------
 *
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
 * @author Alexandre NEDJARI
 * @since 9 sept. 2011
 *--------------------------------------------------------------------------------
 */
public class PlayerActionApplyTimestretch extends PlayerAction {

	/**
	 * the logger for this class
	 */
	private final Logger logger = Logger.getLogger(this.getClass());

	/**
	 * The percent to apply
	 */
	int percent;

	/**
	 * Create a player action to apply a timestretch
	 * @param player the player
	 * @param percent the percent between 50 and 200%
	 */
	public PlayerActionApplyTimestretch(Player player, int percent) {
		super(player);
		this.percent = percent;
	}

	@Override
	public void run() {
		try {
			this.getPlayer().getCurrentSound().setTimeStrechPercent(this.percent);
		} catch (PlayerException e) {
			this.logger.error(StackTracer.getStackTrace(e));
			this.getPlayer().getMessageNotifier().sendError(e.getMessage());
		}

	}

}
