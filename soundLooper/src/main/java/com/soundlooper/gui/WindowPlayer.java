package com.soundlooper.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import com.aned.audio.player.Player;
import com.aned.audio.player.Player.PlayerState;
import com.aned.audio.player.PlayerMessagesListener;
import com.aned.exception.PlayerException;
import com.aned.exception.PlayerRuntimeException;
import com.soundlooper.aide.AideFileGetter;
import com.soundlooper.exception.SoundLooperExceptionHandler;
import com.soundlooper.gui.action.favorite.OpenWindowFavoriteAction;
import com.soundlooper.gui.action.favorite.SearchFavoriteAction;
import com.soundlooper.gui.action.favorite.SwitchFavoriteAction;
import com.soundlooper.gui.action.mark.AddMarkAction;
import com.soundlooper.gui.action.mark.SearchMarkAction;
import com.soundlooper.gui.action.player.OpenFileAction;
import com.soundlooper.gui.action.player.OpenFileFromDialogAction;
import com.soundlooper.gui.action.player.PlayPauseAction;
import com.soundlooper.gui.action.player.SetBeginAlignmentOnCurrentPositionAction;
import com.soundlooper.gui.action.player.SetEndAlignmentOnCurrentPositionAction;
import com.soundlooper.gui.action.player.SetPlayPositionOnLoopBeginAction;
import com.soundlooper.gui.action.timestretch.Add10PercentToTimeStrechAction;
import com.soundlooper.gui.action.timestretch.Add1PercentToTimeStrechAction;
import com.soundlooper.gui.action.timestretch.Remove10PercentToTimeStrechAction;
import com.soundlooper.gui.action.timestretch.Remove1PercentToTimeStrechAction;
import com.soundlooper.gui.action.volume.Add5PercentToVolumeAction;
import com.soundlooper.gui.action.volume.Remove5PercentToVolumeAction;
import com.soundlooper.gui.fenapropos.FenAPropos;
import com.soundlooper.gui.fenapropos.InformationLogiciel;
import com.soundlooper.gui.jplayer.JPlayerListener;
import com.soundlooper.gui.jtimefield.JTimeField;
import com.soundlooper.gui.jtimefield.JTimeFieldLeft;
import com.soundlooper.gui.jtimefield.JTimeFieldListener;
import com.soundlooper.gui.jtimefield.JTimeFieldRight;
import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.model.SoundLooperPlayerListener;
import com.soundlooper.model.SoundLooperPlayerSupport;
import com.soundlooper.model.mark.Mark;
import com.soundlooper.model.song.Song;
import com.soundlooper.service.entite.mark.MarkListener;
import com.soundlooper.service.entite.mark.MarkSupport;
import com.soundlooper.service.entite.song.SongListener;
import com.soundlooper.service.entite.song.SongSupport;
import com.soundlooper.system.preferences.Preferences;
import com.soundlooper.system.preferences.PreferencesListener;
import com.soundlooper.system.preferences.SoundLooperProperties;
import com.soundlooper.system.preferences.recentfile.RecentFile;
import com.soundlooper.system.util.Lock;
import com.soundlooper.system.util.TimeMeasurer;

/*
// TODO multilangagiser
 * //////////////////VERSION 3.0///////////////////////////////////////////
 * Pouvoir attacher une tablature
 * 		- PDF
 * 		- TXT
 * 		- DOC
 * Pouvoir placer des clés dans la chansons pour faire correspondre une partie de la tablature au bon moment de la chanson
 * 		- permettre d'activer/désactiver le défilement automatique, et enregistrer cette information en fonction de la chanson
/////////////////////VERSION 1.0//////////////////////////////////////
 * // TODO Mettre en conf
// TODO Creer DTD + validation des fichiers release note
 */

/**
 * -------------------------------------------------------
 * Sound Looper is an audio player that allow user to loop between two points
 * Copyright (C) 2014 Alexandre NEDJARI
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Window of the soundLooper
 *
 * @author Alexandre NEDJARI
 * @since 6 avr. 2011
 * -------------------------------------------------------
 */
public class WindowPlayer extends JFrame implements SongListener,MarkListener, PlayerMessagesListener, PreferencesListener, SoundLooperPlayerListener, JPlayerListener, JTimeFieldListener {

	/**
	 * The lock name
	 */
	private static final String LOCK_NAME = "SoundLooper_mono_instance";

	/**
	 * Serial number for this class
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The panel for the player controls (play, pause)
	 */
	protected PanelPlayerControls panelPlayerControl = null;

	/**
	 * The panel for the others controls (open, volume)
	 */
	protected PanelOtherControl panelOtherControl = null;

	/**
	 * The panel for the sliders
	 */
	protected PanelSliders panelSliders = null;

	/**
	 * The toolbar panel
	 */
	PanelToolbar panelToolbar = null;

	/**
	 * The label for state
	 */
	protected JLabel labelState = new JLabel("");

	/**
	 * Logger for this class
	 */
	protected static Logger logger = Logger.getLogger(WindowPlayer.class);

	/**
	 * time measurer for this class
	 */
	static TimeMeasurer timeMeasurer = new TimeMeasurer(Logger.getLogger(WindowPlayer.class));

	/**
	 * Barre de menu
	 */
	MenuBar menuBar = new MenuBar();

	/**
	 * Menu fichier de la barre de menu
	 */
	Menu menuFichier = new Menu("Fichier");

	/**
	 * Menu recherche de marqueur
	 */
	MenuItem menuItemRechercheMark;

	/**
	 * Menu des fichiers récents
	 */
	Menu menuRecent = new Menu("Fichiers récents");
	
	public static final int MINIMUM_MS_BETWEEN_CURSOR=100;

	/**
	 * Constructor
	 */
	public WindowPlayer() {
		WindowPlayer.timeMeasurer.startMeasure("Window creation");

		SoundLooperPlayerSupport.getInstance().addToListSoundLooperPlayerListener(this);
		SongSupport.getInstance().addToListSongListener(this);
		MarkSupport.getInstance().addToListMarkListener(this);

		((JComponent) this.getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0),
				SetPlayPositionOnLoopBeginAction.class.getName());
		((JComponent) this.getContentPane()).getActionMap().put(SetPlayPositionOnLoopBeginAction.class.getName(), new SetPlayPositionOnLoopBeginAction());

		((JComponent) this.getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), PlayPauseAction.class.getName());
		((JComponent) this.getContentPane()).getActionMap().put(PlayPauseAction.class.getName(), new PlayPauseAction());

		((JComponent) this.getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD1, InputEvent.CTRL_DOWN_MASK),
				SetBeginAlignmentOnCurrentPositionAction.class.getName());
		((JComponent) this.getContentPane()).getActionMap().put(SetBeginAlignmentOnCurrentPositionAction.class.getName(), new SetBeginAlignmentOnCurrentPositionAction());

		((JComponent) this.getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2, InputEvent.CTRL_DOWN_MASK),
				SetEndAlignmentOnCurrentPositionAction.class.getName());
		((JComponent) this.getContentPane()).getActionMap().put(SetEndAlignmentOnCurrentPositionAction.class.getName(), new SetEndAlignmentOnCurrentPositionAction());

		((JComponent) this.getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0),
				Add5PercentToVolumeAction.class.getName());
		((JComponent) this.getContentPane()).getActionMap().put(Add5PercentToVolumeAction.class.getName(), new Add5PercentToVolumeAction());

		((JComponent) this.getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0),
				Remove5PercentToVolumeAction.class.getName());
		((JComponent) this.getContentPane()).getActionMap().put(Remove5PercentToVolumeAction.class.getName(), new Remove5PercentToVolumeAction());

		((JComponent) this.getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_DOWN_MASK),
				Add1PercentToTimeStrechAction.class.getName());
		((JComponent) this.getContentPane()).getActionMap().put(Add1PercentToTimeStrechAction.class.getName(), new Add1PercentToTimeStrechAction());

		((JComponent) this.getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK), Add10PercentToTimeStrechAction.class.getName());
		((JComponent) this.getContentPane()).getActionMap().put(Add10PercentToTimeStrechAction.class.getName(), new Add10PercentToTimeStrechAction());

		((JComponent) this.getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_DOWN_MASK),
				Remove1PercentToTimeStrechAction.class.getName());
		((JComponent) this.getContentPane()).getActionMap().put(Remove1PercentToTimeStrechAction.class.getName(), new Remove1PercentToTimeStrechAction());

		((JComponent) this.getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK), Remove10PercentToTimeStrechAction.class.getName());
		((JComponent) this.getContentPane()).getActionMap().put(Remove10PercentToTimeStrechAction.class.getName(), new Remove10PercentToTimeStrechAction());

		((JComponent) this.getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), SwitchFavoriteAction.class.getName());
		((JComponent) this.getContentPane()).getActionMap().put(SwitchFavoriteAction.class.getName(), new SwitchFavoriteAction());

		((JComponent) this.getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0), AddMarkAction.class.getName());
		((JComponent) this.getContentPane()).getActionMap().put(AddMarkAction.class.getName(), new AddMarkAction(this));

		WindowPlayer.logger.info("Launching " + SoundLooperProperties.getInstance().getApplicationPresentation());
		this.setPreferredSize(new Dimension(700, 330));

		WindowPlayer.timeMeasurer.endAndStartNewMeasure("Set uncaught exception");
		Thread.setDefaultUncaughtExceptionHandler(new SoundLooperExceptionHandler("SoundLooper", this));

		SoundLooperPlayer.getInstance().getMessageNotifier().addMessageListener(this);

		WindowPlayer.logger.info("Windows creation");
		WindowPlayer.timeMeasurer.endAndStartNewMeasure("Start creation");

		WindowPlayer.timeMeasurer.endAndStartNewMeasure("creation Panel lecteur");
		JPanel panelLecteur = new JPanel();
		panelLecteur.setOpaque(false);
		panelLecteur.setLayout(new BorderLayout());
		JPanel panelSud = new JPanel();
		panelSud.setOpaque(false);
		panelLecteur.add(panelSud, BorderLayout.SOUTH);
		panelSud.setLayout(new BorderLayout());

		this.getContentPane().setLayout(new BorderLayout(0, 8));
		this.setMinimumSize(new Dimension(630, 330));

		WindowPlayer.timeMeasurer.endAndStartNewMeasure("creation Panel player control");
		this.panelPlayerControl = new PanelPlayerControls();
		panelSud.add(this.panelPlayerControl, BorderLayout.WEST);

		WindowPlayer.timeMeasurer.endAndStartNewMeasure("creation Panel slider");
		panelLecteur.add(this.getPanelSliders(), BorderLayout.CENTER);

		WindowPlayer.timeMeasurer.endAndStartNewMeasure("creation Panel other control");
		this.panelOtherControl = new PanelOtherControl(this);
		//panelSud.add(this.panelOtherControl, BorderLayout.EAST);

		WindowPlayer.timeMeasurer.endAndStartNewMeasure("creation Panel toolbar");
		this.panelToolbar = new PanelToolbar(this);

		JPanel panelNord = new JPanel();
		panelNord.setLayout(new BorderLayout());
		panelNord.add(this.panelToolbar, BorderLayout.CENTER);
		panelNord.add(this.panelOtherControl, BorderLayout.EAST);
		//this.panelOtherControl.setOpaque(false);
		panelNord.setOpaque(false);

		this.getContentPane().add(panelNord, BorderLayout.NORTH);

		this.getContentPane().add(panelLecteur, BorderLayout.CENTER);
		this.labelState.setPreferredSize(new Dimension(0, 20));
		this.getContentPane().add(this.labelState, BorderLayout.SOUTH);

		((JPanel) this.getContentPane()).setBorder(new EmptyBorder(6, 2, 2, 2));
		WindowPlayer.timeMeasurer.endAndStartNewMeasure("Pack de l'interface");
		this.pack();
		this.getContentPane().setBackground(new Color(249, 248, 208));
		this.setLocationRelativeTo(null);
		this.setCompleteTitle("");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIconImage(ImageGetter.getImage(ImageGetter.ICONE_SOUND_LOOPER_64));

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				WindowPlayer.this.onWindowClose();
				super.windowClosing(e);
			}
		});

		WindowPlayer.timeMeasurer.endAndStartNewMeasure("creation menu");
		this.setMenu();

		WindowPlayer.timeMeasurer.endAndStartNewMeasure("initialisation player");
		SoundLooperPlayer.getInstance().initialize();

		WindowPlayer.timeMeasurer.endAndStartNewMeasure("Preference init");
		SwingWorker<Object, Object> preferenceInitWorker = new SwingWorker<Object, Object>() {
			@Override
			protected Object doInBackground() {
				WindowPlayer.logger.info("Début d'initialisation des préférences");
				Preferences.getInstance().addListener(WindowPlayer.this);
				WindowPlayer.this.setAlwaysOnTop(Preferences.getInstance().isAlwaisOnTop());

				File lastFile = new File(Preferences.getInstance().getLastPathUsed());
				if (lastFile.exists()) {
					SoundLooperPlayer.getInstance().loadSong(lastFile);
				}

				try {
					SoundLooperPlayer.getInstance().setVolume(Preferences.getInstance().getLastVolumeUsed());
				} catch (PlayerException e) {
					WindowPlayer.this.onError(e.getMessage());
				}
				WindowPlayer.logger.info("Fin d'initialisation des préférences");
				return null;
			}
		};
		preferenceInitWorker.execute();
		WindowPlayer.timeMeasurer.endLocalMeasure();
	}

	/**
	 * Traitement à effectuer à la fermeture de la fenêtre
	 */
	public void onWindowClose() {
		WindowPlayer.this.setVisible(false);
		if (SoundLooperPlayer.getInstance().isSystemInitialized()) {
			SoundLooperPlayer.getInstance().stop();
			SoundLooperPlayer.getInstance().desallocate();
		}
		Preferences.getInstance().save();

		SoundLooperPlayer.getInstance().purgeSong();

		try {
			Lock.unlock(WindowPlayer.LOCK_NAME);
		} catch (IOException e1) {
			WindowPlayer.logger.warn("Unable to remove lock '" + WindowPlayer.LOCK_NAME + "'", e1);
		}
	}

	/**
	 * Create the window menu
	 */
	private void setMenu() {

		MenuItem menuItemOuvrir = new MenuItem("Ouvrir");
		menuItemOuvrir.addActionListener(new OpenFileFromDialogAction(this));
		menuItemOuvrir.setShortcut(new MenuShortcut(KeyEvent.VK_O));
		this.menuFichier.add(menuItemOuvrir);

		this.populateMenuRecent();
		this.menuFichier.add(this.menuRecent);

		this.menuBar.add(this.menuFichier);

		MenuItem menuItemQuitter = new MenuItem("Quitter");
		menuItemQuitter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				WindowPlayer.this.onWindowClose();
				WindowPlayer.this.dispose();
				System.exit(0);
			}
		});
		this.menuFichier.add(menuItemQuitter);

		Menu menuEdition = new Menu("Edition");
		MenuItem menuEditionGererFavoris = new MenuItem("Gérer les favoris...");
		menuEditionGererFavoris.addActionListener(new OpenWindowFavoriteAction(this));
		menuEdition.add(menuEditionGererFavoris);
		this.menuBar.add(menuEdition);
		
		
		Menu menuRecherche = new Menu("Recherches");
		MenuItem menuItemRechercheFavoris = new MenuItem("Rechercher dans les favoris...");
		menuItemRechercheFavoris.setShortcut(new MenuShortcut(KeyEvent.VK_F));
		menuItemRechercheFavoris.addActionListener(new SearchFavoriteAction(this));
		menuRecherche.add(menuItemRechercheFavoris);

		this.menuItemRechercheMark = new MenuItem("Rechercher dans les marqueurs...");
		this.menuItemRechercheMark.setShortcut(new MenuShortcut(KeyEvent.VK_M));
		this.menuItemRechercheMark.addActionListener(new SearchMarkAction(this));
		menuRecherche.add(this.menuItemRechercheMark);
		
		
		
		
		this.menuBar.add(menuRecherche);

		Menu menuAide = new Menu("?");

		MenuItem menuAideGeneral = new MenuItem("Aide");
		menuAideGeneral.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new FenetreAide(WindowPlayer.this, AideFileGetter.getHelpFile(AideFileGetter.HELP_FILE_HELP), "Aide").setVisible(true);
			}
		});
		menuAide.add(menuAideGeneral);

		MenuItem menuAideRaccourcisClavier = new MenuItem("Aide : raccourcis clavier...");
		menuAideRaccourcisClavier.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new FenetreAide(WindowPlayer.this, AideFileGetter.getHelpFile(AideFileGetter.HELP_FILE_SHORTCUT_LIST), "Liste des raccourcis claviers disponibles").setVisible(true);
			}
		});
		menuAide.add(menuAideRaccourcisClavier);

		MenuItem menuItemAPropos = new MenuItem("A propos de...");
		menuItemAPropos.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				InformationLogiciel informationLogiciel = new InformationLogiciel("Alexandre NEDJARI", "Soundlooper", SoundLooperProperties.getInstance().getCompleteVersion(),
						SoundLooperProperties.getInstance().getIteration());
				new FenAPropos(informationLogiciel, WindowPlayer.this, ImageGetter.getImage(ImageGetter.IMAGE_A_PROPOS_164_314)).setVisible(true);
			}
		});
		menuAide.add(menuItemAPropos);
		this.menuBar.add(menuAide);
		this.setMenuBar(this.menuBar);
	}

	/**
	 * Renseigne le menu des fichiers récents
	 */
	private void populateMenuRecent() {
		while (this.menuRecent.getItemCount() > 0) {
			this.menuRecent.remove(0);
		}
		List<RecentFile> recentFileList = Preferences.getInstance().getRecentFileList();
		for (final RecentFile recentFile : recentFileList) {
			if (recentFile.getFile().exists()) {
				MenuItem menuItemRecentFile = new MenuItem(recentFile.getFile().getName());
				menuItemRecentFile.addActionListener(new OpenFileAction());
				menuItemRecentFile.setActionCommand(recentFile.getFile().getAbsolutePath());
				this.menuRecent.insert(menuItemRecentFile, 0);
			}
		}
	}

	/**
	 * Get the sliders panel
	 * @return the slider panel
	 */
	public PanelSliders getPanelSliders() {
		if (this.panelSliders == null) {
			this.panelSliders = new PanelSliders(this);
		}
		return this.panelSliders;
	}

	/**
	 * Set the complete title of the window
	 * @param selectedFile the selected file name or "" if there is no file selected
	 */
	protected void setCompleteTitle(String selectedFile) {
		this.setTitle(SoundLooperProperties.getInstance().getApplicationPresentation());
		if (selectedFile != null && selectedFile.length() > 0) {
			this.setTitle(this.getTitle() + " : " + selectedFile + " (" + SoundLooperPlayer.getInstance().getCurrentMark().getName() + ")");
		} else {
			this.setTitle(this.getTitle() + " (Pas de fichier choisi)");
		}
	}

	/**
	 * Update the interface
	 * @param state the player state
	 */
	private void updateInterface(final int state) {
		WindowPlayer.logger.info("Mise à jour de l'interface : " + SoundLooperPlayer.getInstance().getStateLabel(state));
		SwingUtilities.invokeLater(new Runnable() {
			Boolean lock = Boolean.TRUE;

			@Override
			public void run() {
				synchronized (this.lock) {
					switch (state) {
					case PlayerState.STATE_PLAYER_UNINITIALIZED:
					case PlayerState.STATE_PLAYER_INITIALIZING:
					case PlayerState.STATE_PLAYER_INITIALIZED:
					case PlayerState.STATE_LOADING_SONG:
					case PlayerState.STATE_UNLOAD_SONG:
					case PlayerState.STATE_UNLOAD_PLAYER:
						WindowPlayer.this.setStateUninitialized();
						break;
					case PlayerState.STATE_PREPARING_STOP:
					case PlayerState.STATE_STOPPED:
						WindowPlayer.this.setStateStopped();
						break;
					case PlayerState.STATE_PREPARING_PAUSE:
					case PlayerState.STATE_PAUSED:
						WindowPlayer.this.setStatePaused();
						break;
					case PlayerState.STATE_PREPARING_PLAY:
					case PlayerState.STATE_PLAYING:
						WindowPlayer.this.setStatePlaying();
						break;
					default:
						System.err.println("NO INTERFACE STATE FOR : " + state);
						break;
					}
					WindowPlayer.logger.info("Current state is now : " + SoundLooperPlayer.getInstance().getStateLabel(state));

				}
			}
		});
	}

	/**
	 * Set the interface paused state
	 */
	protected void setStatePaused() {

		WindowPlayer.this.panelPlayerControl.setStatePaused();
		WindowPlayer.this.panelSliders.setStatePaused();
		WindowPlayer.this.panelToolbar.setStatePaused();

	}

	/**
	 * set the interface state for uninitialized player
	 */
	protected void setStateUninitialized() {

		WindowPlayer.this.panelPlayerControl.setStateUninitialized();
		WindowPlayer.this.panelSliders.setStateUninitialized();
		WindowPlayer.this.panelToolbar.setStateUninitialized();
		System.out.println("set complete title vide");
		WindowPlayer.this.setCompleteTitle("");

	}

	/**
	 * Set the stopped state
	 */
	protected void setStateStopped() {

		WindowPlayer.this.panelPlayerControl.setStateStopped();
		WindowPlayer.this.panelSliders.setStateStopped();
		WindowPlayer.this.panelToolbar.setStateStopped();

	}

	/**
	 * Set the playing state
	 */
	protected void setStatePlaying() {

		WindowPlayer.this.panelPlayerControl.setStatePlaying();
		WindowPlayer.this.panelSliders.setStatePlaying();
		WindowPlayer.this.panelToolbar.setStatePlaying();

	}

	@Override
	public void onChangeState(int newState) {
		this.updateInterface(newState);
	}

	@Override
	public void onInfo(String message) {
		this.displayeInfoInStateBar(message);
	}

	@Override
	public void onFatalError(String errorMessage) {
		JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
		System.exit(0);

	}

	@Override
	public void onError(String errorMessage) {
		this.displayeErrorInStateBar(errorMessage);
	}

	@Override
	public void onWarning(String message) {
		this.displayeErrorInStateBar("ATTENTION : " + message);
	}

	/**
	 * Display an error in the state bar
	 * @param message the message to display
	 */
	private void displayeErrorInStateBar(String message) {
		this.labelState.setForeground(Color.RED);
		this.labelState.setText(message);
	}

	/**
	 * Display an info in the state bar for few seconds
	 * @param message the message to display
	 */
	private void displayeInfoInStateBar(String message) {
		this.labelState.setForeground(Color.BLUE);
		this.labelState.setText(message);
		new ThreadCleanLabelState(this.labelState).start();
	}

	@Override
	public void onPreferenceChange(String preferenceKey, Object value) {
		if (preferenceKey.equals(Preferences.KEY_ALWAYS_ON_TOP)) {
			boolean nouvelleValeurAlwaysOnTop = ((Boolean) value).booleanValue();
			this.setAlwaysOnTop(nouvelleValeurAlwaysOnTop);
			WindowPlayer.logger.info("Change le paramètre 'AlwaysOnTop' de la fenêtre : " + nouvelleValeurAlwaysOnTop);
		} else if (preferenceKey.equals(Preferences.KEY_RECENT_FILE_LIST)) {
			this.populateMenuRecent();
		}
	}

	/**
	 *
	 * @return the menu to search marks
	 */
	public MenuItem getMenuItemRechercheMark() {
		return this.menuItemRechercheMark;
	}

	@Override
	public void onPlayLocationChanged(int newMillisecondLocation) {
		this.panelSliders.setPlayCursorPosition(newMillisecondLocation);
	}

	@Override
	public void onLoopPointChanged(int beginPoint, int endPoint) {
		this.panelSliders.setLoopPointsPositions(beginPoint, endPoint);
		this.panelSliders.getTimeFieldLeft().setTime(new Double(beginPoint).intValue());
		this.panelSliders.getTimeFieldRight().setTime(new Double(endPoint).intValue());

	}

	@Override
	public void onVolumeUpdate(int percent) {
		this.panelOtherControl.setVolumePosition(percent);
		Preferences.getInstance().setLastVolumeUsed(percent);
	}

	@Override
	public void onTimestretchUpdated(int percent) {
		this.panelOtherControl.setTimeStretchPosition(percent);

	}

	@Override
	public void onSongLoaded(final Song song) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					WindowPlayer.this.updateFavoriteGUI(song);
					WindowPlayer.this.panelSliders.initializedSlidersFromSong();
					System.out.println("set complete title loaded : " + song.getFile().getName());
					WindowPlayer.this.setCompleteTitle(song.getFile().getName());
					WindowPlayer.this.setStatePaused();
					SoundLooperPlayer.getInstance().generateImage();
				} catch (PlayerException e) {
					WindowPlayer.logger.error(e.getMessage());
					WindowPlayer.this.onError(e.getMessage());
				}
			}
		});
	}

	@Override
	public void onFavoriteUpdated(Song song) {
		this.updateFavoriteGUI(song);
	}

	/**
	 * @param song the song
	 */
	protected void updateFavoriteGUI(Song song) {
		if (!song.equals(SoundLooperPlayer.getInstance().getSong())) {
			//The updated song is not the current song, so there is no update to do
			return;
		}
		final boolean isFavorite = song.isFavorite();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (isFavorite) {
					//TODO créer une classe bouton switch plutot
					WindowPlayer.this.panelToolbar.getBoutonFavori().setIcon(ImageGetter.getImageIcon(ImageGetter.ICONE_FAVORI_SELECTIONNE_32));
					WindowPlayer.this.panelToolbar.getBoutonFavori().setRolloverIcon(ImageGetter.getImageIcon(ImageGetter.ICONE_FAVORI_SELECTIONNE_OVER_32));
				} else {
					WindowPlayer.this.panelToolbar.getBoutonFavori().setIcon(ImageGetter.getImageIcon(ImageGetter.ICONE_FAVORI_DESELECTIONNE_32));
					WindowPlayer.this.panelToolbar.getBoutonFavori().setRolloverIcon(ImageGetter.getImageIcon(ImageGetter.ICONE_FAVORI_DESELECTIONNE_OVER_32));
				}
				WindowPlayer.this.panelToolbar.updateMarkButtonsState(isFavorite);
			}
		});
	}

	@Override
	public void onMarkDeleted(Song song, Mark mark, final long idMarkSupprime) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				//WindowPlayer.this.panelToolbar.getMarkMenu().setVisible(false);
				WindowPlayer.this.panelToolbar.updateMarkListAfterDelete(idMarkSupprime);
			}
		});
	}

	@Override
	public void onFatalError(PlayerRuntimeException e) {
		JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur fatale", JOptionPane.ERROR_MESSAGE);
		WindowPlayer.this.onWindowClose();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				WindowPlayer.this.dispose();
				System.exit(0);
			}
		});
	}

	@Override
	public void onBeginGenerateImage() {
		this.getPanelSliders().startGenerateImage();

	}

	@Override
	public void onEndGenerateImage(BufferedImage image) {
		this.getPanelSliders().setGeneratedImage(image);

	}

	@Override
	public void onNewValeur(double valeur) {
		SoundLooperPlayer.getInstance().setMediaTime(new Double(valeur).intValue());
	}

	@Override
	public void onMarkAdded(Song song, Mark mark) {
		// TODO Auto-generated method stub
		
	}
	
    @Override
    public void onDirtyChanged(Mark mark) {
    	if (mark.isEditable() && mark.getSong() != null && mark.getSong().isFavorite()) {
    		this.panelToolbar.getBoutonSaveMark().setEnabled(mark.isDirty());
    	} else {
    		this.panelToolbar.getBoutonSaveMark().setEnabled(false);
    	}
    }

    @Override
    public void onMarkLoaded(Mark mark) {
        this.panelToolbar.getBoutonSaveMark().setEnabled(false);
        this.setCompleteTitle(mark.getSong().getFile().getName());
    }

	@Override
	public void onNewLoopPoints(double valeurGauche, double valeurDroite) {
		SoundLooperPlayer.getInstance().setLoopPoints(new Double(valeurGauche).intValue(), new Double(valeurDroite).intValue());
		
	}

	@Override
	public void onValueChanged(int newValeur, JTimeField jTimeField) {
		if (jTimeField instanceof JTimeFieldRight) {
			SoundLooperPlayer.getInstance().setLoopPoints(Player.getInstance().getLoopPointBegin(), newValeur);
		} else if (jTimeField instanceof JTimeFieldLeft) {
			SoundLooperPlayer.getInstance().setLoopPoints(newValeur, Player.getInstance().getLoopPointEnd());
		}
		
	}
}
