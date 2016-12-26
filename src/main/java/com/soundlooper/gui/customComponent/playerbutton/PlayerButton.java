package com.soundlooper.gui.customComponent.playerbutton;

import javafx.scene.control.Button;
import javafx.scene.control.Skin;

public class PlayerButton extends Button {

	@Override
	protected Skin<?> createDefaultSkin() {
		return new PlayerButtonSkin(this);
	}
}
