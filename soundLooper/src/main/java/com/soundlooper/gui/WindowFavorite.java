package com.soundlooper.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import com.soundlooper.gui.action.favorite.DeleteFavoriteAction;
import com.soundlooper.gui.action.tag.AddTagAction;
import com.soundlooper.model.SoundLooperObject;
import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.model.song.Song;
import com.soundlooper.model.tag.Tag;
import com.soundlooper.service.entite.song.SongListener;
import com.soundlooper.service.entite.song.SongSupport;
import com.soundlooper.service.entite.tag.TagListener;
import com.soundlooper.service.entite.tag.TagSupport;

/**
 *-------------------------------------------------------
 * Window to use to manage favorite
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
 * @author Alexandre NEDJARI
 * @since  02 jan. 2015
 *-------------------------------------------------------
 */
public class WindowFavorite extends JDialog implements SongListener, TagListener {
	protected static final Color BLUE_COLOR = new Color(36, 168, 206);
	private final class FavoriteTreeNode extends DefaultMutableTreeNode {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private FavoriteTreeNode(Object userObject, boolean allowsChildren) {
			super(userObject, allowsChildren);
		}

		@Override
		public String toString() {
			if (userObject instanceof Tag) {
				return ((Tag)userObject).getName();
			}
			if (userObject instanceof Song) {
				return ((Song)userObject).getFile().getName();
			}
			return super.toString();
		}
	}

	class FavoriteTreeCellRenderer implements TreeCellRenderer {

		private JLabel label;

		FavoriteTreeCellRenderer() {
			label = new JLabel();
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			Object o = ((DefaultMutableTreeNode) value).getUserObject();
			if (o instanceof Tag && ((Tag)o).getParent() != null) {
				Tag tag = (Tag) o;
				label.setIcon(ImageGetter.getImageIcon("/icons/tag_16.png"));
				label.setText(tag.getName());
			} else if (o instanceof Song) {
				Song song = (Song) o;
				label.setIcon(ImageGetter.getImageIcon("/icons/song_16.png"));
				label.setText(song.getFile().getName());
				label.setToolTipText(song.getFile().getAbsolutePath());
			}else {
				label.setIcon(null);
				label.setText("" + value);
			}
			if (selected) {
				label.setBackground(BLUE_COLOR);
				label.setOpaque(true);
			} else {
				label.setOpaque(false);
			}
			return label;
		}
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTree favoriteTree;
	private JPanel mainPanel;
	private JPanel actionPanel;
	private JPanel tagPanel;
	private JPanel panelToolbar;

	public WindowFavorite(WindowPlayer windowPlayer) {
		super(windowPlayer);
		SongSupport.getInstance().addToListSongListener(this);
		TagSupport.getInstance().addToListTagListener(this);
		this.setTitle("Gestion des favoris");
		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.setViewportView(this.getMainPanel());
		this.getContentPane().add(jScrollPane, BorderLayout.CENTER);
		//this.getContentPane().add(getPanelToolbar(), BorderLayout.SOUTH);
		this.getContentPane().add(getActionPanel(), BorderLayout.SOUTH);
		this.getContentPane().add(getTagPanel(), BorderLayout.EAST);
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

	private JPanel getTagPanel() {
		if (tagPanel == null) {
			tagPanel = new JPanel();
			tagPanel.setLayout(new FlowLayout());
			tagPanel.setPreferredSize(new Dimension(200,0));
			tagPanel.setBackground(Color.WHITE);
		}
		return tagPanel;

	}

	private void onElementSelect(final Object object) {
		final List<Tag> listeTag = SoundLooperPlayer.getInstance().getTagList();
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				getTagPanel().removeAll();

				if (object instanceof Song) {
					Song song = (Song) object;
					for (Tag tag : listeTag) {
						JLabel label = new JLabel(" "+tag.getName()+" ");
						if (isTagForSong(tag, song)) {
							label.setBackground(BLUE_COLOR);
							label.setOpaque(true);
						}
						label.setCursor(new Cursor(Cursor.HAND_CURSOR));

						getTagPanel().add(label);
					}
				}
				getTagPanel().updateUI();

			}
		});

	}

	private JPanel getActionPanel() {
		if (actionPanel == null) {
			actionPanel = new JPanel();
			actionPanel.setLayout(new FlowLayout());

			JLabel labelFavoris = new JLabel("Favoris");
			actionPanel.add(labelFavoris);
			JButton deleteFavoriteButton = new JButton("Supprimer");
			deleteFavoriteButton.addActionListener(new DeleteFavoriteAction(this.getFavoriteTree()));
			deleteFavoriteButton.setPreferredSize(new Dimension(95,30));
			actionPanel.add(deleteFavoriteButton);


			JLabel labelTag = new JLabel("Tags");
			actionPanel.add(labelTag);
			JTextField textFieldTagName = new JTextField();
			textFieldTagName.setPreferredSize(new Dimension(95,20));
			actionPanel.add(textFieldTagName);
			JButton addTagButton = new JButton("Créer tag");
			addTagButton.setPreferredSize(new Dimension(95,30));
			addTagButton.addActionListener(new AddTagAction(textFieldTagName, getFavoriteTree()));
			actionPanel.add(addTagButton);

			actionPanel.setPreferredSize(new Dimension(0,50));
		}
		return actionPanel;
	}

	private JTree getFavoriteTree() {
		if (this.favoriteTree == null) {
			List<Song> songList = SoundLooperPlayer.getInstance().getFavoriteSongList();
			List<Tag> tagList = SoundLooperPlayer.getInstance().getTagList();
			
			Tag rootTag = new Tag("Liste des favoris");
			for (Tag tag : tagList) {
				rootTag.addChildren(tag);
			}
			


			DefaultMutableTreeNode rootNode = new FavoriteTreeNode(rootTag, true);
			DefaultTreeModel model = new DefaultTreeModel(rootNode);
			this.favoriteTree = new JTree(model);
			this.favoriteTree.setEditable(true);
			
			//TODO, ne faire l'action que sur le lcic droit
			favoriteTree.addMouseListener(new MouseAdapter() {
				  @Override
				  public void mousePressed(MouseEvent me) {
					  favoriteTree.setSelectionPath(favoriteTree.getClosestPathForLocation(me.getX(), me.getY()));
				  }
				});

			this.favoriteTree.setCellRenderer(new FavoriteTreeCellRenderer());

			addChildrenTagToNode(rootNode, rootTag, songList);
			this.favoriteTree.expandPath(new TreePath(rootNode.getPath()));

			//rajoute toutes les chansons non taggées à la suite
			for (Song song : songList) {
				if (song.getListTag().size() == 0) {
					DefaultMutableTreeNode childrenNode = new FavoriteTreeNode(song, true);
					rootNode.add(childrenNode);
				}
			}

			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					favoriteTree.updateUI();

				}
			});

			this.favoriteTree.addTreeSelectionListener(new TreeSelectionListener() {

				@Override
				public void valueChanged(TreeSelectionEvent e) {
					JTree tree = (JTree) e.getSource();
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)
							tree.getLastSelectedPathComponent();

					if (node == null) {
						//since Nothing is selected.     
						return;
					}
					Object nodeObject = node.getUserObject();
					onElementSelect(nodeObject);
				}
			});


			//this.favoriteTree = new JTree(songList.toArray());
		}
		return this.favoriteTree;
	}

	private void addChildrenTagToNode(DefaultMutableTreeNode node, Tag tag, List<Song> songList) {
		List<Tag> listChildren = tag.getListChildrenCopy();
		for (Tag childrenTag : listChildren) {
			DefaultMutableTreeNode childrenNode = new FavoriteTreeNode(childrenTag, true);
			node.add(childrenNode);
			addChildrenTagToNode(childrenNode, childrenTag, songList);
		}
		for (Song song : songList) {
			if (isTagForSong(tag, song)) {
				DefaultMutableTreeNode childrenSongNode = new FavoriteTreeNode(song, true);
				node.add(childrenSongNode);
			}
		}
	}

	/**
	 * Check if a song has a particular tag
	 * @param tag
	 * @param song
	 * @return
	 */
	private boolean isTagForSong(Tag tag, Song song) {
		for (Tag songTag : song.getListTag()) {
			if (songTag.getName().equals(tag.getName())) {
				return true;
			}
		}
		return false;
	}

	private JPanel getMainPanel() {
		if (this.mainPanel == null) {
			this.mainPanel = new JPanel();
			this.mainPanel.setLayout(new BorderLayout());
			this.mainPanel.setBackground(Color.WHITE);




			this.mainPanel.add(getFavoriteTree(), BorderLayout.WEST);
		}
		return this.mainPanel;
	}
	//	private JPanel getPanelToolbar() {
	//		if (this.panelToolbar == null) {
	//			this.panelToolbar = new JPanel();
	//			this.panelToolbar.setLayout(new FlowLayout());
	//			
	//			//Ajout du bouton fermer
	//			JButton boutonFermer = new JButton("Fermer");
	//			boutonFermer.addActionListener(new ActionListener(){
	//			@Override
	//			public void actionPerformed(ActionEvent e) {
	//				    onWindowClose();
	//					WindowFavorite.this.dispose();
	//				}
	//
	//			
	//			});
	//			this.panelToolbar.add(boutonFermer);
	//			this.panelToolbar.setPreferredSize(new Dimension(0,30));
	//		}
	//		return this.panelToolbar;
	//	}
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

	@Override
	public void onTagDeleted(Tag tag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTagAdded(Tag addedTag, Tag parent) {
		DefaultMutableTreeNode parentTreeNode = searchNode(parent);
		parentTreeNode.insert(new DefaultMutableTreeNode(), parentTreeNode.getChildCount());
		
	}	
	
	 public DefaultMutableTreeNode searchNode(SoundLooperObject userObject) {
		 DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) getFavoriteTree().getModel().getRoot();
		    DefaultMutableTreeNode node = null;
		    Enumeration<?> e = rootNode.breadthFirstEnumeration();
		    while (e.hasMoreElements()) {
		      node = (DefaultMutableTreeNode) e.nextElement();
		      if (userObject.getId() == ((SoundLooperObject)node.getUserObject()).getId()) {
		        return node;
		      }
		    }
		    return null;
		  }
}