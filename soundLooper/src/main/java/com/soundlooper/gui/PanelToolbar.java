/**
 *
 */
package com.soundlooper.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import org.apache.log4j.Logger;

import com.soundlooper.gui.action.favorite.SwitchFavoriteAction;
import com.soundlooper.gui.action.mark.AddMarkAction;
import com.soundlooper.gui.action.mark.DeleteMarkAction;
import com.soundlooper.gui.action.mark.SaveMarkAction;
import com.soundlooper.gui.action.mark.SelectMarkAction;
import com.soundlooper.gui.action.player.OpenFileFromDialogAction;
import com.soundlooper.gui.action.player.SetBeginAlignmentOnCurrentPositionAction;
import com.soundlooper.gui.action.player.SetEndAlignmentOnCurrentPositionAction;
import com.soundlooper.gui.jswitchbutton.JSwitchButton;
import com.soundlooper.gui.jswitchbutton.SwitchButtonActionListener;
import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.model.mark.Mark;
import com.soundlooper.model.song.Song;
import com.soundlooper.system.preferences.Preferences;
import com.soundlooper.system.util.TimeConverter;

/**--------------------------------------------------------------------------------
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
 * Panel like a toolbar
 *
 * @author Alexandre NEDJARI
 * @since 25 juil. 2011
 *--------------------------------------------------------------------------------
 */
public class PanelToolbar extends JPanel {

	/**
	 * The mark menu
	 */
	protected JPopupMenu markMenu = null;

	/**
	 * Serial number for this class
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the begin alignment button
	 */
	private JButton buttonBeginAlignment = null;

	/**
	 * the end alignment button
	 */
	private JButton buttonEndAlignment = null;

	/**
	 * the switch button "Always on top"
	 */
	private JSwitchButton buttonAlwaysOnTop = null;

	/**
	 * The open bouton
	 */
	JButton boutonParcourir = null;

	/**
	 * The fav bouton
	 */
	JButton boutonFavori = null;

	/**
	 * The button to open the all favorites popup
	 */
	JButton boutonPopupFavori = null;

	/**
	 * The fav mark bouton
	 */
	JButton boutonMark = null;
	
    /**
     * The mark save bouton
     */
    JButton boutonSaveMark = null;


	/**
	 * The button to open the all marks popup
	 */
	JButton boutonPopupMark = null;

	/**
	 * The containing windows
	 */
	protected WindowPlayer windowPlayer = null;

	/**
	 * Logger for this class
	 */
	protected Logger logger = Logger.getLogger(this.getClass());

	/**
	 * The panel construction
	 * @param windowPlayer the containing windows
	 */
	public PanelToolbar(WindowPlayer windowPlayer) {
		super();
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(0, 32));
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		this.windowPlayer = windowPlayer;

		this.add(this.getBoutonParcourir());

		this.add(this.getSeparator());
		this.add(this.getButtonBeginAlignment());
		this.add(this.getButtonEndAlignment());

		this.add(this.getSeparator());
		this.add(this.getPanelFavori());
		this.add(this.getPanelMark());

		this.add(this.getSeparator());
		this.add(this.getButtonAlwaysOnTop());

	}

	/**
	 * Get the panel containing the favorites buttons
	 * @return the panel
	 */
	private JPanel getPanelFavori() {
		JPanel panelFavori = new JPanel();
		FlowLayout mgr = new FlowLayout();
		mgr.setHgap(0);
		mgr.setVgap(0);
		panelFavori.setLayout(mgr);
		panelFavori.add(this.getBoutonFavori());
		panelFavori.add(this.getBoutonPopupFavori());
		return panelFavori;
	}

	/**
	 * Get the panel containing the favorites buttons
	 * @return the panel
	 */
	private JPanel getPanelMark() {
		JPanel panelMark = new JPanel();
		FlowLayout mgr = new FlowLayout();
		mgr.setHgap(0);
		mgr.setVgap(0);
		panelMark.setLayout(mgr);
        panelMark.add(this.getBoutonSaveMark());
		panelMark.add(this.getBoutonMark());
		panelMark.add(this.getBoutonPopupMark());
		return panelMark;
	}

	/**
	 * Get a new separator
	 * @return a new separator
	 */
	private JSeparator getSeparator() {
		JSeparator separator = new JSeparator();
		separator.setSize(50, 50);
		separator.setPreferredSize(new Dimension(10, 0));
		separator.setBackground(Color.BLACK);
		separator.setBorder(new LineBorder(Color.BLACK));
		return separator;
	}

	/**
	 * Get the browse button to open a file
	 * @return thr browse button
	 */
	private JButton getBoutonParcourir() {
		if (this.boutonParcourir == null) {
			this.boutonParcourir = SoundLooperGUIHelper.getBouton(new OpenFileFromDialogAction(this.windowPlayer), "ouvrir", "Ouvrir un fichier", true);

		}
		this.boutonParcourir.setFocusable(false);
		this.boutonParcourir.setPreferredSize(new Dimension(32, 32));
		this.boutonParcourir.setBorderPainted(true);
		this.boutonParcourir.setContentAreaFilled(true);
		this.boutonParcourir.setTransferHandler(new FileTransfertHandler());
		return this.boutonParcourir;
	}

	/**
	 * Button to put songs to the favorite
	 * @return the button
	 */
	public JButton getBoutonFavori() {
		if (this.boutonFavori == null) {
			this.boutonFavori = SoundLooperGUIHelper.getBouton(new SwitchFavoriteAction(), "favori_deselectionne", "Ajouter aux favoris", true);
		}
		this.boutonFavori.setFocusable(false);
		this.boutonFavori.setPreferredSize(new Dimension(32, 32));
		this.boutonFavori.setBorderPainted(true);
		this.boutonFavori.setContentAreaFilled(true);
		return this.boutonFavori;
	}

	/**
	 * Button to open the favorite popup
	 * @return the button
	 */
	public JButton getBoutonPopupFavori() {
		if (this.boutonPopupFavori == null) {
			this.boutonPopupFavori = SoundLooperGUIHelper.getBouton("favori_popup", "Lister les favoris", true);
			this.boutonPopupFavori.addActionListener(new ActionListener() {
				JPopupMenu menu = null;

				@Override
				public void actionPerformed(ActionEvent arg0) {

					//if (GererSongService.getInstance().isFavoriteSongListMustBeUpdated()) {
					List<Song> favoriteSongList = SoundLooperPlayer.getInstance().getFavoriteSongList();
					this.createPopup(favoriteSongList);
					//}
					this.menu.show(PanelToolbar.this.getBoutonPopupFavori(), 0, PanelToolbar.this.getBoutonPopupFavori().getHeight());

				}

				private void createPopup(List<Song> favoriteSongList) {
					this.menu = new JPopupMenu();

					for (final Song song : favoriteSongList) {
						JMenuItem menuItem = new JMenuItem(song.getFile().getName());
						menuItem.setToolTipText(song.getFile().getAbsolutePath());
						menuItem.addActionListener(new com.soundlooper.gui.action.player.OpenFileAction());
						menuItem.setActionCommand(song.getFile().getAbsolutePath());
						this.menu.add(menuItem);
					}
				}
			});
		}
		this.boutonPopupFavori.setFocusable(false);
		this.boutonPopupFavori.setPreferredSize(new Dimension(12, 32));
		this.boutonPopupFavori.setBorderPainted(true);
		this.boutonPopupFavori.setContentAreaFilled(true);
		return this.boutonPopupFavori;
	}

	/**
	 * Button to put songs to the favorite
	 * @return the button
	 */
	public JButton getBoutonMark() {
		if (this.boutonMark == null) {
			this.boutonMark = SoundLooperGUIHelper.getBouton(new AddMarkAction(this.windowPlayer), "ajouterMark", "Ajouter l'emplacement des curseurs aux favoris", true);
		}
		this.boutonMark.setFocusable(false);
		this.boutonMark.setPreferredSize(new Dimension(32, 32));
		this.boutonMark.setBorderPainted(true);
		this.boutonMark.setContentAreaFilled(true);
		return this.boutonMark;
	}
	
    /**
     * Button to save changes on a mark
     * @return the button
     */
    public JButton getBoutonSaveMark() {
        if (this.boutonSaveMark == null) {
            this.boutonSaveMark = SoundLooperGUIHelper.getBouton(new SaveMarkAction(this.windowPlayer), "saveMark", "Sauvegarder le marqueur courant", true);
        }
        this.boutonSaveMark.setEnabled(false);
        this.boutonSaveMark.setFocusable(false);
        this.boutonSaveMark.setPreferredSize(new Dimension(32, 32));
        this.boutonSaveMark.setBorderPainted(true);
        this.boutonSaveMark.setContentAreaFilled(true);
        return this.boutonSaveMark;
    }


	/**
	 * Button to open the favorite popup
	 * @return the button
	 */
	public JButton getBoutonPopupMark() {
		if (this.boutonPopupMark == null) {
			this.boutonPopupMark = SoundLooperGUIHelper.getBouton("mark_popup", "Lister les marqueurs", true);
			this.boutonPopupMark.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					createPopupMark();
				}
			});
		}
		this.boutonPopupMark.setFocusable(false);
		this.boutonPopupMark.setPreferredSize(new Dimension(12, 32));
		this.boutonPopupMark.setBorderPainted(true);
		this.boutonPopupMark.setContentAreaFilled(true);
		return this.boutonPopupMark;
	}
	
	protected void createPopupMark() {
		PanelToolbar.this.markMenu = new JPopupMenu();

		List<Mark> listeMark = new ArrayList<Mark>(SoundLooperPlayer.getInstance().getSong().getMarks().values());
		Collections.sort(listeMark, new Comparator<Mark>() {

			@Override
			public int compare(Mark o1, Mark o2) {
				if (o1.getBeginMillisecond() > o2.getBeginMillisecond()) {
					return 1;
				}
				if (o1.getBeginMillisecond() < o2.getBeginMillisecond()) {
					return -1;
				}
				if (o1.getEndMillisecond() > o2.getEndMillisecond()) {
					return 1;
				}
				if (o1.getEndMillisecond() < o2.getEndMillisecond()) {
					return -1;
				}
				return 0;
			}
		});

		for (final Mark mark : listeMark) {
			JMenuItem menuItem = new JMenuItem(new SelectMarkAction(mark));
			menuItem.setLayout(new BorderLayout());
			menuItem.setText(mark.getName() + " (" + TimeConverter.getTimeInformationStringMMSS(mark.getBeginMillisecond()) + " à "
					+ TimeConverter.getTimeInformationStringMMSS(mark.getEndMillisecond()) + ")");
			menuItem.setActionCommand(String.valueOf(mark.getId()));

            JButton boutonSuppression = SoundLooperGUIHelper.getBouton(new DeleteMarkAction(), "supprimerMark",
					"Supprimer ce marqueur", true, 16);
			boutonSuppression.setActionCommand(String.valueOf(mark.getId()));
			menuItem.add(boutonSuppression, BorderLayout.EAST);

			PanelToolbar.this.getMarkMenu().add(menuItem);
		}
		PanelToolbar.this.getMarkMenu().setPreferredSize(
				new Dimension(new Float(PanelToolbar.this.getMarkMenu().getPreferredSize().getWidth() + 20).intValue(), new Float(PanelToolbar.this.getMarkMenu()
						.getPreferredSize().getHeight()).intValue()));
		PanelToolbar.this.getMarkMenu().show(PanelToolbar.this.getBoutonPopupMark(), 0, PanelToolbar.this.getBoutonPopupMark().getHeight());
	}

	/**
	 * @return the menu
	 */
	public JPopupMenu getMarkMenu() {
		return this.markMenu;
	}

	/**
	 * Create the begin alignment button
	 * @return the begin alignment button
	 */
	protected JButton getButtonBeginAlignment() {
		if (this.buttonBeginAlignment == null) {
			this.buttonBeginAlignment = new JButton(new SetBeginAlignmentOnCurrentPositionAction());
			this.buttonBeginAlignment.setFocusable(false);
			this.buttonBeginAlignment.setPreferredSize(new Dimension(32, 32));
			this.buttonBeginAlignment.setBorderPainted(true);
			this.buttonBeginAlignment.setToolTipText("Placer le début de la zone de lecture sur la lecture courante");
			this.buttonBeginAlignment.setContentAreaFilled(true);
			this.buttonBeginAlignment.setIcon(ImageGetter.getImageIcon(ImageGetter.ICONE_BEGIN_ALIGNMENT_32));
		}
		return this.buttonBeginAlignment;
	}

	/**
	 * Create the end alignment button
	 * @return the end alignment button
	 */
	protected JButton getButtonEndAlignment() {
		if (this.buttonEndAlignment == null) {
			this.buttonEndAlignment = new JButton(new SetEndAlignmentOnCurrentPositionAction());
			this.buttonEndAlignment.setFocusable(false);
			this.buttonEndAlignment.setPreferredSize(new Dimension(32, 32));
			this.buttonEndAlignment.setBorderPainted(true);
			this.buttonEndAlignment.setToolTipText("Placer la fin de la zone de lecture sur la lecture courante");
			this.buttonEndAlignment.setContentAreaFilled(true);
			this.buttonEndAlignment.setIcon(ImageGetter.getImageIcon(ImageGetter.ICONE_END_ALIGNMENT_32));
		}
		return this.buttonEndAlignment;
	}

	/**
	 * get the Always on top button
	 * @return the button
	 */
	protected JSwitchButton getButtonAlwaysOnTop() {
		if (this.buttonAlwaysOnTop == null) {
			this.buttonAlwaysOnTop = new JSwitchButton("", Preferences.getInstance().isAlwaisOnTop(), ImageGetter.getImageIcon(ImageGetter.ICONE_ALWAYS_ON_TOP_DISABLED_32), ImageGetter.getImageIcon(ImageGetter.ICONE_ALWAYS_ON_TOP_ENABLED_32), new SwitchButtonActionListener() {
				@Override
				public boolean actionPerformed(ActionEvent e, boolean newEnabled) {
					Preferences.getInstance().setAlwaysOnTop(newEnabled);
					return true;
				}
			});
			this.buttonAlwaysOnTop.setFocusable(false);
			this.buttonAlwaysOnTop.setPreferredSize(new Dimension(32, 32));
			this.buttonAlwaysOnTop.setRolloverEnabled(false);
			this.buttonAlwaysOnTop.setBorderPainted(true);
			this.buttonAlwaysOnTop.setToolTipText("Laisser au dessus des autres applications");
			this.buttonAlwaysOnTop.setContentAreaFilled(true);
		}
		return this.buttonAlwaysOnTop;
	}

	/**
	 * Set the pause state
	 */
	public void setStatePaused() {
		this.getButtonBeginAlignment().setEnabled(true);
		this.getButtonEndAlignment().setEnabled(true);
		this.getBoutonFavori().setEnabled(true);
		//this.getBoutonPopupFavori().setEnabled(true);
	}

	/**
	 * Set the uninitialized state
	 */
	public void setStateUninitialized() {
		this.getButtonBeginAlignment().setEnabled(false);
		this.getButtonEndAlignment().setEnabled(false);
		this.getBoutonFavori().setEnabled(false);
		//this.getBoutonPopupFavori().setEnabled(false);

		//TODO voir si utile
		this.updateMarkButtonsState(false);
        this.getBoutonSaveMark().setEnabled(false);
	}

	/**
	 * Set the stopped state
	 */
	protected void setStateStopped() {
		this.getButtonBeginAlignment().setEnabled(false);
		this.getButtonEndAlignment().setEnabled(false);
		this.getBoutonFavori().setEnabled(true);
		//this.getBoutonPopupFavori().setEnabled(true);
	}

	/**
	 * update the state of the marks buttons dependant of
	 * the current song loaded
	 * @param active true if button must be active
	 */
	public void updateMarkButtonsState(boolean active) {
		if (!active) {
			this.getBoutonMark().setEnabled(false);
			this.getBoutonPopupMark().setEnabled(false);
            this.getBoutonSaveMark().setEnabled(false);
			this.windowPlayer.getMenuItemRechercheMark().setEnabled(false);
		} else {
			this.getBoutonMark().setEnabled(true);
			this.getBoutonPopupMark().setEnabled(true);
			this.windowPlayer.getMenuItemRechercheMark().setEnabled(true);
		}
	}

	/**
	 * Set the playing state
	 */
	protected void setStatePlaying() {
		this.getButtonBeginAlignment().setEnabled(true);
		this.getButtonEndAlignment().setEnabled(true);
		this.getBoutonFavori().setEnabled(true);
		//this.getBoutonPopupFavori().setEnabled(true);
	}

	public void updateMarkListAfterDelete(long idMarkSupprime) {
		createPopupMark();
	}
}
