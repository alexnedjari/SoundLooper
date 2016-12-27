package com.soundlooper.system;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

import com.soundlooper.exception.SoundLooperException;
import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.model.tag.Tag;
import com.soundlooper.system.util.MessagingUtil;

public class ManageFavoriteTreeCellImpl extends TreeCell<Tag> {
	private TextField textField;

	@Override
	public void startEdit() {
		super.startEdit();

		if (textField == null) {
			createTextField();
		}
		setText(null);
		textField.setText(getItem().getName());

		setGraphic(textField);
		textField.requestFocus();
		textField.selectAll();
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		setText(getItem().getName());
		this.setGraphic(ImageGetter.getIconeTag16());
	}

	@Override
	public void commitEdit(Tag newValue) {
		Tag tag = newValue;
		if (textField.getText().length() > 0) {
			tag.setName(textField.getText());
			SoundLooperPlayer.getInstance().saveTag(tag);
			super.commitEdit(newValue);
			ManageFavoriteController.sort(this.getTreeView());
		} else {
			cancelEdit();
		}
	}

	@Override
	public void updateItem(Tag item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setText("");
			this.setGraphic(null);
		} else {
			Tag tag = item;
			if (tag.getId() == Tag.ROOT_TAG_ID) {
				// we are on the root
				setText(MessageReader.getInstance().getMessage(tag.getName()));

				MenuItem addTagMenu = getMenuAddTag(tag);
				this.setContextMenu(new ContextMenu(addTagMenu));
				configureDrop();
			} else {
				if (isEditing()) {
					if (textField != null) {
						textField.setText(tag.getName());
					}
					setText(null);
					setGraphic(textField);
				} else {
					setText(tag.getName());
					this.setGraphic(ImageGetter.getIconeTag16());
					MenuItem deleteTagMenu = new MenuItem(MessageReader.getInstance().getMessage(
							"window.manageFavorite.deleteTag"));
					deleteTagMenu.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							try {
								SoundLooperPlayer.getInstance().deleteTag(tag);

								TreeItem<Tag> treeItem = ManageFavoriteTreeCellImpl.this.getTreeItem();
								treeItem.getParent().getChildren().remove(treeItem);
							} catch (SoundLooperException e) {
								MessagingUtil.displayError("Impossible de supprimer le tag", e);
							}
						}
					});

					MenuItem addTagMenu = getMenuAddTag(tag);

					MenuItem renameTagMenu = new MenuItem(MessageReader.getInstance().getMessage(
							"window.manageFavorite.renameTag"));
					renameTagMenu.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							ManageFavoriteTreeCellImpl.this.startEdit();
						}
					});

					this.setContextMenu(new ContextMenu(addTagMenu, renameTagMenu, deleteTagMenu));
				}
				configureDrag();
				configureDrop();
			}
		}
	}

	private void configureDrag() {
		this.setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				ClipboardContent content = new ClipboardContent();
				Dragboard db = ManageFavoriteTreeCellImpl.this.startDragAndDrop(TransferMode.ANY);
				content.putString(ManageFavoriteController.DRAG_TYPE_TAG);
				db.setContent(content);
				event.consume();
			}
		});
	}

	private void configureDrop() {
		this.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				TreeView<Tag> treeView = ManageFavoriteTreeCellImpl.this.getTreeView();
				treeView.getSelectionModel().select(ManageFavoriteTreeCellImpl.this.getTreeItem());
				if (event.getDragboard().hasString()
						&& event.getDragboard().getString().equals(ManageFavoriteController.DRAG_TYPE_TAG)) {
					event.acceptTransferModes(TransferMode.MOVE);
				} else if (event.getDragboard().hasString()
						&& event.getDragboard().getString().equals(ManageFavoriteController.DRAG_TYPE_SONG)) {
					event.acceptTransferModes(TransferMode.LINK);
				}
				event.consume();
			}
		});

		this.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				Tag destinationTag = ManageFavoriteTreeCellImpl.this.getItem();
				if (event.getDragboard().getString().equals(ManageFavoriteController.DRAG_TYPE_TAG)) {
					ManageFavoriteTreeCellImpl draggedTreeCell = (ManageFavoriteTreeCellImpl) event.getGestureSource();
					if (!draggedTreeCell.equals(ManageFavoriteTreeCellImpl.this)) {
						Tag movedTag = draggedTreeCell.getTreeItem().getValue();
						draggedTreeCell.getTreeItem().getParent().getChildren().remove(draggedTreeCell.getTreeItem());

						SoundLooperPlayer.getInstance().moveTag(movedTag, destinationTag);

						ManageFavoriteTreeCellImpl.this.getTreeItem().getChildren().add(draggedTreeCell.getTreeItem());
						ManageFavoriteController.sort(ManageFavoriteTreeCellImpl.this.getTreeItem());
						draggedTreeCell = null;
					}
					event.consume();
				} else if (event.getDragboard().getString().equals(ManageFavoriteController.DRAG_TYPE_SONG)) {
					SongListCell songListCell = (SongListCell) event.getGestureSource();
					SoundLooperPlayer.getInstance().addTagToSong(songListCell.getItem(), destinationTag);
				}
			}
		});
	}

	private MenuItem getMenuAddTag(Tag tag) {
		MenuItem addTagMenu = new MenuItem(MessageReader.getInstance().getMessage("window.manageFavorite.addTag"));
		addTagMenu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					ManageFavoriteController.addTag(ManageFavoriteTreeCellImpl.this.getTreeItem(), tag);
				} catch (SoundLooperException e) {
					MessagingUtil.displayError("Impossible d'ajouter le tag", e);
				}
			}
		});
		return addTagMenu;
	}

	private void createTextField() {
		Tag tag = ManageFavoriteTreeCellImpl.this.getItem();
		textField = new TextField(tag.getName());
		textField.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent t) {
				if (t.getCode() == KeyCode.ENTER) {
					commitEdit(ManageFavoriteTreeCellImpl.this.getItem());
				} else if (t.getCode() == KeyCode.ESCAPE) {
					cancelEdit();
				}
			}
		});

		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (oldValue == true && newValue == false) {
					commitEdit(tag);
				}
			};
		});
	}
}
