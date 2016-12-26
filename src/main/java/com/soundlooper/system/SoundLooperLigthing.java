package com.soundlooper.system;

import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;

public class SoundLooperLigthing {

	private static Lighting potentiometerLighting;
	private static Lighting potentiometerLightingOver;
	private static Lighting barLighting;

	public static synchronized Lighting getPotentiometerLighting() {
		if (potentiometerLighting == null) {
			potentiometerLighting = createPotentiometerLighting();
		}
		return potentiometerLighting;
	}

	public static synchronized Lighting getPotentiometerLightingOver() {
		if (potentiometerLightingOver == null) {
			potentiometerLightingOver = createPotentiometerLightingOver();
		}
		return potentiometerLightingOver;
	}

	public static synchronized Lighting getBarLighting() {
		if (barLighting == null) {
			barLighting = createBarLighting();
		}
		return barLighting;
	}

	private static Lighting createPotentiometerLighting() {
		potentiometerLighting = new Lighting();
		Light light = new Light.Point(80, 40, 100, Color.WHITE);
		potentiometerLighting.setLight(light);
		return potentiometerLighting;
	}

	private static Lighting createPotentiometerLightingOver() {
		potentiometerLightingOver = new Lighting();
		Light light = new Light.Point(80, 40, 200, Color.WHITE);
		potentiometerLightingOver.setLight(light);
		return potentiometerLightingOver;
	}

	private static Lighting createBarLighting() {
		barLighting = new Lighting();
		Light light = new Light.Distant(100, 50, Color.WHITE);
		barLighting.setLight(light);
		return barLighting;
	}

}
