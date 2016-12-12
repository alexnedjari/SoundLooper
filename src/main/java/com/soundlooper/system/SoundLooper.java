package com.soundlooper.system;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.soundlooper.exception.AlreadyLockedException;
import com.soundlooper.exception.PlayerException;
import com.soundlooper.exception.SoundLooperRuntimeException;
import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.model.database.ConnectionFactory;
import com.soundlooper.system.preferences.Preferences;
import com.soundlooper.system.util.Lock;
import com.soundlooper.system.util.MessagingUtil;

public class SoundLooper extends Application {

	private static final String LOCK_NAME = ".lock";
	private Stage primaryStage;
	private BorderPane rootLayout;
	private static SoundLooper instance;
	private SystemController controller;

	private static Logger logger = LogManager.getLogger(SoundLooper.class);

	@Override
	public void start(Stage primaryStage) throws Exception {

		logger.info("----- Application start -------");
		Thread.setDefaultUncaughtExceptionHandler(SoundLooper::onException);

		primaryStage.getIcons().add(ImageGetter.getSoundLooper64().getImage());

		primaryStage.setMinWidth(750);
		primaryStage.setMinHeight(375);

		instance = this;
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Sound Looper");
		primaryStage.setOnCloseRequest(e -> {
			logger.info("On close request");
			onWindowClose();
			logger.info("After window close");
			Platform.exit();
			logger.info("After platform exit");
			try {
				Lock.unlock(SoundLooper.LOCK_NAME);
			} catch (IOException e1) {
				logger.error("Unable to remove lock '" + SoundLooper.LOCK_NAME + "'", e);
			}
			logger.info("After unlock");

			System.exit(0);
			logger.info("After system exit");
		});

		SoundLooperPlayer.getInstance().initialize();
		SoundLooper.initializePreference();
		SoundLooper.initializeDatabase();

		initRootLayout();
	}

	private static void onException(Thread t, Throwable e) {
		// TODO, open window if is possible
		logger.error("An uncauch exception is detected on the thread " + t.getName() + " : " + e, e);

	}

	public void onWindowClose() {
		primaryStage.hide();
		try {
			SoundLooperPlayer.getInstance().desallocate();
		} catch (PlayerException e) {
			logger.error("Unable to desallocate player", e);
		}
		Preferences.getInstance().save();
		SoundLooperPlayer.getInstance().purgeSong();
	}

	public void initRootLayout() {
		try {
			logger.info("Init the root layout");

			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();

			loader.setResources(MessageReader.getInstance().getBundle());
			loader.setLocation(SoundLooper.class.getResource("/gui/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			controller = (SystemController) loader.getController();
			controller.init();

			File lastFile = new File(Preferences.getInstance().getLastPathUsed());
			if (lastFile.exists()) {

				try {
					SoundLooperPlayer.getInstance().loadSong(lastFile);
					MessagingUtil.displayMessage("Fichier '" + lastFile.getAbsolutePath() + " chargé");
				} catch (PlayerException e) {
					MessagingUtil.displayError("Impossible de charger la chanson " + lastFile, e);
				}
			}

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			scene.getStylesheets().add("/style/application.css");

			// Light light = new Light.Point(, 40, 300, Color.WHITE);
			// Lighting lighting = new Lighting();
			// lighting.setLight(light);
			// scene.getRoot().setEffect(lighting);

			primaryStage.setScene(scene);
			primaryStage.setAlwaysOnTop(Preferences.getInstance().getAlwaysOnTop());
			controller.initShortcut();

			primaryStage.show();
		} catch (IOException e) {
			throw new SoundLooperRuntimeException("Unable to load the root layout", e);
		}
	}

	public static void main(String[] args) throws AlreadyLockedException, IOException {
		logger.info("Launch with arguments : " + StringUtils.join(args));
		if (containsArg(args, "-uninstall")) {
			logger.info("Uninstall, nothing to do");
			return;
		}

		Lock.lock(SoundLooper.LOCK_NAME);
		launch(args);
	}

	private static boolean containsArg(String[] args, String string) {
		for (String arg : args) {
			if (StringUtils.equals(arg, string)) {
				return true;
			}
		}
		return false;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static SoundLooper getInstance() {
		return instance;
	}

	/**
	 * Initialize the database
	 */
	private static void initializeDatabase() {
		SoundLooper.logger.info("Start database initialisation");
		ConnectionFactory.updateDB();
		ConnectionFactory.getNewStatement();
		SoundLooper.logger.info("End database initialisation");

	}

	/**
	 * Initialize the preference
	 */
	private static void initializePreference() {
		// init the properties
		SoundLooper.logger.info("Start properties initialisation");
		Preferences.getInstance();
		SoundLooper.logger.info("End properties initialisation");

	}

	public SystemController getController() {
		return controller;
	}

}
