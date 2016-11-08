package com.soundlooper.system.handler;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class NumericFieldEventFilter implements EventHandler<KeyEvent> {
	@Override
	public void handle(KeyEvent event) {
		if (event.getEventType() == KeyEvent.KEY_TYPED && !event.getCharacter().matches("[1-9]")) {
			event.consume();
		}

		if (event.getEventType() == KeyEvent.KEY_PRESSED && event.getCode() != KeyCode.LEFT
				&& event.getCode() != KeyCode.RIGHT && event.getCode() != KeyCode.BACK_SPACE
				&& event.getCode() != KeyCode.DELETE && event.getCode() != KeyCode.TAB) {
			event.consume();
		}
	}
}
