/**
 *
 */
package com.soundlooper.gui;

import java.awt.Dimension;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
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
 * Contains useful method for GUI
 * @author ANEDJARI
 *
 */
public final class SoundLooperGUIHelper {

	/**
	 * Private constructor
	 */
	private SoundLooperGUIHelper() {
		//to avoid external construction
	}

	/**
	 * Get a created button
	 * @param action action to call
	 * @param actionName the action name
	 * @param tooltip the tooltip content
	 * @param enabled if true, button will be created enabled
	 * @return the created JButton
	 */
	public static JButton getBouton(Action action, String actionName, String tooltip, boolean enabled) {
		int taille = 32;
		return SoundLooperGUIHelper.getBouton(action, actionName, tooltip, enabled, taille);
	}

	/**
	 * Get a created button
	 * @param actionName the action name
	 * @param tooltip the tooltip content
	 * @param enabled if true, button will be created enabled
	 * @return the created JButton
	 */
	public static JButton getBouton(String actionName, String tooltip, boolean enabled) {
		return SoundLooperGUIHelper.getBouton(null, actionName, tooltip, enabled);
	}

	/**
	 * Get a created button
	 * @param actionName the action name
	 * @param tooltip the tooltip content
	 * @param enabled if true, button will be created enabled
	 * @param taille size of the button icon (button size will be icon size + 2)
	 * @return the created JButton
	 */
	public static JButton getBouton(String actionName, String tooltip, boolean enabled, int taille) {
		return SoundLooperGUIHelper.getBouton(null, actionName, tooltip, enabled, taille);
	}

	/**
	 * Get a created button
	 * @param action the action
	 * @param actionName the action name
	 * @param tooltip the tooltip content
	 * @param enabled if true, button will be created enabled
	 * @param taille size of the button icon (button size will be icon size + 2)
	 * @return the created JButton
	 */
	public static JButton getBouton(Action action, String actionName, String tooltip, boolean enabled, int taille) {
		ImageIcon icon = ImageGetter.getImageIcon("/icons/" + actionName + "_" + taille + ".png");
		ImageIcon rolloverIcon = ImageGetter.getImageIcon("/icons/" + actionName + "_over_" + taille + ".png");
		if (rolloverIcon == null) {
			rolloverIcon = icon;
		}

		JButton bouton = new JButton();
		if (action != null) {
			bouton.setAction(action);
		}
		bouton.setIcon(icon);
		bouton.setRolloverIcon(rolloverIcon);
		bouton.setContentAreaFilled(false);
		bouton.setBorderPainted(false);
		bouton.setFocusable(false);
		bouton.setToolTipText(tooltip);
		bouton.setPreferredSize(new Dimension(taille + 2, taille + 2));
		bouton.setEnabled(enabled);
		return bouton;
	}

	/**
	 * Get a created button (not enabled)
	 * @param actionName the action name
	 * @param tooltip the tooltip content
	 * @return the created JButton
	 */
	public static JButton getBouton(String actionName, String tooltip) {
		return SoundLooperGUIHelper.getBouton(actionName, tooltip, false);
	}
}
