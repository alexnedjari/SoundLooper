/**
 *
 */
package com.soundlooper.service.entite.tag;

import java.util.List;

import com.soundlooper.exception.SoundLooperException;
import com.soundlooper.model.tag.Tag;
import com.soundlooper.model.tag.TagDAO;

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
public class TagService {

	/**
	 * The instance
	 */
	private static TagService instance;

	/**
	 * Private constructor to avoid instanciation
	 */
	private TagService() {
		//
	}

	/**
	 * Get the instance
	 * @return the instance
	 */
	public static TagService getInstance() {
		if (TagService.instance == null) {
			TagService.instance = new TagService();
		}
		return TagService.instance;
	}

	/**
	 * @param tag the tag to delete
	 * @return the deleted tag
	 * @throws SoundLooperException if there is an exception
	 */
	public Tag delete(Tag tag) throws SoundLooperException {
		Tag result = TagDAO.getInstance().delete(tag);
		TagSupport.getInstance().fireTagDeleted(tag);
		return result;
	}

	/**
	 * Create tag
	 * @param name name of the tag
	 * @return the created tag
	 * @throws SoundLooperException If an error occured
	 */
	public Tag createTag(String name, Tag parent) throws SoundLooperException {
		
		
		Tag tag = TagDAO.getInstance().createNew();
		tag.setName(name);
		tag.setParent(parent);
		tag.setRoot(false);
		
		Tag result = this.validateTag(tag);
		TagSupport.getInstance().fireTagAdded(tag, parent);
		return result;
	}

	/**
	 * Save a tag
	 * @param tag the tag to save
	 * @return the saved tag
	 * @throws SoundLooperException if an error occured
	 */
	public Tag validateTag(Tag tag) throws SoundLooperException {
		tag = TagDAO.getInstance().persist(tag);
        return tag;
	}

	public List<Tag> getTagList() {
		return TagDAO.getInstance().getList();
	}
	
}
