/**
 *
 */
package com.soundlooper.service.entite.song;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import com.soundlooper.exception.SoundLooperException;
import com.soundlooper.model.song.Song;
import com.soundlooper.model.song.SongDAO;

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
 * Services to manage songs
 *
 * @author Alexandre NEDJARI
 * @since  5 sept. 2012
 *-------------------------------------------------------
 */
public class SongService {

	/**
	 * The instance
	 */
	private static SongService instance;

	/**
	 * Private constructor to avoid instanciation
	 */
	private SongService() {
		//
	}

	/**
	 * Get the instance
	 * @return the instance
	 */
	public static SongService getInstance() {
		if (SongService.instance == null) {
			SongService.instance = new SongService();
		}
		return SongService.instance;
	}

	/**
	 * Get a song by file
	 * @param file the file
	 * @return the song
	 * @throws SoundLooperException If the song is not found
	 */
	public Song getSong(File file) throws SoundLooperException {
		return SongDAO.getInstance().getByFile(file);
	}

	/**
	 * @param song the song to delete
	 * @return the deleted song
	 */
	public Song delete(Song song) {
		return SongDAO.getInstance().delete(song);
	}

	/**
	 * Create song
	 * @param fileToAdd the file on wich song must be created
	 * @return the created song
	 */
	public Song createSong(File fileToAdd) {
		Song song = SongDAO.getInstance().createNew();
		song.setFile(fileToAdd);
		song.setLastUseDate(new Date());
		song.setFavorite(false);
		return song;
	}

	/**
	 * Save a song
	 * @param song the song to save
	 * @return the saved song
	 * @throws SoundLooperException if an error occured
	 */
	public Song validateSong(Song song) throws SoundLooperException {
		return SongDAO.getInstance().persist(song);

	}

	/**
	 * @return the list of files that are in favorites
	 */
	public ArrayList<Song> getFavoriteSongList() {
		return SongDAO.getInstance().getFavoriteSongList();
	}

	/**
	 * Delete all songs that are not favorites
	 */
	public void purgeSong() {
		ArrayList<Song> songList = SongDAO.getInstance().getList();
		for (Song song : songList) {
			if (!song.isFavorite()) {
				this.delete(song);
			}
		}
	}
}
