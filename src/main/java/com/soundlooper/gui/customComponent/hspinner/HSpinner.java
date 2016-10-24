package com.soundlooper.gui.customComponent.hspinner;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Skin;
import javafx.scene.control.Slider;

public class HSpinner extends Slider {

	private BooleanProperty edition = new SimpleBooleanProperty(false);

	public BooleanProperty editionProperty() {
		return edition;
	}

	public boolean isEdition() {
		return edition.get();
	}

	public void setEdition(boolean edition) {
		this.edition.set(edition);
	}

	@Override
	protected Skin<?> createDefaultSkin() {
		return new HSpinnerSkin(this);
	}
}
