package com.soundlooper.model.song;

import java.io.File;
import java.util.HashMap;

import com.soundlooper.exception.SoundLooperDatabaseException;
import com.soundlooper.exception.SoundLooperException;
import com.soundlooper.exception.SoundLooperObjectAlreadyExistsException;

/**
 *
 * ====================================================================
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
 * Factory for the songs
 *
 * @author Alexandre NEDJARI
 * @since 1 juil. 2011
 *
 *====================================================================
 */
@Deprecated
public final class SongFactory {

	/**
	 * Private constructor
	 */
	private SongFactory() {
		//to avoid construction
	}

	/**
	 * List of the saved songs by path
	 */
	private static HashMap<File, Song> savedSongsByPath;

	/**
	 * list of the songs open in this session but not saved
	 */
	private static HashMap<File, Song> nonSavedSongsByPath = new HashMap<File, Song>();

	/**
	 * get a song from his path
	 *
	 * @param filePath
	 *            the file path
	 * @return the saved song if exists, the cached song, or create a non saved
	 *         song
	 * @throws SoundLooperDatabaseException
	 *             if a {@link SoundLooperDatabaseException} is threw
	 */
	public static Song getSong(File filePath) throws SoundLooperDatabaseException {
		if (SongFactory.getSavedSongs().containsKey(filePath)) {
			return SongFactory.getSavedSongs().get(filePath);
		}
		if (SongFactory.nonSavedSongsByPath.containsKey(filePath)) {
			return SongFactory.nonSavedSongsByPath.get(filePath);
		}
		Song song = null; //new Song(filePath); //TODO restaurer
		SongFactory.nonSavedSongsByPath.put(filePath, song);
		return song;

	}

	/**
	 * Get saved song lists
	 *
	 * @return the saved song list
	 * @throws SoundLooperDatabaseException
	 *             if a {@link SoundLooperDatabaseException} is threw
	 */
	private static HashMap<File, Song> getSavedSongs() throws SoundLooperDatabaseException {
		//		if (savedSongsByPath == null) {
		//			try {
		//				ArrayList<Song> songsList = SongDatabaseAccess.getInstance().getSongsList();
		//				savedSongsByPath = new HashMap<File, Song>();
		//				for (Song song : songsList) {
		//					savedSongsByPath.put(song.getFile(), song);
		//				}
		//			} catch (SQLException e) {
		//				throw new SoundLooperDatabaseException("Unable to get the saved song list", e);
		//			}
		//		}
		//		return savedSongsByPath;
		return null; //TODO restaurer
	}

	/**
	 * Update the map when a song path is changed
	 * @param song the song to change
	 * @param newFile the new File
	 * @throws SoundLooperException If the maps cannot be changed
	 */
	public static void changeSongPath(Song song, File newFile) throws SoundLooperException {
		if (SongFactory.getSavedSongs().containsKey(newFile) || SongFactory.nonSavedSongsByPath.containsKey(newFile)) {
			throw new SoundLooperObjectAlreadyExistsException("Duplicate song on file '" + song.getFile() + "'", null, song);
		}
		if (SongFactory.savedSongsByPath.containsKey(song.getFile())) {
			SongFactory.savedSongsByPath.remove(song.getFile());
			SongFactory.savedSongsByPath.put(newFile, song);
		} else if (SongFactory.nonSavedSongsByPath.containsKey(song.getFile())) {
			SongFactory.nonSavedSongsByPath.remove(song.getFile());
			SongFactory.nonSavedSongsByPath.put(newFile, song);
		} else {
			throw new SoundLooperException("Unable to get song for path change : " + song.getDescription());
		}
	}
}
