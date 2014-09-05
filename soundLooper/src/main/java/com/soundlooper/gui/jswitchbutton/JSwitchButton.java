/**
 *
 */
package com.soundlooper.gui.jswitchbutton;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**--------------------------------------------------------------------------------
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
 * Switch button
 *
 * @author Alexandre NEDJARI
 * @since 25 juil. 2011
 *--------------------------------------------------------------------------------
 */
public class JSwitchButton extends JButton {

	/**
	 * Serial ID for this class
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the enabled flag
	 */
	protected boolean enabled;

	/**
	 * The icon when the button is disabled
	 */
	protected ImageIcon disabledIcon = null;

	/**
	 * The icon when the button is enabled
	 */
	protected ImageIcon enabledIcon = null;

	/**
	 * The color when the button is disabled
	 */
	protected Color disabledColor = null;

	/**
	 * The color when the button is enabled
	 */
	protected Color enabledColor = null;

	/**
	 * The text when the button is disabled
	 */
	protected String disabledText = null;

	/**
	 * The text when the button is enabled
	 */
	protected String enabledText = null;

	/**
	 * Construct the button with differents icons
	 * @param enabled the enabled flag
	 * @param disabledIcon The icon when the button is disabled
	 * @param enabledIcon The icon when the button is enabled
	 * @param listener The listener for this button
	 * @param text the button text
	 */
	public JSwitchButton(String text, boolean enabled, ImageIcon disabledIcon, ImageIcon enabledIcon, final SwitchButtonActionListener listener) {
		this.enabled = enabled;
		this.disabledIcon = disabledIcon;
		this.enabledIcon = enabledIcon;
		this.enabledText = text;
		this.disabledText = text;
		this.setBorderPainted(true);
		this.addListener(listener);
		this.setAspectFromState();
	}

	/**
	 * Construct the button with a different text and icon
	 * @param enabled the enabled flag
	 * @param disabledIcon The icon when the button is disabled
	 * @param enabledIcon The icon when the button is enabled
	 * @param listener The listener for this button
	 * @param enabledText the button text when the button is enabled
	 * @param disabledText the button text when the button is disabled
	 */
	public JSwitchButton(String enabledText, String disabledText, boolean enabled, ImageIcon disabledIcon, ImageIcon enabledIcon, final SwitchButtonActionListener listener) {
		this.enabled = enabled;
		this.disabledIcon = disabledIcon;
		this.enabledIcon = enabledIcon;
		this.enabledText = enabledText;
		this.disabledText = disabledText;
		this.setBorderPainted(true);
		this.addListener(listener);
		this.setAspectFromState();
	}

	/**
	 * Construct the button with differents colors
	 * @param enabled the enabled flag
	 * @param disabledColor The color when the button is disabled
	 * @param enabledColor The color when the button is enabled
	 * @param listener The listener for this button
	 * @param text the button text
	 */
	public JSwitchButton(String text, boolean enabled, Color disabledColor, Color enabledColor, final SwitchButtonActionListener listener) {
		this.enabled = enabled;
		this.disabledColor = disabledColor;
		this.enabledColor = enabledColor;
		this.enabledText = text;
		this.disabledText = text;
		this.setBorderPainted(true);
		this.addListener(listener);
		this.setAspectFromState();
	}

	/**
	 * Construct the button with a different colors and icon
	 * @param enabled the enabled flag
	 * @param disabledColor The color when the button is disabled
	 * @param enabledColor The color when the button is enabled
	 * @param listener The listener for this button
	 * @param enabledText the button text when the button is enabled
	 * @param disabledText the button text when the button is disabled
	 */
	public JSwitchButton(String enabledText, String disabledText, boolean enabled, Color disabledColor, Color enabledColor, final SwitchButtonActionListener listener) {
		this.enabled = enabled;
		this.disabledColor = disabledColor;
		this.enabledColor = enabledColor;
		this.enabledText = enabledText;
		this.disabledText = disabledText;
		this.setBorderPainted(true);
		this.addListener(listener);
		this.setAspectFromState();
	}

	/**
	 * Add the listener
	 * @param listener the listener
	 */
	protected void addListener(final SwitchButtonActionListener listener) {
		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (listener.actionPerformed(e, !JSwitchButton.this.enabled)) {
					JSwitchButton.this.enabled = !JSwitchButton.this.enabled;
					JSwitchButton.this.setAspectFromState();
				}
			}
		});
	}

	/**
	 * set the aspect from state
	 */
	protected void setAspectFromState() {
		if (JSwitchButton.this.enabled) {
			JSwitchButton.this.setText(JSwitchButton.this.enabledText);
			if (JSwitchButton.this.enabledIcon != null) {
				JSwitchButton.this.setIcon(JSwitchButton.this.enabledIcon);
			}
			if (JSwitchButton.this.enabledColor != null) {
				JSwitchButton.this.setBackground(JSwitchButton.this.enabledColor);
			}
		} else {
			JSwitchButton.this.setText(JSwitchButton.this.disabledText);
			if (JSwitchButton.this.disabledIcon != null) {
				JSwitchButton.this.setIcon(JSwitchButton.this.disabledIcon);
			}
			if (JSwitchButton.this.disabledColor != null) {
				JSwitchButton.this.setBackground(JSwitchButton.this.disabledColor);
			}
		}
	}
}
