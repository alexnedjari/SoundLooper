package com.soundlooper.gui.customComponent.potentiometer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.SkinBase;
import javafx.scene.image.ImageView;

import com.soundlooper.system.ImageGetter;

public class PotentiometerSkin extends SkinBase<Potentiometer> {

	private ImageView imageView;

	private double dragStart;
	private double initialValue;

	protected PotentiometerSkin(Potentiometer control) {
		super(control);
		if (Potentiometer.SIZE_MEDIUM.equals(control.getSize())) {
			imageView = ImageGetter.getIconePotentiometer50();
		} else if (Potentiometer.SIZE_SMALL.equals(control.getSize())) {
			imageView = ImageGetter.getIconePotentiometer30();
		} else {
			// By default, take a small size
			imageView = ImageGetter.getIconePotentiometer30();
		}
		control.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				getSkinnable().requestLayout();
			}
		});
		getChildren().add(imageView);

	}

	@Override
	protected void layoutChildren(double contentX, double contentY,
			double contentWidth, double contentHeight) {

		Potentiometer potentiometer = getSkinnable();
		imageView.setX(contentX + contentWidth / 2
				- imageView.getLayoutBounds().getWidth() / 2);
		imageView.setY(contentY + contentHeight / 2
				- imageView.getLayoutBounds().getHeight() / 2);
		double unitePerDegree = (360 - potentiometer
				.getRotationBoundaryMinInDegree() - potentiometer
				.getRotationBoundaryMaxInDegree())
				/ (potentiometer.getMax() - potentiometer.getMin());
		double angle = unitePerDegree
				* (potentiometer.getValue() - potentiometer.getMin())
				+ potentiometer.getRotationBoundaryMinInDegree();
		imageView.setRotate(angle);

		if (dragStart == 0) {
			imageView.setOnMousePressed(me -> {
				dragStart = me.getSceneX();
				initialValue = potentiometer.getValue();
			});

			imageView.setOnMouseDragged(me -> {
				double move = dragStart - me.getSceneX();
				double newValue = initialValue
						- (move * potentiometer.getSensibility());
				potentiometer.setValue(newValue);
				me.consume();
			});
		}
	}
}
