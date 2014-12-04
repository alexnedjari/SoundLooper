/**
 *
 */
package com.soundlooper.service.uc.gererSongs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.soundlooper.exception.SoundLooperException;
import com.soundlooper.model.SoundLooperPlayerListener;
import com.soundlooper.model.SoundLooperPlayerSupport;
import com.soundlooper.model.song.Song;
import com.soundlooper.service.entite.song.SongService;
import com.soundlooper.service.entite.song.SongSupport;

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
 * Contains services to manages songs
 *
 * @author Alexandre NEDJARI
 * @since  5 sept. 2012
 *-------------------------------------------------------
 */
public class GererSongService {

	/**
	 * the instance
	 */
	private static GererSongService instance;

	/**
	 * the cache for the favorite song list
	 */
	private ArrayList<Song> cachedFavoriteSongList = null;

	/**
	 * flag to know is the cached favorite song list is out of date
	 */
	private boolean favoriteSongListMustBeUpdated = true;

	/**
	 * Logger for this class
	 */
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * Private constructor to avoid instanciation
	 */
	private GererSongService() {
		//
	}

	/**
	 * Get the instance
	 * @return the instance
	 */
	public static GererSongService getInstance() {
		if (GererSongService.instance == null) {
			GererSongService.instance = new GererSongService();
		}
		return GererSongService.instance;
	}
	
	public void deleteFavorite(Song song) throws SoundLooperException {
		setSongFavorite(song, false);
	}

	/**
	 * Delete all the song that are not favorite
	 */
	public void purgeSong() {
		SongService.getInstance().purgeSong();
	}

	/**
	 * Switch a song in the favorites
	 * @param song the file to add
	 * @throws SoundLooperException If a soundLooperException is threw
	 */
	public void switchSongToFavorite(Song song) throws SoundLooperException {
		this.favoriteSongListMustBeUpdated = true;
		setSongFavorite(song, !song.isFavorite());
	}
	
	public void setSongFavorite(Song song, boolean isFavorite) throws SoundLooperException {
		this.favoriteSongListMustBeUpdated = true;
		song.setFavorite(isFavorite);
		SongService.getInstance().validateSong(song);
		SongSupport.getInstance().fireFavoriteUpdated(song);
	}

	/**
	 * Get a favorite song by file
	 * @param file the file of the song
	 * @return the song
	 * @throws SoundLooperException If the sound is not found
	 */
	public Song getSong(File file) throws SoundLooperException {
		return SongService.getInstance().getSong(file);
	}

	/**
	 * @param file the file on wich song must be created
	 * @return the created song
	 */
	public Song createNewSong(File file) {
		return SongService.getInstance().createSong(file);
	}

	/**
	 * Get the list of songs that are favorite
	 * @return the list of songs that are favorite
	 */
	public List<Song> getFavoriteSongList() {
		if (this.favoriteSongListMustBeUpdated) {
			this.logger.info("Get favorite song list from database");
			this.cachedFavoriteSongList = SongService.getInstance().getFavoriteSongList();
			this.favoriteSongListMustBeUpdated = false;
		} else {
			this.logger.info("Get favorite song list from cache");
		}
		return this.cachedFavoriteSongList;
	}

	/**
	 *
	 * @return True if the cached favorite songs list is out of date
	 */
	public boolean isFavoriteSongListMustBeUpdated() {
		return this.favoriteSongListMustBeUpdated;
	}

}
