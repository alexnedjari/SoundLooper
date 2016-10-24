package com.soundlooper.gui.customComponent.playerView;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import com.soundlooper.model.SoundLooperPlayer;

public class PlayerView extends Control {
	SoundLooperPlayer soundLooperPlayer = SoundLooperPlayer.getInstance();

	@Override
	protected Skin<?> createDefaultSkin() {
		return new PlayerViewSkin(this);
	}

	public SoundLooperPlayer getSoundLooperPlayer() {
		return soundLooperPlayer;
	}

	public void forceLayout() {
		setNeedsLayout(true);
		layout();
	}

}
