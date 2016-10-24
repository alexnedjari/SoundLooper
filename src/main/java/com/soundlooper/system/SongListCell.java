package com.soundlooper.system;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

import com.soundlooper.model.song.Song;

public class SongListCell extends ListCell<Song> {

	@Override
	protected void updateItem(Song item, boolean empty) {
		super.updateItem(item, empty);
		setGraphic(null);
		setText(null);
		if (item != null) {
			this.setGraphic(ImageGetter.getIconeSong16());
			setText(item.getFile().getName() + " ("
					+ item.getFile().getParentFile().getPath() + ")");
			configureDrag();

			MenuItem deleteFavoriteMenu = new MenuItem(MessageReader
					.getInstance().getMessage(
							"window.manageFavorite.deleteFavorite"));
			deleteFavoriteMenu.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					item.setFavorite(false);
				}
			});
			this.setContextMenu(new ContextMenu(deleteFavoriteMenu));
		}
	}

	private void configureDrag() {
		this.setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				ClipboardContent content = new ClipboardContent();

				Dragboard db = SongListCell.this
						.startDragAndDrop(TransferMode.ANY);
				content.putString(ManageFavoriteController.DRAG_TYPE_SONG);
				db.setContent(content);
				event.consume();
			}
		});
	}
}
