package com.soundlooper.service.entite.mark;

import com.soundlooper.model.mark.Mark;
import com.soundlooper.model.song.Song;

public interface MarkListener {

	void onMarkDeleted(Song song, Mark mark, long idMark);

	void onMarkAdded(Song song, Mark mark);

    void onDirtyChanged(Mark mark);

    void onMarkLoaded(Mark mark);
	
}
