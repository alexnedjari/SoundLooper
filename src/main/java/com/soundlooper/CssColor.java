package com.soundlooper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.sun.javafx.css.Declaration;
import com.sun.javafx.css.Rule;
import com.sun.javafx.css.Stylesheet;
import com.sun.javafx.css.converters.PaintConverter.SequenceConverter;

import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public enum CssColor {

	BLUE("blue"), WHITE("white"), GRAY("gray");

	private Map<Double, Color> mapColorByOpacity = new HashMap<>();

	CssColor(String name) {
		String selectorName = "*." + name;
		Stylesheet css = SoundLooperStylesheet.getApplicationStylesheet();

		css.getRules().stream().filter(d -> d.getSelectors().get(0).toString().equals(selectorName))
				.forEach(new Consumer<Rule>() {
					@Override
					public void accept(Rule t) {
						ObservableList<Declaration> declarations = t.getDeclarations();
						for (Declaration declaration : declarations) {
							if (declaration.getProperty().equals("-fx-background-color")) {
								SequenceConverter converter = (SequenceConverter) declaration.getParsedValue()
										.getConverter();
								Paint[] convertedValues = converter.convert(declaration.getParsedValue(), null);
								Paint color = convertedValues[0];
								CssColor.this.mapColorByOpacity.put(1d, (Color) color);
							}
						}
					}
				});
	}

	public Color getColor() {
		return mapColorByOpacity.get(1d);
	}

	public Color getColor(double opacity) {
		if (mapColorByOpacity.containsKey(opacity)) {
			return mapColorByOpacity.get(opacity);
		}
		Color baseColor = getColor();
		Color finalColor = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), opacity);
		mapColorByOpacity.put(opacity, finalColor);
		return finalColor;
	}

	public java.awt.Color getAwtColor() {
		Color baseColor = getColor();
		int awtRed = Double.valueOf(baseColor.getRed() * 255).intValue();
		int awtGreen = Double.valueOf(baseColor.getGreen() * 255).intValue();
		int awtBlue = Double.valueOf(baseColor.getBlue() * 255).intValue();
		return new java.awt.Color(awtRed, awtGreen, awtBlue);
	}

}
