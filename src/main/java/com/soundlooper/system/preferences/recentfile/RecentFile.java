/**
 *
 */
package com.soundlooper.system.preferences.recentfile;

import java.io.File;
import java.util.Date;

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
 * Represent a recent file object
 *
 * @author Alexandre NEDJARI
 * @since  19 juin 2012
 *-------------------------------------------------------
 */
public class RecentFile {
	/**
	 * The file
	 */
	private File file;

	/**
	 * The last access date
	 */
	private Date lastAccessDate;

	/**
	 * Constructor
	 * @param file The file
	 * @param lastAccessDate The last access date
	 */
	public RecentFile(File file, Date lastAccessDate) {
		super();
		this.file = file;
		this.lastAccessDate = lastAccessDate;
	}

	/**
	 * Get the file
	 * @return the file
	 */
	public File getFile() {
		return this.file;
	}

	/**
	 * Set the file
	 * @param file the new File
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Return the last access date
	 * @return the last access date
	 */
	public Date getLastAccessDate() {
		return this.lastAccessDate;
	}

	/**
	 * Set the last access date
	 * @param lastAccessDate the last Access date
	 */
	public void setLastAccessDate(Date lastAccessDate) {
		this.lastAccessDate = lastAccessDate;
	}
}
