package com.soundlooper.system.util;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.util.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.soundlooper.system.SoundLooper;

public class MessagingUtil {

	private static Logger logger = LogManager.getLogger(MessagingUtil.class);

	private static String LOCK_CONFIGURATION = "LOCK_CONFIGURATION";

	public static void displayMessage(String message) {
		synchronized (LOCK_CONFIGURATION) {
			Label labelState = SoundLooper.getInstance().getController().getLabelState();
			logger.info("display message : " + message);
			labelState.getStyleClass().removeAll("hiddenLabel", "labelInformation", "labelError");
			labelState.getStyleClass().add("labelInformation");
			labelState.setText(message);
			scheduleDeleteMessage(labelState);
		}
	}

	public static void displayError(String error, Exception e) {
		synchronized (LOCK_CONFIGURATION) {
			Label labelState = SoundLooper.getInstance().getController().getLabelState();
			logger.error("display error : " + error, e);
			labelState.getStyleClass().removeAll("hiddenLabel", "labelInformation", "labelError");
			labelState.getStyleClass().add("labelError");
			labelState.setText(error);
			scheduleDeleteMessage(labelState);
		}
	}

	private static void scheduleDeleteMessage(Label labelState) {
		new Thread(new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				Thread.sleep(3000);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), labelState);
						fadeTransition.setFromValue(1.0);
						fadeTransition.setToValue(0.0);
						fadeTransition.play();
					}
				});
				return null;
			}
		}).start();
	}

}
