package com.soundlooper.service.entite.tag;

import java.util.Comparator;

import com.soundlooper.model.tag.Tag;

public final class TagComparator implements Comparator<Tag> {
	@Override
	public int compare(Tag o1, Tag o2) {
		return TagService.getInstance().getFullPath(o1)
				.compareTo(TagService.getInstance().getFullPath(o2));
	}
}