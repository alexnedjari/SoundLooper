package com.soundlooper.gui.customComponent.potentiometer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

import com.soundlooper.gui.customComponent.util.ArrowFactory;
import com.soundlooper.system.ImageGetter;

public class PotentiometerSkin extends SkinBase<Potentiometer> {

	// private ImageView imageView;
	private Label valueLabel;

	private AnchorPane potentiometerView;
	ImageView imageView;

	private double dragStart;
	private double initialValue;

	private AnchorPane anchorPane = new AnchorPane();
	Rotate rotate = new Rotate(0, 25, 25, 0, Rotate.Z_AXIS);
	Polygon arrow;

	protected PotentiometerSkin(Potentiometer control) {
		super(control);

		// this.getSkinnable().setBackground(
		// new Background(new BackgroundFill(new Color(1, 0, 0, 1),
		// CornerRadii.EMPTY, Insets.EMPTY)));
		if (Potentiometer.SIZE_MEDIUM.equals(control.getSize())) {
			// imageView = ImageGetter.getIconePotentiometer50();
			anchorPane.resize(100, 100);

			potentiometerView = new AnchorPane();
			// potentiometerView.setBackground(new Background(new
			// BackgroundFill(new Color(0, 1, 0, 1), CornerRadii.EMPTY,
			// Insets.EMPTY)));
			potentiometerView.resize(50, 50);
			imageView = ImageGetter.getDrawablePotentiometer50();

			potentiometerView.getChildren().add(imageView);

			arrow = ArrowFactory.getArrow(0.50);
			// double factor = 0.50;
			// arrow.getPoints().setAll(0d, 64d * factor, 0d, 0d, 17d * factor,
			// 37d * factor, 9d * factor, 37d * factor,
			// 9d * factor, 64d * factor);
			// arrow.getPoints().setAll(10d * factor, 27d * factor, 10d *
			// factor, 0d, 21d * factor, 0d, 21d * factor,
			// 64d * factor, 0d, 27d * factor);
			// arrow.setRotationAxis(new Point3D(0, 27 * factor, 0));
			// arrow.setRotationAxis(new Point3D(0, 0, 0));
			potentiometerView.getChildren().add(arrow);

			rotate = new Rotate(0, 8, 15, 0, Rotate.Z_AXIS);
			arrow.relocate(17, 10);
			arrow.setFill(new Color(0.95, 0.95, 0.95, 1));

			// potentiometerView.setBorder(new Border(new
			// BorderStroke(Color.RED, BorderStrokeStyle.SOLID,
			// CornerRadii.EMPTY, BorderWidths.DEFAULT)));
			// arrow.getTransforms().add(rotate);

		} else if (Potentiometer.SIZE_SMALL.equals(control.getSize())) {
			// imageView = ImageGetter.getIconePotentiometer30();
			// anchorPane.resize(60, 60);
		} else {
			// By default, take a small size
			// imageView = ImageGetter.getIconePotentiometer30();
		}
		control.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				getSkinnable().requestLayout();
			}
		});

		// control.setEffect(SoundLooperLigthing.getPotentiometerLighting());

		anchorPane.getChildren().add(potentiometerView);

		arrow.getTransforms().add(rotate);

		// control.getBottomLeftControl().addListener(new
		// ChangeListener<ButtonBase>() {
		// @Override
		// public void changed(ObservableValue<? extends ButtonBase> observable,
		// ButtonBase oldValue,
		// ButtonBase newValue) {
		// if (oldValue != null) {
		// anchorPane.getChildren().remove(oldValue);
		// }
		//
		// if (newValue != null) {
		// anchorPane.getChildren().add(0, newValue);
		// getSkinnable().forceLayout();
		// }
		//
		// }
		// });
		if (control.getBottomLeftControl() != null) {

			anchorPane.getChildren().add(0, control.getBottomLeftControl());
			getSkinnable().forceLayout();
			// control.getCentralButton().setBackground(
			// new Background(new BackgroundFill(Color.LIGHTYELLOW, new
			// CornerRadii(16), Insets.EMPTY)));

			control.getBottomLeftControl().relocate(0, 60);

			// if (control.getDisplayValue()) {
			// // valueLabel = new Label();
			// //
			// valueLabel.textProperty().bindBidirectional(control.valueProperty(),
			// // new NumberStringConverter());
			// // anchorPane.getChildren().add(0, valueLabel);
			// control.getCentralButton().textProperty()
			// .bindBidirectional(control.valueProperty(), new
			// NumberStringConverter());
			// }
		}
		if (control.getBottomRightControl() != null) {

			anchorPane.getChildren().add(0, control.getBottomRightControl());
			getSkinnable().forceLayout();
			control.getBottomRightControl().relocate(70, 70);
		}
		getChildren().add(anchorPane);

		potentiometerView.relocate(25, 0);
		imageView.relocate(0, 0);
	}

	@Override
	protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {

		// anchorPane.setBackground(new Background(new
		// BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));

		Potentiometer potentiometer = getSkinnable();
		double potentiometerViewX = contentX + contentWidth / 2 - potentiometerView.getWidth() / 2;
		double potentiometerViewY = contentY + contentHeight / 2 - potentiometerView.getHeight() / 2;
		potentiometerView.relocate(potentiometerViewX, potentiometerViewY);
		double unitePerDegree = (360 - potentiometer.getRotationBoundaryMinInDegree() - potentiometer
				.getRotationBoundaryMaxInDegree()) / (potentiometer.getMax() - potentiometer.getMin());
		double angle = unitePerDegree * (potentiometer.getValue() - potentiometer.getMin())
				+ potentiometer.getRotationBoundaryMinInDegree();

		rotate.setAngle(angle);

		if (dragStart == 0) {
			potentiometerView.setOnMousePressed(me -> {
				dragStart = me.getSceneX();
				initialValue = potentiometer.getValue();
			});

			potentiometerView.setOnMouseDragged(me -> {
				double move = dragStart - me.getSceneX();
				double newValue = initialValue - (move * potentiometer.getSensibility());
				potentiometer.setValue(newValue);
				me.consume();
			});
		}

		// ButtonBase centralButton = potentiometer.getCentralButton();
		// if (centralButton != null) {
		// // centralButton.resize(24, 24);
		//
		// centralButton.setBackground(new Background(new
		// BackgroundFill(Color.LIGHTYELLOW, new CornerRadii(16),
		// Insets.EMPTY)));
		//
		// double buttonHeight = centralButton.getLayoutBounds().getHeight();
		//
		// centralButton.relocate(potentiometerViewX - 7, potentiometerViewY +
		// potentiometerView.getHeight()
		// - buttonHeight + 7);
		// }
		//
		// if (valueLabel != null) {
		// valueLabel.relocate(15, 15);
		// }

	}
}
