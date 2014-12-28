package com.soundlooper.gui.jtimefield;


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
