package com.soundlooper.system;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.github.GHIssue;

import com.soundlooper.exception.AlreadyLockedException;
import com.soundlooper.exception.PlayerException;
import com.soundlooper.exception.SoundLooperRuntimeException;
import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.model.database.ConnectionFactory;
import com.soundlooper.system.preferences.Preferences;
import com.soundlooper.system.util.IssueSender;
import com.soundlooper.system.util.Lock;
import com.soundlooper.system.util.MessagingUtil;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SoundLooper extends Application {

	private static final String LOCK_NAME = ".lock";
	private static Stage primaryStage;
	private static SoundLooper instance;
	private SystemController controller;

	private static Logger logger = LogManager.getLogger(SoundLooper.class);

	@Override
	public void start(Stage primaryStage) throws Exception {

		logger.info("----- Application start -------");
		Thread.setDefaultUncaughtExceptionHandler(SoundLooper::onException);

		primaryStage.getIcons().add(ImageGetter.getSoundLooper64().getImage());

		primaryStage.setMinWidth(750);
		primaryStage.setMinHeight(335);

		instance = this;
		SoundLooper.primaryStage = primaryStage;
		SoundLooper.primaryStage.setTitle("Sound Looper");

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
		logger.error("An uncauch exception is detected on the thread " + t.getName() + " : " + e, e);
		Alert alert = new Alert(AlertType.ERROR, "Souhaitez-vous envoyer un rapport d'erreur ?", ButtonType.OK,
				ButtonType.CANCEL);
		alert.initOwner(primaryStage);
		alert.setTitle("Erreur critique");
		alert.setHeaderText("Une erreur inattendue est survenue");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			logger.info("User want to create a github issue");
			GHIssue issue = IssueSender.sendIssue(e);
			logger.info("Issue " + issue.getNumber() + " created");
			Alert issueConfirmationAlert = new Alert(AlertType.INFORMATION,
					"Le ticket de bug numéro '" + issue.getNumber() + "' a bien été créé");
			issueConfirmationAlert.setTitle("Création du rapport d'erreur");
			issueConfirmationAlert.setHeaderText("Le rapport d'erreur a été envoyé avec succès");
			issueConfirmationAlert.initOwner(primaryStage);
			issueConfirmationAlert.showAndWait();
		} else {
			logger.info("User doesn't want to create a github issue");
		}
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
			Pane rootLayout = (Pane) loader.load();

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
