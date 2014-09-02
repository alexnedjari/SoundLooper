/**
 *
 */
package com.soundlooper.aide;

import java.util.Arrays;
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
 * Représente un raccourci clavier, utilisé pour la génération de l'aide
 *
 * @author Alexandre NEDJARI
 * @since  18 nov. 2012
 *-------------------------------------------------------
 */
public class AideRaccourciClavier {

	/**
	 * Description du raccourci
	 */
	private String description;

	/**
	 * Liste des touches de ce raccourci clavier
	 */
	private List<AideToucheRaccourciClavier> listeTouchesRaccourcisClavier;

	/**
	 *
	 * @param description Description du raccourci
	 * @param aideToucheRaccourciClavier touche du raccouric clavier
	 */
	public AideRaccourciClavier(String description, AideToucheRaccourciClavier aideToucheRaccourciClavier) {
		this(description, Arrays.asList(aideToucheRaccourciClavier));

	}

	/**
	 *
	 * @param description Description du raccourci
	 * @param listeTouchesRaccourcisClavier Liste des touches du raccourci clavier
	 */
	public AideRaccourciClavier(String description, List<AideToucheRaccourciClavier> listeTouchesRaccourcisClavier) {
		super();
		this.description = description;
		this.listeTouchesRaccourcisClavier = listeTouchesRaccourcisClavier;

	}

	/**
	 *
	 * @return la description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 *
	 * @return la liste des touches de ce raccourci clavier
	 */
	public List<AideToucheRaccourciClavier> getListeTouchesRaccourcisClavier() {
		return this.listeTouchesRaccourcisClavier;
	}

}
