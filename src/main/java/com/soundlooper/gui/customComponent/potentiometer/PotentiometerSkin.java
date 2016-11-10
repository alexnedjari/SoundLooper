package com.soundlooper.gui.customComponent.potentiometer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.SkinBase;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import com.soundlooper.system.ImageGetter;

public class PotentiometerSkin extends SkinBase<Potentiometer> {

	private ImageView imageView;

	private double dragStart;
	private double initialValue;

	private AnchorPane anchorPane = new AnchorPane();

	protected PotentiometerSkin(Potentiometer control) {
		super(control);
		if (Potentiometer.SIZE_MEDIUM.equals(control.getSize())) {
			imageView = ImageGetter.getIconePotentiometer50();
			anchorPane.resize(100, 100);
		} else if (Potentiometer.SIZE_SMALL.equals(control.getSize())) {
			imageView = ImageGetter.getIconePotentiometer30();
			anchorPane.resize(60, 60);
		} else {
			// By default, take a small size
			imageView = ImageGetter.getIconePotentiometer30();
		}
		control.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				getSkinnable().requestLayout();
			}
		});
		anchorPane.getChildren().add(imageView);

		control.centralButtonProperty().addListener(new ChangeListener<ButtonBase>() {
			@Override
			public void changed(ObservableValue<? extends ButtonBase> observable, ButtonBase oldValue,
					ButtonBase newValue) {
				if (oldValue != null) {
					anchorPane.getChildren().remove(oldValue);
				}

				if (newValue != null) {
					anchorPane.getChildren().add(0, newValue);
					getSkinnable().forceLayout();
				}

			}
		});
		if (control.getCentralButton() != null) {
			anchorPane.getChildren().add(0, control.getCentralButton());
			getSkinnable().forceLayout();
		}

		getChildren().add(anchorPane);
	}

	@Override
	protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {

		// anchorPane.setBackground(new Background(new
		// BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));

		Potentiometer potentiometer = getSkinnable();
		imageView.setX(contentX + contentWidth / 2 - imageView.getLayoutBounds().getWidth() / 2);
		imageView.setY(contentY + contentHeight / 2 - imageView.getLayoutBounds().getHeight() / 2);
		double unitePerDegree = (360 - potentiometer.getRotationBoundaryMinInDegree() - potentiometer
				.getRotationBoundaryMaxInDegree()) / (potentiometer.getMax() - potentiometer.getMin());
		double angle = unitePerDegree * (potentiometer.getValue() - potentiometer.getMin())
				+ potentiometer.getRotationBoundaryMinInDegree();
		imageView.setRotate(angle);

		if (dragStart == 0) {
			imageView.setOnMousePressed(me -> {
				dragStart = me.getSceneX();
				initialValue = potentiometer.getValue();
			});

			imageView.setOnMouseDragged(me -> {
				double move = dragStart - me.getSceneX();
				double newValue = initialValue - (move * potentiometer.getSensibility());
				potentiometer.setValue(newValue);
				me.consume();
			});
		}

		ButtonBase centralButton = potentiometer.getCentralButton();
		if (centralButton != null) {
			centralButton.resize(24, 24);

			centralButton.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW, new CornerRadii(16),
					Insets.EMPTY)));

			double buttonWidth = centralButton.getLayoutBounds().getWidth();
			double buttonHeight = centralButton.getLayoutBounds().getHeight();

			double potentiometerCenterX = /* contentX + */imageView.getX() + (imageView.getImage().getWidth() / 2);
			double potentiometerCenterY = /* contentY + */imageView.getY() + (imageView.getImage().getHeight() / 2);

			double buttonPositionX = potentiometerCenterX - (buttonWidth);
			double buttonPositionY = potentiometerCenterY - (buttonHeight);

			centralButton.relocate(imageView.getX() - 7, imageView.getY() + imageView.getImage().getHeight()
					- buttonHeight + 7);
		}

	}
}
