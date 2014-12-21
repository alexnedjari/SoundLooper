/**
 *
 */
package com.soundlooper.gui.action.player;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.aned.audio.player.Player;
import com.soundlooper.model.SoundLooperPlayer;

/**
 *-------------------------------------------------------
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
 *
 * @author Alexandre NEDJARI
 * @since  16 f�vr. 2014
 *-------------------------------------------------------
 */
public class SetEndAlignmentOnCurrentPositionAction extends AbstractAction {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		int beginTime = Player.getInstance().getLoopPointBegin();
		int endTime = Player.getInstance().getMediaTime();
		SoundLooperPlayer.getInstance().setLoopPoints(beginTime, endTime);
	}

	@Override
	public boolean isEnabled() {
		return Player.getInstance().isSoundInitialized();
	}

}
