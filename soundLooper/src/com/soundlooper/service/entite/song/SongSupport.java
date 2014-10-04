package com.soundlooper.service.entite.song;

import java.util.ArrayList;
import java.util.List;

import com.soundlooper.model.song.Song;

public class SongSupport {
	private static SongSupport instance;
	
	private List<SongListener> listSongListener = new ArrayList<SongListener>();

	public synchronized static SongSupport getInstance() {
		if (instance == null) {
			instance = new SongSupport();
		}
		return instance;
	}
	
	public void addToListSongListener(SongListener songListener) {
		this.listSongListener.add(songListener);
	}
	
	public void removeFromListSongListener(SongListener songListener) {
		this.listSongListener.remove(songListener);
	}
	
	public void fireFavoriteUpdated(Song song) {
		for (SongListener listener : this.listSongListener) {
			listener.onFavoriteUpdated(song);
		}
	}
	
}
