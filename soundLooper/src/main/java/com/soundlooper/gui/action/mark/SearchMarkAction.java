/**
 *
 */
package com.soundlooper.gui.action.mark;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;

import com.soundlooper.gui.DialogSearch;
import com.soundlooper.gui.WindowPlayer;
import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.model.mark.Mark;
import com.soundlooper.model.song.Song;

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
 * @since  28 sept. 2012
 *-------------------------------------------------------
 */
public class SearchMarkAction extends AbstractAction {

	private WindowPlayer windowPlayer;

	/**
	 * @param windowPlayer
	 */
	public SearchMarkAction(WindowPlayer windowPlayer) {
		super();
		this.windowPlayer = windowPlayer;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Song song = SoundLooperPlayer.getInstance().getSong();
		ArrayList<Mark> markList = new ArrayList<Mark>(song.getMarks().values());
		DialogSearch dialogSearch = new DialogSearch(this.windowPlayer, markList, 0, "Recherche dans les marqueurs");
		dialogSearch.setVisible(true);
		Mark mark = (Mark) dialogSearch.getResultat();
        SoundLooperPlayer.getInstance().selectMark(mark);

	}

	/* (non-Javadoc)
	 * @see javax.swing.AbstractAction#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return SoundLooperPlayer.getInstance().isSoundInitialized() && SoundLooperPlayer.getInstance().getSong().isFavorite();
	}

}
