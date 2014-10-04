/**
 *
 */
package com.soundlooper.gui.action.favorite;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;

import com.soundlooper.gui.DialogSearch;
import com.soundlooper.gui.WindowPlayer;
import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.model.song.Song;
import com.soundlooper.service.uc.gererSongs.GererSongService;

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
public class SearchFavoriteAction extends AbstractAction {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	private WindowPlayer windowPlayer;

	/**
	 * @param windowPlayer
	 */
	public SearchFavoriteAction(WindowPlayer windowPlayer) {
		super();
		this.windowPlayer = windowPlayer;
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		List<Song> favoriteSongList = GererSongService.getInstance().getFavoriteSongList();
		DialogSearch dialogSearch = new DialogSearch(this.windowPlayer, favoriteSongList, 0, "Recherche dans les favoris");
		dialogSearch.setVisible(true);
		Song song = (Song) dialogSearch.getResultat();
		if (song != null) {
			SoundLooperPlayer.getInstance().loadSong(song.getFile());
		}
	}

}
