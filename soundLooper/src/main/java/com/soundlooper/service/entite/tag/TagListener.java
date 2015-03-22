package com.soundlooper.service.entite.tag;

import com.soundlooper.model.mark.Mark;
import com.soundlooper.model.song.Song;
import com.soundlooper.model.tag.Tag;

/**
 *-------------------------------------------------------
 * Tag listeners
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
public interface TagListener {

	void onTagDeleted(Tag tag);

	void onTagAdded(Tag addedTag, Tag parent);
	
}