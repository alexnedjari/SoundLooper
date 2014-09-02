/**
 *
 */
package com.soundlooper.system.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyAdapter;

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
 * @author Alexandre NEDJARI
 * @date 10 juil. 2009
 */
public class GUIHelper {
	public static void setBackground(Container component, Color bg) {
		Component[] components = component.getComponents();
		for (int i = 0; i < components.length; i++) {
			Component sousComponent = components[i];
			if (sousComponent != null) {
				sousComponent.setBackground(bg);
				if (sousComponent instanceof Container) {
					GUIHelper.setBackground((Container) sousComponent, bg);
				}
			}
		}
	}

	public static void setEnabled(Container component, boolean state) {
		Component[] components = component.getComponents();
		for (int i = 0; i < components.length; i++) {
			Component sousComponent = components[i];
			if (sousComponent != null) {
				sousComponent.setEnabled(state);
				if (sousComponent instanceof Container) {
					GUIHelper.setEnabled((Container) sousComponent, state);
				}
			}
		}
	}

	public static void addShortcut(Container component, KeyAdapter keyAdapter) {
		Component[] components = component.getComponents();
		for (int i = 0; i < components.length; i++) {
			Component sousComponent = components[i];
			if (sousComponent != null) {
				sousComponent.addKeyListener(keyAdapter);
				if (sousComponent instanceof Container) {
					GUIHelper.addShortcut((Container) sousComponent, keyAdapter);
				}
			}
		}
	}
}
