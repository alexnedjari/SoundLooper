package com.soundlooper.system.util;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.util.Duration;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.soundlooper.system.SoundLooper;

public class MessagingUtil {

	private static Logger logger = LogManager.getLogger(MessagingUtil.class);

	private static Queue<Message> queue = new ConcurrentLinkedQueue<Message>();

	private static Thread threadMessage = new Thread() {
		@Override
		public void run() {
			Label labelState = SoundLooper.getInstance().getController().getLabelState();
			FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), labelState);
			while (true) {
				try {
					Thread.sleep(500);
					while (!queue.isEmpty()) {
						// if the fade animation is playing, we must stop it
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								fadeTransition.stop();
							}
						});

						Message message = queue.remove();

						logger.info("display message : " + message);
						labelState.getStyleClass().removeAll("hiddenLabel", "labelInformation", "labelError");

						if (StringUtils.equals(message.level, Message.LEVEL_ERROR)) {
							labelState.getStyleClass().add("labelError");
						} else {
							labelState.getStyleClass().add("labelInformation");
						}
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								labelState.setText(message.message);
							}
						});

						Thread.sleep(3000);
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								fadeTransition.setFromValue(1.0);
								fadeTransition.setToValue(0.0);
								fadeTransition.playFromStart();
							}
						});
					}
				} catch (InterruptedException e) {
					logger.error("Interruption du thread message : ", e);
				}
			}
		};
	};

	static {
		threadMessage.start();
	}

	public static void displayMessage(String message) {
		addMessageToQueue(new Message(message, Message.LEVEL_INFO));
	}

	public static void displayError(String message, Exception e) {
		logger.error(message, e);
		addMessageToQueue(new Message(message, Message.LEVEL_ERROR));
	}

	private static void addMessageToQueue(Message message) {
		logger.info("Affichage du message " + message);
		queue.add(message);
	}

	private static class Message {
		public final static String LEVEL_INFO = "INFO";
		public final static String LEVEL_ERROR = "ERROR";

		private String message;
		private String level;

		public Message(String message, String level) {
			super();
			this.message = message;
			this.level = level;
		}

		@Override
		public String toString() {
			return "Message [message=" + message + ", level=" + level + "]";
		}
	}

}
