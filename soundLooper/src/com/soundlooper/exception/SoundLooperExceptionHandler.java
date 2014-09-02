package com.soundlooper.exception;

import java.awt.Window;
import java.io.File;

import org.apache.log4j.Logger;

import com.soundlooper.system.util.StackTracer;

/**
 * --------------------------------------------------------------------------------
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
 * Handler for uncaught exceptions
 *
 * @author Alexandre NEDJARI
 * @since 20 juil. 2011
 *--------------------------------------------------------------------------------
 */
public class SoundLooperExceptionHandler implements Thread.UncaughtExceptionHandler {

	/**
	 * the application name
	 */
	private String applicationName;

	/**
	 * Logger for this class
	 */
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * Fenêtre à masquer pour ne pas géner la lecture du message d'erreur
	 */
	private Window windowAMasquer;

	/**
	 * Constructor
	 * @param applicationName the application name
	 * @param windowAMasquer Fenêtre à masquer pour ne pas géner la lecture du message d'erreur
	 */
	public SoundLooperExceptionHandler(String applicationName, Window windowAMasquer) {
		super();
		this.applicationName = applicationName;
		this.windowAMasquer = windowAMasquer;
	}

	/**
	 * Action to perform on uncaught exceptions
	 * @param thread the thread
	 * @param e the uncaught throwable
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable e) {
		if (this.windowAMasquer != null) {
			this.windowAMasquer.setVisible(false);
		}

		String[] attachmentsURL;
		File logGUI = new File("logs" + File.separator + "GUI.log");
		File logGeneral = new File("logs" + File.separator + "general.log");
		File logDatabase = new File("logs" + File.separator + "database.log");
		File logPlayer = new File("logs" + File.separator + "player.log");
		File logSystem = new File("logs" + File.separator + "system.log");
		attachmentsURL = new String[] {
				logGUI.getAbsolutePath(),
				logGeneral.getAbsolutePath(),
				logDatabase.getAbsolutePath(),
				logPlayer.getAbsolutePath(),
				logSystem.getAbsolutePath(),
		};
		this.logger.error(StackTracer.getStackTrace(e));
		e.printStackTrace();
		new WindowsException(e, this.applicationName, attachmentsURL).setVisible(true);

	}
}
