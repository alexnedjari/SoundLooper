/**
 *
 */
package com.soundlooper.aide;

import java.io.IOException;
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
 * @author Alexandre NEDJARI
 * @since  18 nov. 2012
 *-------------------------------------------------------
 */
public class GenererAide {

	/**
	 * @param args non utilis�
	 * @throws IOException Si la g�n�ration �choue
	 */
	public static void main(String[] args) throws IOException {
		GenererAide.genererAideRaccourcisClavier();
		GenererAide.genererAide();
		System.out.println("g�n�ration termin�e");
	}

	/**
	 * @throws IOException Si la g�n�ration �choue
	 *
	 */
	private static void genererAideRaccourcisClavier() throws IOException {
		List<AideSectionRaccourciClavier> listeSectionRaccourciClavier = new ArrayList<AideSectionRaccourciClavier>();

		AideSectionRaccourciClavier sectionGeneral = new AideSectionRaccourciClavier("G�n�ral");
		listeSectionRaccourciClavier.add(sectionGeneral);
		sectionGeneral.getListeRaccourcisClavier().add(new AideRaccourciClavier("Ouvrir un fichier", new AideToucheRaccourciClavier("O", true, false, false)));

		AideSectionRaccourciClavier sectionLecteur = new AideSectionRaccourciClavier("Lecteur");
		listeSectionRaccourciClavier.add(sectionLecteur);
		sectionLecteur.getListeRaccourcisClavier().add(new AideRaccourciClavier("Lecture/Pause", new AideToucheRaccourciClavier("Espace", false, false, false)));
		sectionLecteur.getListeRaccourcisClavier().add(
				new AideRaccourciClavier("Augmenter la vitesse de lecture de 1%", new AideToucheRaccourciClavier("Haut", true, false, false)));
		sectionLecteur.getListeRaccourcisClavier().add(
				new AideRaccourciClavier("Augmenter la vitesse de lecture de 10%", new AideToucheRaccourciClavier("Haut", true, true, false)));
		sectionLecteur.getListeRaccourcisClavier().add(new AideRaccourciClavier("Diminuer la vitesse de lecture de 1%", new AideToucheRaccourciClavier("Bas", true, false, false)));
		sectionLecteur.getListeRaccourcisClavier().add(new AideRaccourciClavier("Diminuer la vitesse de lecture de 10%", new AideToucheRaccourciClavier("Bas", true, true, false)));
		sectionLecteur.getListeRaccourcisClavier().add(new AideRaccourciClavier("Monter le volume", new AideToucheRaccourciClavier("+", false, false, false)));
		sectionLecteur.getListeRaccourcisClavier().add(new AideRaccourciClavier("Baisser le volume", new AideToucheRaccourciClavier("-", false, false, false)));
		sectionLecteur.getListeRaccourcisClavier().add(
				new AideRaccourciClavier("Placer la lecture sur le curseur de d�but", new AideToucheRaccourciClavier("Origine", false, false, false)));

		AideSectionRaccourciClavier sectionRecherche = new AideSectionRaccourciClavier("Recherches");
		listeSectionRaccourciClavier.add(sectionRecherche);
		sectionRecherche.getListeRaccourcisClavier().add(new AideRaccourciClavier("Rechercher dans les favoris", new AideToucheRaccourciClavier("F", true, false, false)));
		sectionRecherche.getListeRaccourcisClavier().add(new AideRaccourciClavier("Rechercher dans les marqueurs", new AideToucheRaccourciClavier("M", true, false, false)));

		AideSectionRaccourciClavier sectionEdition = new AideSectionRaccourciClavier("Editions");
		listeSectionRaccourciClavier.add(sectionEdition);
		sectionEdition.getListeRaccourcisClavier().add(
				new AideRaccourciClavier("Ajouter/supprimer la chanson actuelle des favoris", new AideToucheRaccourciClavier("F", false, false, false)));
		sectionEdition.getListeRaccourcisClavier().add(
				new AideRaccourciClavier("Ajouter un marqueur aux positions actuelles des curseurs", new AideToucheRaccourciClavier("M", false, false, false)));
		sectionEdition.getListeRaccourcisClavier().add(
				new AideRaccourciClavier("Placer le marqueur de d�but � la position de lecture courante", new AideToucheRaccourciClavier("1", true, false, false)));
		sectionEdition.getListeRaccourcisClavier().add(
				new AideRaccourciClavier("Placer le marqueur de fin � la position de lecture courante", new AideToucheRaccourciClavier("2", true, false, false)));

		new GenerateurAideRaccourciClavier().lancerGeneration(listeSectionRaccourciClavier);
	}

	/**
	 * G�n�re l'aide
	 * @throws IOException Si la g�n�ration �choue
	 */
	private static void genererAide() throws IOException {
		Aide aide = new Aide();
		aide.addParagraphe(new AideParagraphe("Introduction", "Bienvenue dans le SoundLooper!",
				"Ce logiciel s'adresse avant tout aux musiciens, pour le d�chiffrage et le travail de parties de musiques.",
				"Un certain nombre d'outils sont donc disponibles pour faciliter le travail de l'utilisateur."));
		aide.addParagraphe(new AideParagraphe("Lecture d'une musique",
				"Afin d'ouvrir une musique, on peut utiliser le bouton repr�sentant un dossier (Clic ou glisser d�poser de la musique sur le bouton).",
				"Une fois la musique charg�e, il est possible de :", "    - Lire gr�ce au bouton de lecture", "    - Mettre en pause gr�ce au bouton pause",
				"    - Modifier le volume", "    - Modifier la vitesse de lecture sans modifier la hauteur"));
		aide.addParagraphe(new AideParagraphe(
				"Boucler sur une partie d'une musique",
				"L'inter�t premier du logiciel est de pouvoir boucler sur une partie d'une chanson.",
				"Pour cela, deux curseurs sont disponibles, afin de d�terminer les point de d�but et de fin de la lecture dans la chanson. Une fois les curseurs de boucle positionn�s, la lecture se fera en boucle entre eux"));
		aide.addParagraphe(new AideParagraphe("Gestion des favoris",
				"L'ic�ne en forme d'�toile permet d'ajouter/supprimer une musique des favoris. Une musique favorite peut �tre retrouv�e facilement de deux mani�res : ",
				"    - Gr�ce au bouton en forme de double fl�che attenant au bouton en forme d'�toile (Liste des favoris)",
				"    - Gr�ce � la fen�tre de recherche de favoris (Recherche textuelle accessible depuis le menu 'Recherche/Rechercher dans les favoris')"));
		aide.addParagraphe(new AideParagraphe(
				"Gestion des marqueurs",
				"Un marqueur est la sauvegarde d'une position des curseurs, il est ainsi possible de naviguer facilement entre les diff�rentes parties d'une chanson. Les marqueurs ne peuvent �tre g�r�s que sur les musiques favorites. Si une musique est supprim�e de la liste des favoris, ses marqueurs sont d�finitivement perdus � la fermeture du logiciel.",
				"L'ic�ne en forme de + permet d'ajouter/supprimer une position des marqueurs d'une chanson. Un marqueur peut �tre retrouv� facilement de deux mani�res : ",
				"    - Gr�ce au bouton en forme de double fl�che attenant au bouton en forme de + (Liste des marqueurs de la chanson)",
				"    - Gr�ce � la fen�tre de recherche de marqueurs (Recherche textuelle accessible depuis le menu 'Recherche/Rechercher dans les marqueurs')",
				"",
				"Un marqueur \"Chanson compl�te\" est toujours disponible, c'est celui charg� par d�faut.",
				"Le marqueur actuellement charg� est indiqu� dans la barre de titre. Quand un curseur est d�plac�, il est possible :",
				"    - De sauvegarder un nouveau marqueur aux positions actuelles en cliquant sur le bouton \"+\"",
				"    - De modifier le marqueur actuellement charg� en cliquant sur le bouton \"Sauvegarder le marqueur courant\". Ce bouton est uniquement disponible si le marqueur actuellement charg� est un marqueur modifiable"));
		new GenerateurAide().lancerGeneration(aide);
	}
}
