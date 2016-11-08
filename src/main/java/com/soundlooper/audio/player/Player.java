/**
 *
 */
package com.soundlooper.audio.player;

import java.io.File;
import java.util.HashMap;
import java.util.function.Consumer;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jouvieje.fmodex.FmodEx;
import org.jouvieje.fmodex.System;
import org.jouvieje.fmodex.enumerations.FMOD_RESULT;

import com.soundlooper.exception.PlayerException;
import com.soundlooper.exception.PlayerNotInitializedException;
import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.system.util.MessagingUtil;

/**
 * AudioEngine is an audio engine based on FMOD Copyright (C) 2014 Alexandre
 * NEDJARI
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author ANEDJARI
 *
 */
public class Player {

	/**
	 * The current state of the player
	 */
	private IntegerProperty state = new SimpleIntegerProperty(PlayerState.STATE_PLAYER_UNINITIALIZED);

	/**
	 * Save the current volume to apply it to the new songs
	 */
	private IntegerProperty volume = new SimpleIntegerProperty(100);

	/**
	 * Save the current timestretsh to apply it to the new songs
	 */
	private IntegerProperty timeStretch = new SimpleIntegerProperty(100);

	/**
	 * The FMOD system
	 */
	private static System system = new System();

	/**
	 * The current player (corresponding to the current song)
	 */
	private SoundFile sound = null;

	/**
	 * The state
	 */
	private PlayerState playerState = new PlayerState();

	/**
	 * True if there is an error on a state change
	 */
	private boolean changeStateError;

	/**
	 * Logger for this class
	 */
	private static Logger LOGGER = LogManager.getLogger(Player.class);

	/**
	 * Udes to generate song representation
	 */
	private ThreadImageGenerator threadGenerationImage;

	protected static final int MINIMAL_MS_LOOP = 100;

	/**
	 * Private constructor
	 */
	protected Player() {
		volume.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				try {
					applyVolume(newValue.intValue());
				} catch (PlayerException e) {
					MessagingUtil.displayError("Impossible de modifier le volume", e);
				}
			}
		});

		timeStretch.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				try {
					applyTimeStretch(oldValue.intValue(), newValue.intValue());
				} catch (PlayerException e) {
					MessagingUtil.displayError("Impossible de modifier le timestrech", e);
				}
			}
		});
	}

	/**
	 * Set the state value
	 * 
	 * @param newState
	 *            the new state value
	 */
	public void setState(int newState) {
		this.state.set(newState);
	}

	/**
	 * get the current state of the player
	 * 
	 * @return the current state of the player
	 */
	public int getState() {
		return this.state.get();
	}

	public IntegerProperty stateProperty() {
		return state;
	}

	/**
	 * Initialize the player
	 * 
	 * @throws PlayerException
	 */
	public void initialize() throws PlayerException {
		new PlayerActionInit(this).run();
	}

	/**
	 * Load a song in the player
	 * 
	 * @param fileToLoad
	 *            the file to load
	 * @throws PlayerException
	 */
	protected void loadSong(File fileToLoad) throws PlayerException {
		new PlayerActionLoad(this, fileToLoad).run();
	}

	/**
	 * Play the song
	 * 
	 * @throws PlayerException
	 */
	public void play() throws PlayerException {
		new PlayerActionStart(this).run();
	}

	/**
	 * Stop the play and set time to zero
	 * 
	 * @throws PlayerException
	 */
	public void stop() throws PlayerException {
		new PlayerActionStop(this).run();
	}

	/**
	 * Pause the song
	 * 
	 * @throws PlayerException
	 */
	public void pause() throws PlayerException {
		new PlayerActionPause(this).run();
	}

	/**
	 * Deallocate the song
	 * 
	 * @throws PlayerException
	 */
	public void desallocate() throws PlayerException {
		new PlayerActionDesallocateSong(this).run();

	}

	/**
	 * Check that the sound is initialized
	 * 
	 * @throws PlayerException
	 *             if the sound is not initialized (= null)
	 */
	void checkCurrentSoundInitialized() throws PlayerNotInitializedException {
		if (!this.isSoundInitialized()) {
			throw new PlayerNotInitializedException("Le lecteur n'est pas initialisé");
		}
	}

	/**
	 * Check that the system is initialized
	 * 
	 * @throws PlayerException
	 *             if the system is not initialized (= null)
	 */
	void checkSystemInitialized() throws PlayerException {
		if (!this.isSystemInitialized()) {
			throw new PlayerException("Le système n'est pas initialisé");
		}
	}

	/**
	 * Check if the system is initialized
	 * 
	 * @return true if the system is initialized or initializing
	 */
	public boolean isSystemInitialized() {
		if (this.getState() == PlayerState.STATE_PLAYER_UNINITIALIZED) {
			return false;
		}
		return true;
	}

	/**
	 * Check that the sound is initialized
	 * 
	 * @return true if the sound is initialized
	 */
	public boolean isSoundInitialized() {
		if (this.sound == null) {
			LOGGER.info("Sound is not initialized");
			return false;
		}
		return true;
	}

	/**
	 * get the current sound
	 * 
	 * @return the sound
	 * @throws PlayerException
	 *             if the sound is not initialized
	 */
	public SoundFile getCurrentSound() throws PlayerNotInitializedException {
		this.checkCurrentSoundInitialized();
		return this.sound;
	}

	/**
	 * Return the FMod system
	 * 
	 * @return the FMod system
	 */
	public static System getSystem() {
		return Player.system;
	}

	/**
	 * set the current sound
	 * 
	 * @param newSound
	 *            the new sound
	 */
	void setCurrentSound(SoundFile newSound) {
		this.sound = newSound;
	}

	/**
	 * Change the stateError
	 * 
	 * @param newChangeStateError
	 *            the new StateError value
	 */
	public void setChangeStateError(boolean newChangeStateError) {
		this.changeStateError = newChangeStateError;
	}

	/**
	 * Get the changeStateError value
	 * 
	 * @return the changeStateValue
	 */
	public boolean isChangeStateError() {
		return this.changeStateError;
	}

	/**
	 * Check if the result is corresponding to an error
	 * 
	 * @param result
	 *            the result to check
	 * @throws PlayerException
	 *             if the result is an error
	 */
	public static void errorCheck(FMOD_RESULT result) throws PlayerException {
		if (result != FMOD_RESULT.FMOD_OK) {
			LOGGER.info("JNI : Searching string error corresponding to " + result.asInt());
			throw new PlayerException("FMOD error! (" + result.asInt() + ") " + FmodEx.FMOD_ErrorString(result));
		}
	}

	/**
	 * get the state label from state
	 * 
	 * @param state
	 *            the state
	 * @return the state label
	 */
	public String getStateLabel(int state) {
		return this.playerState.getStateLabel(state);
	}

	// TODO supprimer cette sous classe
	/**
	 * The state of the player
	 * 
	 * @author ANEDJARI
	 */
	public class PlayerState {

		/**
		 * The state for unitialized players (no song loaded)
		 */
		public static final int STATE_PLAYER_UNINITIALIZED = 0;

		/**
		 * The state used during the player initialization
		 */
		public static final int STATE_PLAYER_INITIALIZING = 1;

		/**
		 * No song loaded, but the player is ready
		 */
		public static final int STATE_PLAYER_INITIALIZED = 2;

		/**
		 * State when the song is loading
		 */
		public static final int STATE_LOADING_SONG = 3;

		/**
		 * State when the song is loaded
		 */
		// public static final int STATE_SONG_LOADED = 4;

		/**
		 * State when preparing play
		 */
		public static final int STATE_PREPARING_PLAY = 5;

		/**
		 * State when playing
		 */
		public static final int STATE_PLAYING = 6;

		/**
		 * State when preparing pause
		 */
		public static final int STATE_PREPARING_PAUSE = 7;

		/**
		 * State when it's paused
		 */
		public static final int STATE_PAUSED = 8;

		/**
		 * State when preparing stop
		 */
		public static final int STATE_PREPARING_STOP = 9;

		/**
		 * State when it's paused
		 */
		public static final int STATE_STOPPED = 10;

		/**
		 * The state when unload current song
		 */
		public static final int STATE_UNLOAD_SONG = 11;

		/**
		 * The state when unload the player
		 */
		public static final int STATE_UNLOAD_PLAYER = 12;

		/**
		 * The label map for states
		 */
		private HashMap<Integer, String> stateLabel = new HashMap<Integer, String>();

		/**
		 * Constructor
		 */
		public PlayerState() {
			super();
			this.stateLabel.put(Integer.valueOf(PlayerState.STATE_PLAYER_UNINITIALIZED), "STATE_PLAYER_UNINITIALIZED");
			this.stateLabel.put(Integer.valueOf(PlayerState.STATE_PLAYER_INITIALIZING), "STATE_PLAYER_INITIALIZING");
			this.stateLabel.put(Integer.valueOf(PlayerState.STATE_PLAYER_INITIALIZED), "STATE_PLAYER_INITIALIZED");
			this.stateLabel.put(Integer.valueOf(PlayerState.STATE_LOADING_SONG), "STATE_LOADING_SONG");
			// this.stateLabel.put(Integer.valueOf(PlayerState.STATE_SONG_LOADED),
			// "STATE_SONG_LOADED");
			this.stateLabel.put(Integer.valueOf(PlayerState.STATE_PREPARING_PLAY), "STATE_PREPARING_PLAY");
			this.stateLabel.put(Integer.valueOf(PlayerState.STATE_PLAYING), "STATE_PLAYING");
			this.stateLabel.put(Integer.valueOf(PlayerState.STATE_PREPARING_PAUSE), "STATE_PREPARING_PAUSE");
			this.stateLabel.put(Integer.valueOf(PlayerState.STATE_PAUSED), "STATE_PAUSED");
			this.stateLabel.put(Integer.valueOf(PlayerState.STATE_PREPARING_STOP), "STATE_PREPARING_STOP");
			this.stateLabel.put(Integer.valueOf(PlayerState.STATE_STOPPED), "STATE_STOPPED");
			this.stateLabel.put(Integer.valueOf(PlayerState.STATE_UNLOAD_SONG), "STATE_UNLOAD_SONG");
			this.stateLabel.put(Integer.valueOf(PlayerState.STATE_UNLOAD_PLAYER), "STATE_UNLOAD_PLAYER");
		}

		/**
		 * get the label of a state
		 * 
		 * @param stateToFind
		 *            the state
		 * @return the label of a state
		 */
		public String getStateLabel(int stateToFind) {
			String label = this.stateLabel.get(Integer.valueOf(stateToFind));
			if (label == null) {
				label = "UNKNOW STATE";
			}
			return label;
		}

	}

	/**
	 * Set the media time
	 * 
	 * @param beginMsValue
	 *            the bebin MS value
	 * @throws PlayerException
	 */
	public void setMediaTime(int beginMsValue) throws PlayerException {
		new PlayerActionSetPosition(this, beginMsValue).run();
	}

	public void moveMediaTime(int millisecondTime) throws PlayerException {
		if (!isSoundInitialized()) {
			return;
		}
		int newMediaTime = getMediaTime() + millisecondTime;

		new PlayerActionSetPosition(this, newMediaTime).run();
	}

	/**
	 * Get the media time
	 * 
	 * @return the media time
	 * @throws PlayerException
	 */
	public int getMediaTime() {
		if (!this.isSoundInitialized()) {
			return 0;
		}
		try {
			return this.getCurrentSound().getMediaTime();
		} catch (PlayerException e) {
			LOGGER.error(e);
			return 0;
		}
	}

	/**
	 * Generate image for current song in a different thread When the image is
	 * generated, listeners are notified This task is not added to the queue for
	 * allow others player task to be excuted during the generation
	 */
	public void generateImage(Consumer<File> onSuccessConsumer) {
		if (this.threadGenerationImage != null && this.threadGenerationImage.isAlive()) {
			this.threadGenerationImage.interrupt();
		}
		try {
			this.threadGenerationImage = new ThreadImageGenerator(this.getCurrentSound(), onSuccessConsumer);
			this.threadGenerationImage.start();
		} catch (PlayerException e) {
			MessagingUtil.displayError("Impossible de générer l'image audio", e);
		}

	}

	// /////////////////VOLUME MANAGEMENT////////////////////

	public IntegerProperty volumeProperty() {
		return volume;
	}

	public int getVolume() {
		return volume.get();
	}

	public void setVolume(int percent) {
		volume.set(percent);
	}

	/**
	 * Set the volume
	 * 
	 * @param percent
	 *            the volume in percent
	 */
	private void applyVolume(int percent) throws PlayerException {
		LOGGER.info("Set the volume percent : " + percent);
		if (this.isSoundInitialized()) {
			this.sound.setVolume(new Float(percent / 100.0).floatValue());
			volume.set(percent);
		}

	}

	public void incrementVolume(int percent) {
		int newVolume = getVolume() + percent;
		if (newVolume > 100) {
			newVolume = 100;
		}
		if (newVolume < 0) {
			newVolume = 0;
		}
		setVolume(newVolume);
	}

	// /////////////////END VOLUME MANAGEMENT////////////////////

	// /////////////////TIMESTRETCH MANAGEMENT////////////////////
	public IntegerProperty timeStretchProperty() {
		return timeStretch;
	}

	public int getTimeStretch() {
		return timeStretch.get();
	}

	public void setTimeStretch(int percent) {
		timeStretch.set(percent);
	}

	public void incrementTimeStretch(int percent) {
		int newPercent = getTimeStretch() + percent;
		if (newPercent > 200) {
			newPercent = 200;
		}
		if (newPercent < 50) {
			newPercent = 50;
		}
		setTimeStretch(newPercent);
	}

	private void applyTimeStretch(int oldPercent, int newPercent) throws PlayerException {
		if (oldPercent == newPercent) {
			return;
		}

		LOGGER.info("Update timestrech from " + oldPercent + " to " + newPercent);

		if (this.isSoundInitialized()) {
			new PlayerActionApplyTimestretch(this, newPercent).run();
		}
	}

	/**
	 * Set the loop points of the song
	 * 
	 * @param beginTime
	 *            the begin time in milliseconds
	 * @param endTime
	 *            the end time in milliseconds
	 * @throws PlayerException
	 */
	protected void applyLoopPoints(final int beginTime, final int endTime) throws PlayerException {
		PlayerActionApplyLoopPoint action = new PlayerActionApplyLoopPoint(SoundLooperPlayer.getInstance(), beginTime,
				endTime);
		action.run();
	}

	public PlayerState getPlayerState() {
		return playerState;
	}

	// /////////////////END LOOP POINTS MANAGEMENT////////////////////
}
