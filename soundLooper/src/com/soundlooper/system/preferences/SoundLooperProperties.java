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

import org.apache.log4j.Logger;

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
 * Properties of sound looper
 *
 * @author Alexandre NEDJARI
 * @since 27 juil. 2011
 *--------------------------------------------------------------------------------
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
	public static String KEY_MAJOR_VERSION = "version.major";

	/**
	 * The property file key for minor version key
	 */
	public static String KEY_MINOR_VERSION = "version.minor";

	/**
	 * The property file key for iteration key
	 */
	public static String KEY_ITERATION = "version.iteration";

	/**
	 * The property file key for db update flag
	 */
	public static String KEY_DB_TO_UPDATE = "db.toUpdate";

	/**
	 * The used properties
	 */
	private Properties properties = new Properties();

	/**
	 * The property file
	 */
	private File propertyFile = new File("SoundLooper.properties");

	/**
	 * The logger for this class
	 */
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * Constructor
	 */
	private SoundLooperProperties() {
		try {
			if (!this.propertyFile.exists()) {
				this.propertyFile.createNewFile();
				this.properties.setProperty(SoundLooperProperties.KEY_MAJOR_VERSION, "0");
				this.properties.setProperty(SoundLooperProperties.KEY_MINOR_VERSION, "0");
				this.properties.setProperty(SoundLooperProperties.KEY_ITERATION, "0");
				this.properties.setProperty(SoundLooperProperties.KEY_APPLICATION_NAME, "Sound Looper");
				this.properties.store(new FileOutputStream(this.propertyFile), "");
			}
			this.properties.load(new FileInputStream(this.propertyFile));
		} catch (IOException e) {
			this.logger.error("Impossible d'accéder au fichier '" + this.propertyFile.getAbsolutePath() + "', utilisation des valeurs par défaut");
		}
	}

	/**
	 * Save the properties
	 * @throws IOException If save failed
	 */
	public void save() throws IOException {
		this.properties.store(new BufferedOutputStream(new FileOutputStream(this.propertyFile)), "");
	}

	/**
	 * Get the instance
	 * @return the instance
	 */
	synchronized public static SoundLooperProperties getInstance() {
		if (SoundLooperProperties.instance == null) {
			SoundLooperProperties.instance = new SoundLooperProperties();
		}
		return SoundLooperProperties.instance;
	}

	/**
	 * get the major version
	 * @return the major version
	 */
	public String getMajorVersion() {
		return this.properties.getProperty(SoundLooperProperties.KEY_MAJOR_VERSION, "0");
	}

	/**
	 * get the minor version
	 * @return the minor version
	 */
	public String getMinorVersion() {
		return this.properties.getProperty(SoundLooperProperties.KEY_MINOR_VERSION, "0");
	}

	/**
	 * get the iteration
	 * @return the iteration
	 */
	public String getIteration() {
		return this.properties.getProperty(SoundLooperProperties.KEY_ITERATION, "0");
	}

	/**
	 * get the db update flag
	 * @return the db update flag
	 */
	public boolean isDbToUpdate() {
		return !(this.properties.getProperty(SoundLooperProperties.KEY_DB_TO_UPDATE, "0").equals("0"));
	}

	/**
	 * Set the db to update flag value
	 * @param dbToUpdate the new db to update flag value
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
	 * @return the application name
	 */
	public String getApplicationName() {
		return this.properties.getProperty(SoundLooperProperties.KEY_APPLICATION_NAME, "UNKNOW");
	}

	/**
	 * get the complete version
	 * @return the complete version
	 */
	public String getCompleteVersion() {
		return this.getMajorVersion() + "." + this.getMinorVersion() + "." + this.getIteration();
	}

	/**
	 * Get the application name and version
	 * @return the application name and version
	 */
	public String getApplicationPresentation() {
		return this.getApplicationName() + " " + this.getCompleteVersion();
	}
}
