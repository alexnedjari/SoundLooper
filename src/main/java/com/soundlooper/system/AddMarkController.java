package com.soundlooper.system;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import com.soundlooper.model.SoundLooperPlayer;

public class AddMarkController {

	@FXML
	private ComboBox<String> comboBoxName;

	@FXML
	private Label labelResultat;

	private String result = null;

	private Stage stage;

	public void init(Stage stage) {
		this.stage = stage;
		comboBoxName.getItems().add(MessageReader.getInstance().getMessage("mark.defaultName.intro"));
		comboBoxName.getItems().add(MessageReader.getInstance().getMessage("mark.defaultName.verse"));
		comboBoxName.getItems().add(MessageReader.getInstance().getMessage("mark.defaultName.refrain"));
		comboBoxName.getItems().add(MessageReader.getInstance().getMessage("mark.defaultName.solo"));
		comboBoxName.getItems().add(MessageReader.getInstance().getMessage("mark.defaultName.bridge"));
		comboBoxName.getItems().add(MessageReader.getInstance().getMessage("mark.defaultName.break"));
		comboBoxName.getItems().add(MessageReader.getInstance().getMessage("mark.defaultName.outro"));

		stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER) {
					validate();
				} else if (e.getCode() == KeyCode.ESCAPE) {
					cancel();
				}
			}
		});

		labelResultat.textProperty().bindBidirectional(comboBoxName.getEditor().textProperty(),
				new StringConverter<String>() {
					@Override
					public String fromString(String string) {
						return string + "FROM";
					}

					@Override
					public String toString(String object) {
						if (object == null) {
							return "";
						}
						return SoundLooperPlayer.getInstance().getValidNameForMark(
								SoundLooperPlayer.getInstance().getSong(), object);
					}
				});
	}

	public String getResult() {
		return result;
	}

	@FXML
	public void validate() {
		if (!"".equals(labelResultat.getText())) {
			result = labelResultat.getText();
			cancel();
		}
	}

	private void cancel() {
		stage.close();
	}

}
