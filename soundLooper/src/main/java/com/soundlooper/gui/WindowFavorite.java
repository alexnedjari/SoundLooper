package com.soundlooper.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.soundlooper.exception.SoundLooperException;
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

	//TODO dans la fenêtre de changement de nom, empêcher la validation si nom vide
	//TODO faire en sorte qu'il puisse y avoir plusieurs tags avec le même nom, modifier l'affichage du nom pour afficher toute l'arborescence
	//TODO faire un tri sur les tags, et un sur les favoris
	//TODO Faire la suppression (+ suppression de la sous arborescence + détachement de tous les favoris) 
	//TODO Pouvoir modifier l'arborescence des tags en faisant du glisser déposer
	//TODO Pouvoir ajouter des tags à un favori en cliquant sur le panneau de droite
	//TODO Pouvoir déplacer un favoris d'un tag à l'autre en faisant glisser une chanson dans l'arborescence
	//TODO Raffraichir la liste des tags dans le panneau de droite dès qu'on en ajoute un
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
		final Set<Tag> setTag = getListeTagAsSet();


		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				getTagPanel().removeAll();

				if (object instanceof Song) {
					Song song = (Song) object;
					for (Tag tag : setTag) {
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

	private Set<Tag> getListeTagAsSet() {
		final List<Tag> listeTag = SoundLooperPlayer.getInstance().getTagList();
		Set<Tag > setTag = new HashSet<Tag>();
		for (Tag tag : listeTag) {
			setTag.add(tag);
			addChildren(setTag, tag);
		}
		return setTag;
	}

	private void addChildren(Set<Tag> setTag, Tag tag) {
		List<Tag> listChildrenCopy = tag.getListChildrenCopy();
		for (Tag tagChild : listChildrenCopy) {
			setTag.add(tagChild);
			addChildren(setTag, tagChild);
		}

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
	
	protected void updateTagNameInDialog(final Tag tag) {
		String initialName = tag.getName();
		DialogNameTag dialogNameTag = new DialogNameTag(WindowFavorite.this, tag);
		dialogNameTag.setVisible(true);
		boolean validatedWindow = dialogNameTag.isValidatedWindow();
		if (validatedWindow) {
			if (!tag.getName().equals(initialName))  {
				try {
					SoundLooperPlayer.getInstance().validateTag(tag);
					SwingUtilities.invokeLater(new Runnable() {	
						@Override
						public void run() {
							favoriteTree.updateUI();
						}
					});
					
				} catch (SoundLooperException e1) {
					e1.printStackTrace();
				}
			}
		} 
	}
	
	protected void createTagInDialog(final Tag parent, final DefaultMutableTreeNode node) {
		final Tag tagTemp = new Tag(); 
		tagTemp.setName("Nouveau tag");
		DialogNameTag dialogNameTag = new DialogNameTag(WindowFavorite.this, tagTemp);
		dialogNameTag.setVisible(true);
		boolean validatedWindow = dialogNameTag.isValidatedWindow();
		if (validatedWindow) {
			parent.addChildren(tagTemp);
			try {
				SoundLooperPlayer.getInstance().validateTag(tagTemp);
				SwingUtilities.invokeLater(new Runnable() {	
					@Override
					public void run() {
						DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(tagTemp);
						node.add(newChild);
						favoriteTree.updateUI();
						favoriteTree.scrollPathToVisible(new TreePath(newChild.getPath()));
					}
				});

			} catch (SoundLooperException e1) {
				e1.printStackTrace();
			}
		}
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
			//			model.addTreeModelListener(new TreeModelListener() {
			//				
			//				@Override
			//				public void treeStructureChanged(TreeModelEvent e) {
			//					// TODO Auto-generated method stub
			//					
			//				}
			//				
			//				@Override
			//				public void treeNodesRemoved(TreeModelEvent e) {
			//					// TODO Auto-generated method stub
			//					
			//				}
			//				
			//				@Override
			//				public void treeNodesInserted(TreeModelEvent e) {
			//					// TODO Auto-generated method stub
			//					
			//				}
			//				
			//				@Override
			//				public void treeNodesChanged(TreeModelEvent e) {
			//					DefaultMutableTreeNode node;
			//					node = (DefaultMutableTreeNode)
			//							(e.getTreePath().getLastPathComponent());
			//
			//					/*
			//					 * If the event lists children, then the changed
			//					 * node is the child of the node we have already
			//					 * gotten.  Otherwise, the changed node and the
			//					 * specified node are the same.
			//					 */
			//					try {
			//						int index = e.getChildIndices()[0];
			//						node = (DefaultMutableTreeNode)
			//								(node.getChildAt(index));
			//					} catch (NullPointerException exc) {}
			//					
			//				//	SoundLooperPlayer.getInstance().renameTag(e.getSource()(String)node.getUserObject());
			//
			//					System.out.println("The user has finished editing the node.");
			//					System.out.println("New value: " + node.getUserObject());
			//					
			//				}
			//			});

			this.favoriteTree = new JTree(model);
			//this.favoriteTree.setEditable(true);
			this.favoriteTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			this.favoriteTree.setShowsRootHandles(false);
			this.favoriteTree.setCellEditor(new FavoriteTreeCellEditor(favoriteTree, (DefaultTreeCellRenderer) favoriteTree.getCellRenderer()));


			
			favoriteTree.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					super.keyPressed(e);
					if (e.getKeyCode() == KeyEvent.VK_F2) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode)
								favoriteTree.getLastSelectedPathComponent();
						if (node == null) {
							//since Nothing is selected.     
							return;
						}
						final Object nodeObject = node.getUserObject();
						if (nodeObject instanceof Tag) {
							updateTagNameInDialog((Tag)nodeObject);
						}
					}
				}
			});
			favoriteTree.addMouseListener(new MouseAdapter() {

				@Override
				public void mousePressed(MouseEvent me) {
					if (me.getButton() == MouseEvent.BUTTON3) {
						//select under mouse element
						int row = favoriteTree.getClosestRowForLocation(me.getX(), me.getY());
						favoriteTree.setSelectionRow(row);
						
						final DefaultMutableTreeNode node = (DefaultMutableTreeNode)
								favoriteTree.getLastSelectedPathComponent();
						if (node == null) {
							//since Nothing is selected.     
							return;
						}
						final Object nodeObject = node.getUserObject();
						if (nodeObject instanceof Tag) {
							JPopupMenu menu = new JPopupMenu();
							
							JMenuItem menuItemAddTag = new JMenuItem("Ajouter un tag");
							menuItemAddTag.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									Tag parent = (Tag) nodeObject;
									createTagInDialog(parent, node);
								}
							});
							menu.add(menuItemAddTag);
							
							JMenuItem menuItemRenameTag = new JMenuItem("Renommer");
							menuItemRenameTag.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									updateTagNameInDialog((Tag)nodeObject);
								}
							});
							menu.add(menuItemRenameTag);
							
							menu.show(favoriteTree, me.getX(), me.getY());
						}
					}
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
		if (parent != null) {
			DefaultMutableTreeNode parentTreeNode = searchNode(parent);
			parentTreeNode.insert(new DefaultMutableTreeNode(), parentTreeNode.getChildCount());
		} else {
			DefaultMutableTreeNode parentTreeNode = (DefaultMutableTreeNode) this.getFavoriteTree().getModel().getRoot();
			parentTreeNode.insert(new DefaultMutableTreeNode(), parentTreeNode.getChildCount());
		}
		getFavoriteTree().updateUI();

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

	private class FavoriteTreeCellEditor extends DefaultTreeCellEditor {

		public FavoriteTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer) {
			super(tree, renderer);
		}

		@Override
		public boolean isCellEditable(EventObject anEvent) {
			if (lastPath != null && lastPath.getPathCount()==2){
				System.out.println("lastpath : " + lastPath);
				JTree jTree = (JTree) anEvent.getSource();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)jTree.getLastSelectedPathComponent();
				if (node == null) {
					return true;
				}
				return node.getUserObject() instanceof Tag;
			} 
			return false;
		}
	}
}