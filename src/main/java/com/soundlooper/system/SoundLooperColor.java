package com.soundlooper.system;

import javafx.scene.paint.Color;

public class SoundLooperColor {
	public static java.awt.Color getAwtWhite() {
		// TODO voir pourquoi la couleur finale est 240 240 240
		return new java.awt.Color(242, 243, 243);
	}

	public static java.awt.Color getAwtBlue() {
		return new java.awt.Color(46, 167, 195);
	}

	public static Color getWhite() {
		return getWhite(1);
	}

	public static Color getBlue() {
		return getBlue(1);
	}

	public static Color getSeparatorColor() {
		return getSeparatorColor(1);
	}

	public static Color getSeparatorColor(double opacity) {
		return getColor(56, 110, 117, opacity);
	}

	public static Color getWhite(double opacity) {
		return getColor(240, 240, 241, opacity);
	}

	public static Color getBlue(double opacity) {
		return getColor(46, 167, 195, opacity);
	}

	private static Color getColor(int red, int green, int blue, double opacity) {
		return new Color(red / 255d, green / 255d, blue / 255d, opacity);
	}

	public static final Color WHITE = Color.valueOf("#FFFFFF");
	public static final Color GRAY_5 = Color.valueOf("#F3F3F3");
	public static final Color GRAY_10 = Color.valueOf("#E7E7E7");
	public static final Color GRAY_50 = Color.valueOf("#7F7F7F");
	public static final Color DARK_GRAY = Color.valueOf("#373737");
	public static final Color LIGHT_GRAY = Color.valueOf("#434343");

}
