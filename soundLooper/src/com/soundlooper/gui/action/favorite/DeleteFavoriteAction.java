/**
 *
 */
package com.soundlooper.gui.action.favorite;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import com.soundlooper.exception.SoundLooperException;
import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.model.song.Song;
import com.soundlooper.service.uc.gererSongs.GererSongService;

/**
 *-------------------------------------------------------
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
 *
 * @author Alexandre NEDJARI
 * @since  28 sept. 2012
 *-------------------------------------------------------
 */
public class DeleteFavoriteAction extends AbstractAction {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	
	private JTree treeFavorite = null;
	
	public DeleteFavoriteAction(JTree treeFavorite) {
		this.treeFavorite = treeFavorite;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			Object objet = treeFavorite.getLastSelectedPathComponent();
			
			if (objet instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)objet;
				if (treeNode.isLeaf() && treeNode.getUserObject() instanceof Song) {
					Song song = (Song)treeNode.getUserObject();
					GererSongService.getInstance().deleteFavorite(song);
				}
			}
			
		} catch (SoundLooperException e2) {
			//TODO voir comment gérer exceptions dans les actions
		}
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
