package com.soundlooper.system;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import com.soundlooper.system.search.SearchEngine;
import com.soundlooper.system.search.Searchable;
import com.soundlooper.system.search.StringTransformerAccentuation;
import com.soundlooper.system.search.StringTransformerNoCase;

public class SearchDialogController {

	@FXML
	private TextField searchText;

	@FXML
	private ListView<Searchable> resultList;

	private Stage stage;
	private List<? extends Searchable> listSearchable = new ArrayList<Searchable>();
	private Searchable result;
	private SearchEngine search;

	@FXML
	public void close() {
		stage.close();
	}

	public void init(List<? extends Searchable> listSearchable, Stage stage) {
		this.stage = stage;
		this.listSearchable = listSearchable;
		this.search = new SearchEngine(this.listSearchable);

		this.search.addTransformer(new StringTransformerAccentuation());
		this.search.addTransformer(new StringTransformerNoCase());

		resultList.setItems(this.search.getLastResult());
		searchText.textProperty().addListener(
				(observable, oldValue, newValue) -> {
					this.search.cancelSearch();
					this.search.performSearch(newValue);
				});

		this.search.performSearch("");
	}

	@FXML
	public void onMouseClickedList(MouseEvent event) {
		if (resultList.getSelectionModel().getSelectedItem() != null) {
			if (event.getClickCount() > 1) {
				validateSearch(resultList.getSelectionModel().getSelectedItem());
			}
		}
	}

	public void onKeyReleasedSearchText(KeyEvent event) {
		if (event.getCode() == KeyCode.DOWN) {
			if (resultList.getSelectionModel().getSelectedItem() != null) {
				resultList.getSelectionModel().selectNext();
			} else if (this.resultList.getItems().size() > 0) {
				resultList.getSelectionModel().select(0);
			}
		} else if (event.getCode() == KeyCode.UP) {
			if (resultList.getSelectionModel().getSelectedItem() != null) {
				resultList.getSelectionModel().selectPrevious();
			}
		}
	}

	@FXML
	public void onKeyReleased(KeyEvent event) {
		if (event.getCode() == KeyCode.ESCAPE) {
			close();
		} else if (event.getCode() == KeyCode.ENTER) {
			if (this.resultList.getItems().size() == 1) {
				validateSearch(this.resultList.getItems().get(0));
			} else if (resultList.getSelectionModel().getSelectedItem() != null) {
				validateSearch(resultList.getSelectionModel().getSelectedItem());
			}
		}
	}

	private void validateSearch(Searchable searchable) {
		this.result = searchable;
		close();
	}

	public Searchable getResult() {
		return this.result;
	}

}
