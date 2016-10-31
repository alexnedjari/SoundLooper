package com.soundlooper.system;

import javafx.scene.image.ImageView;

public class ImageGetter {
	public static String getIconURL(String iconName) {
		return ImageGetter.class.getResource("/style/icon/" + iconName).toExternalForm();
	}

	public static ImageView getIcon(String iconName) {
		return new ImageView(getIconURL(iconName));
	}

	public static ImageView getIconeTag16() {
		return getIcon("tag_16.png");
	}

	public static ImageView getIconeSong16() {
		return getIcon("song_16.png");
	}

	public static ImageView getIconePotentiometer50() {
		return getIcon("potentiometer_50.png");
	}

	public static ImageView getIconePotentiometer30() {
		return getIcon("potentiometer_30.png");
	}

	public static ImageView getSoundLooper64() {
		return getIcon("soundLooper64.png");
	}
}