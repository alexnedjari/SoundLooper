/**
 *
 */
package com.soundlooper.system.preferences;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ----------------------------------------------------------------------------
 * ---- Sound Looper is an audio player that allow user to loop between two
 * points Copyright (C) 2014 Alexandre NEDJARI
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
 * Properties of sound looper
 *
 * @author Alexandre NEDJARI
 * @since 27 juil. 2011
 *        ----------------------------------------------------------
 *        ----------------------
 */
public class SoundLooperProperties {

	/**
	 * The instance
	 */
	private static SoundLooperProperties instance;

	/**
	 * The property file key for application name
	 */
	public static String KEY_APPLICATION_NAME = "application.name";

	/**
	 * The property file key for major version key
	 */
	public static String KEY_VERSION = "version";

	/**
	 * The property file key for db update flag
	 */
	public static String KEY_DB_TO_UPDATE = "db.toUpdate";

	/**
	 * The property file key for resources path
	 */
	public static String KEY_REPO_PATH_ISSUE = "repo.path.issue";

	/**
	 * The property file key for author
	 */
	public static String KEY_AUTHOR = "author";

	/**
	 * The used properties
	 */
	private Properties properties = new Properties();

	/**
	 * The property file
	 */
	private File propertyFile;

	/**
	 * The logger for this class
	 */
	private Logger logger = LogManager.getLogger(this.getClass());

	/**
	 * Constructor
	 */
	private SoundLooperProperties() {
		try {
			propertyFile = new File("SoundLooper.properties");

			if (!this.propertyFile.exists()) {
				this.propertyFile.createNewFile();
				this.properties.setProperty(SoundLooperProperties.KEY_VERSION, "0");
				this.properties.setProperty(SoundLooperProperties.KEY_APPLICATION_NAME, "Sound Looper");
				this.properties.setProperty(SoundLooperProperties.KEY_DB_TO_UPDATE, "1");
				this.properties.setProperty(SoundLooperProperties.KEY_REPO_PATH_ISSUE, "");
				this.properties.store(new FileOutputStream(this.propertyFile), "");
			}
			this.properties.load(new FileInputStream(this.propertyFile));
		} catch (IOException e) {
			this.logger.error("Impossible d'acc�der au fichier '" + this.propertyFile.getAbsolutePath()
					+ "', utilisation des valeurs par d�faut");
		}
	}

	/**
	 * Save the properties
	 * 
	 * @throws IOException
	 *             If save failed
	 */
	public void save() throws IOException {
		this.properties.store(new BufferedOutputStream(new FileOutputStream(this.propertyFile)), "");
	}

	/**
	 * Get the instance
	 * 
	 * @return the instance
	 */
	synchronized public static SoundLooperProperties getInstance() {
		if (SoundLooperProperties.instance == null) {
			SoundLooperProperties.instance = new SoundLooperProperties();
		}
		return SoundLooperProperties.instance;
	}

	/**
	 * get the version
	 * 
	 * @return the major version
	 */
	public String getVersion() {
		return this.properties.getProperty(SoundLooperProperties.KEY_VERSION, "0");
	}

	/**
	 * get the resources path
	 * 
	 * @return the resources path
	 */
	public String getRepoPathIssues() {
		return this.properties.getProperty(SoundLooperProperties.KEY_REPO_PATH_ISSUE, "");
	}

	/**
	 * get the db update flag
	 * 
	 * @return the db update flag
	 */
	public boolean isDbToUpdate() {
		return !(this.properties.getProperty(SoundLooperProperties.KEY_DB_TO_UPDATE, "0").equals("0"));
	}

	/**
	 * Set the db to update flag value
	 * 
	 * @param dbToUpdate
	 *            the new db to update flag value
	 */
	public void setDbToUpdate(boolean dbToUpdate) {
		if (dbToUpdate) {
			this.properties.setProperty(SoundLooperProperties.KEY_DB_TO_UPDATE, "1");
		} else {
			this.properties.setProperty(SoundLooperProperties.KEY_DB_TO_UPDATE, "0");
		}
	}

	/**
	 * get the application name
	 * 
	 * @return the application name
	 */
	public String getApplicationName() {
		return this.properties.getProperty(SoundLooperProperties.KEY_APPLICATION_NAME, "UNKNOW");
	}

	/**
	 * Get the application name and version
	 * 
	 * @return the application name and version
	 */
	public String getApplicationPresentation() {
		return this.getApplicationName() + " " + this.getVersion();
	}

	public String getAuthor() {
		return this.properties.getProperty(SoundLooperProperties.KEY_AUTHOR, "UNKNOW");
	}
}
