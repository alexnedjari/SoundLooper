package com.soundlooper.system;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import com.soundlooper.exception.SoundLooperException;
import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.model.song.Song;
import com.soundlooper.model.tag.Tag;
import com.soundlooper.service.entite.song.SongService;
import com.soundlooper.system.util.MessagingUtil;

public class ManageFavoriteController {

	public final static String DRAG_TYPE_TAG = "TAG";
	public final static String DRAG_TYPE_SONG = "SONG";

	@FXML
	private TreeView<Tag> treeView;

	@FXML
	private ListView<Song> songListView;

	@FXML
	private ListView<Tag> tagListView;

	private TreeItem<Tag> rootTreeItem;
	private Tag root;

	public void initialize(Stage stage) {
		root = Tag.getRoot();

		rootTreeItem = new TreeItem<>(root);
		createLevel(root, rootTreeItem);

		treeView.setRoot(rootTreeItem);

		treeView.setCellFactory(new Callback<TreeView<Tag>, TreeCell<Tag>>() {
			@Override
			public TreeCell<Tag> call(TreeView<Tag> p) {
				return new ManageFavoriteTreeCellImpl();
			}
		});

		sort(treeView);
		rootTreeItem.setExpanded(true);
		songListView.setItems(SongService.getInstance().getSortedByNameFavoriteSongList());
		songListView.setCellFactory(c -> new SongListCell());
		ChangeListener<Song> changeSongListener = new ChangeListener<Song>() {
			@Override
			public void changed(ObservableValue<? extends Song> observable, Song oldValue, Song newValue) {
				tagListView.setItems(newValue.tagListProperty());

			}
		};
		songListView.getSelectionModel().selectedItemProperty().addListener(changeSongListener);
		tagListView.setCellFactory(c -> new SongTagListCell(this));

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				songListView.getSelectionModel().selectedItemProperty().removeListener(changeSongListener);

			}
		});
	}

	private void createLevel(Tag tag, TreeItem<Tag> treeItem) {
		List<Tag> listChildrenCopy = tag.getListChildrenCopy();
		for (Tag children : listChildrenCopy) {
			TreeItem<Tag> childTreeItem = new TreeItem<>(children);
			treeItem.getChildren().add(childTreeItem);
			createLevel(children, childTreeItem);
		}
	}

	@FXML
	public void deleteFavorite() {
		Song song = songListView.getSelectionModel().getSelectedItem();
		song.setFavorite(false);
	}

	@FXML
	public void deleteTag() {
		try {
			TreeItem<Tag> treeItem = treeView.getSelectionModel().getSelectedItem();
			SoundLooperPlayer.getInstance().deleteTag(treeItem.getValue());
			treeItem.getParent().getChildren().remove(treeItem);
		} catch (SoundLooperException e) {
			MessagingUtil.displayError("Impossible de supprimer le tag", e);
		}
	}

	@FXML
	public void addTag() {
		Tag newTag;
		try {
			TreeItem<Tag> parentTreeItem = treeView.getSelectionModel().getSelectedItem();
			newTag = SoundLooperPlayer.getInstance().createTag(
					MessageReader.getInstance().getMessage("tag.defaultName"), parentTreeItem.getValue());
			TreeItem<Tag> treeItem = new TreeItem<Tag>(newTag);
			parentTreeItem.getChildren().add(treeItem);
			sort(parentTreeItem);
			parentTreeItem.setExpanded(true);
		} catch (SoundLooperException e) {
			MessagingUtil.displayError("Impossible d'ajouter le tag", e);
		}
	}

	public static void addTag(TreeItem<Tag> parentTreeItem, Tag parentTag) throws SoundLooperException {
		Tag newTag = SoundLooperPlayer.getInstance().createTag(
				MessageReader.getInstance().getMessage("tag.defaultName"), parentTag);

		TreeItem<Tag> newTagTreeItem = new TreeItem<Tag>(newTag);
		parentTreeItem.getChildren().add(newTagTreeItem);
		sort(parentTreeItem);
		parentTreeItem.setExpanded(true);
	}

	public static void sort(TreeView<Tag> tree) {
		sort(tree.getRoot());
	}

	public static void sort(TreeItem<Tag> item) {
		if (item == null || item.isLeaf()) {
			return;
		}
		FXCollections.sort(item.getChildren(), new ManageFavoriteTreeComparator());
		for (TreeItem<Tag> childItem : item.getChildren()) {
			sort(childItem);
		}
	}

	public ListView<Song> getSongListView() {
		return songListView;
	}

}
