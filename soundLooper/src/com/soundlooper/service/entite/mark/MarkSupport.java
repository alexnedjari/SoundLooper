package com.soundlooper.service.entite.mark;

import java.util.ArrayList;
import java.util.List;

import com.soundlooper.model.mark.Mark;
import com.soundlooper.model.song.Song;

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
	
	public void fireMarkDeleted(Song song, Mark mark) {
		for (MarkListener listener : this.listMarkListener) {
			listener.onMarkDeleted(song, mark);
		}
	}
	
	public void fireMarkAdded(Song song, Mark mark) {
		for (MarkListener listener : this.listMarkListener) {
			listener.onMarkAdded(song, mark);
		}
	}
}
