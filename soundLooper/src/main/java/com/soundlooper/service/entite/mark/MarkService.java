/**
 *
 */
package com.soundlooper.service.entite.mark;

import com.soundlooper.exception.SoundLooperException;
import com.soundlooper.model.mark.Mark;
import com.soundlooper.model.mark.MarkDAO;
import com.soundlooper.model.song.Song;

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
 * Services to manage marks
 *
 * @author Alexandre NEDJARI
 * @since  5 sept. 2012
 *-------------------------------------------------------
 */
public class MarkService {

	/**
	 * The instance
	 */
	private static MarkService instance;

	/**
	 * Private constructor to avoid instanciation
	 */
	private MarkService() {
		//
	}

	/**
	 * Get the instance
	 * @return the instance
	 */
	public static MarkService getInstance() {
		if (MarkService.instance == null) {
			MarkService.instance = new MarkService();
		}
		return MarkService.instance;
	}

	/**
	 * @param mark the mark to delete
	 * @return the deleted mark
	 * @throws SoundLooperException if there is an exception
	 */
	public Mark delete(Mark mark) throws SoundLooperException {
		return MarkDAO.getInstance().delete(mark);
	}

	/**
	 * Create mark
	 * @param name name of the mark
	 * @param song the containing song
	 * @param beginMillisecond the begin time of the mark
	 * @param endMillisecond the end time of the mark
	 * @return the created mark
	 * @throws SoundLooperException If an error occured
	 */
	public Mark createMark(Song song, String name, int beginMillisecond, int endMillisecond) throws SoundLooperException {
		Mark mark = MarkDAO.getInstance().createNew();
		mark.setBeginMillisecond(beginMillisecond);
		mark.setEndMillisecond(endMillisecond);
		mark.setSong(song);
		mark.setName(name);
		return this.validateMark(mark);
	}

	/**
	 * Save a mark
	 * @param mark the mark to save
	 * @return the saved mark
	 * @throws SoundLooperException if an error occured
	 */
	public Mark validateMark(Mark mark) throws SoundLooperException {
        mark = MarkDAO.getInstance().persist(mark);
        mark.setDirty(false);
        return mark;
	}
}
