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
 * Permet de gérer les sections de raccourcis clavier dans les fichiers d'aide
 *
 * @author Alexandre NEDJARI
 * @since  18 nov. 2012
 *-------------------------------------------------------
 */
public class AideSectionRaccourciClavier {

	/**
	 * Titre de la section
	 */
	private String titre;

	/**
	 * Liste des raccourcis claviers de la section
	 */
	List<AideRaccourciClavier> listeRaccourcisClavier = new ArrayList<AideRaccourciClavier>();

	/**
	 * Permet d'obtenir la liste des raccourcis clavier de la section
	 * @return les raccourcis
	 */
	public List<AideRaccourciClavier> getListeRaccourcisClavier() {
		return this.listeRaccourcisClavier;
	}

	/**
	 *
	 * @param titre le titre de la section
	 */
	public AideSectionRaccourciClavier(String titre) {
		super();
		this.titre = titre;
	}

	/**
	 *
	 * @return le titre de la section
	 */
	public String getTitre() {
		return this.titre;
	}

}
