package com.soundlooper.gui.customComponent.util;

import javafx.scene.shape.Polygon;

public class ArrowFactory {
	public static Polygon getArrow(double factor) {
		Polygon arrow = new Polygon();
		arrow.getPoints().setAll(10d * factor, 27d * factor, 10d * factor, 0d, 19d * factor, 0d, 19d * factor,
				64d * factor, 0d, 27d * factor);
		return arrow;
	}
}
