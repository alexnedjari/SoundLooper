/**
 *
 */
package com.soundlooper.aide;

/**
 *-------------------------------------------------------
 *Sound Looper is an audio player that allow user to loop between two points
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
 * Represente une combinaison de touche d'un raccourci clavier
 *
 * @author Alexandre NEDJARI
 * @since  18 nov. 2012
 *-------------------------------------------------------
 */
public class AideToucheRaccourciClavier {
	/**
	 * Touche utilisé pour le raccourci
	 */
	private String touche;

	/**
	 * Touche ctrl utilisée dans ce raccourci?
	 */
	private boolean ctrlUtilise;

	/**
	 * Touche maj utilisée dans ce raccourci?
	 */
	private boolean majUtilise;

	/**
	 * Touche alt utilisée dans ce raccourci?
	 */
	private boolean altUtilise;

	/**
	 * @param touche Touche utilisé pour le raccourci
	 * @param ctrlUtilise Touche ctrl utilisée dans ce raccourci?
	 * @param majUtilise Touche maj utilisée dans ce raccourci?
	 * @param altUtilise Touche alt utilisée dans ce raccourci?
	 */
	public AideToucheRaccourciClavier(String touche, boolean ctrlUtilise, boolean majUtilise, boolean altUtilise) {
		super();
		this.touche = touche;
		this.ctrlUtilise = ctrlUtilise;
		this.majUtilise = majUtilise;
		this.altUtilise = altUtilise;
	}

	/**
	 *
	 * @return La touche utilisée
	 */
	public String getTouche() {
		return this.touche;
	}

	/**
	 *
	 * @return true si ctrl est utilisé dans le raccourci
	 */
	public boolean isCtrlUtilise() {
		return this.ctrlUtilise;
	}

	/**
	 *
	 * @return true si maj est utilisé dans le raccourci
	 */
	public boolean isMajUtilise() {
		return this.majUtilise;
	}

	/**
	 *
	 * @return true si alt est utilisé dans le raccourci
	 */
	public boolean isAltUtilise() {
		return this.altUtilise;
	}
}
