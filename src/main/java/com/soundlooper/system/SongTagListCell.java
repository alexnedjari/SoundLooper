package com.soundlooper.system;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;

import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.model.tag.Tag;
import com.soundlooper.service.entite.tag.TagService;

public class SongTagListCell extends ListCell<Tag> {

	private ManageFavoriteController manageFavoriteController;

	public SongTagListCell(ManageFavoriteController manageFavoriteController) {
		this.manageFavoriteController = manageFavoriteController;
	}

	@Override
	protected void updateItem(Tag item, boolean empty) {
		super.updateItem(item, empty);
		setGraphic(null);
		setText(null);
		if (item != null) {
			this.setGraphic(ImageGetter.getIconeTag16());
			setText(TagService.getInstance().getFullPath(item));
			MenuItem deleteFavoriteMenu = new MenuItem(MessageReader
					.getInstance().getMessage(
							"window.manageFavorite.deleteTagSongLink"));
			deleteFavoriteMenu.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					SoundLooperPlayer.getInstance().removeTagFromSong(
							manageFavoriteController.getSongListView()
									.getSelectionModel().getSelectedItem(),
							item);
				}
			});
			this.setContextMenu(new ContextMenu(deleteFavoriteMenu));
		}
	}

}
