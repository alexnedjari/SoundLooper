/**
 *
 */
package com.soundlooper.gui.fenapropos;


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
 * @since  18 déc. 2011
 *-------------------------------------------------------
 */
public class InformationLogiciel {
	String auteur;
	String nomApplication;
	String versionApplication;
	String iterationApplication;

	//ImageIcon image;
	//ImageIcon iconeFenetre;
	public InformationLogiciel(String auteur, String nomApplication, String versionApplication, String iterationApplication) {
		super();
		this.auteur = auteur;
		this.nomApplication = nomApplication;
		this.versionApplication = versionApplication;
		this.iterationApplication = iterationApplication;
	}

	public String getAuteur() {
		return this.auteur;
	}

	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}

	public String getNomApplication() {
		return this.nomApplication;
	}

	public void setNomApplication(String nomApplication) {
		this.nomApplication = nomApplication;
	}

	public String getVersionApplication() {
		return this.versionApplication;
	}

	public void setVersionApplication(String versionApplication) {
		this.versionApplication = versionApplication;
	}

	public String getIterationApplication() {
		return this.iterationApplication;
	}

	public void setIterationApplication(String iterationApplication) {
		this.iterationApplication = iterationApplication;
	}

	public String getCompleteVersionApplication() {
		String completeVersion = this.versionApplication;
		if (this.iterationApplication != null) {
			completeVersion += "." + this.iterationApplication;
		}
		return completeVersion;
	}
}
