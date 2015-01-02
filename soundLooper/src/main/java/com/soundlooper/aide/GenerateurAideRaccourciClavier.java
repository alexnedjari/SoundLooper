/**
 *
 */
package com.soundlooper.aide;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
 * @author Alexandre NEDJARI
 * @since  18 nov. 2012
 *-------------------------------------------------------
 */
public class GenerateurAideRaccourciClavier {

	/**
	 * Lance la génération
	 * @param listeSectionRaccourciClavier la liste des sections à générer
	 * @throws IOException Si la génération échoue
	 */
	public void lancerGeneration(List<AideSectionRaccourciClavier> listeSectionRaccourciClavier) throws IOException {
		BufferedWriter output = new BufferedWriter(new FileWriter("src/main/resources/aide/listeRaccourciClavier.html"));
		int largeurTable = 550;
		int largeurColonneLibelle = 400;
		try {
			output.write("<html><head><title>Liste des raccourcis clavier</title></head><body><h2><center>Liste des raccourcis claviers disponibles</center></h2>");

			for (AideSectionRaccourciClavier aideSectionRaccourciClavier : listeSectionRaccourciClavier) {
				output.write("<br/><table border='0' align='center' width='" + largeurTable + "px'><tr><td colspan='2'><h3><b>" + aideSectionRaccourciClavier.getTitre()
						+ " :</b></h3></td></tr>");

				List<AideRaccourciClavier> listeRaccourcisClavier = aideSectionRaccourciClavier.getListeRaccourcisClavier();
				for (AideRaccourciClavier aideRaccourciClavier : listeRaccourcisClavier) {
					output.write("<tr><td width='" + largeurColonneLibelle + "px'>" + aideRaccourciClavier.getDescription() + " : </td><td>");
					List<AideToucheRaccourciClavier> listeTouchesRaccourcisClavier = aideRaccourciClavier.getListeTouchesRaccourcisClavier();

					boolean premiereTouche = true;
					for (AideToucheRaccourciClavier aideToucheRaccourciClavier : listeTouchesRaccourcisClavier) {
						if (premiereTouche) {
							premiereTouche = false;
						} else {
							output.write(", ");
						}

						if (aideToucheRaccourciClavier.isCtrlUtilise()) {
							output.write("CTRL + ");
						}

						if (aideToucheRaccourciClavier.isAltUtilise()) {
							output.write("ALT + ");
						}

						if (aideToucheRaccourciClavier.isMajUtilise()) {
							output.write("MAJ + ");
						}
						output.write(aideToucheRaccourciClavier.getTouche());
					}
					output.write("</td>");
				}

				output.write("</table>");
			}

			output.write("</body></html>");
		} finally {
			output.close();
		}

	}
}
