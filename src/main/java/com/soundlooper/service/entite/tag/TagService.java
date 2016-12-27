/**
 *
 */
package com.soundlooper.service.entite.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;

import com.soundlooper.exception.SoundLooperException;
import com.soundlooper.model.song.Song;
import com.soundlooper.model.tag.Tag;
import com.soundlooper.model.tag.TagDAO;
import com.soundlooper.system.MessageReader;

/**
 * ------------------------------------------------------- Sound Looper is an
 * audio player that allow user to loop between two points Copyright (C) 2014
 * Alexandre NEDJARI
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
 * Services to manage marks
 *
 * @author Alexandre NEDJARI
 * @since 5 sept. 2012 -------------------------------------------------------
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
	 * 
	 * @return the instance
	 */
	public static TagService getInstance() {
		if (TagService.instance == null) {
			TagService.instance = new TagService();
		}
		return TagService.instance;
	}

	/**
	 * @param tag
	 *            the tag to delete
	 * @return the deleted tag
	 * @throws SoundLooperException
	 *             if there is an exception
	 */
	public Tag delete(Tag tag) throws SoundLooperException {
		Tag result = TagDAO.getInstance().delete(tag);
		return result;
	}

	/**
	 * Create tag
	 * 
	 * @param name
	 *            name of the tag
	 * @return the created tag
	 * @throws SoundLooperException
	 *             If an error occured
	 */
	public Tag createTag(String name, Tag parent) throws SoundLooperException {
		Tag tag = TagDAO.getInstance().createNew();
		tag.setName(name);
		tag.setParent(parent);
		tag.setRoot(false);

		Tag result = this.validateTag(tag);
		return result;
	}

	/**
	 * Save a tag
	 * 
	 * @param tag
	 *            the tag to save
	 * @return the saved tag
	 * @throws SoundLooperException
	 *             if an error occured
	 */
	public Tag validateTag(Tag tag) throws SoundLooperException {
		tag = TagDAO.getInstance().persist(tag);
		return tag;
	}

	public Tag getTagTree() {
		return TagDAO.getInstance().getTagTree();
	}

	/**
	 * Delete tag and childs recursively
	 * 
	 * @param tag
	 *            the tag
	 */
	public void deleteTag(Tag tag) {
		// Check the attached songs
		ObservableList<Song> favoriteList = Song.getFavoriteList();
		for (Song song : favoriteList) {
			List<Tag> tagList = song.getTagList();
			List<Tag> tagListToDetach = new ArrayList<Tag>();
			for (Tag tagToCheck : tagList) {
				if (tagToCheck.getId() == tag.getId()) {
					// The song is on the deleted tag, we must detach it
					tagListToDetach.add(tagToCheck);
				}
			}
			for (Tag tagToDetach : tagListToDetach) {
				removeTagFromSong(song, tagToDetach);
			}
		}

		// Delete the tag and the childrens
		tag.setParent(null);
		List<Tag> listChildrenCopy = tag.getListChildrenCopy();
		for (Tag children : listChildrenCopy) {
			deleteTag(children);
		}
		TagDAO.getInstance().delete(tag);

		// TODO détacher les chansons et rattacher éventuellement les chansons
		// orphelines à la racine
	}

	public void moveTag(Tag movedTag, Tag destinationTag) {
		movedTag.setParent(destinationTag);
		TagDAO.getInstance().persist(movedTag);
	}

	public void addTagToSong(Song song, Tag newTag) {
		if (!song.getTagList().contains(newTag)) {
			TagDAO.getInstance().saveTagOnSong(song, newTag);
			song.getTagList().add(newTag);
			song.getTagList().sort(new TagComparator());
		}
	}

	public void removeTagFromSong(Song song, Tag tagToRemove) {
		TagDAO.getInstance().removeTagFromSong(song, tagToRemove);
		song.getTagList().remove(tagToRemove);
		if (song.getTagList().size() == 0) {
			addTagToSong(song, Tag.getRoot());
		}
	}

	public String getFullPath(Tag tag) {
		if (tag.getParent() == null) {
			return MessageReader.getInstance().getMessage("tag.root");
		}
		return getFullPath(tag.getParent()) + "/" + tag.getName();
	}

	public Map<Long, Tag> getTagByIdMap() {
		Tag root = Tag.getRoot();
		Map<Long, Tag> mapTagById = new HashMap<>();

		populateMapTagById(root, mapTagById);
		return mapTagById;
	}

	private void populateMapTagById(Tag tag, Map<Long, Tag> mapTagById) {
		mapTagById.put(tag.getId(), tag);
		List<Tag> listChildrenCopy = tag.getListChildrenCopy();
		for (Tag children : listChildrenCopy) {
			populateMapTagById(children, mapTagById);
		}
	}

}
