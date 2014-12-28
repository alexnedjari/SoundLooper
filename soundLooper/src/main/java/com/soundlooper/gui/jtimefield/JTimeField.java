package com.soundlooper.gui.jtimefield;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

public class JTimeField extends JPanel {
	
	private List<JTimeFieldDigit> listDigit = new ArrayList<JTimeFieldDigit>();
	
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public static final int EPAISSEUR_BORDURE_GAUCHE = 2;
    public static final int EPAISSEUR_BORDURE_DROITE = 1;
    public static final int EPAISSEUR_BORDURE_HAUT = 2;
    public static final int EPAISSEUR_BORDURE_BAS = 1;
    public static final int NOMBRE_DIGIT = 9;
    public static final int MARGE_INTERNE = 4;
    public static final int LARGEUR = JTimeFieldDigitUI.LARGEUR_DIGIT * NOMBRE_DIGIT + MARGE_INTERNE*2;
	public static final int HAUTEUR = JTimeFieldDigitUI.HAUTEUR_DIGIT + EPAISSEUR_BORDURE_HAUT + EPAISSEUR_BORDURE_BAS;
	
	private int maxValue;
    
	private JTimeFieldNumericDigit selectedDigit = null;
	
    public JTimeField() {
        super();
        this.setTime(0);
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 0,0));
        this.setPreferredSize(new Dimension(LARGEUR, HAUTEUR));
        this.setBackground(Color.WHITE);
        this.setBorder(new BevelBorder(BevelBorder.LOWERED)); //2px en haut/gache, 1 en bas droite
        
        //create digits
        JTimeFieldNumericDigit digit600000ms = new JTimeFieldNumericDigit(600000, null, this,9);
        JTimeFieldNumericDigit digit60000ms = new JTimeFieldNumericDigit(60000, digit600000ms, this,9);
        JTimeFieldNumericDigit digit10000ms = new JTimeFieldNumericDigit(10000, digit60000ms, this,5);
        JTimeFieldNumericDigit digit1000ms = new JTimeFieldNumericDigit(1000, digit10000ms, this,9);
        JTimeFieldNumericDigit digit100ms = new JTimeFieldNumericDigit(100, digit1000ms, this,9);
        JTimeFieldNumericDigit digit10ms = new JTimeFieldNumericDigit(10, digit100ms, this,9);
        JTimeFieldNumericDigit digit1ms = new JTimeFieldNumericDigit(1, digit10ms, this,9);
        
        digit600000ms.setDigitInferieur(digit60000ms);
        digit60000ms.setDigitInferieur(digit10000ms);
        digit10000ms.setDigitInferieur(digit1000ms);
        digit1000ms.setDigitInferieur(digit100ms);
        digit100ms.setDigitInferieur(digit10ms);
        digit10ms.setDigitInferieur(digit1ms);
        
        //Add millisecond
		listDigit.add(digit1ms);
		listDigit.add(digit10ms);
		listDigit.add(digit100ms);
        listDigit.add(new JTimeFieldSeparatorDigit());
        
        //add minuts
		listDigit.add(digit1000ms);
		listDigit.add(digit10000ms);
        listDigit.add(new JTimeFieldSeparatorDigit());
        
        //add hours
		listDigit.add(digit60000ms);
		listDigit.add(digit600000ms);
		
		
		ArrayList<JTimeFieldDigit> listeInversee = new ArrayList<JTimeFieldDigit>(listDigit);
		Collections.reverse(listeInversee);
		for (JTimeFieldDigit jTimeFieldDigit : listeInversee) {
				this.add(jTimeFieldDigit);
		}
    }

    public List<JTimeFieldDigit> getListDigit() {
		return listDigit;
	}

	public void setListDigit(List<JTimeFieldDigit> listDigit) {
		this.listDigit = listDigit;
	}

	public int calculateMillisecondValue() {
    	int millisecondValue = 0;
    	for (JTimeFieldDigit jTimeFieldDigit : listDigit) {
			if (jTimeFieldDigit instanceof JTimeFieldNumericDigit) {
				JTimeFieldNumericDigit digit = (JTimeFieldNumericDigit)jTimeFieldDigit;
				millisecondValue += digit.getNumericValue() * digit.getMultiplicateur();
			}
		}
    	
        return millisecondValue;
    }

    public void setTime(int millisecondTime) {
    	System.out.println("JTimeField reçoit temps : " + millisecondTime);
    	millisecondTime = getCheckedTime(millisecondTime);
    	
    	System.out.println("Après correction : " + millisecondTime);
    	
        ArrayList<JTimeFieldDigit> listeInversee = new ArrayList<JTimeFieldDigit>(listDigit);
		Collections.reverse(listeInversee);
		for (JTimeFieldDigit jTimeFieldDigit : listeInversee) {
			if (jTimeFieldDigit instanceof JTimeFieldNumericDigit) {
				JTimeFieldNumericDigit digit = (JTimeFieldNumericDigit)jTimeFieldDigit;
				int valeurDigit = millisecondTime / digit.getMultiplicateur();
				digit.setValue(String.valueOf(valeurDigit).charAt(0));
				millisecondTime -= valeurDigit * digit.getMultiplicateur();
			} 
		}
		refresh();
    }

	protected int getCheckedTime(int millisecondTime) {
		if (millisecondTime > maxValue) {
    		millisecondTime = maxValue;
    		System.out.println("Place la malue sur le max : " + maxValue);
    	}
		return millisecondTime;
	}
    
    public void selectDigit(JTimeFieldNumericDigit jTimeFieldDigit) {
    	this.selectedDigit = jTimeFieldDigit;
    	if (selectedDigit != null) {
    		this.selectedDigit.requestFocus();
    	}
    	refresh();
    }

	public void refresh() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				updateUI();
			}
		});
	}

	public JTimeFieldNumericDigit getSelectedDigit() {
		return selectedDigit;
	}

	public void setSelectedDigit(JTimeFieldNumericDigit selectedDigit) {
		this.selectedDigit = selectedDigit;
	}

	public void onValueChanged() {
		int millisecondTime = calculateMillisecondValue();
		int millisecondTimeCorrige = getCheckedTime(millisecondTime);
		if (millisecondTime != millisecondTimeCorrige) {
			//Replace value if needed
			setTime(millisecondTimeCorrige);
		}
		System.out.println("JTimeField donne temps : " + millisecondTime + " corrigé : " + millisecondTimeCorrige);
		JTimeFieldSupport.getInstance().fireValueChanged(millisecondTimeCorrige, this);
		
	}

	public void addJTimeFieldListener(JTimeFieldListener listener) {
		JTimeFieldSupport.getInstance().addJPlayerListener(listener);
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public int getMaxValue() {
		return maxValue;
	}
}
