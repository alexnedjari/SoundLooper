package com.soundlooper.gui.customComponent.potentiometer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application{

	public void start(Stage primaryStage) throws Exception {
		
		
		Slider slider = new Slider(50, 200, 100);
		Potentiometer potentiometer = new Potentiometer();
		potentiometer.valueProperty().bindBidirectional(slider.valueProperty());
		potentiometer.setMax(200);
		potentiometer.setMin(50);
		
		slider.setValue(100);
		
		VBox myPane = new VBox(slider, potentiometer);
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
