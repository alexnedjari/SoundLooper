package com.soundlooper.gui.customComponent.potentiometer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableBooleanProperty;
import javafx.css.SimpleStyleableDoubleProperty;
import javafx.css.SimpleStyleableStringProperty;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.Slider;

public class Potentiometer extends Slider {

	public static final String SIZE_SMALL = "small";
	public static final String SIZE_MEDIUM = "medium";

	private StyleableStringProperty size;
	private DoubleProperty rotationBoundaryMinInDegree = new SimpleDoubleProperty(30);
	private DoubleProperty rotationBoundaryMaxInDegree = new SimpleDoubleProperty(30);
	private StyleableDoubleProperty sensibility;
	private StyleableBooleanProperty displayValue;

	private SimpleObjectProperty<Control> bottomLeftControl = new SimpleObjectProperty<>();
	private SimpleObjectProperty<Control> bottomRightControl = new SimpleObjectProperty<>();

	@Override
	protected Skin<?> createDefaultSkin() {
		return new PotentiometerSkin(this);
	}

	public StyleableStringProperty sizeProperty() {
		if (size == null) {
			size = new SimpleStyleableStringProperty(StyleableProperties.SIZE, this, "size", Potentiometer.SIZE_SMALL);
		}
		return size;
	}

	public StyleableBooleanProperty displayValueProperty() {
		if (displayValue == null) {
			displayValue = new SimpleStyleableBooleanProperty(StyleableProperties.DISPLAY_VALUE, this, "display-value",
					false);
		}
		return displayValue;
	}

	public StyleableDoubleProperty sensibilityProperty() {
		if (sensibility == null) {
			sensibility = new SimpleStyleableDoubleProperty(StyleableProperties.SENSIBILITY, this, "sensibility", 1d);
		}
		return sensibility;
	}

	public boolean getDisplayValue() {
		return displayValue == null ? false : displayValue.get();
	}

	public double getSensibility() {
		return sensibility == null ? 1 : sensibility.get();
	}

	public void setSensibility(double sensibility) {
		this.rotationBoundaryMaxInDegree.set(sensibility);
	}

	public DoubleProperty rotationBoundaryMinInDegreeProperty() {
		return rotationBoundaryMinInDegree;
	}

	public DoubleProperty rotationBoundaryMaxInDegreeProperty() {
		return rotationBoundaryMaxInDegree;
	}

	public String getSize() {
		return size == null ? Potentiometer.SIZE_SMALL : size.get();
	}

	public void setSize(String size) {
		this.size.set(size);
	}

	public double getRotationBoundaryMinInDegree() {
		return rotationBoundaryMinInDegree.get();
	}

	public void setRotationBoundaryMinInDegree(double rotationBoundaryMinInDegree) {
		this.rotationBoundaryMinInDegree.set(rotationBoundaryMinInDegree);
	}

	public double getRotationBoundaryMaxInDegree() {
		return rotationBoundaryMaxInDegree.get();
	}

	public void setRotationBoundaryMaxInDegree(double rotationBoundaryMaxInDegree) {
		this.rotationBoundaryMaxInDegree.set(rotationBoundaryMaxInDegree);
	}

	private static class StyleableProperties {
		private static final CssMetaData<Potentiometer, Number> SENSIBILITY = new CssMetaData<Potentiometer, Number>(
				"-fx-potentiometer-sensibility", new StyleConverter<String, Number>(), 1) {

			@Override
			public boolean isSettable(Potentiometer potentiometer) {
				return potentiometer.sensibility == null || !potentiometer.sensibility.isBound();
			}

			@Override
			public StyleableProperty<Number> getStyleableProperty(Potentiometer potentiometer) {
				return potentiometer.sensibilityProperty();
			}

		};

		private static final CssMetaData<Potentiometer, String> SIZE = new CssMetaData<Potentiometer, String>(
				"-fx-potentiometer-size", new StyleConverter<String, String>(), Potentiometer.SIZE_SMALL) {

			@Override
			public boolean isSettable(Potentiometer potentiometer) {
				return potentiometer.size == null || !potentiometer.size.isBound();
			}

			@Override
			public StyleableProperty<String> getStyleableProperty(Potentiometer potentiometer) {
				return potentiometer.sizeProperty();
			}
		};

		private static final CssMetaData<Potentiometer, Boolean> DISPLAY_VALUE = new CssMetaData<Potentiometer, Boolean>(
				"-fx-potentiometer-display-value", new StyleConverter<String, Boolean>(), false) {

			@Override
			public boolean isSettable(Potentiometer potentiometer) {
				return potentiometer.displayValue == null || !potentiometer.displayValue.isBound();
			}

			@Override
			public StyleableProperty<Boolean> getStyleableProperty(Potentiometer potentiometer) {
				return potentiometer.displayValueProperty();
			}
		};

		private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
		static {
			final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>();
			Collections.addAll(styleables, SENSIBILITY, SIZE, DISPLAY_VALUE);
			STYLEABLES = Collections.unmodifiableList(styleables);
		}

	}

	@Override
	protected List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
		return getClassCssMetaData();
	}

	public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
		return StyleableProperties.STYLEABLES;
	}

	public void setBottomLeftControl(Control centralNode) {
		this.bottomLeftControl.set(centralNode);
	}

	public Control getBottomLeftControl() {
		return bottomLeftControl.get();
	}

	public SimpleObjectProperty<Control> bottomLeftControlProperty() {
		return bottomLeftControl;
	}

	public void setBottomRightControl(Control centralNode) {
		this.bottomRightControl.set(centralNode);
	}

	public Control getBottomRightControl() {
		return bottomRightControl.get();
	}

	public SimpleObjectProperty<Control> bottomRightControlProperty() {
		return bottomLeftControl;
	}

	public void forceLayout() {
		setNeedsLayout(true);
		layout();
	}

}
