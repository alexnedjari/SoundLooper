/**
 *
 */
package com.soundlooper.model.tag;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import com.soundlooper.exception.SoundLooperObjectAlreadyExistsException;
import com.soundlooper.model.SoundLooperObject;
import com.soundlooper.model.mark.Mark;
import com.soundlooper.system.search.Searchable;

/**
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
 * Data that represents a song
 *
 * @author Alexandre NEDJARI
 * @since 28 juin 2011
 *
 *====================================================================
 */
public class Tag extends SoundLooperObject implements Searchable {

	/**
	 * List of marks for this song
	 */
	private HashMap<String, Mark> marks = new HashMap<String, Mark>();

	/**
	 * Favorite state of a song
	 */
	private boolean isFavorite;

	/**
	 * File for this song
	 */
	private File file;

	/**
	 * last use of this song
	 */
	private Date lastUseDate;

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
	 * @return true is the song is favorite
	 */
	public boolean isFavorite() {
		return this.isFavorite;
	}

	/**
	 * Change the favorite song flag
	 * @param isFavorite the favorite value
	 */
	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
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
	 * @param newFile the song file
	 */
	public void setFile(File newFile) {
		this.file = newFile;
	}

	/**
	 * Get the marks of the song
	 *
	 * @return the marks list of the song
	 */
	public HashMap<String, Mark> getMarks() {
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
	 * @param markToAdd the mark to add
	 * @throws SoundLooperObjectAlreadyExistsException if a mark with this name already exists
	 */
	public void addMark(Mark markToAdd) throws SoundLooperObjectAlreadyExistsException {
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
		return this.getFile().getAbsolutePath();
	}
}
