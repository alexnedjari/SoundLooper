/**
 *
 */
package com.soundlooper.gui;

import javax.swing.JLabel;

/**
 * --------------------------------------------------------------------------------
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
 * Class used to remove infos from state bar after
 * some seconds
 *
 * @author Alexandre NEDJARI
 * @since 25 juil. 2011
 *--------------------------------------------------------------------------------
 */
public class ThreadCleanLabelState extends Thread {

	/**
	 * The initial message that must be cleaned
	 */
	private String initialMessage;

	/**
	 * JLabel
	 */
	private JLabel label;

	/**
	 * Constructor
	 * @param label the label that must be cleaned
	 */
	public ThreadCleanLabelState(JLabel label) {
		super();
		this.initialMessage = label.getText();
		this.label = label;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(3000);
			if (this.label.getText().equals(this.initialMessage)) {
				//clean the label only if the text is still the same
				this.label.setText("");
			}
		} catch (InterruptedException e) {
			WindowPlayer.logger.error(e.getMessage());
		}
	}
}