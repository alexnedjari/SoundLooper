package com.soundlooper.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import com.aned.exception.PlayerRuntimeException;
import com.soundlooper.gui.action.favorite.DeleteFavoriteAction;
import com.soundlooper.model.SoundLooperPlayerListener;
import com.soundlooper.model.SoundLooperPlayerSupport;
import com.soundlooper.model.mark.Mark;
import com.soundlooper.model.song.Song;
import com.soundlooper.service.entite.song.SongListener;
import com.soundlooper.service.entite.song.SongService;
import com.soundlooper.service.entite.song.SongSupport;

public class WindowFavorite extends JDialog implements SongListener {
	private JTree favoriteTree;
	private JPanel mainPanel;
	private JPanel actionPanel;
	private JPanel panelToolbar;
	
	public WindowFavorite(WindowPlayer windowPlayer) {
		super(windowPlayer);
		SongSupport.getInstance().addToListSongListener(this);
		this.setTitle("Gestion des favoris");
		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.setViewportView(this.getMainPanel());
		this.getContentPane().add(jScrollPane, BorderLayout.CENTER);
		this.getContentPane().add(getPanelToolbar(), BorderLayout.SOUTH);
		this.getContentPane().add(getActionPanel(), BorderLayout.EAST);
		this.setPreferredSize(new Dimension(800,600));
		this.setMinimumSize(new Dimension(200, 200));
		this.pack();
		this.setLocationRelativeTo(null);
		
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				onWindowClose();
				super.windowClosing(e);
			}
		});
	}
	
	private JPanel getActionPanel() {
		if (actionPanel == null) {
			actionPanel = new JPanel();
			actionPanel.setLayout(new FlowLayout());
			
			JButton deleteFavoriteButton = new JButton("Supprimer");
			deleteFavoriteButton.addActionListener(new DeleteFavoriteAction(this.getFavoriteTree()));
			actionPanel.add(deleteFavoriteButton);
			
			actionPanel.setPreferredSize(new Dimension(100,0));
		}
		return actionPanel;
	}
	
	private JTree getFavoriteTree() {
		if (this.favoriteTree == null) {
			ArrayList<Song> songList = SongService.getInstance().getFavoriteSongList();
			this.favoriteTree = new JTree(songList.toArray());
		}
		return this.favoriteTree;
	}
	
	private JPanel getMainPanel() {
		if (this.mainPanel == null) {
			this.mainPanel = new JPanel();
			this.mainPanel.setLayout(new BorderLayout());
			this.mainPanel.setBackground(Color.WHITE);
			
			
			this.mainPanel.add(new JLabel("Liste des favoris"), BorderLayout.NORTH);
			
			
			this.mainPanel.add(getFavoriteTree(), BorderLayout.WEST);
		}
		return this.mainPanel;
	}
	private JPanel getPanelToolbar() {
		if (this.panelToolbar == null) {
			this.panelToolbar = new JPanel();
			this.panelToolbar.setLayout(new FlowLayout());
			
			//Ajout du bouton fermer
			JButton boutonFermer = new JButton("Fermer");
			boutonFermer.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				    onWindowClose();
					WindowFavorite.this.dispose();
				}

			
			});
			this.panelToolbar.add(boutonFermer);
			this.panelToolbar.setPreferredSize(new Dimension(0,30));
		}
		return this.panelToolbar;
	}
	private void onWindowClose() {
		SongSupport.getInstance().removeFromListSongListener(WindowFavorite.this);
	}

	
	@Override
	public void onFavoriteUpdated(final Song song) {
		if (song.isFavorite()) {
			//new favorite, add to list
			final DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.getFavoriteTree().getModel().getRoot();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					root.add(new DefaultMutableTreeNode(song));
					getFavoriteTree().updateUI();
				}
			});
			
		} else {
			//deleted favorite, +remove from list
			final DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.getFavoriteTree().getModel().getRoot();
			for (int i = 0; i<root.getChildCount();i++) {
				final DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(i);
				if (song.equals(child.getUserObject())) {
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							root.remove(child);
							getFavoriteTree().updateUI();
						}
					}); 
					
					break;
				}
			}
		}
		
	}	
}