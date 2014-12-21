/**
 *
 */
package com.soundlooper.model.mark;

import com.soundlooper.exception.SoundLooperObjectAlreadyExistsException;
import com.soundlooper.model.SoundLooperObject;
import com.soundlooper.model.song.Song;
import com.soundlooper.service.entite.mark.MarkSupport;
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
 * Data representing a saved mark on a song
 *
 * @author Alexandre NEDJARI
 * @since 28 juin 2011
 *
 *====================================================================
 */
public class Mark extends SoundLooperObject implements Searchable {

	/**
	 * The mark name
	 */
	private String name;

	/**
	 * The mark begin in the song
	 */
	private int beginMillisecond;

	/**
	 * The mark end in the song
	 */
	private int endMillisecond;

	/**
	 * The containing song
	 */
	private Song song;


	/**
	 * Create a new Mark
	 */
	public Mark() {

	}

	public void setSong(Song song) {
		this.song = song;
        setDirty(true);
	}
	
    @Override
    protected void fireDirtyChanged() {
        MarkSupport.getInstance().fireDirtyChanged(this);
    }


	/**
	 * Get the begin time of the mark
	 *
	 * @return the begin time of the mark
	 */
	public int getBeginMillisecond() {
		return this.beginMillisecond;
	}

	/**
	 * Set the begin time of the mark
	 *
	 * @param newBeginMillisecond
	 *            the begin time of the mark
	 */
	public void setBeginMillisecond(int newBeginMillisecond) {
		this.beginMillisecond = newBeginMillisecond;
        setDirty(true);

	}

	/**
	 * Set the end time of the mark
	 *
	 * @return the end time of the mark
	 */
	public int getEndMillisecond() {
		return this.endMillisecond;
	}

	/**
	 * Set the end time of the mark
	 *
	 * @param newEndMillisecond
	 *            the end time of the mark
	 */
	public void setEndMillisecond(int newEndMillisecond) {
		this.endMillisecond = newEndMillisecond;
        setDirty(true);

	}

	@Override
	public String getDescription() {
		return "Mark : (" + this.id + ") " + this.name + " from " + this.beginMillisecond + " to " + this.endMillisecond;
	}

	/**
	 * Get the name of the mark
	 *
	 * @return the name of the mark
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the containing song
	 *
	 * @return the containing song
	 */
	public Song getSong() {
		return this.song;
	}

	/**
	 * Set a new name for this mark
	 *
	 * @param newName the new name
	 * @throws SoundLooperObjectAlreadyExistsException if an object with this name already exists
	 */
	public void setName(String newName) throws SoundLooperObjectAlreadyExistsException {
		if (!newName.equals(this.name)) {
			this.getSong().getMarks().remove(this.name);
			this.name = newName;
			this.getSong().addMark(this);
	        setDirty(true);
		}
	}

	@Override
	public String getSearchableString() {
		return this.getName();
	}

	@Override
	public String toString() {
		return this.getName();
	}
	
	@Override
	public Mark clone() {
		Mark clone = new Mark();
		clone.name = name;
		clone.setBeginMillisecond(beginMillisecond);
		clone.setDirty(isDirty());
		clone.setEndMillisecond(endMillisecond);
		clone.setId(id);
		clone.setSong(song);
		return clone;
	} 

}
