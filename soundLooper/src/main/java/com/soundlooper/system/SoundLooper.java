/**
 *
 */
package com.soundlooper.system;

import java.io.IOException;
import java.sql.SQLException;

import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import com.soundlooper.exception.AlreadyLockedException;
import com.soundlooper.gui.WindowPlayer;
import com.soundlooper.model.database.ConnectionFactory;
import com.soundlooper.system.preferences.Preferences;
import com.soundlooper.system.util.Lock;
import com.soundlooper.system.util.TimeMeasurer;

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
 *
 * Entry point of the application
 *
 * @author Alexandre NEDJARI
 * @since  17 févr. 2014
 *-------------------------------------------------------
 */
public class SoundLooper {

	/**
	 * Logger for this class
	 */
	protected static Logger logger = Logger.getLogger(SoundLooper.class);

	/**
	 * time measurer for this class
	 */
	static TimeMeasurer timeMeasurer = new TimeMeasurer(Logger.getLogger(WindowPlayer.class));

	/**
	 * The lock name
	 */
	private static final String LOCK_NAME = "SoundLooper_mono_instance";

	/**
	 * Entry point
	 * @param args not used
	 * @throws AlreadyLockedException if the application is already launched
	 * @throws IOException If an uncknown error is threw
	 */
	public static void main(String[] args) throws AlreadyLockedException, IOException {
		SoundLooper.logger.info("Démarrage du SoundLooper");
		SoundLooper.timeMeasurer.startMeasure("Lock");
		Lock.lock(SoundLooper.LOCK_NAME);
		SoundLooper.initializePreference();
		SoundLooper.initializeDatabase();

		new WindowPlayer().setVisible(true);

	}

	/**
	 * Initialize the database
	 */
	private static void initializeDatabase() {
		try {
			ConnectionFactory.updateDB();
		} catch (SQLException e) {
			SoundLooper.logger.error("Error when try to initialize db", e);
			e.printStackTrace();
		} catch (IOException e) {
			SoundLooper.logger.error("Error when try to initialize db", e);
			e.printStackTrace();
		}
		//init the database connection (au cas où il n'y ai pas eu de mise à jour dans le updateUD)
		SwingWorker<Object, Object> databaseInitWorker = new SwingWorker<Object, Object>() {
			@Override
			protected Object doInBackground() {

				try {
					SoundLooper.logger.info("Début d'initialisation de la bdd");
					ConnectionFactory.getNewStatement();
					SoundLooper.logger.info("Fin d'initialisation de la bdd");
				} catch (SQLException e) {
					SoundLooper.logger.error("Error when try to initialize db", e);
				}
				return null;
			}
		};
		databaseInitWorker.execute();
	}

	/**
	 * Initialize the preference
	 */
	private static void initializePreference() {
		//init the properties
		SwingWorker<Object, Object> preferenceInitWorker = new SwingWorker<Object, Object>() {
			@Override
			protected Object doInBackground() {
				SoundLooper.logger.info("Debut d'initialisation des properties");
				Preferences.getInstance();
				SoundLooper.logger.info("Fin d'initialisation des properties");
				return null;
			}
		};
		preferenceInitWorker.execute();
	}

}
