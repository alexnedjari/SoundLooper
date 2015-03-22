package com.soundlooper.service.entite.tag;

import java.util.ArrayList;
import java.util.List;

import com.soundlooper.model.mark.Mark;
import com.soundlooper.model.song.Song;
import com.soundlooper.model.tag.Tag;

/**
 *-------------------------------------------------------
 * Tags listeners managment
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
public class TagSupport {
	private static TagSupport instance;
	private List<TagListener> listTagListener = new ArrayList<TagListener>();
	
	
	public synchronized static TagSupport getInstance() {
		if (instance == null) {
			instance = new TagSupport();
		}
		return instance;
	}
	public void addToListTagListener(TagListener tagListener) {
		this.listTagListener.add(tagListener);
	}
	
	public void removeFromListMarkListener(TagListener tagListener) {
		this.listTagListener.remove(tagListener);
	}
	
	public void fireTagDeleted(Tag tag) {
		for (TagListener listener : this.listTagListener) {
			listener.onTagDeleted(tag);
		}
	}
	
	public void fireTagAdded(Tag tag, Tag parent) {
		for (TagListener listener : this.listTagListener) {
			listener.onTagAdded(tag, parent);
		}
	}

}
