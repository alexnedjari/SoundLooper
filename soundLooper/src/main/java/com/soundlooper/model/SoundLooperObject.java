/**
 *
 */
package com.soundlooper.model;

import com.soundlooper.exception.SoundLooperDatabaseException;

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
 * Parent of all datas of soundLooper
 *
 * @author Alexandre NEDJARI
 * @since 28 juin 2011
 *
 *====================================================================
 */
public abstract class SoundLooperObject {

	/**
	 * Default ID of any persistable sound looper object
	 */
	public static final int ID_NOT_INITIALIZED = -1;

	/**
	 * Database id
	 */
	protected long id;
    
    /**
     * dirty state
     */
    private boolean dirty;


	/**
	 * Constructor
	 */
	public SoundLooperObject() {
		super();
		this.id = SoundLooperObject.ID_NOT_INITIALIZED;
	}

	/**
	 * Return a description of the object
	 *
	 * @return the description
	 */
	public abstract String getDescription();
	
    /**
     * Get the dirty state
     * @return the dirty state
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * Set the dirty state
     * @param dirty the new state
     */
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
        this.fireDirtyChanged();
    }

    /**
     * Child class can override this to fire the support class
     */
    protected void fireDirtyChanged() {
        //Default, do nothing
    }


	/**
	 * Get the ID
	 *
	 * @return the database ID
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * Set the object ID
	 * @param newId the object ID
	 * @throws SoundLooperDatabaseException if a {@link SoundLooperDatabaseException} is threw
	 */
	public void setId(long newId) throws SoundLooperDatabaseException {
		if (this.id != SoundLooperObject.ID_NOT_INITIALIZED && newId != SoundLooperObject.ID_NOT_INITIALIZED) {
			throw new SoundLooperDatabaseException("The id is already setted");
		}
		this.id = newId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SoundLooperObject other = (SoundLooperObject) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
