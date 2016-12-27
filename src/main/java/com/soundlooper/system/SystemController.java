package com.soundlooper.system;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.converter.NumberStringConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.soundlooper.audio.player.Player.PlayerState;
import com.soundlooper.exception.PlayerException;
import com.soundlooper.exception.SoundLooperException;
import com.soundlooper.exception.SoundLooperRuntimeException;
import com.soundlooper.gui.customComponent.playerView.PlayerView;
import com.soundlooper.gui.customComponent.potentiometer.Potentiometer;
import com.soundlooper.gui.customComponent.timeselection.TimeSelectionView;
import com.soundlooper.model.SoundLooperObject;
import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.model.mark.Mark;
import com.soundlooper.model.song.Song;
import com.soundlooper.model.tag.Tag;
import com.soundlooper.service.entite.song.SongService;
import com.soundlooper.system.handler.NumericFieldEventFilter;
import com.soundlooper.system.handler.NumericFieldEventHandler;
import com.soundlooper.system.preferences.Preferences;
import com.soundlooper.system.preferences.SoundLooperProperties;
import com.soundlooper.system.preferences.recentfile.RecentFile;
import com.soundlooper.system.search.Searchable;
import com.soundlooper.system.util.MessagingUtil;

public class SystemController {

	private Logger logger = LogManager.getLogger(this.getClass());

	@FXML
	private Button setBeginAlignmentButton;

	@FXML
	private Button setEndAlignmentButton;

	@FXML
	private Button playButton;

	@FXML
	private Button addMarkButton;

	@FXML
	private Button saveMarkButton;

	@FXML
	private Button pauseButton;

	@FXML
	private Label labelState;

	@FXML
	private Menu recentFileMenu;

	@FXML
	private ToggleButton alwaysOnTopButton;

	@FXML
	private ToggleButton favoriteButton;

	@FXML
	private MenuButton favoriteMenuButton;

	@FXML
	private MenuButton markMenuButton;

	@FXML
	private Potentiometer volumePotentiometer;

	@FXML
	private Potentiometer timestretchPotentiometer;

	@FXML
	private PlayerView playerView;

	@FXML
	private TimeSelectionView timeSelectionView;

	public void init() {
		initInterfaceState();

		// --------------------------------------------------

		timestretchPotentiometer.setMin(50);
		timestretchPotentiometer.setMax(200);
		timestretchPotentiometer.setValue(100);
		timestretchPotentiometer.valueProperty().bindBidirectional(
				SoundLooperPlayer.getInstance().timeStretchProperty());

		MenuButton timestrechButton = new MenuButton();
		timestrechButton.textProperty().bind(Bindings.convert(SoundLooperPlayer.getInstance().timeStretchProperty()));

		timestrechButton.getItems().add(createTimestrechMenuItem(50));
		timestrechButton.getItems().add(createTimestrechMenuItem(90));
		timestrechButton.getItems().add(createTimestrechMenuItem(100));
		timestrechButton.getItems().add(createTimestrechMenuItem(110));
		timestrechButton.getItems().add(createTimestrechMenuItem(200));

		TextField textfieldTimestretch = new TextField();
		textfieldTimestretch.textProperty().bindBidirectional(SoundLooperPlayer.getInstance().timeStretchProperty(),
				new NumberStringConverter());
		textfieldTimestretch.addEventFilter(KeyEvent.ANY, new NumericFieldEventFilter());
		textfieldTimestretch.addEventHandler(KeyEvent.ANY, new NumericFieldEventHandler());

		textfieldTimestretch.setOnMouseClicked(e -> {
			textfieldTimestretch.selectAll();
		});

		MenuItem menuItemSpinnerTimestrech = new MenuItem("", textfieldTimestretch);
		timestrechButton.getItems().add(menuItemSpinnerTimestrech);
		timestrechButton.setPrefSize(40, 32);
		timestrechButton.setFocusTraversable(false);
		timestrechButton.getStyleClass().add("timestrechButton");

		// timestrechButton.getStyleClass().add("toggleMuteButton");
		timestretchPotentiometer.setCentralButton(timestrechButton);

		// -----------------------------------------------------
		volumePotentiometer.setMin(0);
		volumePotentiometer.setMax(100);
		volumePotentiometer.valueProperty().bindBidirectional(SoundLooperPlayer.getInstance().volumeProperty());
		volumePotentiometer.setValue(100);

		ToggleButton muteButton = new ToggleButton();
		muteButton.setPrefSize(40, 32);
		muteButton.selectedProperty().bindBidirectional(SoundLooperPlayer.getInstance().muteProperty());
		muteButton.setFocusTraversable(false);

		muteButton.getStyleClass().add("toggleMuteButton");
		volumePotentiometer.setCentralButton(muteButton);

		// --------------------------------------------
		favoriteMenuButton.showingProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue == true) {
					updateFavoriteList();
				}
			}
		});
		// ---------------------ALWAYS_ON_TOP BUTTON-----------------------
		alwaysOnTopButton.selectedProperty().bindBidirectional(Preferences.getInstance().alwaysOnTopProperty());
		alwaysOnTopButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				SoundLooper.getInstance().getPrimaryStage().setAlwaysOnTop(newValue);
			}
		});

		// ---------------------FAVORITE BUTTON-----------------------
		SoundLooperPlayer.getInstance().songProperty().addListener(new ChangeListener<Song>() {
			@Override
			public void changed(ObservableValue<? extends Song> observable, Song oldSong, Song newSong) {

				if (oldSong != null) {
					// oldSong.isFavoriteProperty().unbindBidirectional(
					// favoriteButton.selectedProperty());
					favoriteButton.selectedProperty().unbindBidirectional(oldSong.isFavoriteProperty());
				}
				favoriteButton.selectedProperty().bindBidirectional(newSong.isFavoriteProperty());
			};
		});

		// ----------------------MARK LIST-------------------------------------
		SoundLooperPlayer.getInstance().songProperty().addListener(new ChangeListener<Song>() {
			protected MapChangeListener<String, Mark> mapMarkListener = new MapChangeListener<String, Mark>() {
				@Override
				public void onChanged(MapChangeListener.Change<? extends String, ? extends Mark> change) {
					updateMarkList();

				}
			};

			@Override
			public void changed(ObservableValue<? extends Song> observable, Song oldSong, Song newSong) {
				if (oldSong != null) {
					oldSong.marksProperty().removeListener(mapMarkListener);
				}
				newSong.marksProperty().addListener(mapMarkListener);
				updateMarkList();
			};
		});

		// -------------------------WINDOW TITLE----------------------------
		setCompleteTitle(null);
		SoundLooperPlayer.getInstance().markProperty().addListener(new ChangeListener<Mark>() {
			@Override
			public void changed(ObservableValue<? extends Mark> observable, Mark oldValue, Mark newValue) {
				setCompleteTitle(newValue);
				updateMarkList();
			}
		});

	}

	private MenuItem createTimestrechMenuItem(int timestrechPercent) {
		MenuItem menuItem = new MenuItem("", new Label(String.valueOf(timestrechPercent)));
		menuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				SoundLooperPlayer.getInstance().setTimeStretch(timestrechPercent);

			}
		});
		return menuItem;
	}

	public void initShortcut() {
		SoundLooper.getInstance().getPrimaryStage().getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				try {
					SoundLooperPlayer player = SoundLooperPlayer.getInstance();
					if (event.getCode() == KeyCode.O && event.isControlDown()) {
						// CTRL-O : Open a file
						openFile();
						event.consume();
					} else if (event.getCode() == KeyCode.SPACE) {
						// SPACE : Play / Pause
						int state = player.getState();
						if (state == PlayerState.STATE_PAUSED) {
							playSong();
						} else if (state == PlayerState.STATE_PLAYING) {
							pauseSong();
						}
						event.consume();
					} else if (event.getCode() == KeyCode.UP && event.isControlDown() && event.isShiftDown()) {
						// CTRL + SHIFT + UP : timestretch +10
						player.incrementTimeStretch(10);
						event.consume();
					} else if (event.getCode() == KeyCode.UP && event.isControlDown()) {
						// CTRL + UP : timestretch +1
						player.incrementTimeStretch(1);
						event.consume();
					} else if (event.getCode() == KeyCode.DOWN && event.isControlDown() && event.isShiftDown()) {
						// CTRL + SHIFT + DOWN : timestretch -10
						player.incrementTimeStretch(-10);
						event.consume();
					} else if (event.getCode() == KeyCode.DOWN && event.isControlDown()) {
						// CTRL + UP : timestretch -1
						player.incrementTimeStretch(-1);
						event.consume();
					} else if (event.getCode() == KeyCode.ADD) {
						// + : increase level
						player.incrementVolume(5);
						event.consume();
					} else if (event.getCode() == KeyCode.SUBTRACT) {
						// - : decrease level
						player.incrementVolume(-5);
						event.consume();
					} else if (event.getCode() == KeyCode.HOME) {
						// Origin : set the media time at the start position
						if (player.isSoundInitialized()) {
							player.setMediaTime(player.getLoopPointBeginMillisecond());
						}
						event.consume();
					} else if (event.getCode() == KeyCode.F && event.isControlDown()) {
						// CTRL + F : search in the favorites
						searchFavorite();
						event.consume();
					} else if (event.getCode() == KeyCode.M && event.isControlDown()) {
						// CTRL + M : search in the favorites
						searchMark();
						event.consume();
					} else if (event.getCode() == KeyCode.F) {
						// F : switch favorite status
						player.switchFavoriteOnCurrentSong();
						event.consume();
					} else if (event.getCode() == KeyCode.M) {
						// M : Add current position to marks
						openAddMarkDialog();
						event.consume();
					} else if (event.getCode() == KeyCode.NUMPAD1 && event.isControlDown()) {
						// CTRL + 1 : Set the begin position from media time
						setBeginAlignment();
						event.consume();
					} else if (event.getCode() == KeyCode.NUMPAD2 && event.isControlDown()) {
						// CTRL + 2 : Set the end position from media time
						setEndAlignment();
						event.consume();
					} else if (event.getCode() == KeyCode.LEFT) {
						// LEFT : Move in media -3s
						player.moveMediaTime(-3000);
						event.consume();
					} else if (event.getCode() == KeyCode.RIGHT) {
						// RIGHT : Move in media +3s
						player.moveMediaTime(3000);
						event.consume();
					}
				} catch (SoundLooperException e) {
					MessagingUtil.displayError("Impossible d'effectuer l'action demandée", e);
				}
			}
		});
	}

	private void initInterfaceState() {
		SoundLooperPlayer soundLooperPlayer = SoundLooperPlayer.getInstance();
		playButton.disableProperty().bind(soundLooperPlayer.stateProperty().isEqualTo(PlayerState.STATE_PAUSED).not());
		pauseButton.disableProperty()
				.bind(soundLooperPlayer.stateProperty().isEqualTo(PlayerState.STATE_PLAYING).not());
		markMenuButton.disableProperty().bind(soundLooperPlayer.isCurrentSongFavoriteProperty().not());
		saveMarkButton.disableProperty().bind(
				soundLooperPlayer.isCurrentSongFavoriteProperty().not()
						.or(soundLooperPlayer.isCurrentMarkEditableProperty().not())
						.or(soundLooperPlayer.isCurrentMarkDirtyProperty().not()));

		addMarkButton.disableProperty().bind(soundLooperPlayer.isCurrentSongFavoriteProperty().not());

		favoriteButton.disableProperty().bind(soundLooperPlayer.songProperty().isNull());
		setBeginAlignmentButton.disableProperty().bind(soundLooperPlayer.songProperty().isNull());
		setEndAlignmentButton.disableProperty().bind(soundLooperPlayer.songProperty().isNull());
	}

	@FXML
	public void increaseLevel(ActionEvent e) {

		int newVolume = SoundLooperPlayer.getInstance().getVolume() + 5;
		if (newVolume > 100) {
			newVolume = 100;
		}
		SoundLooperPlayer.getInstance().setVolume(newVolume);

	}

	@FXML
	public void decreaseLevel(ActionEvent e) {
		int newVolume = SoundLooperPlayer.getInstance().getVolume() - 5;
		if (newVolume < 0) {
			newVolume = 0;
		}
		SoundLooperPlayer.getInstance().setVolume(newVolume);

	}

	/**
	 * Set the complete title of the window
	 * 
	 * @param selectedFile
	 *            the selected file name or "" if there is no file selected
	 */
	private void setCompleteTitle(Mark mark) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Stage primaryStage = SoundLooper.getInstance().getPrimaryStage();
				primaryStage.setTitle(SoundLooperProperties.getInstance().getApplicationPresentation());
				if (mark != null) {
					String selectedFile = mark.getSong().getFile().getName();
					primaryStage.setTitle(primaryStage.getTitle() + " : " + selectedFile + " (" + mark.getName() + ")");
				} else {
					primaryStage.setTitle(primaryStage.getTitle() + " ("
							+ MessageReader.getInstance().getMessage("window.main.noFileLoaded") + ")");
				}

			}
		});
	}

	@FXML
	public void close() {
		SoundLooper.getInstance().getPrimaryStage().getOnCloseRequest()
				.handle(new WindowEvent(SoundLooper.getInstance().getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
		SoundLooper.getInstance().getPrimaryStage().close();
	}

	@FXML
	public void manageFavorite() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/ManageFavoriteDialog.fxml"));

			loader.load();

			Parent root = loader.getRoot();
			ManageFavoriteController controller = (ManageFavoriteController) loader.getController();

			Stage modalDialog = new Stage(StageStyle.UTILITY);
			modalDialog.initOwner(SoundLooper.getInstance().getPrimaryStage());
			modalDialog.setTitle(MessageReader.getInstance().getMessage("window.manageFavorite.title"));

			Scene scene = new Scene(root);
			scene.getStylesheets().add("/style/application.css");

			modalDialog.setScene(scene);
			controller.initialize(modalDialog);

			modalDialog.showAndWait();
		} catch (IOException e) {
			throw new SoundLooperRuntimeException("Unable to open dialog", e);
		}
	}

	@FXML
	public void searchFavorite() {
		List<Song> favoriteSongList = SoundLooperPlayer.getInstance().getFavoriteSongList();
		openSearchDialog(favoriteSongList, MessageReader.getInstance().getMessage("search.favorites"),
				(controller) -> {
					Song song = (Song) controller.getResult();
					if (song != null) {
						openFile(song.getFile());
					}
				});
	}

	private void openSearchDialog(List<? extends Searchable> favoriteSongList, String title,
			Consumer<SearchDialogController> onDialogClosed) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/SearchDialog.fxml"));
			loader.load();
			Parent root = loader.getRoot();
			SearchDialogController controller = (SearchDialogController) loader.getController();

			Stage modalDialog = new Stage(StageStyle.UTILITY);
			modalDialog.initModality(Modality.APPLICATION_MODAL);
			modalDialog.initOwner(SoundLooper.getInstance().getPrimaryStage());
			modalDialog.setTitle(title);
			Scene scene = new Scene(root);

			controller.init(favoriteSongList, modalDialog);
			modalDialog.setScene(scene);
			modalDialog.showAndWait();

			onDialogClosed.accept(controller);
		} catch (IOException e) {
			throw new SoundLooperRuntimeException("Unable to open dialog", e);
		}

	}

	@FXML
	public void searchMark() {
		Song song = SoundLooperPlayer.getInstance().getSong();
		if (song != null && song.isFavorite()) {
			ArrayList<Mark> markList = new ArrayList<Mark>(song.getMarks().values());
			openSearchDialog(markList, MessageReader.getInstance().getMessage("search.marks"), (controller) -> {
				Mark mark = (Mark) controller.getResult();
				if (mark != null) {
					selectMark(mark);
				}
			});
		}

	}

	private void selectMark(Mark mark) {
		try {
			SoundLooperPlayer.getInstance().selectMark(mark);
		} catch (PlayerException e) {
			MessagingUtil.displayError("Impossible de sélectionner le marqueur", e);
		}
	}

	private void openFile(File file) {

		try {
			SoundLooperPlayer.getInstance().loadSong(file);
			MessagingUtil.displayMessage("Fichier '" + file.getAbsolutePath() + " chargé");
		} catch (PlayerException e) {
			MessagingUtil.displayError("Impossible d'ouvrir le fichier '" + file.getAbsolutePath() + "'", e);
		}

	}

	@FXML
	public void openFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(MessageReader.getInstance().getMessage("window.openFile.title"));
		File lastSelectedFile = new File(Preferences.getInstance().getLastPathUsed());
		if (lastSelectedFile != null && lastSelectedFile.getParentFile() != null
				&& lastSelectedFile.getParentFile().exists()) {
			fileChooser.setInitialDirectory(lastSelectedFile.getParentFile());
		}
		File selectedFile = fileChooser.showOpenDialog(SoundLooper.getInstance().getPrimaryStage().getScene()
				.getWindow());
		if (selectedFile != null) {
			openFile(selectedFile);
		}
	}

	@FXML
	public void onDragOver(DragEvent event) {
		Dragboard db = event.getDragboard();
		if (db.hasFiles()) {
			event.acceptTransferModes(TransferMode.COPY);
		} else {
			event.consume();
		}
	}

	@FXML
	public void onDragDropped(DragEvent event) {
		Dragboard db = event.getDragboard();
		boolean success = false;
		if (db.hasFiles()) {
			success = true;
			for (File file : db.getFiles()) {
				openFile(file);
				// Only open the first file
				break;
			}
		}
		event.setDropCompleted(success);
		event.consume();
	}

	@FXML
	public void playSong() throws PlayerException {

		int state = SoundLooperPlayer.getInstance().getState();
		if (state == PlayerState.STATE_PAUSED) {
			try {
				SoundLooperPlayer.getInstance().play();
			} catch (PlayerException e) {
				MessagingUtil.displayError("Impossible de lancer la lecture", e);
			}
		}
	}

	@FXML
	public void pauseSong() {
		int state = SoundLooperPlayer.getInstance().getState();
		if (state == PlayerState.STATE_PLAYING) {
			try {
				SoundLooperPlayer.getInstance().pause();
			} catch (PlayerException e) {
				MessagingUtil.displayError("Impossible de mettre en pause", e);
			}
		}
	}

	public void populateRecentFileMenu() {

		recentFileMenu.getItems().clear();
		List<RecentFile> recentFileList = Preferences.getInstance().getRecentFileList();
		for (final RecentFile recentFile : recentFileList) {
			if (recentFile.getFile().exists()) {
				MenuItem menuItemRecentFile = new MenuItem(recentFile.getFile().getName());
				menuItemRecentFile.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						openFile(recentFile.getFile());
					}
				});
				recentFileMenu.getItems().add(menuItemRecentFile);
			}
		}
	}

	private void updateMarkList() {
		markMenuButton.getItems().removeAll(markMenuButton.getItems());
		List<Mark> markList = new ArrayList<Mark>(SoundLooperPlayer.getInstance().getSong().getMarks().values());
		markList.sort(new Comparator<Mark>() {
			@Override
			public int compare(Mark o1, Mark o2) {
				if (o1.getId() == SoundLooperObject.ID_NOT_INITIALIZED) {
					return -1;
				}
				if (o2.getId() == SoundLooperObject.ID_NOT_INITIALIZED) {
					return 1;
				}
				return Integer.valueOf(o1.getBeginMillisecond()).compareTo(Integer.valueOf(o2.getBeginMillisecond()));
			}
		});
		for (Mark mark : markList) {
			CustomMenuItem menuItem = new CustomMenuItem();
			menuItem.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {

					selectMark(mark);
				}
			});

			BorderPane borderPane = new BorderPane();
			borderPane.setPrefWidth(300);
			borderPane.setLeft(new Label(mark.getName()));
			if (mark.getId() != SoundLooperObject.ID_NOT_INITIALIZED) {
				Button deleteButton = new Button();
				deleteButton.getStyleClass().add("deleteButton");
				deleteButton.getStyleClass().add("toolbar-button");

				deleteButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						try {
							SoundLooperPlayer.getInstance().deleteMarkOnCurrentSong(String.valueOf(mark.getId()));
						} catch (SoundLooperException e) {
							MessagingUtil.displayError("Impossible de supprimer le marqueur '" + mark.getName() + "'",
									e);
						}
					}
				});

				borderPane.setRight(deleteButton);
			}
			menuItem.setContent(borderPane);
			markMenuButton.getItems().add(menuItem);
		}
	}

	private void updateFavoriteList() {
		favoriteMenuButton.getItems().removeAll(favoriteMenuButton.getItems());

		// Create the tag menu tree
		Tag root = Tag.getRoot();
		Map<Long, Menu> mapMenuByIdTag = new HashMap<>();
		List<Tag> listChildrenCopy = root.getListChildrenCopy();
		for (Tag tag : listChildrenCopy) {
			Menu menu = new Menu(tag.getName());
			mapMenuByIdTag.put(tag.getId(), menu);
			favoriteMenuButton.getItems().add(menu);
			List<Tag> listChildrenCopy2 = tag.getListChildrenCopy();
			for (Tag children : listChildrenCopy2) {
				addTagUnderMenu(children, menu, mapMenuByIdTag);
			}
		}

		// Add the songs
		ObservableList<Song> favoriteList = SongService.getInstance().getSortedByNameFavoriteSongList();

		for (Song song : favoriteList) {
			List<Tag> tagList = song.getTagList();
			for (Tag tag : tagList) {
				MenuItem menuItem = new MenuItem(song.getFile().getName());
				menuItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						openFile(song.getFile());
					}
				});
				if (tag.getId() == Tag.ROOT_TAG_ID) {
					// the song has the root tag, so add it to the root
					favoriteMenuButton.getItems().add(menuItem);
				} else {
					// add it under the tag menu
					mapMenuByIdTag.get(tag.getId()).getItems().add(menuItem);
				}
			}
		}
	}

	private void addTagUnderMenu(Tag tag, Menu parentMenu, Map<Long, Menu> mapMenuByIdTag) {
		Menu menu = new Menu(tag.getName());
		mapMenuByIdTag.put(tag.getId(), menu);
		parentMenu.getItems().add(menu);
		List<Tag> listChildren = tag.getListChildrenCopy();
		for (Tag children : listChildren) {
			addTagUnderMenu(children, menu, mapMenuByIdTag);
		}
	}

	@FXML
	public void setBeginAlignment() {
		if (SoundLooperPlayer.getInstance().isSoundInitialized()) {
			int milliSecondsTime = SoundLooperPlayer.getInstance().getMediaTime();
			try {
				SoundLooperPlayer.getInstance().setLoopPointBegin(milliSecondsTime);
			} catch (PlayerException e) {
				MessagingUtil.displayError("Impossible de modifier la position de début", e);
			}
		}
	}

	@FXML
	public void setEndAlignment() {
		if (SoundLooperPlayer.getInstance().isSoundInitialized()) {
			int endTime = SoundLooperPlayer.getInstance().getMediaTime();
			try {
				SoundLooperPlayer.getInstance().setLoopPointEnd(endTime);
			} catch (PlayerException e) {
				MessagingUtil.displayError("Impossible de modifier la position de fin", e);
			}
		}
	}

	@FXML
	public void openAddMarkDialog() {
		try {
			Song song = SoundLooperPlayer.getInstance().getSong();
			if (song == null || !song.isFavorite()) {
				return;
			}

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/AddMarkDialog.fxml"));
			loader.load();
			Parent root = loader.getRoot();
			AddMarkController controller = (AddMarkController) loader.getController();

			Stage modalDialog = new Stage(StageStyle.UTILITY);
			modalDialog.initModality(Modality.APPLICATION_MODAL);
			modalDialog.initOwner(SoundLooper.getInstance().getPrimaryStage());
			modalDialog.setTitle(MessageReader.getInstance().getMessage("window.addMark.title"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add("/style/application.css");
			modalDialog.setScene(scene);
			controller.init(modalDialog);
			modalDialog.showAndWait();

			String result = controller.getResult();
			if (result != null) {
				try {
					SoundLooperPlayer.getInstance().createNewMarkAtCurrentPosition(result);
				} catch (SoundLooperException e1) {
					MessagingUtil.displayError("Impossible de créer le nouveau marqueur", e1);
				}
			}
		} catch (IOException e) {
			throw new SoundLooperRuntimeException("Unable to open dialog", e);
		}
	}

	PlayerView getPlayerView() {

		return playerView;
	}

	@FXML
	public void saveCurrentMark() {
		try {
			SoundLooperPlayer.getInstance().saveCurrentMark();

			// The begin or end of a mark was changed, we need to refresh list
			// to update
			// list entries time
			updateMarkList();
		} catch (SoundLooperException e) {
			MessagingUtil.displayError("Impossible de sauvegarder le marqueur", e);
		}
	}

	public Label getLabelState() {
		return labelState;
	}

	@FXML
	public void openAboutDialog() {
		logger.info("Open the about dialog");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/aboutFrame.fxml"));
		loader.setResources(MessageReader.getInstance().getBundle());

		try {
			loader.load();
			AboutController controller = loader.<AboutController> getController();

			Parent root = loader.getRoot();

			Stage modalDialog = new Stage(StageStyle.UTILITY);
			modalDialog.initOwner(SoundLooper.getInstance().getPrimaryStage());
			modalDialog.setTitle(MessageReader.getInstance().getMessage("menu.about"));
			modalDialog.setResizable(false);

			Scene scene = new Scene(root);
			scene.getStylesheets().add("/style/application.css");

			modalDialog.setScene(scene);

			controller.init(modalDialog);

			modalDialog.showAndWait();
		} catch (IOException e) {
			throw new SoundLooperRuntimeException("Unable to open about dialog", e);
		}
	}

	@FXML
	public void openHelpDialog() {
		openHelpDialog("help/help.md");
	}

	@FXML
	public void openShortcutDialog() {
		openHelpDialog("help/shortcut.md");
	}

	private void openHelpDialog(String fileName) {
		logger.info("Open the help dialog");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/helpFrame.fxml"));
		loader.setResources(MessageReader.getInstance().getBundle());

		try {
			loader.load();
			HelpController controller = loader.<HelpController> getController();
			controller.loadContent(fileName);

			Parent root = loader.getRoot();

			Stage modalDialog = new Stage(StageStyle.UTILITY);
			modalDialog.initOwner(SoundLooper.getInstance().getPrimaryStage());
			modalDialog.setTitle(MessageReader.getInstance().getMessage("menu.help"));
			modalDialog.setResizable(false);

			Scene scene = new Scene(root);
			scene.getStylesheets().add("/style/application.css");

			modalDialog.setScene(scene);

			controller.init(modalDialog);
			modalDialog.showAndWait();
		} catch (IOException e) {
			throw new SoundLooperRuntimeException("Unable to open help dialog", e);
		}
	}

}
