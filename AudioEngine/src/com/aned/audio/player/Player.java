/**
 *
 */
package com.aned.audio.player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.jouvieje.fmodex.FmodEx;
import org.jouvieje.fmodex.System;
import org.jouvieje.fmodex.enumerations.FMOD_RESULT;

import com.aned.exception.PlayerException;

/**
 * AudioEngine is an audio engine based on FMOD
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
 * @author ANEDJARI
 *
 */
public class Player {

	/**
	 * Action listener list
	 */
	private List<PlayerActionListener> listPlayerActionListener = new ArrayList<PlayerActionListener>();

	/**
	 * The FMOD system
	 */
	private static System system = new System();

	/**
	 * The current player (corresponding to the current song)
	 */
	private SoundFile sound = null;

	/**
	 * Save the current volume to apply it to the new songs
	 */
	private int currentVolumePercent = 100;

	/**
	 * The state
	 */
	private PlayerState playerState = new PlayerState();

	/**
	 * The queue used by the player
	 */
	private PlayerActionQueue queue = PlayerActionQueue.getNewInstance();

	/**
	 * the notifier
	 */
	private PlayerMessageNotifier messageNotifier = new PlayerMessageNotifier();

	/**
	 * True if there is an error on a state change
	 */
	private boolean changeStateError;

	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = Logger.getLogger(Player.class);

	/**
	 * Udes to generate song representation
	 */
	private ThreadGenerationImage threadGenerationImage;

	//begin of the player actions
	/**
	 * the instance
	 */
	protected static Player instance;

	/**
	 * Get the instance
	 * @return the player instance
	 */
	public static Player getInstance() {
		if (Player.instance == null) {
			Player.instance = new Player();
		}
		return Player.instance;
	}

	/**
	 * Private constructor
	 */
	protected Player() {
		//To avoid construction
	}

	/**
	 * Add a new listener to list
	 * @param playerActionListener the listener to add
	 */
	public void addToListPlayerActionListener(PlayerActionListener playerActionListener) {
		this.listPlayerActionListener.add(playerActionListener);
	}

	/**
	 * Remove a new listener from list
	 * @param playerActionListener the listener to remove
	 */
	public void removeFromListPlayerActionListener(PlayerActionListener playerActionListener) {
		this.listPlayerActionListener.remove(playerActionListener);
	}

	/**
	 * @return the playerActionListener list
	 */
	List<PlayerActionListener> getListPlayerActionListener() {
		return this.listPlayerActionListener;
	}

	/**
	 * @return the loop begin point
	 */
	public int getLoopPointBegin() {
		if (this.sound != null) {
			return this.sound.getLoopPointBegin();
		}
		return 0;
	}

	/**
	 * @return the loop end point
	 */
	public int getLoopPointEnd() {
		if (this.sound != null) {
			return this.sound.getLoopPointEnd();
		}
		return 0;
	}

	/**
	 * Initialize the player
	 */
	public void initialize() {
		this.addActionToQueue(new PlayerActionInit(this));
	}

	/**
	 * Load a song in the player
	 * @param fileToLoad the file to load
	 */
	public void loadSong(File fileToLoad) {
		this.addActionToQueue(new PlayerActionLoad(this, fileToLoad));
	}

	/**
	 * Play the song
	 */
	public void play() {
		this.addActionToQueue(new PlayerActionStart(this));
	}

	/**
	 * Stop the play and set time to zero
	 */
	public void stop() {
		this.addActionToQueue(new PlayerActionStop(this));
	}

	/**
	 * Pause the song
	 */
	public void pause() {
		this.addActionToQueue(new PlayerActionPause(this));
	}

	/**
	 * Deallocate the song
	 */
	public void desallocate() {
		this.addActionToQueue(new PlayerActionDesallocateSong(this));

	}

	//End of the player actions

	/**
	 * Get the message notifier
	 * @return the message notifier
	 */
	public PlayerMessageNotifier getMessageNotifier() {
		return this.messageNotifier;
	}

	/**
	 * Set the position in the song
	 * @param millisecondTime the new position in milliseconds
	 */
	//	public void setPosition(long millisecondTime) {
	//		//addActionToQueue(new PlayerActionSetPosition(this, millisecondTime));
	//		try {
	//			this.checkCurrentSoundInitialized();
	//			this.getCurrentSound().setMediaTime(new Time(millisecondTime));
	//		} catch (PlayerException e) {
	//			LOGGER.error(StackTracer.getStackTrace(e));
	//			this.getMessageNotifier().sendError(e.getMessage());
	//		}
	//	}

	/**
	 * Set the loop points of the song
	 * @param beginTime the begin time in milliseconds
	 * @param endTime the end time in milliseconds
	 */
	public void setLoopPoints(int beginTime, int endTime) {
		this.addActionToQueue(new PlayerActionSetLoopPoint(this, beginTime, endTime));
	}

	/**
	 * Add an action to the action queue
	 * @param action action to add
	 */
	private void addActionToQueue(PlayerAction action) {
		this.queue.addAction(action);
	}

	/**
	 * Set the volume
	 * @param percent the volume in percent
	 * @throws PlayerException If the player is not initialized
	 */
	public void setVolume(int percent) throws PlayerException {

		if (this.isSoundInitialized()) {
			this.sound.setVolume(new Float(percent / 100.0).floatValue());
		}
		this.currentVolumePercent = percent;

		for (PlayerActionListener listener : this.listPlayerActionListener) {
			listener.onVolumeUpdate(percent);
		}
	}

	/**
	 * Set the rate
	 * @param percent the volume in percent
	 */
	public void setTimestretch(int percent) {
		if (this.isSoundInitialized()) {
			this.addActionToQueue(new PlayerActionApplyTimestretch(this, percent));
		}
	}

	/**
	 * Get the current volume percent
	 * @return the current volume
	 */
	public int getCurrentVolume() {
		return this.currentVolumePercent;
	}

	/**
	 * Check that the sound is initialized
	 * @throws PlayerException if the sound is not initialized (= null)
	 */
	void checkCurrentSoundInitialized() throws PlayerException {
		if (!this.isSoundInitialized()) {
			throw new PlayerException("Player is not initialized");
		}
	}

	/**
	 * Check that the system is initialized
	 * @throws PlayerException if the system is not initialized (= null)
	 */
	void checkSystemInitialized() throws PlayerException {
		if (!this.isSystemInitialized()) {
			throw new PlayerException("System is not initialized");
		}
	}

	/**
	 * Check if the system is initialized
	 * @return true if the system is initialized or initializing
	 */
	public boolean isSystemInitialized() {
		if (this.playerState.getState() == PlayerState.STATE_PLAYER_UNINITIALIZED) {
			return false;
		}
		return true;
	}

	/**
	 * Check that the sound is initialized
	 * @return true if the sound is initialized
	 */
	public boolean isSoundInitialized() {
		if (this.sound == null) {
			return false;
		}
		return true;
	}

	/**
	 * get the current sound
	 * @return the sound
	 * @throws PlayerException if the sound is not initialized
	 */
	public SoundFile getCurrentSound() throws PlayerException {
		this.checkCurrentSoundInitialized();
		return this.sound;
	}

	/**
	 * Return the FMod system
	 * @return the FMod system
	 */
	public static System getSystem() {
		return Player.system;
	}

	/**
	 * set the current sound
	 * @param newSound the new sound
	 */
	void setCurrentSound(SoundFile newSound) {
		this.sound = newSound;
	}

	/**
	 * Change the stateError
	 * @param newChangeStateError the new StateError value
	 */
	public void setChangeStateError(boolean newChangeStateError) {
		this.changeStateError = newChangeStateError;
	}

	/**
	 * Get the changeStateError value
	 * @return the changeStateValue
	 */
	public boolean isChangeStateError() {
		return this.changeStateError;
	}

	/**
	 * Désalloue le player
	 * 	- Stoppe la chanson en cours et la désaloue
	 *  - Désalloue le Système FMOD
	 * 	- Stoppe la file d'attente
	 */
	public void deallocatePlayer() {
		this.addActionToQueue(new PlayerActionDesallocatePlayer(this));
		this.queue.killProcess();
	}

	/**
	 * Check if the result is corresponding to an error
	 * @param result the result to check
	 * @throws PlayerException if the result is an error
	 */
	public static void errorCheck(FMOD_RESULT result) throws PlayerException {
		if (result != FMOD_RESULT.FMOD_OK) {
			Player.LOGGER.info("JNI : Searching string error corresponding to " + result.asInt());
			//java.lang.System.exit(0);
			throw new PlayerException("FMOD error! (" + result.asInt() + ") " + FmodEx.FMOD_ErrorString(result));
		}
	}

	/**
	 * Get the player state
	 * @return the state
	 */
	public int getState() {
		return this.playerState.getState();
	}

	/**
	 * set the new state of the player
	 * @param newState the new state
	 */
	public void setState(int newState) {
		this.playerState.setState(newState);
	}

	/**
	 * get the state label from state
	 * @param state the state
	 * @return the state label
	 */
	public String getStateLabel(int state) {
		return this.playerState.getStateLabel(state);
	}

	/**
	 * --------------------------------------------------------------------------------
	 *
	 * Notifier for messages
	 *
	 * @author Alexandre NEDJARI
	 * @since 20 juil. 2011
	 *--------------------------------------------------------------------------------
	 */
	public class PlayerMessageNotifier {

		/**
		 * Listeners to notifies when a message is generated
		 */
		private ArrayList<PlayerMessagesListener> messageListeners = new ArrayList<PlayerMessagesListener>();

		/**
		 * add a message listener
		 * @param messageListener the listener to add
		 */
		public void addMessageListener(PlayerMessagesListener messageListener) {
			this.messageListeners.add(messageListener);
		}

		/**
		 * Notifies observers of a state change
		 * @param newState the new state
		 */
		public void changeState(int newState) {
			for (PlayerMessagesListener listener : this.messageListeners) {
				listener.onChangeState(newState);
			}
		}

		/**
		 * Send an error to the listeners
		 * @param errorMessage the error message
		 */
		public void sendError(String errorMessage) {
			for (PlayerMessagesListener listener : this.messageListeners) {
				listener.onError(errorMessage);
			}
		}

		/**
		 * Send an message to the listeners
		 * @param message the message
		 */
		public void sendInfo(String message) {
			for (PlayerMessagesListener listener : this.messageListeners) {
				listener.onInfo(message);
			}
		}
	}

	/**
	 * The state of the player
	 * @author ANEDJARI
	 */
	public class PlayerState {
		/**
		 * The current state of the player
		 */
		private int state = PlayerState.STATE_PLAYER_UNINITIALIZED;

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
		public static final int STATE_SONG_LOADED = 4;

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
		protected PlayerState() {
			super();
			this.stateLabel.put(Integer.valueOf(PlayerState.STATE_PLAYER_UNINITIALIZED), "STATE_PLAYER_UNINITIALIZED");
			this.stateLabel.put(Integer.valueOf(PlayerState.STATE_PLAYER_INITIALIZING), "STATE_PLAYER_INITIALIZING");
			this.stateLabel.put(Integer.valueOf(PlayerState.STATE_PLAYER_INITIALIZED), "STATE_PLAYER_INITIALIZED");
			this.stateLabel.put(Integer.valueOf(PlayerState.STATE_LOADING_SONG), "STATE_LOADING_SONG");
			this.stateLabel.put(Integer.valueOf(PlayerState.STATE_SONG_LOADED), "STATE_SONG_LOADED");
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
		 * @param stateToFind the state
		 * @return the label of a state
		 */
		public String getStateLabel(int stateToFind) {
			String label = this.stateLabel.get(Integer.valueOf(stateToFind));
			if (label == null) {
				label = "UNKNOW STATE";
			}
			return label;
		}

		/**
		 * Set the state value
		 * @param newState the new state value
		 */
		public void setState(int newState) {
			this.state = newState;
			Player.this.getMessageNotifier().changeState(newState);
		}

		/**
		 * get the current state of the player
		 * @return the current state of the player
		 */
		public int getState() {
			return this.state;
		}
	}

	/**
	 * Set the media time
	 * @param beginMsValue the bebin MS value
	 */
	public void setMediaTime(int beginMsValue) {
		this.addActionToQueue(new PlayerActionSetPosition(this, beginMsValue));
	}

	/**
	 * Get the media time
	 * @return the media time
	 */
	public int getMediaTime() {
		//waitForQueueEnd();
		PlayerActionGetPosition playerActionGetPosition = new PlayerActionGetPosition(this);
		this.addActionToQueue(playerActionGetPosition);
		this.waitForQueueEnd();
		return playerActionGetPosition.getResult();
	}

	/**
	 * Generate image for current song in a different thread
	 * When the image is generated, listeners are notified
	 * This task is not added to the queue for allow others
	 * player task to be excuted during the generation
	 */
	public void generateImage() {
		if (this.threadGenerationImage != null && this.threadGenerationImage.isAlive()) {
			this.threadGenerationImage.interrupt();
		}
		try {
			this.threadGenerationImage = new ThreadGenerationImage(this.getMessageNotifier(), this.getListPlayerActionListener(), this.getCurrentSound());
			this.threadGenerationImage.start();
		} catch (PlayerException e) {
			this.getMessageNotifier().sendError(e.getMessage());
		}

	}

	/**
	 * Get the timestretch
	 * @return the timestretch
	 */
	public int getTimeStretch() {
		//waitForQueueEnd();
		PlayerActionGetTimestretch playerActionGetTimestretch = new PlayerActionGetTimestretch(this);
		this.addActionToQueue(playerActionGetTimestretch);
		this.waitForQueueEnd();
		return playerActionGetTimestretch.getResult();
	}

	/**
	 * Wait for the queue end
	 */
	public void waitForQueueEnd() {
		while (this.queue.actionsToDo.size() > 0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				//never interrupted
				e.printStackTrace();
			}
		}
	}

}
