package com.soundlooper.gui.customComponent.hspinner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		Slider slider = new Slider(50, 200, 100);
		HSpinner spinner = new HSpinner();
		spinner.valueProperty().bindBidirectional(slider.valueProperty());
		spinner.setMax(200);
		spinner.setMin(50);

		slider.setValue(100);

		VBox myPane = new VBox(slider, spinner);
		myPane.setAlignment(Pos.CENTER);
		myPane.setSpacing(12);
		myPane.setPadding(new Insets(0, 0, 6, 0));
		Scene myScene = new Scene(myPane);
		primaryStage.setScene(myScene);
		primaryStage.setTitle("App");
		primaryStage.setWidth(300);
		primaryStage.setHeight(200);
		primaryStage.show();
	}

	public static void main(String[] args) {
		Main.launch(args);
	}

}
