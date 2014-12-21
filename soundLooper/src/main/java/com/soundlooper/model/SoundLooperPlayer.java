/**
 *
 */
package com.soundlooper.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.aned.audio.player.Player;
import com.aned.audio.player.PlayerActionListener;
import com.aned.exception.PlayerRuntimeException;
import com.soundlooper.exception.SoundLooperException;
import com.soundlooper.model.mark.Mark;
import com.soundlooper.model.song.Song;
import com.soundlooper.service.entite.mark.MarkSupport;
import com.soundlooper.service.entite.song.SongService;
import com.soundlooper.service.uc.gererMarks.GererMarkService;
import com.soundlooper.service.uc.gererSongs.GererSongService;
import com.soundlooper.system.preferences.Preferences;

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
 * The sound Looper general model
 *
 * @author Alexandre NEDJARI
 * @since  17 f�vr. 2014
 *-------------------------------------------------------
 */
public class SoundLooperPlayer extends Player implements PlayerActionListener {

	/**
	 * The current song
	 */
	private Song song;

    /**
     * The current mark
     * This mark is NEVER an element of the mark list of the current song, but a copy
     * to avoid problems when the user update it but without savin it
     */
    private Mark mark;
	

	/**
	 * @return the song
	 */
	public Song getSong() {
		return this.song;
	}
	
    /**
     * Get the current Mark
     * @return the current mark or null if there is no current mark
     */
    public Mark getCurrentMark() {
        return mark;
    }


	public static SoundLooperPlayer getInstance() {
		if (Player.instance == null) {
			Player.instance = new SoundLooperPlayer();
			Player.getInstance().addToListPlayerActionListener((SoundLooperPlayer)instance);
		}
		return (SoundLooperPlayer) Player.instance;
	}

	@Override
	public void loadSong(File selectedFile) {
		super.loadSong(selectedFile);
	}

	/**
	 * Switch the favorite attribute on the current loaded song
	 * @throws SoundLooperException if an exception is threw
	 */
	public void switchFavoriteOnCurrentSong() throws SoundLooperException {
		if (this.song == null) {
			return;
		}
		GererSongService.getInstance().switchSongToFavorite(this.song);
	}

	

	

	/**
	 * @param idMark the mark id
	 * @throws SoundLooperException if an error es detected
	 */
	public void deleteMarkOnCurrentSong(String idMark) throws SoundLooperException {
		Mark mark = this.getMarkFromId(idMark);
		GererMarkService.getInstance().delete(mark);
		

	}

	/**
	 * @param nom the mark's name
	 * @throws SoundLooperException if error is detected
	 */
	public void createNewMarkAtCurrentPosition(String nom) throws SoundLooperException {
        Mark mark = GererMarkService.getInstance().createNewMark(this.song, nom, this.getLoopPointBegin(), this.getLoopPointEnd());
        this.selectMark(mark);
	}

	/**
	 *
	 * @param idMark the mark's id
	 * @return the finded mark
	 * @throws SoundLooperException if the mark is not found
	 */
	private Mark getMarkFromId(String idMark) throws SoundLooperException {
		HashMap<String, Mark> mapMarks = this.getSong().getMarks();
		Set<String> marksNames = mapMarks.keySet();
		for (String name : marksNames) {
			Mark mark = mapMarks.get(name);
			if (mark.getId() == Integer.valueOf(idMark).intValue()) {
				return mark;
			}
		}
		throw new SoundLooperException("Le marqueur avec l'id '" + idMark + "' n'existe pas pour la chanson actuellement charg�e");
	}

	/**
	 * @return the favorite list
	 */
	public List<Song> getFavoriteSongList() {
		return SongService.getInstance().getFavoriteSongList();
	}

	/**
	 *
	 */
	public void purgeSong() {
		GererSongService.getInstance().purgeSong();

	}



	public void setSong(Song song) {
		this.song=song;
		
	}

	@Override
	public void onPlayLocationChanged(int newMillisecondLocation) {
		SoundLooperPlayerSupport.getInstance().firePlayLocationChanged(newMillisecondLocation);
		
	}

	@Override
	public void onLoopPointChanged(int beginPoint, int endPoint) {
        if (this.mark != null) {
            if (beginPoint != this.mark.getBeginMillisecond()) {
                this.mark.setBeginMillisecond(beginPoint);
            }
            if (endPoint != this.mark.getEndMillisecond()) {
                this.mark.setEndMillisecond(endPoint);
            }
        }
		SoundLooperPlayerSupport.getInstance().fireLoopPointChanged(beginPoint, endPoint);
	}

	@Override
	public void onVolumeUpdate(int percent) {
		SoundLooperPlayerSupport.getInstance().fireVolumeUpdate(percent);
		
	}

	@Override
	public void onTimestretchUpdated(int percent) {
		SoundLooperPlayerSupport.getInstance().fireTimestretchUpdated(percent);
	}

	@Override
	public void onSongLoaded(File songFile) {
		Preferences.getInstance().setLastPathUsed(songFile.getAbsolutePath());
		Preferences.getInstance().addFileToRecentFileList(songFile.getAbsolutePath());
		try {
            setSong(GererSongService.getInstance().getSong(songFile));
			//TODO cr�er un type d'exception particulier quand un objet n'existe pas
		} catch (SoundLooperException e) {
			//La chanson n'est pas encore enregistr�e, on cr�e un nouvel objet
            setSong(GererSongService.getInstance().createNewSong(songFile));
		}
        this.selectMark(null);
		SoundLooperPlayerSupport.getInstance().fireSongLoaded(songFile);
	}
	
    /**
     * @param mark the mark to select id
     */
    public void selectMark(Mark mark)  {
        if (mark != null) {
        	this.mark = mark.clone();
            this.setLoopPoints(mark.getBeginMillisecond(), mark.getEndMillisecond());
        } else {
        	this.mark = null;
        }
        MarkSupport.getInstance().fireMarkLoaded(mark);
    }
    
    /**
     * Set the loop points of the song
     * @param beginTime the begin time in milliseconds
     * @param endTime the end time in milliseconds
     */
    public void setLoopPoints(int beginTime, int endTime) {
        super.setLoopPoints(beginTime, endTime);
        if (mark != null) {
            MarkSupport.getInstance().fireDirtyChanged(mark);
        }
    }


	@Override
	public void onFatalError(PlayerRuntimeException e) {
		SoundLooperPlayerSupport.getInstance().fireFatalError(e);
		
	}

	@Override
	public void onBeginGenerateImage() {
		SoundLooperPlayerSupport.getInstance().fireBeginGenerateImage();
		
	}

	@Override
	public void onEndGenerateImage(BufferedImage image) {
		SoundLooperPlayerSupport.getInstance().fireEndGenerateImage(image);
	}
	

    public void saveCurrentMark() throws SoundLooperException {
        if (mark == null) {
            return;
        }
        
        Mark clone = mark.clone();
		GererMarkService.getInstance().saveMark(clone);
		song.getMarks().put(clone.getName(), clone);
    }

}
