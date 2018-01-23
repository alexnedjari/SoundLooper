package com.soundlooper.gui.customComponent.potentiometer;

import com.soundlooper.gui.customComponent.util.ArrowFactory;
import com.soundlooper.system.ImageGetter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

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
		if (Potentiometer.SIZE_MEDIUM.equals(control.getSize())) {
			anchorPane.resize(100, 100);

			potentiometerView = new AnchorPane();
			potentiometerView.resize(50, 50);
			imageView = ImageGetter.getDrawablePotentiometer50();

			potentiometerView.getChildren().add(imageView);

			arrow = ArrowFactory.getArrow(0.50);
			potentiometerView.getChildren().add(arrow);

			rotate = new Rotate(0, 8, 15, 0, Rotate.Z_AXIS);
			arrow.relocate(17, 10);
			arrow.setFill(new Color(0.95, 0.95, 0.95, 1));
		} else if (Potentiometer.SIZE_SMALL.equals(control.getSize())) {
			// TODO manage other potentionmeter size
		} else {
			// TODO manage other potentionmeter size
		}
		control.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				getSkinnable().requestLayout();
			}
		});

		anchorPane.getChildren().add(potentiometerView);

		arrow.getTransforms().add(rotate);

		if (control.getBottomLeftControl() != null) {
			anchorPane.getChildren().add(0, control.getBottomLeftControl());
			getSkinnable().forceLayout();
			control.getBottomLeftControl().relocate(0, 60);
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
		Potentiometer potentiometer = getSkinnable();
		double potentiometerViewX = contentX + contentWidth / 2 - potentiometerView.getWidth() / 2;
		double potentiometerViewY = contentY + contentHeight / 2 - potentiometerView.getHeight() / 2;
		potentiometerView.relocate(potentiometerViewX, potentiometerViewY);
		double unitePerDegree = (360 - potentiometer.getRotationBoundaryMinInDegree()
				- potentiometer.getRotationBoundaryMaxInDegree()) / (potentiometer.getMax() - potentiometer.getMin());
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
	}
}
