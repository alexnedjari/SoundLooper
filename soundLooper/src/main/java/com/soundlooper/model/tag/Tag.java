/**
 *
 */
package com.soundlooper.model.tag;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.soundlooper.exception.SoundLooperObjectAlreadyExistsException;
import com.soundlooper.model.SoundLooperObject;
import com.soundlooper.model.mark.Mark;
import com.soundlooper.system.search.Searchable;

/**
 * ====================================================================
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
 * Data that represents a song
 *
 * @author Alexandre NEDJARI
 * @since 28 juin 2011
 *
 *====================================================================
 */
public class Tag extends SoundLooperObject {
	
	

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
		child.setParent(this);
	}
	
	public void removeChildren(Tag child) {
		this.listChildren.remove(child);
		child.setParent(null);
	}

	public Tag getParent() {
		return parent;
	}

	public void setParent(Tag parent) {
		this.parent = parent;
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

	
}
