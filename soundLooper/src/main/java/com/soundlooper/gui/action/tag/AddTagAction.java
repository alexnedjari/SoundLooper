/**
 *
 */
package com.soundlooper.gui.action.tag;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.soundlooper.exception.SoundLooperException;
import com.soundlooper.gui.WindowAddMark;
import com.soundlooper.gui.WindowFavorite;
import com.soundlooper.gui.WindowPlayer;
import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.model.tag.Tag;

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
public class AddTagAction extends AbstractAction {

	/**
	 * Sérial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * La fenêtre parente à la popup à ouvrir
	 */
	private JTextField textFieldTagName;
	
	private JTree tree;

	/**
	 * Constructeur
	 * @param windowPlayer la fenêtre parente de la popup à ouvrir
	 */
	public AddTagAction(JTextField textFieldTagName, JTree tree) {
		this.textFieldTagName = textFieldTagName;
		this.tree = tree;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String nom = textFieldTagName.getText();
		Object object = ((DefaultMutableTreeNode)tree.getLastSelectedPathComponent()).getUserObject();
		if (!(object instanceof Tag)) {
			return;
		}
		if (nom == null) {
			return;
		}

		try {
			SoundLooperPlayer.getInstance().createNewTag(nom, (Tag)object);
		} catch (SoundLooperException e1) {
			//TODO gérer l'erreur
		}
	}

}
