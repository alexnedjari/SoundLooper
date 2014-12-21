/**
 *
 */
package com.soundlooper.service.uc.gererMarks;

import com.soundlooper.exception.SoundLooperException;
import com.soundlooper.model.mark.Mark;
import com.soundlooper.model.song.Song;
import com.soundlooper.service.entite.mark.MarkService;

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
 * Contains services to manages marks
 *
 * @author Alexandre NEDJARI
 * @since  5 sept. 2012
 *-------------------------------------------------------
 */
public class GererMarkService {

	/**
	 * the instance
	 */
	private static GererMarkService instance;

	/**
	 * Private constructor to avoid instanciation
	 */
	private GererMarkService() {
		//
	}

	/**
	 * Get the instance
	 * @return the instance
	 */
	public static GererMarkService getInstance() {
		if (GererMarkService.instance == null) {
			GererMarkService.instance = new GererMarkService();
		}
		return GererMarkService.instance;
	}

	/**
	 * Create mark
	 * @param name name of the mark
	 * @param song the containing song
	 * @param beginMillisecond the begin time of the mark
	 * @param endMillisecond the end time of the mark
	 * @return the created mark
	 * @throws SoundLooperException If an exception is threw
	 */
	public Mark createNewMark(Song song, String name, int beginMillisecond, int endMillisecond) throws SoundLooperException {
		return MarkService.getInstance().createMark(song, name, beginMillisecond, endMillisecond);
	}

	/**
	 * @param mark the mark to delete
	 * @return the deleted mark
	 * @throws SoundLooperException if there is an exception
	 */
	public Mark delete(Mark mark) throws SoundLooperException {
		Mark delete = MarkService.getInstance().delete(mark);
		
		return delete;
	}
	
    /**
     * Save a mark
     * @param mark the mark to save
     * @throws SoundLooperException 
     */
    public void saveMark(Mark mark) throws SoundLooperException {
        MarkService.getInstance().validateMark(mark);
        
    }

}
