/**
 *
 */
package com.soundlooper.audio.player;

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
public abstract class PlayerAction {

	/**
	 * Local player used
	 */
	private Player player;

	public PlayerAction(Player player) {
		super();
		this.player = player;
	}

	/**
	 * @return the player
	 */
	protected Player getPlayer() {
		synchronized (this.player) {
			return this.player;
		}
	}

	/**
	 * Run the action
	 */
	public abstract void run() throws PlayerException;

}
