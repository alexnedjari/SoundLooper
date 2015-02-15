/**
 *
 */
package com.soundlooper.aide;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

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
 * @since  18 nov. 2012
 *-------------------------------------------------------
 */
public class GenerateurAide {

	/**
	 * Lance la génération
	 * @param aide l'aide
	 * @throws IOException Si la génération échoue
	 */
	public void lancerGeneration(Aide aide) throws IOException {

		BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/main/resources/aide/aide.html"), "UTF-8"));
		
		
		

		output.write("<!DOCTYPE html><html><head><title>Aide</title><meta charset=\"UTF-8\"></head><body>");
		try {
			//TODO restaurer quand jtext pane pourra comprendre le css
			//			output.write("<div style='position:fixed;left:10px;right:200px;top:50px'>");
			int ancre = 0;
			//			for (AideParagraphe paragraphe : aide.getListeParagraphe()) {
			//				output.write("<a href='#" + ancre + "'>" + paragraphe.getTitre() + "</a><br>");
			//				ancre++;
			//			}
			//			output.write("</div>");
			//			output.write("<br>");
			//output.write("<div style='position:absolute;top:50px;left:240px;autoscroll:true;'>");
			ancre = 0;
			for (AideParagraphe paragraphe : aide.getListeParagraphe()) {
				output.write("<span id='" + ancre + "'><b><br>" + paragraphe.getTitre() + "</b><br><br>");
				String[] listeContenu = paragraphe.getContenu();
				for (String string : listeContenu) {
					output.write(string + "<br>");
				}
				output.write("<span>");
				ancre++;
			}
			//output.write("</div>");

			output.write("</body></html>");
		} finally {
			output.close();
		}

	}
}
