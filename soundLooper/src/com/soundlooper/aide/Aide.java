/**
 *
 */
package com.soundlooper.aide;

import java.util.ArrayList;
import java.util.List;

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
public class Aide {

	/**
	 * Liste des paragraphes
	 */
	private List<AideParagraphe> listeParagraphe = new ArrayList<AideParagraphe>();

	/**
	 * Ajoute un paragraphe
	 * @param paragraphe paragraphe
	 */
	public void addParagraphe(AideParagraphe paragraphe) {
		this.listeParagraphe.add(paragraphe);
	}

	/**
	 * Récupère la liste des paragraphes
	 * @return la liste
	 */
	public List<AideParagraphe> getListeParagraphe() {
		return this.listeParagraphe;
	}

}
