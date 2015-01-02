package com.soundlooper.gui.jtimefield;

/**
 *-------------------------------------------------------
 * JTimeField to display on the right of the player 
 * 
 * Copyright (C) 2015 Alexandre NEDJARI
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
public class JTimeFieldRight extends JTimeField {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTimeFieldLeft jtimeFieldLeft;	
	
	private int minimumBetweenCursors;
	
	

	public JTimeFieldRight(int minimumBetweenCursors) {
		super();
		this.minimumBetweenCursors = minimumBetweenCursors;
	}

	public void setTimeFieldLeft(JTimeFieldLeft jtimeFieldLeft) {
		this.jtimeFieldLeft = jtimeFieldLeft;
	}

	@Override
	protected int getCheckedTime(int millisecondTime) {
		millisecondTime = super.getCheckedTime(millisecondTime);
		
		if (jtimeFieldLeft != null) {
			int leftTime = jtimeFieldLeft.calculateMillisecondValue();
			if (millisecondTime <= leftTime + minimumBetweenCursors) {
				millisecondTime = leftTime + minimumBetweenCursors;
			}
		}
		
		return millisecondTime;
	}
    
    
}
