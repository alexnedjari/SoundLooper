package com.soundlooper.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.aned.exception.PlayerRuntimeException;

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
