package com.soundlooper.gui.jplayer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 *
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
 * UI for the JPlayer component
 *
 * @author Alexandre NEDJARI
 * @since  28 août 2014
 *-------------------------------------------------------
 */
public class JPlayerUI extends ComponentUI implements MouseListener, MouseMotionListener, ComponentListener {

	protected JPlayer jPlayer = null;

	// Objet contenant l'élement actuellement survolé
	private Object elementSurvole = null;

	//Objet contenant l'élément actuellement draggé
	private Object elementDrag = null;

	//lors du survol, sauvegarde la coordonnée x de la souris pour afficher
	// la barre verticale sur l'image sous le pointeur
	private int xSourisPx = 0;

	//Permet de connaitre l'écart entre la valeur de l'élément draggé et la position de la souris
	//au début du drag (permet de savoir à quel endroit la poignée a été "attrapée"
	private int ecartSourisValeurDragPx;

	// Elements graphiques
	private Polygon poigneeGauche = new Polygon();
	private Polygon poigneeDroite = new Polygon();
	private Rectangle2D rectangleSegment = new Rectangle();
	private Rectangle2D rectangleImage = new Rectangle();

	//Valeurs en pixels (cache pour limiter les calculs)
	private double largeurGraduationPx = 1;
	private int valeurSliderGauchePx = 0;
	private int valeurSliderDroitePx = 0;
	private int largeurAffichablePx = 1;
	private int hauteurImagePx = 1;
	private int basImagePx = 1;
	private int droiteReglePx = 1;

	// Dimensions fixes.
	private final static int HAUTEUR_REGLE = 13;
	public final static int MARGE_GAUCHE = 17;
	private final static int MARGE_HAUT = 13;
	public final static int MARGE_DROITE = 18;
	private final static int MARGE_BAS = 3;
	private final static int HAUT_IMAGE = JPlayerUI.MARGE_HAUT + JPlayerUI.HAUTEUR_REGLE;

	public JPlayerUI(JPlayer jPlayer) {
		this.jPlayer = jPlayer;
		this.positionneCurseurs();

		this.jPlayer.addMouseListener(this);
		this.jPlayer.addMouseMotionListener(this);
		this.jPlayer.addComponentListener(this);
	}

	/**
	 * Change les valeurs des curseurs
	 */
	public void positionneCurseurs() {

		//Mise à jour des valeurs
		this.valeurSliderGauchePx = this.getValeurPxFromValeur(this.jPlayer.getValeurSliderGauche());
		this.valeurSliderDroitePx = this.getValeurPxFromValeur(this.jPlayer.getValeurSliderDroite());

		this.rectangleSegment.setRect(this.valeurSliderGauchePx, JPlayerUI.MARGE_HAUT + 1,
				this.valeurSliderDroitePx - this.valeurSliderGauchePx - 2, JPlayerUI.HAUTEUR_REGLE - 2);

		int hauteurCurseur = 18;
		int largeurCurseur = 18;
		int distanceHautRegle = 2; //distance en px entre le haut de la règle et le haut des curseurs
		int hautRegle = JPlayerUI.MARGE_HAUT - distanceHautRegle;

		this.poigneeGauche.reset();
		this.poigneeGauche.addPoint(this.valeurSliderGauchePx, hautRegle);
		this.poigneeGauche.addPoint(this.valeurSliderGauchePx - largeurCurseur, hautRegle);
		this.poigneeGauche.addPoint(this.valeurSliderGauchePx, hautRegle + hauteurCurseur);
		this.poigneeGauche.addPoint(this.valeurSliderGauchePx, this.jPlayer.getHeight() - 4);
		this.poigneeGauche.addPoint(this.valeurSliderGauchePx, hautRegle + hauteurCurseur);
		this.poigneeGauche.addPoint(this.valeurSliderGauchePx, hautRegle);

		this.poigneeDroite.reset();
		this.poigneeDroite.addPoint(this.valeurSliderDroitePx, hautRegle);
		this.poigneeDroite.addPoint(this.valeurSliderDroitePx + largeurCurseur, hautRegle);
		this.poigneeDroite.addPoint(this.valeurSliderDroitePx, hautRegle + hauteurCurseur);
		this.poigneeDroite.addPoint(this.valeurSliderDroitePx, this.jPlayer.getHeight() - 4);
		this.poigneeDroite.addPoint(this.valeurSliderDroitePx, hautRegle + hauteurCurseur);
		this.poigneeDroite.addPoint(this.valeurSliderDroitePx, hautRegle);
	}

	private int getValeurPxFromValeur(double valeur) {
		return (int) (JPlayerUI.MARGE_GAUCHE + (valeur * this.largeurGraduationPx));
	}

	private double getValeurFromValeurPx(double valeurPx) {
		return (valeurPx - JPlayerUI.MARGE_GAUCHE) / this.largeurGraduationPx;
	}

	@Override
	public void update(Graphics graphics_Arg, JComponent comp) {
		this.paint(graphics_Arg);
	}

	public void paint(Graphics g) {
		if (g == null) {
			return;
		}
		Graphics2D graphics2d = (Graphics2D) g;
		Font font = this.jPlayer.getFont();
		Font fontGras = new Font(font.getName(), Font.BOLD, font.getSize());
		FontMetrics fontMetrics = graphics2d.getFontMetrics();

		double valeurSliderGauche = this.jPlayer.getValeurSliderGauche();
		double valeurSliderDroite = this.jPlayer.getValeurSliderDroite();

		RenderingHints renderingHints = new RenderingHints(null);
		renderingHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		graphics2d.setRenderingHints(renderingHints);

		//AFFICHAGE DE LA ZONE IMAGE
		if (this.jPlayer.isGenerationEnCours()) {
			graphics2d.setColor(this.jPlayer.getColor());
			graphics2d.fillRect(JPlayerUI.MARGE_GAUCHE, JPlayerUI.HAUT_IMAGE, this.largeurAffichablePx, this.hauteurImagePx);
			graphics2d.setColor(Color.WHITE);
			graphics2d.setFont(fontGras);
			graphics2d.drawString("Génération de l'image en cours", JPlayerUI.MARGE_GAUCHE + 10, JPlayerUI.HAUT_IMAGE + 30);
		} else if (this.jPlayer.getImage() != null) {
			graphics2d.drawImage(this.jPlayer.getImage(), JPlayerUI.MARGE_GAUCHE, JPlayerUI.HAUT_IMAGE,
					this.largeurAffichablePx, this.hauteurImagePx, null);
		} else {
			graphics2d.setColor(new Color(36, 168, 206));
			graphics2d.fillRect(JPlayerUI.MARGE_GAUCHE, JPlayerUI.HAUT_IMAGE, this.largeurAffichablePx, this.hauteurImagePx);
		}

		//AFFICHAGE DU FOND DE LA REGLE
		graphics2d.setColor(this.jPlayer.getBackgroundColor());
		graphics2d.fillRoundRect(JPlayerUI.MARGE_GAUCHE, JPlayerUI.MARGE_HAUT, this.largeurAffichablePx, JPlayerUI.HAUTEUR_REGLE, 0, 0);

		//AFFICHAGE DE LA ZONE SELECTIONNEE SUR LA REGLE
		graphics2d.setColor(this.jPlayer.getColor());
		if (valeurSliderGauche < valeurSliderDroite) {
			graphics2d.fill(new Rectangle(this.valeurSliderGauchePx, JPlayerUI.MARGE_HAUT + 1, this.valeurSliderDroitePx - this.valeurSliderGauchePx, JPlayerUI.HAUTEUR_REGLE - 1));
		}

		//DEBUT AFFICHAGE CONTOUR CHAMP ET SEPARATION INTERIEURE ENTRE REGLE ET IMAGE
		graphics2d.setColor(Color.BLACK);
		graphics2d.drawRoundRect(JPlayerUI.MARGE_GAUCHE, JPlayerUI.MARGE_HAUT, this.largeurAffichablePx,
				JPlayerUI.HAUTEUR_REGLE + this.hauteurImagePx, 0, 0);
		graphics2d.setColor(Color.DARK_GRAY);
		graphics2d.drawLine(JPlayerUI.MARGE_GAUCHE, JPlayerUI.HAUT_IMAGE, this.droiteReglePx, JPlayerUI.HAUT_IMAGE);
		graphics2d.setColor(Color.BLACK);

		//DESSIN DES CURSEURS
		this.dessinerCurseur(graphics2d, this.poigneeGauche);
		this.dessinerCurseur(graphics2d, this.poigneeDroite);

		// DESSIN DES OMBRES DES CURSEURS
		if (this.elementSurvole == this.poigneeGauche) {
			graphics2d.setColor(SystemColor.control.darker().darker());
			graphics2d.drawLine(this.poigneeGauche.xpoints[0] + 1, this.poigneeGauche.ypoints[0] + 2, this.poigneeGauche.xpoints[2] + 1, this.poigneeGauche.ypoints[2] - 1);
		} else if (this.elementSurvole == this.poigneeDroite) {
			graphics2d.setColor(SystemColor.control.darker().darker());
			graphics2d.drawLine(this.poigneeDroite.xpoints[1] + 1, this.poigneeDroite.ypoints[1], this.poigneeDroite.xpoints[2] + 1, this.poigneeDroite.ypoints[2]);
		}

		//Affichage sur drag de la valeur en mm:ss
		graphics2d.setFont(font);
		if (this.elementDrag == this.poigneeDroite || this.elementDrag == this.rectangleSegment) {
			String valeurMMSS = this.jPlayer.getDecimalFormater().format(valeurSliderDroite);
			graphics2d.setColor(Color.BLACK);
			graphics2d.drawString(valeurMMSS, this.valeurSliderDroitePx - 32, JPlayerUI.MARGE_HAUT + fontMetrics.getAscent() - 3);
		}

		if (this.elementDrag == this.poigneeGauche || this.elementDrag == this.rectangleSegment) {
			String valeurMMSS = this.jPlayer.getDecimalFormater().format(valeurSliderGauche);
			graphics2d.setColor(Color.BLACK);
			graphics2d.drawString(valeurMMSS, this.valeurSliderGauchePx + 5, JPlayerUI.MARGE_HAUT + fontMetrics.getAscent() - 3);
		}

		//AFFICHAGE DU CURSEUR DE LECTURE
		graphics2d.setColor(Color.RED);
		int coordonneeX = this.getValeurPxFromValeur(this.jPlayer.getValeur());
		graphics2d.drawLine(coordonneeX, JPlayerUI.HAUT_IMAGE + 1, coordonneeX, this.basImagePx - 1);

		//AFFICHAGE DU CURSEUR A LA POSITION DE LA SOURIS
		if (this.elementSurvole == this.rectangleImage) {
			graphics2d.setColor(Color.GRAY);
			graphics2d.drawLine(this.xSourisPx, JPlayerUI.HAUT_IMAGE + 1, this.xSourisPx, this.basImagePx - 2);
		}
	}

	private void dessinerCurseur(Graphics2D graphics2d, Polygon curseur) {
		//REMPLISSAGE
		graphics2d.setColor(SystemColor.control);
		if (this.elementDrag == curseur) {
			graphics2d.setColor(MetalLookAndFeel.getPrimaryControlShadow());
		} else if (this.elementSurvole == curseur) {
			graphics2d.setColor(MetalLookAndFeel.getControlHighlight());
		}
		graphics2d.fillPolygon(curseur);

		//DESSIN DES CONTOURS
		graphics2d.setColor(Color.BLACK);
		graphics2d.drawPolygon(curseur);
	}

	@Override
	public void mousePressed(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
			if (this.rectangleImage.contains(mouseEvent.getX(), mouseEvent.getY())) { //Clic dans l'image
				int valeurPixel = mouseEvent.getX() - JPlayerUI.MARGE_GAUCHE;
				float pourcentage = new Float(valeurPixel) / this.largeurAffichablePx;
				double valeur = this.jPlayer.getMaximumValue() * pourcentage;
				this.jPlayer.changeValeur(valeur);
			} else if (this.poigneeGauche.contains(mouseEvent.getX(), mouseEvent.getY())) { //drag poignée gauche
				this.elementDrag = this.poigneeGauche;
				this.ecartSourisValeurDragPx = mouseEvent.getX() - this.valeurSliderGauchePx;
			} else if (this.poigneeDroite.contains(mouseEvent.getX(), mouseEvent.getY())) { // drag poignée droite
				this.elementDrag = this.poigneeDroite;
				this.ecartSourisValeurDragPx = mouseEvent.getX() - this.valeurSliderDroitePx;
			} else if (this.rectangleSegment.contains(mouseEvent.getX(), mouseEvent.getY())) { //drag selection
				this.elementDrag = this.rectangleSegment;
				//Quand le segment est déplacé, on prend arbitrairement le slider gauche comme repère
				this.ecartSourisValeurDragPx = mouseEvent.getX() - this.valeurSliderGauchePx;
			} else {
				return;
			}
		}
		this.jPlayer.repaint();
		mouseEvent.consume();
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		//Nouvelle position de l'élement déplacé (valeur curseur gauche, droite ou gauche dans le cas d'un drag du segment)
		int nouvellePositionPx = event.getX() - this.ecartSourisValeurDragPx;
		int valueMinimumEntreCurseurPx = new Double(jPlayer.getMinimumBetweenCursors() * this.largeurGraduationPx).intValue();
		if (valueMinimumEntreCurseurPx == 0) {
			valueMinimumEntreCurseurPx = 1;
		}

		if (this.elementDrag == this.poigneeGauche) { // Drag de la valeur min
			if (nouvellePositionPx > this.valeurSliderDroitePx - valueMinimumEntreCurseurPx) {
				nouvellePositionPx = this.valeurSliderDroitePx - valueMinimumEntreCurseurPx;
			} else if (nouvellePositionPx < JPlayerUI.MARGE_GAUCHE) {
				nouvellePositionPx = JPlayerUI.MARGE_GAUCHE;
			}
			this.jPlayer.setValeurs(Math.ceil(this.getValeurFromValeurPx(nouvellePositionPx)), this.jPlayer.getValeurSliderDroite());

		} else if (this.elementDrag == this.poigneeDroite) { // Drag de la valeur max
			if (nouvellePositionPx < this.valeurSliderGauchePx + valueMinimumEntreCurseurPx) {
				nouvellePositionPx = this.valeurSliderGauchePx + valueMinimumEntreCurseurPx;
			} else if (nouvellePositionPx > this.droiteReglePx) {
				nouvellePositionPx = this.droiteReglePx;
			}

			this.jPlayer.setValeurs(this.jPlayer.getValeurSliderGauche(), Math.ceil(this.getValeurFromValeurPx(nouvellePositionPx)));
		} else if (this.elementDrag == this.rectangleSegment) { // Drag du segment
			int largeurZoneSelectionneePx = this.getValeurPxFromValeur(this.jPlayer.getValeurSliderDroite()) - this.getValeurPxFromValeur(this.jPlayer.getValeurSliderGauche());

			//Pour le segment, nouvellePositionPx correspond au bord gauche
			if (nouvellePositionPx < JPlayerUI.MARGE_GAUCHE) {
				nouvellePositionPx = JPlayerUI.MARGE_GAUCHE;
			} else if (nouvellePositionPx + largeurZoneSelectionneePx > this.droiteReglePx) {
				nouvellePositionPx = this.droiteReglePx - largeurZoneSelectionneePx;
			}
			double nouvellePositionDroitePx = nouvellePositionPx + largeurZoneSelectionneePx;
			this.jPlayer.setValeurs(Math.ceil(this.getValeurFromValeurPx(nouvellePositionPx)), Math.ceil(this.getValeurFromValeurPx(nouvellePositionDroitePx)));
		}

		this.jPlayer.repaint();
		event.consume();
	}

	@Override
	public void mouseReleased(MouseEvent MouseEvent_Arg) {
		if (elementDrag == poigneeDroite || elementDrag == poigneeGauche || elementDrag == rectangleSegment) {
			this.jPlayer.changeLoopPoints();
		}
		this.elementDrag = null;
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		this.xSourisPx = event.getX();
		if (this.rectangleImage.contains(event.getX(), event.getY())) {
			this.elementSurvole = this.rectangleImage;
		} else if (this.poigneeDroite.contains(event.getX(), event.getY())) {
			this.elementSurvole = this.poigneeDroite;
		} else if (this.poigneeGauche.contains(event.getX(), event.getY())) {
			this.elementSurvole = this.poigneeGauche;
		} else if (this.rectangleSegment != null && this.rectangleSegment.contains(event.getX(), event.getY())) {
			this.elementSurvole = this.rectangleSegment;
		} else {
			this.elementSurvole = null;
		}
		this.jPlayer.repaint();

	}

	@Override
	public void componentResized(ComponentEvent ComponentEvent_Arg) {
		//mise à jour de toutes les données membres impactées
		this.hauteurImagePx = this.jPlayer.getHeight() - JPlayerUI.MARGE_HAUT - JPlayerUI.HAUTEUR_REGLE - JPlayerUI.MARGE_BAS;
		this.basImagePx = this.jPlayer.getHeight() - JPlayerUI.MARGE_BAS;
		this.largeurAffichablePx = this.jPlayer.getSize().width - JPlayerUI.MARGE_GAUCHE - JPlayerUI.MARGE_DROITE;
		this.droiteReglePx = this.largeurAffichablePx + JPlayerUI.MARGE_GAUCHE;
		this.largeurGraduationPx = this.largeurAffichablePx / this.jPlayer.getMaximumValue();
		this.rectangleImage.setRect(JPlayerUI.MARGE_GAUCHE, JPlayerUI.HAUT_IMAGE, this.largeurAffichablePx, this.hauteurImagePx);
		this.positionneCurseurs();
		this.jPlayer.repaint();
	}

	public void onValeurMaxChanged() {
		this.largeurGraduationPx = this.largeurAffichablePx / this.jPlayer.getMaximumValue();
		this.positionneCurseurs();
		this.jPlayer.repaint();
	}

	@Override
	public void componentShown(ComponentEvent e) {/*Non géré*/
	}

	@Override
	public void componentHidden(ComponentEvent e) {/*Non géré*/
	}

	@Override
	public void componentMoved(ComponentEvent e) {/*Non géré*/
	}

	@Override
	public void mouseClicked(MouseEvent MouseEvent_Arg) {/*Non géré*/
		MouseEvent_Arg.consume();
	}

	@Override
	public void mouseEntered(MouseEvent MouseEvent_Arg) {/*Non géré*/
	}

	@Override
	public void mouseExited(MouseEvent MouseEvent_Arg) {
		this.elementSurvole = null;
		this.jPlayer.repaint();
	}

}
