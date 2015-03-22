/**
 *
 */
package com.soundlooper.model.tag;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.soundlooper.exception.SoundLooperDatabaseException;
import com.soundlooper.model.SoundLooperObject;
import com.soundlooper.model.database.ConnectionFactory;
import com.soundlooper.model.database.SoundLooperDAO;
import com.soundlooper.model.song.Song;

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
 * The class that access database for songs access
 *
 * @author Alexandre NEDJARI
 * @since 5 sept. 2012
 * -------------------------------------------------------
 */
public class TagDAO extends SoundLooperDAO<Tag> {

	/**
	 * The instance
	 */
	private static TagDAO instance;

	/**
	 * Private constructor
	 */
	private TagDAO() {
		// To avoid construction
	}

	/**
	 * Get the instance
	 *
	 * @return the instance
	 */
	public static TagDAO getInstance() {
		if (TagDAO.instance == null) {
			TagDAO.instance = new TagDAO();
		}
		return TagDAO.instance;
	}

	@Override
	public Tag createNew() {
		return new Tag();
	}

	@Override
	protected void insert(Tag tag) {
		try {
			// Check that there is no tag with this name
			PreparedStatement statementCheckPath = ConnectionFactory.getNewPreparedStatement("SELECT id FROM tag WHERE name=?");
			statementCheckPath.setString(1, tag.getName());
			ResultSet checkQuery = statementCheckPath.executeQuery();
			if (checkQuery.next()) {
				// there is already a song for this path
				throw new SoundLooperDatabaseException("Un tag avec le nom : '" + tag.getName() + "' existe déjà",
						SoundLooperDatabaseException.ERROR_CODE_TAG_NAME_ALREADY_EXISTS_IN_DATABASE);
			}

			// crée un nouveau tag dans la base de données, et rempli le
			// champ ID
			PreparedStatement updateStatement = ConnectionFactory.getNewPreparedStatement("INSERT INTO tag (name, id_parent) VALUES(?, ?)");
			updateStatement.setString(1, tag.getName());
			if (tag.getParent() != null) {
				updateStatement.setLong(2, tag.getParent().getId());
			} else {
				updateStatement.setLong(2, SoundLooperObject.ID_NOT_INITIALIZED);
			}
			updateStatement.executeUpdate();

			ResultSet generatedKeys = updateStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				tag.setId(generatedKeys.getLong(1));
			} else {
				throw new SoundLooperDatabaseException("No ID generated when persisting tag : " + tag.getName(),
						SoundLooperDatabaseException.ERROR_CODE_NO_ID_GENERATED_FOR_NEW_SONG);
			}

			ConnectionFactory.commit();
		} catch (SQLException e) {
			this.rollbackCurrentTransaction();
			throw new SoundLooperDatabaseException("Error when creating new Tag", e);
		} catch (SoundLooperDatabaseException e) {
			this.rollbackCurrentTransaction();
			throw e;
		} 
	}

	@Override
	protected void update(Tag tag) {
		try {
			// check that the tag with this ID exists
			ResultSet checkQueryID = ConnectionFactory.getNewStatement().executeQuery("SELECT id FROM tag WHERE id = '" + tag.getId() + "'");
			if (!checkQueryID.next()) {
				// there is no tag with this ID
				throw new SoundLooperDatabaseException("Tag with this ID is not persisted : " + tag.getName(),
						SoundLooperDatabaseException.ERROR_CODE_SONG_ID_DOES_NOT_EXISTS_IN_DATABASE);
			}

			// check that the path of the updated tag is not already used
			PreparedStatement statementCheckPath = ConnectionFactory.getNewPreparedStatement("SELECT id FROM tag WHERE id != ? AND name=?");
			statementCheckPath.setLong(1, tag.getId());
			statementCheckPath.setString(2, tag.getName());
			ResultSet checkQueryPath = statementCheckPath.executeQuery();
			if (checkQueryPath.next()) {
				// There is a tag that have already the new name
				throw new SoundLooperDatabaseException("Tag with this name already exists in database : " + tag.getName(),
						SoundLooperDatabaseException.ERROR_CODE_TAG_NAME_ALREADY_EXISTS_IN_DATABASE);
			}

			// modifie les attributs d'une chanson en fonction de son ID
			PreparedStatement updateStatement = ConnectionFactory.getNewPreparedStatement("UPDATE tag SET name=?, id_parent=? WHERE id = ?");
			updateStatement.setString(1, tag.getName());
			if (tag.getParent() != null) {
				updateStatement.setLong(2, tag.getParent().getId());
			} else {
				updateStatement.setLong(2, SoundLooperObject.ID_NOT_INITIALIZED);	
			}
			updateStatement.executeUpdate();

			if (updateStatement.getUpdateCount() != 1) {
				throw new SoundLooperDatabaseException("Update tag query must update 1 row and it update " + updateStatement.getUpdateCount() + " for the tag "
						+ tag.getName());
			}
			ConnectionFactory.commit();

		} catch (SQLException e) {
			this.rollbackCurrentTransaction();
			throw new SoundLooperDatabaseException("Error when trying to update tag" + tag.getName(), e);
		} catch (SoundLooperDatabaseException e) {
			this.rollbackCurrentTransaction();
			throw e;
		}
	}

	@Override
	public Tag delete(Tag tag) {
		try {
			// check that the tag with this ID exists
			ResultSet checkQueryID = ConnectionFactory.getNewStatement().executeQuery("SELECT ID FROM TAG WHERE ID = '" + tag.getId() + "'");
			if (!checkQueryID.next()) {
				// there is no tag with this ID
				throw new SoundLooperDatabaseException("Tag with this ID is not persisted : " + tag.getDescription(),
						SoundLooperDatabaseException.ERROR_CODE_TAG_ID_DOES_NOT_EXISTS_IN_DATABASE);
			}

			//delete childrens recursively
			ArrayList<Tag> childrenList = getChildrenList(tag);
			for (Tag children : childrenList) {
				delete(children);
			}

			//detach all songs


			Statement updateStatement = ConnectionFactory.getNewStatement();
			updateStatement.executeUpdate("DELETE FROM TAG WHERE ID = '" + tag.getId() + "'");

			if (updateStatement.getUpdateCount() != 1) {
				throw new SoundLooperDatabaseException("Delete tag query must update 1 row and it delete " + updateStatement.getUpdateCount() + " for the tag " + tag.getName());
			}

			ConnectionFactory.commit();
			tag.setId(SoundLooperObject.ID_NOT_INITIALIZED);
			return tag;
		} catch (SQLException e) {
			this.rollbackCurrentTransaction();
			throw new SoundLooperDatabaseException("Error when deleting tag", e);
		} catch (SoundLooperDatabaseException e) {
			this.rollbackCurrentTransaction();
			throw e;
		}
	}
	
	public void attachSong(Tag tag, Song song) {
		try{
			Statement updateStatement = ConnectionFactory.getNewStatement();
			updateStatement.executeUpdate("INSERT INTO tag_song (id_tag, id_song) VALUES  (" + tag.getId() + ", " + song.getId()+")");
		} catch (SQLException e) {
			this.rollbackCurrentTransaction();
			throw new SoundLooperDatabaseException("Error when deleting tag", e);
		} catch (SoundLooperDatabaseException e) {
			this.rollbackCurrentTransaction();
			throw e;
		}
	}

	public void detachSong(Tag tag, Song song) {
		try{
			Statement updateStatement = ConnectionFactory.getNewStatement();
			updateStatement.executeUpdate("DELETE FROM tag_song WHERE id_tag = " + tag.getId() + " and id_song=" + song.getId()+"");
		} catch (SQLException e) {
			this.rollbackCurrentTransaction();
			throw new SoundLooperDatabaseException("Error when deleting tag", e);
		} catch (SoundLooperDatabaseException e) {
			this.rollbackCurrentTransaction();
			throw e;
		}
	}

	public void detachAllSongs(Tag tag) {
		try{
			Statement updateStatement = ConnectionFactory.getNewStatement();
			updateStatement.executeUpdate("DELETE FROM tag_song WHERE id_tag = '" + tag.getId() + "'");
		} catch (SQLException e) {
			this.rollbackCurrentTransaction();
			throw new SoundLooperDatabaseException("Error when deleting tag", e);
		} catch (SoundLooperDatabaseException e) {
			this.rollbackCurrentTransaction();
			throw e;
		}

	}

	public ArrayList<Tag> getChildrenList(Tag parent) {
		ArrayList<Tag> tagList = new ArrayList<Tag>();
		try {
			// get all tags list
			ResultSet songsQuery = ConnectionFactory.getNewStatement().executeQuery("SELECT id, name FROM tag WHERE id_parent=?");
			while (songsQuery.next()) {
				long id = songsQuery.getLong("id");
				String name = songsQuery.getString("name");

				Tag tag = new Tag();
				tag.setId(id);
				tag.setName(name);
				tag.setParent(parent);

				tagList.add(tag);
			}
		} catch (SQLException e) {
			throw new SoundLooperDatabaseException("Error when get the tags list", e);
		}
		return tagList;
	}

	//	@Override
	public ArrayList<Tag> getList() {
		ArrayList<Tag> tagList = new ArrayList<Tag>();
		Map<Tag, Long>mapIdParentByTag = new HashMap<Tag, Long>();
		try {
			// get all tags list
			ResultSet songsQuery = ConnectionFactory.getNewStatement().executeQuery("SELECT id, name, id_parent FROM tag");
			while (songsQuery.next()) {
				long id = songsQuery.getLong("id");
				String name = songsQuery.getString("name");
				Long idParent = songsQuery.getLong("id_parent");

				Tag tag = new Tag();
				tag.setId(id);
				tag.setName(name);

				mapIdParentByTag.put(tag, idParent);
			}

			//The map is OK, put it in the list now
			Set<Tag> setTag = mapIdParentByTag.keySet();
			for (Tag tag : setTag) {
				Long idParent = mapIdParentByTag.get(tag);
				if(idParent == null || idParent.longValue() == SoundLooperObject.ID_NOT_INITIALIZED ) {
					tag.setParent(null);
					tagList.add(tag);
				} else {
					//search the parent in the list
					for (Tag tagParent : setTag) {
						if (tag.getId() == tagParent.getId()) {
							tag.setParent(tagParent);
							break;
						}
					}
				}

			}
		} catch (SQLException e) {
			throw new SoundLooperDatabaseException("Error when get the tags list", e);
		}
		return tagList;
	}
}
