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
		// BorderPane borderPane = new BorderPane();
		// borderPane.setBackground(new Background(new BackgroundFill(Color.RED,
		// arg1, arg2)));
		// getChildren().add(borderPane);
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
			// label.setBackground(new Background(new
			// BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY,
			// Insets.EMPTY)));
			// label.getStyleClass().remove(".lightGray");
			// label.getStyleClass().add(".veryLightGray");
			label.getStyleClass().removeAll("white", "lightgray");
			label.getStyleClass().add("lightgray");
		} else {
			// label.setBackground(new Background(new
			// BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY,
			// Insets.EMPTY)));
			// label.getStyleClass().remove(".veryLightGray");
			label.getStyleClass().removeAll("white", "lightgray");
			label.getStyleClass().add("white");
			// label.setStyle("-fx-background-color: #242424");
		}
	}

}
