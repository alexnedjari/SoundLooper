/**
 *
 */
package com.soundlooper.model.database;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.soundlooper.exception.SoundLooperDatabaseException;
import com.soundlooper.exception.SoundLooperException;
import com.soundlooper.model.SoundLooperObject;

/**
 * -------------------------------------------------------
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
 *
 * @author Alexandre NEDJARI
 * @param <T> the SoundLooperObjectType
 * @since 5 sept. 2012
 * -------------------------------------------------------
 */
public abstract class SoundLooperDAO<T extends SoundLooperObject> {

	/**
	 * Logger for this class
	 */
	protected static Logger logger = Logger.getLogger(SoundLooperDAO.class);

	/**
	 * persist an object
	 * @param object the object to persist
	 * @throws SoundLooperException If an error occured
	 * @return the persisted object
	 */
	public T persist(T object) throws SoundLooperException {
		if (object.getId() == SoundLooperObject.ID_NOT_INITIALIZED) {
			this.insert(object);
		} else {
			this.update(object);
		}
        object.setDirty(false);
		return object;
	}

	/**
	 * Get a date formated for query insert
	 *
	 * @param date
	 *            the date to format
	 * @return the formated date
	 */
	protected static String getFormatedDate(java.util.Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	/**
	 * Convert a java.util.date in java.sql.date
	 * @param date the date to convert
	 * @return the converted date
	 */
	protected static java.sql.Date getSqlDate(java.util.Date date) {
		return new java.sql.Date(date.getTime());
	}

	/**
	 * Get a boolean from int value
	 * 0 = false
	 * 1 = true
	 * @param numericValue the numeric value
	 * @return the boolean value
	 * @throws SoundLooperDatabaseException if the value is not a valid boolean value
	 */
	protected boolean getBoolean(long numericValue) throws SoundLooperDatabaseException {
		if (numericValue == 0) {
			return false;
		} else if (numericValue == 1) {
			return true;
		}
		throw new SoundLooperDatabaseException("La valeur '" + numericValue + "' n'est pas une valeur booléenne valide");
	}

	/**
	 * Insert a new Object
	 *
	 * @param object
	 *            the object to insert
	 * @throws SoundLooperException if an error occured
	 */
	protected abstract void insert(T object) throws SoundLooperException;

	/**
	 * Update an existing object
	 *
	 * @param object
	 *            the object
	 * @throws SoundLooperException If an error occured
	 */
	protected abstract void update(T object) throws SoundLooperException;

	/**
	 * Rollback the current transaction
	 */
	protected void rollbackCurrentTransaction() {
		try {
			ConnectionFactory.rollback();
		} catch (SQLException e) {
			SoundLooperDAO.logger.error("Cannot rollback transaction : ", e);
		}
	}

	/**
	 * @param booleanValue the boolean value
	 * @return 0 or 1
	 */
	protected String getNumericValue(boolean booleanValue) {
		if (booleanValue) {
			return "1";
		}
		return "0";
	}

	/**
	 * Get a object by id
	 * @param id the id object
	 * @return the object
	 * @throws SoundLooperException if an error occured
	 */
	public abstract T getById(long id) throws SoundLooperException;

	/**
	 * Create a new objec instance
	 *
	 * @return the new instance
	 */
	public abstract T createNew();

	/**
	 * Delete a object in database
	 *
	 * @param object
	 *            the object to delete (ID is used for delete)
	 * @throws SoundLooperException
	 *             if the ID is not persisted, or no row is deleted
	 * @return the persisted object
	 */
	public abstract T delete(T object) throws SoundLooperException;

	/**
	 * Get the list of all the database object
	 *
	 * @return the list of readed objects
	 * @throws SoundLooperException If a {@link SoundLooperException} is threw
	 */
	public abstract ArrayList<T> getList() throws SoundLooperException;
}
