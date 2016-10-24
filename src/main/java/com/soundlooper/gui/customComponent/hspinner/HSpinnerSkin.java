package com.soundlooper.gui.customComponent.hspinner;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;

public class HSpinnerSkin extends SkinBase<HSpinner> {

	private BorderPane borderPane = new BorderPane();

	private Button leftButton;

	private Button rightButton;

	private Label label;

	private Text text;

	protected HSpinnerSkin(HSpinner control) {
		super(control);

		leftButton = new Button("<");
		leftButton.prefWidth(100);
		rightButton = new Button(">");
		rightButton.prefWidth(100);
		label = new Label();
		label.textProperty().bindBidirectional(control.valueProperty(), new NumberStringConverter());
		leftButton.prefWidth(100);
		text = new Text();
		// TODO css
		text.prefWidth(100);
		text.textProperty().bindBidirectional(label.textProperty());

		borderPane.prefWidth(300);
		borderPane.minWidth(300);
		borderPane.maxWidth(300);
		borderPane.prefHeight(300);
		borderPane.minHeight(300);
		borderPane.maxHeight(300);
		borderPane.setStyle("-fx-background-color:blue;");

		control.editionProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					borderPane.setCenter(text);
				} else {
					borderPane.setCenter(label);
				}
			}
		});

		getChildren().add(borderPane);
	}

	@Override
	protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {

	}
}