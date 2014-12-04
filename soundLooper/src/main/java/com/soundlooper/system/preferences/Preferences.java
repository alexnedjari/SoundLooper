/**
 *
 */
package com.soundlooper.system.preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.soundlooper.system.preferences.recentfile.RecentFile;
import com.soundlooper.system.preferences.recentfile.RecentFileSet;

/**--------------------------------------------------------------------------------
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
 * Preference access
 *
 * @author Alexandre NEDJARI
 * @since 26 juil. 2011
 *--------------------------------------------------------------------------------
 */
public final class Preferences {

	/**
	 * Logger for this class
	 */
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * The key for the always on top preference
	 */
	public static final String KEY_ALWAYS_ON_TOP = "always.on.top";

	/**
	 * The key for the last path used preference
	 */
	public static final String KEY_LAST_PATH_USED = "last.path.used";

	/**
	 * The key for the last path used preference
	 */
	public static final String KEY_LAST_VOLUME_USED = "last.volume.used";

	/**
	 * The key for the recent file list preference
	 */
	public static final String KEY_RECENT_FILE_LIST = "recent.file.list";

	/**
	 * The key for the recent file list size preference
	 */
	public static final String KEY_RECENT_FILE_LIST_SIZE = "recent.file.list.size";

	/**
	 * Always on top preference
	 */
	private boolean alwaysOnTop = false;

	/**
	 * Last path used preference
	 */
	private String lastPathUsed = "";

	/**
	 * Last volume used preference
	 */
	private int lastVolumeUsed = 100;

	/**
	 * The recent file list
	 */
	private RecentFileSet recentFileSet = null;

	/**
	 * The number of recent file to store
	 */
	private int recentFileListSize = 10;

	/**
	 * list of listeners
	 */
	private ArrayList<PreferencesListener> listeners = new ArrayList<PreferencesListener>();

	/**
	 * The instance
	 */
	private static Preferences instance = null;

	/**
	 * The properties
	 */
	private Properties properties;

	/**
	 * The properties file
	 */
	private File propertieFile;

	/**
	 * Get the instance
	 * @return the instance
	 */
	public synchronized static Preferences getInstance() {
		if (Preferences.instance == null) {
			Preferences.instance = new Preferences();
			
		}
		return Preferences.instance;
	}

	/**
	 * Private constructor
	 */
	private Preferences() {
			propertieFile = new File("preferences.properties");
		
		try {
			this.properties = new Properties();

			if (!this.propertieFile.exists()) {
				this.propertieFile.createNewFile();
				this.properties.storeToXML(new FileOutputStream(this.propertieFile), "", "UTF-8");
			}
			this.properties.loadFromXML(new FileInputStream(this.propertieFile));
			String stringAlwaysOnTop = this.properties.getProperty(Preferences.KEY_ALWAYS_ON_TOP, "false");
			if (stringAlwaysOnTop.equals(Boolean.TRUE.toString())) {
				this.alwaysOnTop = true;
			} else if (stringAlwaysOnTop.equals(Boolean.FALSE.toString())) {
				this.alwaysOnTop = false;
			} else {
				this.logger.warn("Value for " + Preferences.KEY_ALWAYS_ON_TOP + " is not valide : '" + stringAlwaysOnTop + "', use the default value");
			}
			this.lastPathUsed = this.properties.getProperty(Preferences.KEY_LAST_PATH_USED, "");
			this.lastVolumeUsed = Integer.parseInt(this.properties.getProperty(Preferences.KEY_LAST_VOLUME_USED, "100"));
			this.recentFileListSize = Integer.parseInt(this.properties.getProperty(Preferences.KEY_RECENT_FILE_LIST_SIZE, "10"));
			this.recentFileSet = this.getRecentFileSet();
		} catch (IOException e) {
			this.logger.warn("Unable to read properties, use the default values", e);
		}

		this.logger.info("Initial value for '" + Preferences.KEY_ALWAYS_ON_TOP + "' : " + this.alwaysOnTop);
		this.logger.info("Initial value for '" + Preferences.KEY_LAST_PATH_USED + "' : " + this.lastPathUsed);
	}

	/**
	 * File are stored as File1:Date1|File2:Date2
	 * @return the file as map
	 */
	private RecentFileSet getRecentFileSet() {
		if (this.recentFileSet == null) {
			String recentFileListString = this.properties.getProperty(Preferences.KEY_RECENT_FILE_LIST, "");
			this.logger.info("Initial value for '" + Preferences.KEY_RECENT_FILE_LIST + "' : " + recentFileListString);
			this.recentFileSet = new RecentFileSet(this.recentFileListSize);

			StringTokenizer recentFileStringTokenizer = new StringTokenizer(recentFileListString, "|");
			while (recentFileStringTokenizer.hasMoreElements()) {
				String recentFileString = recentFileStringTokenizer.nextToken();
				String[] recentFileTab = recentFileString.split("\\*");
				if (recentFileTab.length != 2) {
					continue;
				}
				File file = new File(recentFileTab[0]);
				Date lastAccessDate = new Date(Long.valueOf(recentFileTab[1]).longValue());
				this.recentFileSet.addRecentFile(new RecentFile(file, lastAccessDate));
			}
		}
		return this.recentFileSet;
	}

	/**
	 * Get the last volume used
	 * @return the volume
	 */
	public int getLastVolumeUsed() {
		return this.lastVolumeUsed;
	}

	/**
	 * @param newLastVolumeUsed the lastVolumeUsed to set
	 */
	public void setLastVolumeUsed(int newLastVolumeUsed) {
		this.logger.info(Preferences.KEY_LAST_VOLUME_USED + " is changed from " + this.lastVolumeUsed + " to " + newLastVolumeUsed);
		this.lastVolumeUsed = newLastVolumeUsed;
		this.properties.setProperty(Preferences.KEY_LAST_VOLUME_USED, Integer.toString(this.lastVolumeUsed));
		for (PreferencesListener listener : this.listeners) {
			this.logger.info("Notifie listener : " + listener.toString());
			listener.onPreferenceChange(Preferences.KEY_LAST_VOLUME_USED, Integer.valueOf(newLastVolumeUsed));
		}
	}

	/**
	 * Get the recent file list
	 * @return the recent file list
	 */
	public List<RecentFile> getRecentFileList() {
		return this.getRecentFileSet().getRecentFileListCopy();
	}

	/**
	 * Add a new file to the recent file list
	 * @param absoluteFilePath the file to add absolute path
	 */
	public void addFileToRecentFileList(String absoluteFilePath) {
		this.getRecentFileSet().addRecentFile(new RecentFile(new File(absoluteFilePath), new Date()));

		String recentFileListRepresentation = "";
		for (RecentFile recentFile : this.getRecentFileSet().getRecentFileListCopy()) {
			recentFileListRepresentation += recentFile.getFile().getAbsolutePath() + "*" + recentFile.getLastAccessDate().getTime() + "|";
		}
		this.logger.info(Preferences.KEY_RECENT_FILE_LIST + " is changed to " + recentFileListRepresentation);
		this.properties.setProperty(Preferences.KEY_RECENT_FILE_LIST, recentFileListRepresentation);
		for (PreferencesListener listener : this.listeners) {
			this.logger.info("Notifie listener : " + listener.toString());
			listener.onPreferenceChange(Preferences.KEY_RECENT_FILE_LIST, this.getRecentFileSet());
		}
	}

	/**
	 * get the always.on.top value
	 * @return the always.on.top.value
	 */
	public boolean isAlwaisOnTop() {
		return this.alwaysOnTop;
	}

	/**
	 * Set the always.on.top value
	 * @param newAlwaysOnTop the always.on.top new value
	 */
	public void setAlwaysOnTop(boolean newAlwaysOnTop) {
		this.logger.info(Preferences.KEY_ALWAYS_ON_TOP + " is changed from " + this.alwaysOnTop + " to " + newAlwaysOnTop);
		this.alwaysOnTop = newAlwaysOnTop;
		this.properties.setProperty(Preferences.KEY_ALWAYS_ON_TOP, Boolean.toString(this.alwaysOnTop));
		for (PreferencesListener listener : this.listeners) {
			this.logger.info("Notifie listener : " + listener.toString());
			listener.onPreferenceChange(Preferences.KEY_ALWAYS_ON_TOP, Boolean.valueOf(newAlwaysOnTop));
		}
	}

	/**
	 * Get the last path used value
	 * @return the last path used value
	 */
	public String getLastPathUsed() {
		return this.lastPathUsed;
	}

	/**
	 * Set the new last path used value
	 * @param newLastPathUsed the new value
	 */
	public void setLastPathUsed(String newLastPathUsed) {
		this.logger.info(Preferences.KEY_LAST_PATH_USED + " is changed from " + this.lastPathUsed + " to " + newLastPathUsed);
		this.lastPathUsed = newLastPathUsed;
		this.properties.setProperty(Preferences.KEY_LAST_PATH_USED, this.lastPathUsed);
		for (PreferencesListener listener : this.listeners) {
			this.logger.info("Notifie listener : " + listener.toString());
			listener.onPreferenceChange(Preferences.KEY_LAST_PATH_USED, Boolean.valueOf(newLastPathUsed));
		}
	}

	/**
	 * Add a listener to the list
	 * @param listener the listener to add
	 */
	public void addListener(PreferencesListener listener) {
		this.listeners.add(listener);
		this.logger.info("Ajout d'un listener de pr�f�rences : " + listener.getClass().getSimpleName() + ". Nombre de listeners : " + this.listeners.size());
	}

	/**
	 * Save the preferences
	 */
	public void save() {
		this.logger.info("Save preferences");
		try {
			this.properties.storeToXML(new FileOutputStream(this.propertieFile), "", "UTF-8");
		} catch (IOException e) {
			this.logger.error("Preferences cannot be saved", e);
		}
	}

}
