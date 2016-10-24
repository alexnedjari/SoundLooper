/**
 *
 */
package com.soundlooper.model.song;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.soundlooper.exception.SoundLooperObjectAlreadyExistsException;
import com.soundlooper.model.SoundLooperObject;
import com.soundlooper.model.mark.Mark;
import com.soundlooper.model.tag.Tag;
import com.soundlooper.service.entite.song.SongService;
import com.soundlooper.service.entite.tag.TagService;
import com.soundlooper.system.search.Searchable;

/**
 * ==================================================================== Sound
 * Looper is an audio player that allow user to loop between two points
 * Copyright (C) 2014 Alexandre NEDJARI
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
 * Data that represents a song
 *
 * @author Alexandre NEDJARI
 * @since 28 juin 2011
 *
 *        ====================================================================
 */
public class Song extends SoundLooperObject implements Searchable {

	private static SimpleListProperty<Song> favoriteList = new SimpleListProperty<>(
			FXCollections.observableArrayList());

	static {
		favoriteList.addAll(SongService.getInstance().getFavoriteSongList());
	}

	/**
	 * List of marks for this song
	 */
	// private HashMap<String, Mark> marks = new HashMap<String, Mark>();

	private SimpleMapProperty<String, Mark> marks = new SimpleMapProperty<>(
			FXCollections.observableHashMap());

	/**
	 * List of this song's tags
	 * 
	 */
	private SimpleListProperty<Tag> tagList = new SimpleListProperty<>(
			FXCollections.observableArrayList());

	/**
	 * Favorite state of a song
	 */
	private SimpleBooleanProperty isFavorite = new SimpleBooleanProperty(true);

	/**
	 * File for this song
	 */
	private File file;

	/**
	 * last use of this song
	 */
	private Date lastUseDate;

	public Song() {
		isFavorite.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				SongDAO.getInstance().persist(Song.this);
				if (Song.this.isFavorite()) {
					favoriteList.add(Song.this);
					if (getTagList().isEmpty()) {
						TagService.getInstance().addTagToSong(Song.this,
								Tag.getRoot());
					}
				} else {
					favoriteList.remove(Song.this);
				}
			}
		});

	}

	public SimpleListProperty<Tag> tagListProperty() {
		return tagList;

	}

	public static SimpleListProperty<Song> favoriteListProperty() {
		return favoriteList;
	}

	/**
	 * get the last use date of this song
	 *
	 * @return the last use date of this song
	 */
	public Date getLastUseDate() {
		return this.lastUseDate;
	}

	/**
	 * Is the song favorite?
	 * 
	 * @return true is the song is favorite
	 */
	public boolean isFavorite() {
		return this.isFavorite.get();
	}

	/**
	 * Change the favorite song flag
	 * 
	 * @param isFavorite
	 *            the favorite value
	 */
	public void setFavorite(boolean isFavorite) {
		this.isFavorite.set(isFavorite);
	}

	public SimpleBooleanProperty isFavoriteProperty() {
		return isFavorite;
	}

	/**
	 * Set the last use date of this song
	 *
	 * @param newLastUseDate
	 *            the last use date of this song
	 */
	public void setLastUseDate(Date newLastUseDate) {
		this.lastUseDate = newLastUseDate;
	}

	/**
	 * Set the song file
	 *
	 * @param newFile
	 *            the song file
	 */
	public void setFile(File newFile) {
		this.file = newFile;
	}

	/**
	 * Get the marks of the song
	 *
	 * @return the marks list of the song
	 */
	public Map<String, Mark> getMarks() {
		return this.marks.get();
	}

	public MapProperty<String, Mark> marksProperty() {
		return this.marks;
	}

	/**
	 * Get the file of the song
	 *
	 * @return the file of the song
	 */
	public File getFile() {
		return this.file;
	}

	/**
	 * Add a mark to the sound
	 *
	 * @param markToAdd
	 *            the mark to add
	 * @throws SoundLooperObjectAlreadyExistsException
	 *             if a mark with this name already exists
	 */
	public void addMark(Mark markToAdd)
			throws SoundLooperObjectAlreadyExistsException {
		if (this.marks.containsKey(markToAdd.getName())) {
			throw new SoundLooperObjectAlreadyExistsException(this, markToAdd);
		}
		this.marks.put(markToAdd.getName(), markToAdd);
	}

	@Override
	public String getDescription() {
		String description = this.file + " (" + this.id + ") with Marks : \n";
		for (String markName : this.marks.keySet()) {
			description += this.marks.get(markName).getDescription();
		}
		return description;
	}

	@Override
	public String getSearchableString() {
		return this.getFile().getAbsolutePath();
	}

	@Override
	public String toString() {
		return "Song [marks=" + marks + ", isFavorite=" + isFavorite
				+ ", file=" + file + ", lastUseDate=" + lastUseDate + ", id="
				+ id + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Song other = (Song) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		return true;
	}

	public List<Tag> getTagList() {
		return tagList.get();
	}

	public void setTag(ObservableList<Tag> tagList) {
		this.tagList.set(tagList);
	}

	public static ObservableList<Song> getFavoriteList() {
		return favoriteList.get();
	}

}
