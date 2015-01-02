package com.soundlooper.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.aned.exception.PlayerRuntimeException;

/**
 *-------------------------------------------------------
 * player listeners managment
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
 * @author Alexandre NEDJARI
 * @since  02 jan. 2015
 *-------------------------------------------------------
 */
public class SoundLooperPlayerSupport {
	/**
	 * Listener list
	 */
	private List<SoundLooperPlayerListener> listSoundLooperPlayerListener = new ArrayList<SoundLooperPlayerListener>();
	
	private static SoundLooperPlayerSupport instance;
	
	public static synchronized SoundLooperPlayerSupport getInstance() {
		if (instance == null) {
			instance = new SoundLooperPlayerSupport();
			
		}
		return instance;
	}
	
	/**
	 * Add a listener to the player
	 * @param listener listener
	 */
	public void addToListSoundLooperPlayerListener(SoundLooperPlayerListener listener) {
		this.listSoundLooperPlayerListener.add(listener);
	}

	/**
	 * Remove a listener from the player
	 * @param listener listener
	 */
	public void removeFromListSoundLooperPlayerListener(SoundLooperPlayerListener listener) {
		this.listSoundLooperPlayerListener.remove(listener);
	}
	
	
	public void firePlayLocationChanged(int newMillisecondLocation) {
		for (SoundLooperPlayerListener listener : this.listSoundLooperPlayerListener) {
			listener.onPlayLocationChanged(newMillisecondLocation);
		}
	}

	public void fireLoopPointChanged(int beginPoint, int endPoint) {
		for (SoundLooperPlayerListener listener : this.listSoundLooperPlayerListener) {
			listener.onLoopPointChanged(beginPoint, endPoint);
		}

	}

	public void fireVolumeUpdate(int percent) {
		for (SoundLooperPlayerListener listener : this.listSoundLooperPlayerListener) {
			listener.onVolumeUpdate(percent);
		}

	}

	public void fireTimestretchUpdated(int percent) {
		for (SoundLooperPlayerListener listener : this.listSoundLooperPlayerListener) {
			listener.onTimestretchUpdated(percent);
		}
	}

	public void fireSongLoaded(File songFile) {
		for (SoundLooperPlayerListener listener : this.listSoundLooperPlayerListener) {
			listener.onSongLoaded(SoundLooperPlayer.getInstance().getSong());
		}
	}
	
	public void fireFatalError(PlayerRuntimeException e) {
		for (SoundLooperPlayerListener listener : this.listSoundLooperPlayerListener) {
			listener.onFatalError(e);
		}
	}

	public void fireBeginGenerateImage() {
		for (SoundLooperPlayerListener listener : this.listSoundLooperPlayerListener) {
			listener.onBeginGenerateImage();
		}

	}

	public void fireEndGenerateImage(BufferedImage image) {
		for (SoundLooperPlayerListener listener : this.listSoundLooperPlayerListener) {
			listener.onEndGenerateImage(image);
		}

	}
}
