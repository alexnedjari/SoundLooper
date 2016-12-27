/**
 *
 */
package com.soundlooper.exception;

import com.soundlooper.model.SoundLooperObject;

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
 * Threw when try to duplicate an object
 *
 * @author Alexandre NEDJARI
 * @since 28 juin 2011
 *
 *        ====================================================================
 */
public class SoundLooperRecordNotFoundException extends
		SoundLooperDatabaseException {

	/**
	 * Serial number
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 *
	 * @param searchedObject
	 *            the searched object
	 * @param conditionsMessage
	 *            message explaining conditions
	 */
	public SoundLooperRecordNotFoundException(String searchedObject,
			String conditionsMessage) {
		super("L'objet " + searchedObject + " n'a pas été trouvé : "
				+ conditionsMessage);
	}

	/**
	 * Constructor
	 *
	 * @param message
	 *            the complement message
	 * @param container
	 *            the container object
	 * @param objectToAdd
	 *            the object to add
	 */
	public SoundLooperRecordNotFoundException(String message,
			SoundLooperObject container, SoundLooperObject objectToAdd) {
		super("Object "
				+ SoundLooperRecordNotFoundException
						.getContainerDescription(objectToAdd)
				+ " is already in "
				+ SoundLooperRecordNotFoundException
						.getContainerDescription(container) + " : \n" + message);
	}

	/**
	 * Get the description of the container, it can be null
	 *
	 * @param container
	 *            the container
	 * @return the description
	 */
	private static String getContainerDescription(SoundLooperObject container) {
		String description = "null container";
		if (container != null) {
			description = container.getDescription();
		}
		return description;
	}
}
