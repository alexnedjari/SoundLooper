/**
 *
 */
package com.soundlooper.gui.action.player;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import org.apache.log4j.Logger;

import com.soundlooper.gui.WindowPlayer;
import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.system.FiltreAudio;
import com.soundlooper.system.preferences.Preferences;

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
 * The "Open file" action
 *
 * @author Alexandre NEDJARI
 * @since  30 juin 2012
 *-------------------------------------------------------
 */
public class OpenFileFromDialogAction extends AbstractAction {

	/**
	 * The instance (open file action is mono instance)
	 */
	private static OpenFileFromDialogAction instance;

	/**
	 * logger
	 */
	private static Logger logger = Logger.getLogger(OpenFileFromDialogAction.class);

	/**
	 * Parent window
	 */
	private WindowPlayer windowPlayer;

	/**
	 * @param windowPlayer
	 */
	public OpenFileFromDialogAction(WindowPlayer windowPlayer) {
		super();
		this.windowPlayer = windowPlayer;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser choix = new JFileChooser();
		choix.setFileSelectionMode(JFileChooser.FILES_ONLY);
		choix.setFileFilter(new FiltreAudio());

		if (!Preferences.getInstance().getLastPathUsed().equals("")) {
			choix.setSelectedFile(new File(Preferences.getInstance().getLastPathUsed()));
		}
		int retour = choix.showOpenDialog(this.windowPlayer);
		if (retour == JFileChooser.APPROVE_OPTION) {
			File selectedFile = choix.getSelectedFile();
			OpenFileFromDialogAction.logger.info("Fichier à charger : " + selectedFile);
			SoundLooperPlayer.getInstance().loadSong(selectedFile);

		}
		OpenFileFromDialogAction.logger.info("Pas de fichier choisi !");
	}
}
