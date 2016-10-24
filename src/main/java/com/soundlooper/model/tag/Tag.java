/**
 *
 */
package com.soundlooper.model.tag;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleObjectProperty;

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
 * Data that represents a song
 *
 * @author Alexandre NEDJARI
 * @since 28 juin 2011
 *
 *        ====================================================================
 */
public class Tag extends SoundLooperObject {

	public static final Long ROOT_TAG_ID = 0l;

	private static SimpleObjectProperty<Tag> root = new SimpleObjectProperty<>();

	static {
		root.set(TagDAO.getInstance().getTagTree());
	}

	/**
	 * Name of the tag
	 */
	private String name;

	/**
	 * Parent tag
	 */
	private Tag parent;

	/**
	 * Is this tag is the root tag?
	 */
	private boolean isRoot = false;

	/**
	 * List of the child tags
	 */
	private List<Tag> listChildren = new ArrayList<Tag>();

	public Tag() {
		super();
	}

	public Tag(String name) {
		super();
		this.name = name;
	}

	@Override
	public String getDescription() {
		return "Tag '" + name + "'";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addChildren(Tag child) {
		this.listChildren.add(child);
		// child.setParent(this);
	}

	public void removeChildren(Tag child) {
		this.listChildren.remove(child);
		// child.setParent(null);
	}

	public Tag getParent() {
		return parent;
	}

	public void setParent(Tag parent) {
		if (this.parent != null) {
			this.parent.removeChildren(this);

		}
		this.parent = parent;
		if (this.parent != null) {
			this.parent.addChildren(this);
		}
	}

	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	public List<Tag> getListChildrenCopy() {
		return new ArrayList<Tag>(this.listChildren);
	}

	@Override
	public String toString() {
		return "Tag [name=" + name + "]";
	}

	public static Tag getRoot() {
		return root.get();
	}
}
