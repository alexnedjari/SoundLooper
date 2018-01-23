/**
 *
 */
package com.soundlooper.model.tag;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.soundlooper.exception.SoundLooperDatabaseException;
import com.soundlooper.exception.SoundLooperRuntimeException;
import com.soundlooper.model.SoundLooperObject;
import com.soundlooper.model.database.ConnectionFactory;
import com.soundlooper.model.database.SoundLooperDAO;
import com.soundlooper.model.song.Song;

/**
 * ------------------------------------------------------- Sound Looper is an
 * audio player that allow user to loop between two points Copyright (C) 2014
 * Alexandre NEDJARI
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * The class that access database for songs access
 *
 * @author Alexandre NEDJARI
 * @since 5 sept. 2012 -------------------------------------------------------
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
			// crée un nouveau tag dans la base de données, et rempli le
			// champ ID
			PreparedStatement updateStatement = ConnectionFactory
					.getNewPreparedStatement("INSERT INTO tag (name, id_parent) VALUES(?, ?)");
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
		} catch (SQLException | SoundLooperDatabaseException e) {
			this.rollbackCurrentTransaction();
			throw new SoundLooperRuntimeException("Error when creating new Tag", e);
		}
	}

	@Override
	protected void update(Tag tag) {
		try {
			// check that the tag with this ID exists
			ResultSet checkQueryID = ConnectionFactory.getNewStatement()
					.executeQuery("SELECT id FROM tag WHERE id = '" + tag.getId() + "'");
			if (!checkQueryID.next()) {
				// there is no tag with this ID
				throw new SoundLooperDatabaseException("Tag with this ID is not persisted : " + tag.getName(),
						SoundLooperDatabaseException.ERROR_CODE_SONG_ID_DOES_NOT_EXISTS_IN_DATABASE);
			}

			// modifie les attributs d'une chanson en fonction de son ID
			PreparedStatement updateStatement = ConnectionFactory
					.getNewPreparedStatement("UPDATE tag SET name=?, id_parent=? WHERE id = ?");
			updateStatement.setString(1, tag.getName());
			if (tag.getParent() != null) {
				updateStatement.setLong(2, tag.getParent().getId());
			} else {
				updateStatement.setLong(2, SoundLooperObject.ID_NOT_INITIALIZED);
			}
			updateStatement.setLong(3, tag.getId());
			updateStatement.executeUpdate();

			if (updateStatement.getUpdateCount() != 1) {
				throw new SoundLooperDatabaseException("Update tag query must update 1 row and it update "
						+ updateStatement.getUpdateCount() + " for the tag " + tag.getName());
			}
			ConnectionFactory.commit();

		} catch (SQLException | SoundLooperDatabaseException e) {
			this.rollbackCurrentTransaction();
			throw new SoundLooperRuntimeException("Error when trying to update tag" + tag.getName(), e);
		}
	}

	@Override
	public Tag delete(Tag tag) {
		try {
			if (Tag.ROOT_TAG_ID.equals(tag.getId())) {
				throw new SoundLooperDatabaseException("Try to delete tag root",
						SoundLooperDatabaseException.ERROR_CODE_TAG_ID_DOES_NOT_EXISTS_IN_DATABASE);
			}

			// check that the tag with this ID exists
			ResultSet checkQueryID = ConnectionFactory.getNewStatement()
					.executeQuery("SELECT ID FROM TAG WHERE ID = '" + tag.getId() + "'");
			if (!checkQueryID.next()) {
				// there is no tag with this ID
				throw new SoundLooperDatabaseException("Tag with this ID is not persisted : " + tag.getDescription(),
						SoundLooperDatabaseException.ERROR_CODE_TAG_ID_DOES_NOT_EXISTS_IN_DATABASE);
			}

			// delete childrens recursively
			ArrayList<Tag> childrenList = getChildrenList(tag);
			for (Tag children : childrenList) {
				delete(children);
			}

			// detach all songs

			Statement updateStatement = ConnectionFactory.getNewStatement();
			updateStatement.executeUpdate("DELETE FROM TAG WHERE ID = '" + tag.getId() + "'");

			if (updateStatement.getUpdateCount() != 1) {
				throw new SoundLooperDatabaseException("Delete tag query must update 1 row and it delete "
						+ updateStatement.getUpdateCount() + " for the tag " + tag.getName());
			}

			ConnectionFactory.commit();
			tag.setId(SoundLooperObject.ID_NOT_INITIALIZED);
			return tag;
		} catch (SQLException | SoundLooperDatabaseException e) {
			this.rollbackCurrentTransaction();
			throw new SoundLooperRuntimeException("Error when deleting tag", e);
		}
	}

	public void detachAllSongs(Tag tag) {
		try {
			Statement updateStatement = ConnectionFactory.getNewStatement();
			updateStatement.executeUpdate("DELETE FROM tag_song WHERE id_tag = '" + tag.getId() + "'");
		} catch (SQLException e) {
			this.rollbackCurrentTransaction();
			throw new SoundLooperRuntimeException("Error when deleting tag", e);
		}

	}

	public ArrayList<Tag> getChildrenList(Tag parent) {
		ArrayList<Tag> tagList = new ArrayList<>();
		try {
			// get all tags list
			PreparedStatement statement = ConnectionFactory
					.getNewPreparedStatement("SELECT id, name FROM tag WHERE id_parent=?");

			statement.setObject(1, null);
			ResultSet songsQuery = statement.executeQuery();
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
			throw new SoundLooperRuntimeException("Error when get the tags list", e);
		}
		return tagList;
	}

	/**
	 * @return the root tag with all the childrens tree
	 */
	public Tag getTagTree() {
		Tag root = new Tag();
		try {
			// get all tags list
			ResultSet songsQuery = ConnectionFactory.getNewStatement()
					.executeQuery("SELECT id, name, id_parent FROM tag");
			Map<Long, Tag> mapTagById = new HashMap<>();
			Map<Long, Long> mapIdParentById = new HashMap<>();

			while (songsQuery.next()) {
				long id = songsQuery.getLong("id");
				String name = songsQuery.getString("name");
				Long idParent = songsQuery.getLong("id_parent");

				Tag tag = new Tag();
				tag.setId(id);
				tag.setName(name);
				if (idParent == SoundLooperObject.ID_NOT_INITIALIZED) {
					root = tag;
				}
				mapTagById.put(id, tag);
				mapIdParentById.put(id, idParent);
			}

			// The map is OK, we now fill the parent information of each tag
			Collection<Tag> tagList = mapTagById.values();
			for (Tag tag : tagList) {
				Long idParent = mapIdParentById.get(tag.getId());
				if (idParent == SoundLooperObject.ID_NOT_INITIALIZED) {
					// It's the root
					continue;
				}
				Tag parent = mapTagById.get(idParent);
				if (parent == null) {
					// There's a problem in database
					continue;
				}
				tag.setParent(parent);
			}
		} catch (SQLException e) {
			throw new SoundLooperRuntimeException("Error when get the tags list", e);
		}
		return root;
	}

	public void saveTagOnSong(Song song, Tag newTag) {
		try {
			// Check if the link already exists
			PreparedStatement statement = ConnectionFactory
					.getNewPreparedStatement("SELECT count(*) as nb FROM tag_song WHERE id_song=? AND id_tag=?");
			statement.setLong(1, song.getId());
			statement.setLong(2, newTag.getId());
			ResultSet result = statement.executeQuery();
			result.next();
			int nb = result.getInt("nb");
			if (nb > 0) {
				// this link already exists
				return;
			}

			PreparedStatement updateStatement = ConnectionFactory
					.getNewPreparedStatement("INSERT INTO tag_song (id_tag, id_song) VALUES (?, ?)");
			updateStatement.setLong(1, newTag.getId());
			updateStatement.setLong(2, song.getId());
			updateStatement.executeUpdate();
			ConnectionFactory.commit();
		} catch (SQLException e) {
			throw new SoundLooperRuntimeException("Error when get the link between tag and song", e);
		}
	}

	public void removeTagFromSong(Song song, Tag tagToRemove) {
		try {
			PreparedStatement updateStatement = ConnectionFactory
					.getNewPreparedStatement("DELETE FROM tag_song WHERE id_tag = ? AND id_song = ?");
			updateStatement.setLong(1, tagToRemove.getId());
			updateStatement.setLong(2, song.getId());
			updateStatement.executeUpdate();
			ConnectionFactory.commit();
		} catch (SQLException e) {
			throw new SoundLooperRuntimeException("Error when delete the link between tag and song", e);
		}
	}

	public List<Tag> getSongTagList(Song song) {
		List<Tag> tagList = new ArrayList<>();
		try {
			PreparedStatement statement = ConnectionFactory.getNewPreparedStatement(
					"SELECT tag1.id, tag1.name FROM tag_song link, tag tag1 WHERE link.id_tag = tag1.id AND link.id_song=?");
			statement.setLong(1, song.getId());
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				long id = result.getLong("id");
				String name = result.getString("name");

				Tag tag = new Tag();
				tag.setId(id);
				tag.setName(name);
				tagList.add(tag);
			}
		} catch (SQLException e) {
			throw new SoundLooperRuntimeException("Error when get tags of the song : " + song.getFile().getName(), e);
		}
		return tagList;
	}
}
