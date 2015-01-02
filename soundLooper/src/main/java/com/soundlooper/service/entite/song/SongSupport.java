package com.soundlooper.service.entite.song;

import java.util.ArrayList;
import java.util.List;

import com.soundlooper.model.song.Song;

/**
 *-------------------------------------------------------
 * Song listeners managment
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
 * @since  02 jan. 2015
 *-------------------------------------------------------
 */
public class SongSupport {
	private static SongSupport instance;
	
	private List<SongListener> listSongListener = new ArrayList<SongListener>();

	public synchronized static SongSupport getInstance() {
		if (instance == null) {
			instance = new SongSupport();
		}
		return instance;
	}
	
	public void addToListSongListener(SongListener songListener) {
		this.listSongListener.add(songListener);
	}
	
	public void removeFromListSongListener(SongListener songListener) {
		this.listSongListener.remove(songListener);
	}
	
	public void fireFavoriteUpdated(Song song) {
		for (SongListener listener : this.listSongListener) {
			listener.onFavoriteUpdated(song);
		}
	}
	
}
