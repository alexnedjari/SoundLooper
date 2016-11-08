package com.soundlooper.system.handler;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class NumericFieldEventHandler implements EventHandler<KeyEvent> {
	@Override
	public void handle(KeyEvent event) {
		if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
			event.consume();
		}
	}
}
