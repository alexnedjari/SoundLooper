/**
 *
 */
package com.soundlooper.service.entite.song;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;

import com.soundlooper.exception.SoundLooperException;
import com.soundlooper.exception.SoundLooperRecordNotFoundException;
import com.soundlooper.model.song.Song;
import com.soundlooper.model.song.SongDAO;
import com.soundlooper.model.tag.Tag;
import com.soundlooper.model.tag.TagDAO;
import com.soundlooper.service.entite.tag.TagComparator;
import com.soundlooper.service.entite.tag.TagService;

/**
 * ------------------------------------------------------- Sound Looper is an
 * audio player that allow user to loop between two points Copyright (C) 2014
 * Alexandre NEDJARI
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
 * Services to manage songs
 *
 * @author Alexandre NEDJARI
 * @since 5 sept. 2012 -------------------------------------------------------
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
	 * 
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
	 * 
	 * @param file
	 *            the file
	 * @return the song
	 * @throws SoundLooperException
	 *             If the song is not found
	 */
	public Song getSong(File file) throws SoundLooperRecordNotFoundException {
		return SongDAO.getInstance().getByFile(file);
	}

	public Song getOrCreateSong(File file) throws SoundLooperException {
		Song song = getSong(file);
		if (song != null) {
			return song;
		}
		return createSong(file);
	}

	/**
	 * @param song
	 *            the song to delete
	 * @return the deleted song
	 */
	public Song delete(Song song) {
		return SongDAO.getInstance().delete(song);
	}

	/**
	 * Create song
	 * 
	 * @param fileToAdd
	 *            the file on wich song must be created
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
	 * 
	 * @param song
	 *            the song to save
	 * @return the saved song
	 * @throws SoundLooperException
	 *             if an error occured
	 */
	public Song validateSong(Song song) throws SoundLooperException {
		return SongDAO.getInstance().persist(song);
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

	/**
	 * Get the list of songs that are favorite
	 * 
	 * @return the list of songs that are favorite
	 */
	public List<Song> getFavoriteSongList() {
		ArrayList<Song> favoriteSongList = SongDAO.getInstance().getFavoriteSongList();
		Map<Long, Tag> tagByIdMap = TagService.getInstance().getTagByIdMap();

		for (Song song : favoriteSongList) {
			// Initialisation / chargement des tags
			List<Tag> tagList = TagDAO.getInstance().getSongTagList(song);
			if (tagList.isEmpty()) {
				// there is no tag, we must add the root tag
				TagService.getInstance().addTagToSong(song, tagByIdMap.get(Tag.ROOT_TAG_ID));
			} else {
				for (Tag tag : tagList) {
					Tag tagInMap = tagByIdMap.get(tag.getId());
					if (tagInMap == null) {
						// tag was not found in the tag map, we can broke the
						// link with the song
						TagDAO.getInstance().detachAllSongs(tag);
					} else {
						song.getTagList().add(tagInMap);
					}
				}
			}
			song.getTagList().sort(new TagComparator());
		}
		return favoriteSongList;
	}

	public ObservableList<Song> getSortedByNameFavoriteSongList() {
		Song.getFavoriteList().sort(new Comparator<Song>() {
			@Override
			public int compare(Song o1, Song o2) {
				return o1.getFile().getName().compareTo(o2.getFile().getName());
			}
		});
		return Song.getFavoriteList();
	}
}
