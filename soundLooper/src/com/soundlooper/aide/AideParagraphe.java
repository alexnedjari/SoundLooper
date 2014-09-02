/**
 *
 */
package com.soundlooper.aide;

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
 * @since  28 déc. 2012
 *-------------------------------------------------------
 */
public class AideParagraphe {
	/**
	 * Titre
	 */
	private String titre;

	/**
	 * Contenu
	 */
	private String[] contenu;

	/**
	 * @param titre titre
	 * @param contenu contenu
	 */
	public AideParagraphe(String titre, String... contenu) {
		super();
		this.titre = titre;
		this.contenu = contenu;
	}

	/**
	 *
	 * @return le contenu
	 */
	public String[] getContenu() {
		return this.contenu;
	}

	/**
	 *
	 * @return le titre
	 */
	public String getTitre() {
		return this.titre;
	}

}
