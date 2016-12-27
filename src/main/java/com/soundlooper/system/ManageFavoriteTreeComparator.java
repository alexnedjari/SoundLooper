package com.soundlooper.system;

import java.util.Comparator;

import javafx.scene.control.TreeItem;

import com.soundlooper.model.SoundLooperObject;
import com.soundlooper.model.song.Song;
import com.soundlooper.model.tag.Tag;

public class ManageFavoriteTreeComparator implements Comparator<TreeItem<Tag>> {
	@Override
	public int compare(TreeItem<Tag> o1, TreeItem<Tag> o2) {
		SoundLooperObject soundLooperObject1 = o1.getValue();
		SoundLooperObject soundLooperObject2 = o2.getValue();
		if (soundLooperObject1 instanceof Song
				&& soundLooperObject2 instanceof Song) {
			Song song1 = (Song) soundLooperObject1;
			Song song2 = (Song) soundLooperObject2;
			return song1.getFile().getName()
					.compareTo(song2.getFile().getName());
		}
		if (soundLooperObject1 instanceof Tag
				&& soundLooperObject2 instanceof Tag) {
			Tag song1 = (Tag) soundLooperObject1;
			Tag song2 = (Tag) soundLooperObject2;
			return song1.getName().compareTo(song2.getName());
		}
		if (soundLooperObject1 instanceof Tag) {
			return -1;
		}
		return 1;
	}
}
