package com.soundlooper.gui.customComponent.timeselection;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import com.soundlooper.model.SoundLooperPlayer;

public class TimeSelectionView extends Control {

	private SoundLooperPlayer soundLooperPlayer = SoundLooperPlayer.getInstance();

	@Override
	protected Skin<?> createDefaultSkin() {
		return new TimeSelectionViewSkin(this);
	}

	public void forceLayout() {
		setNeedsLayout(true);
		layout();
	}

	public SoundLooperPlayer getSoundLooperPlayer() {
		return soundLooperPlayer;
	}

}
