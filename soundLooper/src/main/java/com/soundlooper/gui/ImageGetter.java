package com.soundlooper.gui;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 *-------------------------------------------------------
 * Class used to have icons path
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
 * @since  02 jan. 2015
 *-------------------------------------------------------
 */
public class ImageGetter {
	public static final String ICONE_SOUND_LOOPER_64 = "/icons/soundLooper64.png";
	public static final String ICONE_ERREUR_16 = "/icons/redcross-16.png"; 
	public static final String ICONE_AIDE_16 = "/icons/aide.png"; 
	public static final String ICONE_VOLUME_16 = "/icons/volume_16.png"; 
	public static final String ICONE_BEGIN_ALIGNMENT_32 = "/icons/beginAlignment_32.png"; 
	public static final String ICONE_END_ALIGNMENT_32 = "/icons/endAlignment_32.png"; 
	public static final String ICONE_ALWAYS_ON_TOP_ENABLED_32 = "/icons/alwaisOnTopEnabled-32.png"; 
	public static final String ICONE_ALWAYS_ON_TOP_DISABLED_32 = "/icons/alwaisOnTopDisabled-32.gif"; 

	public static final String ICONE_FAVORI_SELECTIONNE_32 = "/icons/favori_selectionne_32.png"; 
	public static final String ICONE_FAVORI_SELECTIONNE_OVER_32 = "/icons/favori_selectionne_over_32.png"; 
	public static final String ICONE_FAVORI_DESELECTIONNE_32 = "/icons/favori_deselectionne_32.png"; 
	public static final String ICONE_FAVORI_DESELECTIONNE_OVER_32 = "/icons/favori_deselectionne_over_32.png"; 
	
	public static final String IMAGE_A_PROPOS_164_314 = "/icons/aPropos.png";
	
	
	public static Image getImage(String path) {
		ImageIcon icon = getImageIcon(path);
		if (icon == null) {
			return null;
		}
		return icon.getImage();
	}
	
	public static ImageIcon getImageIcon(String path) {
		URL resource = ImageGetter.class.getResource(path);
		if (resource == null) {
			return null;
		}
		return new ImageIcon(resource);
	}
}
