package com.soundlooper.gui.customComponent.timeselection;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class TimeTextFieldView extends Control {

	private SimpleIntegerProperty time = new SimpleIntegerProperty();

	@Override
	protected Skin<?> createDefaultSkin() {
		return new TimeTextFieldViewSkin(this);
	}

	public void forceLayout() {
		setNeedsLayout(true);
		layout();
	}

	public int getTime() {
		return time.get();
	}

	public void setTime(int time) {
		this.time.set(time);
	}

	public SimpleIntegerProperty timeProperty() {
		return time;
	}

	public void select(TimeDigitView control) {
		((TimeTextFieldViewSkin) getSkin()).select(control);

	}

}
