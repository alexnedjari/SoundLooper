package com.soundlooper.service.entite.song;

import com.soundlooper.model.song.Song;

public interface SongListener {

	void onFavoriteUpdated(Song song);
}
