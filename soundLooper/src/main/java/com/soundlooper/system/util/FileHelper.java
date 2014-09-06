package com.soundlooper.system.util;

import java.io.File;

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
 * @author Alexandre NEDJARI
 * @since  28 août 2014
 *-------------------------------------------------------
 */
public class FileHelper {

	/**
	 * Permet d'obtenir l'extension d'un fichier
	 */
	public static String getExtension(File fichier) {
		String nomFichier = fichier.getName();
		String extension = nomFichier.substring(nomFichier.lastIndexOf('.') + 1);
		return extension;
	}
}
