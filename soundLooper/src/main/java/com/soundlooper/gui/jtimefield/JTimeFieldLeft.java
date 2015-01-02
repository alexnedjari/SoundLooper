package com.soundlooper.gui.jtimefield;

/**
 *-------------------------------------------------------
 * JTimeField to display on the left of the screen 
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
public class JTimeFieldLeft extends JTimeField {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTimeFieldRight jtimeFieldRight; 
	
	private int minimumBetweenCursors;
	
	

	public JTimeFieldLeft(int minimumBetweenCursors) {
		super();
		this.minimumBetweenCursors = minimumBetweenCursors;
	}



	public void setTimeFieldRight(JTimeFieldRight jtimeFieldRight) {
		this.jtimeFieldRight = jtimeFieldRight;
	}



	@Override
	protected int getCheckedTime(int millisecondTime) {
		millisecondTime = super.getCheckedTime(millisecondTime);
		if (jtimeFieldRight != null) {
			int rightValue = jtimeFieldRight.calculateMillisecondValue();
			if (millisecondTime >= rightValue - minimumBetweenCursors){
				millisecondTime = rightValue - minimumBetweenCursors;
				if (millisecondTime < 0) {
					millisecondTime = 0;
				}
			}
		}
		return millisecondTime;
	}
    
    
}
