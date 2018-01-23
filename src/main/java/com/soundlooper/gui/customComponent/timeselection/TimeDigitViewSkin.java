package com.soundlooper.gui.customComponent.timeselection;

import com.soundlooper.CssColor;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class TimeDigitViewSkin extends SkinBase<TimeDigitView> {

	Label label = new Label("0");
	Rectangle rectangle;

	protected TimeDigitViewSkin(TimeDigitView control) {
		super(control);
		label.resize(TimeTextFieldViewSkin.DIGIT_WIDTH, 20);
		getChildren().add(label);
		label.textProperty().bind(control.digitProperty().asString());
		label.setTextFill(CssColor.BLUE.getColor());

		rectangle = new Rectangle();

		label.setFocusTraversable(false);
		rectangle.setFocusTraversable(false);
		control.setFocusTraversable(false);

		control.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				control.getParentControl().requestFocus();
				control.getParentControl().select(control);
			};
		});
	}

	@Override
	protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
		if (getSkinnable().isSelected()) {
			label.getStyleClass().removeAll("white", "lightgray");
			label.getStyleClass().add("lightgray");
		} else {
			label.getStyleClass().removeAll("white", "lightgray");
			label.getStyleClass().add("white");
		}
	}

}
