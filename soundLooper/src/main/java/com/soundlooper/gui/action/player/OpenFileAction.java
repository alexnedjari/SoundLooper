/**
 *
 */
package com.soundlooper.gui.action.player;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;

import org.apache.log4j.Logger;

import com.soundlooper.model.SoundLooperPlayer;

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
 * The "Open file" action
 *
 * @author Alexandre NEDJARI
 * @since  30 juin 2012
 *-------------------------------------------------------
 */
public class OpenFileAction extends AbstractAction {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger
	 */
	Logger logger = Logger.getLogger(OpenFileAction.class);

	@Override
	public void actionPerformed(ActionEvent e) {
		File recentFile = new File(e.getActionCommand());
		if (!recentFile.exists()) {
			OpenFileAction.this.logger.info("Le fichier '" + recentFile.getAbsolutePath() + "' n'existe pas");
		}

		OpenFileAction.this.logger.info("Fichier à charger : " + recentFile);
		SoundLooperPlayer.getInstance().loadSong(recentFile);

	}
}
