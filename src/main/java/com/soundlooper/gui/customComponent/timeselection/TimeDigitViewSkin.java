package com.soundlooper.gui.customComponent.timeselection;

import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;

public class TimeDigitViewSkin extends SkinBase<TimeDigitView> {

	Label label = new Label("0");

	protected TimeDigitViewSkin(TimeDigitView control) {
		super(control);
		// BorderPane borderPane = new BorderPane();
		// borderPane.setBackground(new Background(new BackgroundFill(Color.RED,
		// arg1, arg2)));
		// getChildren().add(borderPane);
		label.resize(10, 20);
		getChildren().add(label);
		label.textProperty().bind(control.digitProperty().asString());
	}

	@Override
	protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
	}

}
