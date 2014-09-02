/**
 *
 */
package com.soundlooper.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;

import com.soundlooper.exception.SoundLooperException;
import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.system.FiltreAudio;
import com.soundlooper.system.util.FileHelper;

/**
 *-------------------------------------------------------
 *
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
 * @author Alexandre NEDJARI
 * @since  23 nov. 2012
 *-------------------------------------------------------
 */
public class FileTransfertHandler extends TransferHandler {
	/**
	 * sérial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canImport(JComponent cp, DataFlavor[] df) {
		for (int i = 0; i < df.length; i++) {
			if (df[i].equals(DataFlavor.javaFileListFlavor)) {
				return true;
			}
			//			if (df[i].equals(DataFlavor.stringFlavor)) {
			//				return true;
			//			}
		}
		return false;
	}

	/**
	 *
	 * @param df dataFlavor
	 * @return true si c'est un fichier
	 */
	private boolean hasFileFlavor(DataFlavor[] df) {
		boolean result = false;
		for (DataFlavor flavor : df) {
			result = DataFlavor.javaFileListFlavor.equals(flavor);
			if (result) {
				break;
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean importData(JComponent cp, Transferable tr) {
		if (this.hasFileFlavor(tr.getTransferDataFlavors())) {
			try {
				List<File> files = (List<File>) (tr.getTransferData(DataFlavor.javaFileListFlavor));
				for (File file : files) {
					this.openFile(cp, file);
					break;
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		return false;
	}

	/**
	 * Ouvre un fichier
	 * @param cp le composant
	 * @param file le fichier à ouvrir
	 * @throws SoundLooperException Si ça se passe mal
	 */
	private void openFile(JComponent cp, File file) throws SoundLooperException {
		boolean extensionAutorisee = false;
		String extensionFichier = FileHelper.getExtension(file);
		for (String extension : FiltreAudio.SUFFIX_LIST) {
			if (extensionFichier.toUpperCase().equals(extension.toUpperCase())) {
				extensionAutorisee = true;
			}
		}
		if (!extensionAutorisee) {
			String message = "Le type de fichier (" + extensionFichier + ") n'est pas pris en charge";
			JOptionPane.showMessageDialog(cp, message, "Impossible de charger le fichier", JOptionPane.ERROR_MESSAGE);
			throw new SoundLooperException(message);
		}

		if (file.length() > 60000000) {
			JOptionPane.showMessageDialog(cp, "Le fichier est trop volumineux pour être chargé", "Impossible de charger le fichier", JOptionPane.ERROR_MESSAGE);
			throw new SoundLooperException("Le fichier à charger est trop gros");
		}
		SoundLooperPlayer.getInstance().loadSong(file);
	}
}
