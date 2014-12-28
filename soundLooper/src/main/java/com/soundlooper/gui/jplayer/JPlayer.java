package com.soundlooper.gui.jplayer;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

import javax.swing.JComponent;

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
 * Composant graphique contenant la barre avec les deux sliders, et la zone image
 *
 * @author Alexandre NEDJARI
 * @since  22 août 2014
 *-------------------------------------------------------
 */

public class JPlayer extends JComponent {

	/**numéro de série de la classe */
	private static final long serialVersionUID = 1L;

	/** Gestion de l'affichage et des évènements*/
	protected JPlayerUI jPlayerUI = null;


	/** Valeur maxi*/
	private double maximumValue = 100;

	/** Valeur courante*/
	private double valeur = 0;

	/**Valeur du curseur gauche*/
	private double valeurSliderGauche = 0;

	/**Valeur du curseur droite*/
	private double valeurSliderDroite = this.maximumValue;

	/**Couleur de fond*/
	private Color backgroundColor = new Color(230, 230, 230);

	/** Couleur de la zone sélectionnée*/
	private Color color = Color.WHITE;

	/** Formatteur */
	private DecimalFormat decimalFormater = new TimeDecimalFormat();
	
	/** Valeur maxi*/
	private double minimumBetweenCursors = 0;

	/**
	 * Flag permettant de savoir si la génération de l'image est en cours
	 * Dans ce cas, il faut noter "Génération de l'image en cours" à la place
	 * de l'image
	 */
	private boolean isGenerationEnCours = false;

	/**
	 * Image représentant la chanson temporellement
	 */
	private Image image;

	/**
	 * Constructor
	 */
	public JPlayer() {
		super();
		this.jPlayerUI = new JPlayerUI(this);
		this.setUI(this.jPlayerUI);
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				requestFocus();
				super.mouseClicked(e);
			}
		});
	}

	/**
	 * @return the DecimalFormat used to display numbers on the player
	 **/
	public DecimalFormat getDecimalFormater() {
		return this.decimalFormater;
	}

	/**
	 * Affecte la valeur des deux sliders
	 *
	 * @param valeurSliderGauche  la nouvelle valeur gauche
	 * @param valeurSliderDroite  la nouvelle valeur droite
	 */
	public void setValeurSlider(double valeurSliderGauche, double valeurSliderDroite) {
		if (valeurSliderGauche == this.valeurSliderGauche && valeurSliderDroite == this.valeurSliderDroite) {
			//même valeur qu'avant
			return;
		}
		
		if (valeurSliderGauche > valeurSliderDroite || valeurSliderDroite < valeurSliderGauche) {
			//valeurs incohérentes
			return;
		}
		
		
		
		this.valeurSliderDroite = valeurSliderDroite;
		this.valeurSliderGauche = valeurSliderGauche;
		this.jPlayerUI.positionneCurseurs();
		this.repaint();
	
	}
	
	/**
	 * Affecte la valeur du curseur de gauche
	 *
	 * @param valeurSliderGauche  la nouvelle valeur
	 */
	public void setValeurSliderGauche(double valeurSliderGauche) {
		if (valeurSliderGauche != this.valeurSliderGauche && valeurSliderGauche <= this.valeurSliderDroite) {
			this.valeurSliderGauche = valeurSliderGauche;
			this.jPlayerUI.positionneCurseurs();
			this.repaint();
		}
	}

	/**
	 * Récupère la valeur du curseur de gauche
	 * @return la valeur du curseur de gauche.
	 */
	public double getValeurSliderGauche() {
		return this.valeurSliderGauche;
	}

	/**
	 * Affecte la valeur du curseur de droite
	 *
	 * @param valeurSliderDroite la nouvelle valeur
	 */
	public void setValeurSliderDroite(double valeurSliderDroite) {
		if (valeurSliderDroite != this.valeurSliderDroite && valeurSliderDroite <= this.maximumValue) {
			this.valeurSliderDroite = valeurSliderDroite;
			this.jPlayerUI.positionneCurseurs();
			this.repaint();
		}
	}

	/**
	 * Récupère la valeur du curseur de droite
	 * @return la valeur du curseur de droite.
	 */
	public double getValeurSliderDroite() {
		return this.valeurSliderDroite;
	}

	/**
	 * Affecte les valeurs des curseurs gauche et droite
	 * @param valeurSliderGauche  la valeur du curseur de gauche
	 * @param valeurSliderDroite  la valeur du curseur de droite
	 */
	public void setValeurs(double valeurSliderGauche, double valeurSliderDroite) {
		if (valeurSliderGauche <= valeurSliderDroite && valeurSliderDroite <= this.maximumValue
				&& (valeurSliderGauche != this.valeurSliderGauche || this.valeurSliderDroite != valeurSliderDroite)) {
			this.valeurSliderGauche = valeurSliderGauche;
			this.valeurSliderDroite = valeurSliderDroite;
			this.jPlayerUI.positionneCurseurs();
			this.repaint();
		}
	}

	/**
	 * Affecte la valeur maximale du composant
	 * @param maximumValue The new maximumValue value
	 */
	public void setMaximumValue(double maximumValue) {
		this.maximumValue = maximumValue;

		if (maximumValue < this.valeurSliderGauche) {
			this.valeurSliderGauche = maximumValue;
		}

		if (maximumValue < this.valeurSliderDroite) {
			this.valeurSliderDroite = maximumValue;
		}

		this.jPlayerUI.onValeurMaxChanged();
		this.repaint();
	}

	/**
	 * Récupère la valeur maximale du composant
	 * @return  la valeur maximale du composant
	 */
	public double getMaximumValue() {
		return this.maximumValue;
	}

	/**
	 * Récupère la couleur de fond
	 * @return La couleur de fond
	 */
	public Color getBackgroundColor() {
		return this.backgroundColor;
	}

	/**
	 * Modifie la couleur de la zone sélectionnée
	 * @param color nouvelle couleur
	 */
	public void setColor(Color color) {
		if (this.color != color) {
			this.color = color;
			this.repaint();
		}
	}

	/**
	 * Récupère la couleur de la zone sélectionnée
	 * @return la couleur
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * @param l Le nouveau listener.
	 */
	public synchronized void addJPlayerListener(JPlayerListener l) {
        JPlayerSupport.getInstance().addJPlayerListener(l);

	}

	/**
	 * @param l le listener à supprimer.
	 */
	public synchronized void removeJPlayerListener(JPlayerListener l) {
		JPlayerSupport.getInstance().removeJPLayerListener(l);
	}

	/**
	 * @return the valeur
	 */
	public double getValeur() {
		return this.valeur;
	}

	/**
	 * @param valeur the valeur to set
	 */
	public void setValeur(double valeur) {
		this.valeur = valeur;
		this.repaint();
	}

	/**
	 * Change the value. The listener are notified
	 * @param valeur the valeur to set
	 */
	public void changeValeur(double newValeur) {
		setValeur(newValeur);
		JPlayerSupport.getInstance().fireNewValeur(newValeur);
	}

	/**
	 * Change the loop points value. The listener are notified
	 * @param valeur the valeur to set
	 */
	public void changeLoopPoints() {
		JPlayerSupport.getInstance().fireNewLoopPoints(valeurSliderGauche, valeurSliderDroite);
	}

	/**
	 * The song image generation is started, we must display it in the image zone
	 */
	public void startGenerateImage() {
		this.isGenerationEnCours = true;
		this.repaint();
	}

	/**
	 * @return the isGenerationEnCours
	 */
	public boolean isGenerationEnCours() {
		return this.isGenerationEnCours;
	}

	/**
	 * @return the image
	 */
	public Image getImage() {
		return this.image;
	}

	/**
	 * @param image the song image
	 */
	public void setImage(Image image) {
		this.image = image;
		this.isGenerationEnCours = false;
		this.repaint();
	}

	public double getMinimumBetweenCursors() {
		return minimumBetweenCursors;
	}

	public void setMinimumBetweenCursors(double minimumBetweenCursors) {
		this.minimumBetweenCursors = minimumBetweenCursors;
	}
}
