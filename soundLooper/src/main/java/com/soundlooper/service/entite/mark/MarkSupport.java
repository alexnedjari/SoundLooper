package com.soundlooper.service.entite.mark;

import java.util.ArrayList;
import java.util.List;

import com.soundlooper.model.mark.Mark;
import com.soundlooper.model.song.Song;

/**
 *-------------------------------------------------------
 * Marks listeners managment
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
 * @author Alexandre NEDJARI
 * @since  02 jan. 2015
 *-------------------------------------------------------
 */
public class MarkSupport {
	private static MarkSupport instance;
	private List<MarkListener> listMarkListener = new ArrayList<MarkListener>();
	
	
	public synchronized static MarkSupport getInstance() {
		if (instance == null) {
			instance = new MarkSupport();
		}
		return instance;
	}
	public void addToListMarkListener(MarkListener markListener) {
		this.listMarkListener.add(markListener);
	}
	
	public void removeFromListMarkListener(MarkListener markListener) {
		this.listMarkListener.remove(markListener);
	}
	
	public void fireMarkDeleted(Song song, Mark mark,long idMark) {
		for (MarkListener listener : this.listMarkListener) {
			listener.onMarkDeleted(song, mark, idMark);
		}
	}
	
	public void fireMarkAdded(Song song, Mark mark) {
		for (MarkListener listener : this.listMarkListener) {
			listener.onMarkAdded(song, mark);
		}
	}
	
    public void fireDirtyChanged(Mark mark) {
        for (MarkListener listener : this.listMarkListener) {
            listener.onDirtyChanged(mark);
        }
        
    }
    public void fireMarkLoaded(Mark mark) {
        for (MarkListener listener : this.listMarkListener) {
            listener.onMarkLoaded(mark);
        }
    }

}
