package com.soundlooper.gui.customComponent.timeselection;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class TimeDigitView extends Control {

	private SimpleIntegerProperty digit = new SimpleIntegerProperty();

	@Override
	protected Skin<?> createDefaultSkin() {
		return new TimeDigitViewSkin(this);
	}

	public void forceLayout() {
		setNeedsLayout(true);
		layout();
	}

	public int getDigit() {
		return digit.get();
	}

	public void setDigit(int digit) {
		this.digit.set(digit);
	}

	public SimpleIntegerProperty digitProperty() {
		return digit;
	}

}
