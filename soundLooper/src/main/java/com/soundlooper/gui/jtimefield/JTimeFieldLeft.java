package com.soundlooper.gui.jtimefield;


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
