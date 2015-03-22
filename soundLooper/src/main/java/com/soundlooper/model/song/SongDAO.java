/**
 *
 */
package com.soundlooper.model.song;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Set;

import com.soundlooper.exception.SoundLooperDatabaseException;
import com.soundlooper.exception.SoundLooperException;
import com.soundlooper.exception.SoundLooperRecordNotFoundException;
import com.soundlooper.model.SoundLooperObject;
import com.soundlooper.model.database.ConnectionFactory;
import com.soundlooper.model.database.SoundLooperDAO;
import com.soundlooper.model.mark.Mark;
import com.soundlooper.model.mark.MarkDAO;
import com.soundlooper.model.tag.Tag;

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
public class SongDAO extends SoundLooperDAO<Song> {

	/**
	 * The instance
	 */
	private static SongDAO instance;

	/**
	 * Private constructor
	 */
	private SongDAO() {
		// To avoid construction
	}

	/**
	 * Get the instance
	 *
	 * @return the instance
	 */
	public static SongDAO getInstance() {
		if (SongDAO.instance == null) {
			SongDAO.instance = new SongDAO();
		}
		return SongDAO.instance;
	}

	@Override
	public Song createNew() {
		return new Song();
	}

//	@Override
//	public Song getById(long id) throws SoundLooperException {
//		// récupère la liste des chansons créées
//		try {
//			ResultSet songsQuery = ConnectionFactory.getNewStatement().executeQuery("SELECT id, file, lastuse, isfavorite FROM song WHERE id='" + id + "'");
//			if (songsQuery.next()) {
//				File file = new File(songsQuery.getString("file"));
//				Timestamp lastUseDate = songsQuery.getTimestamp("lastuse");
//				boolean isFavorite = this.getBoolean(songsQuery.getLong("isfavorite"));
//				Song song = new Song();
//
//				song.setId(id);
//				song.setLastUseDate(lastUseDate);
//				song.setFile(file);
//				song.setFavorite(isFavorite);
//				return song;
//			}
//		} catch (SQLException e) {
//			throw new SoundLooperDatabaseException("Error when trying to get song with i='" + id + "'", e);
//		}
//
//		throw new SoundLooperRecordNotFoundException("chanson", "id = '" + id + "'");
//	}

	@Override
	protected void insert(Song song) {
		try {
			// Check that there is no song with this path
			PreparedStatement statementCheckPath = ConnectionFactory.getNewPreparedStatement("SELECT id FROM song WHERE file=?");
			statementCheckPath.setString(1, song.getFile().getAbsolutePath());
			ResultSet checkQuery = statementCheckPath.executeQuery();
			if (checkQuery.next()) {
				// there is already a song for this path
				throw new SoundLooperDatabaseException("Song with this path already exists in database : " + song.getDescription(),
						SoundLooperDatabaseException.ERROR_CODE_SONG_PATH_ALREADY_EXISTS_IN_DATABASE);
			}

			// crée une nouvelle chanson dans la base de données, et rempli le
			// champ ID de la chanson
			PreparedStatement updateStatement = ConnectionFactory.getNewPreparedStatement("INSERT INTO song (file, lastuse, isFavorite) VALUES(?, ?, ?)");
			updateStatement.setString(1, song.getFile().getAbsolutePath());
			updateStatement.setDate(2, SoundLooperDAO.getSqlDate(song.getLastUseDate()));
			updateStatement.setBoolean(3, song.isFavorite());
			updateStatement.executeUpdate();

			ResultSet generatedKeys = updateStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				song.setId(generatedKeys.getLong(1));
			} else {
				throw new SoundLooperDatabaseException("No ID generated when persisting song : " + song.getDescription(),
						SoundLooperDatabaseException.ERROR_CODE_NO_ID_GENERATED_FOR_NEW_SONG);
			}

//			// sauvegarder dans la foulée les marqueurs de la chanson
//			HashMap<String, Mark> marks = song.getMarks();
//			for (String markName : marks.keySet()) {
//				Mark mark = marks.get(markName);
//				MarkDAO.getInstance().persist(mark);
//			}
			ConnectionFactory.commit();
		} catch (SQLException e) {
			this.rollbackCurrentTransaction();
			throw new SoundLooperDatabaseException("Error when creating new Song", e);
		} catch (SoundLooperDatabaseException e) {
			this.rollbackCurrentTransaction();
			throw e;
		} 
	}

	@Override
	protected void update(Song song) {
		try {
			// check that the song with this ID exists
			ResultSet checkQueryID = ConnectionFactory.getNewStatement().executeQuery("SELECT id FROM song WHERE id = '" + song.getId() + "'");
			if (!checkQueryID.next()) {
				// there is no song with this ID
				throw new SoundLooperDatabaseException("Song with this ID is not persisted : " + song.getDescription(),
						SoundLooperDatabaseException.ERROR_CODE_SONG_ID_DOES_NOT_EXISTS_IN_DATABASE);
			}

			// check that the path of the updated song is not already used
			PreparedStatement statementCheckPath = ConnectionFactory.getNewPreparedStatement("SELECT id FROM song WHERE id != ? AND file=?");
			statementCheckPath.setLong(1, song.getId());
			statementCheckPath.setString(2, song.getFile().getAbsolutePath());
			ResultSet checkQueryPath = statementCheckPath.executeQuery();
			if (checkQueryPath.next()) {
				// There is a song that have already the new path
				throw new SoundLooperDatabaseException("Song with this path already exists in database : " + song.getDescription(),
						SoundLooperDatabaseException.ERROR_CODE_SONG_PATH_ALREADY_EXISTS_IN_DATABASE);
			}

			// modifie les attributs d'une chanson en fonction de son ID
			PreparedStatement updateStatement = ConnectionFactory.getNewPreparedStatement("UPDATE song SET file=?, lastuse=?, isFavorite=? WHERE id = ?");
			updateStatement.setString(1, song.getFile().getAbsolutePath());
			updateStatement.setDate(2, SoundLooperDAO.getSqlDate(song.getLastUseDate()));
			updateStatement.setBoolean(3, song.isFavorite());
			updateStatement.setLong(4, song.getId());
			updateStatement.executeUpdate();

			if (updateStatement.getUpdateCount() != 1) {
				throw new SoundLooperDatabaseException("Update song query must update 1 row and it update " + updateStatement.getUpdateCount() + " for the song "
						+ song.getDescription());
			}
			ConnectionFactory.commit();

		} catch (SQLException e) {
			this.rollbackCurrentTransaction();
			throw new SoundLooperDatabaseException("Error when trying to update song " + song.toString(), e);
		} catch (SoundLooperDatabaseException e) {
			this.rollbackCurrentTransaction();
			throw e;
		}
	}

	@Override
	public Song delete(Song song) {
		try {
			// check that the song with this ID exists
			ResultSet checkQueryID = ConnectionFactory.getNewStatement().executeQuery("SELECT ID FROM SONG WHERE ID = '" + song.getId() + "'");
			if (!checkQueryID.next()) {
				// there is no song with this ID
				throw new SoundLooperDatabaseException("Song with this ID is not persisted : " + song.getDescription(),
						SoundLooperDatabaseException.ERROR_CODE_SONG_ID_DOES_NOT_EXISTS_IN_DATABASE);
			}

			Statement updateStatement = ConnectionFactory.getNewStatement();
			updateStatement.executeUpdate("DELETE FROM SONG WHERE ID = '" + song.getId() + "'");

			if (updateStatement.getUpdateCount() != 1) {
				throw new SoundLooperDatabaseException("Delete song query must update 1 row and it delete " + updateStatement.getUpdateCount() + " for the song " + song);
			}

			// delete all the marks for this song
			Set<String> markNameSet = song.getMarks().keySet();
			for (String markName : markNameSet) {
				Mark mark = song.getMarks().get(markName);
				if (mark.getId() != SoundLooperObject.ID_NOT_INITIALIZED) {
					//delete only the persisted marks
					MarkDAO.getInstance().delete(mark);
				}
			}

			ConnectionFactory.commit();
			song.setId(SoundLooperObject.ID_NOT_INITIALIZED);
			song.setFavorite(false);
			return song;
		} catch (SQLException e) {
			this.rollbackCurrentTransaction();
			throw new SoundLooperDatabaseException("Error when deleting song", e);
		} catch (SoundLooperDatabaseException e) {
			this.rollbackCurrentTransaction();
			throw e;
		}
	}

//	@Override
	public ArrayList<Song> getList() {
		ArrayList<Song> songList = new ArrayList<Song>();
		try {
			// récupère la liste des chansons créées
			ResultSet songsQuery = ConnectionFactory.getNewStatement().executeQuery("SELECT id, file, lastuse, isfavorite FROM song");
			while (songsQuery.next()) {
				long id = songsQuery.getLong("id");
				Timestamp lastUseDate = songsQuery.getTimestamp("lastuse");
				boolean isFavorite = this.getBoolean(songsQuery.getLong("isfavorite"));
				File file = new File(songsQuery.getString("file"));

				Song song = new Song();
				song.setId(id);
				song.setLastUseDate(lastUseDate);
				song.setFile(file);
				song.setFavorite(isFavorite);
				songList.add(song);
			}
		} catch (SQLException e) {
			throw new SoundLooperDatabaseException("Error when get the songs list", e);
		}
		return songList;
	}

	/**
	 * Get a song by file
	 * @param file the file
	 * @return the song
	 * @throws SoundLooperException If the song is not found
	 *
	 */
	public Song getByFile(File file) throws SoundLooperException {
		// récupère la liste des chansons créées
		try {
			PreparedStatement statement = ConnectionFactory.getNewPreparedStatement("SELECT id, file, lastuse, isfavorite FROM song WHERE file=?");
			statement.setString(1, file.getAbsolutePath());
			ResultSet songsQuery = statement.executeQuery();
			if (songsQuery.next()) {
				long id = songsQuery.getLong("id");
				Timestamp lastUseDate = songsQuery.getTimestamp("lastuse");
				boolean isFavorite = this.getBoolean(songsQuery.getLong("isfavorite"));
				Song song = new Song();

				song.setId(id);
				song.setLastUseDate(lastUseDate);
				song.setFile(file);
				song.setFavorite(isFavorite);

				//charge les marqueurs associés
				try {
					ArrayList<Mark> listMark = MarkDAO.getInstance().getList(song);
					for (Mark mark : listMark) {
						song.getMarks().put(mark.getName(), mark);
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
				return song;
			}
		} catch (SQLException e) {
			throw new SoundLooperDatabaseException("Error when trying to get song on file='" + file.getAbsolutePath() + "'", e);
		}

		throw new SoundLooperRecordNotFoundException("chanson", "file = '" + file.getAbsolutePath() + "'");
	}

	/**
	 * @return the list of files that are in favorite
	 */
	public ArrayList<Song> getFavoriteSongList() {
		ArrayList<Song> songList = new ArrayList<Song>();
		try {
			// récupère la liste des chansons créées
			ResultSet songsQuery = ConnectionFactory.getNewStatement().executeQuery("SELECT id, file, lastuse, isfavorite FROM song WHERE isFavorite=1");
			while (songsQuery.next()) {
				long id = songsQuery.getLong("id");
				Timestamp lastUseDate = songsQuery.getTimestamp("lastuse");
				boolean isFavorite = this.getBoolean(songsQuery.getLong("isfavorite"));
				File file = new File(songsQuery.getString("file"));
				Song song = new Song();

				song.setId(id);
				song.setLastUseDate(lastUseDate);
				song.setFile(file);
				song.setFavorite(isFavorite);
				songList.add(song);
			}
		} catch (SQLException e) {
			throw new SoundLooperDatabaseException("Error when get the songs list", e);
		}
		return songList;
	}
	
	/**
	 * @return the list of files that are in favorite
	 */
	public ArrayList<Song> getFavoriteSongListForTag(Tag tag) {
		ArrayList<Song> songList = new ArrayList<Song>();
		try {
			// récupère la liste des chansons créées
			PreparedStatement statement = ConnectionFactory.getNewPreparedStatement("SELECT id, file, lastuse, isfavorite FROM song WHERE EXISTS (SELECT id_song FROM tag_song WHERE id_tag=?)");
			statement.setLong(1, tag.getId());
			ResultSet songsQuery = statement.executeQuery();
			while (songsQuery.next()) {
				long id = songsQuery.getLong("id");
				Timestamp lastUseDate = songsQuery.getTimestamp("lastuse");
				boolean isFavorite = this.getBoolean(songsQuery.getLong("isfavorite"));
				File file = new File(songsQuery.getString("file"));
				Song song = new Song();

				song.setId(id);
				song.setLastUseDate(lastUseDate);
				song.setFile(file);
				song.setFavorite(isFavorite);
				songList.add(song);
			}
		} catch (SQLException e) {
			throw new SoundLooperDatabaseException("Error when get the songs list", e);
		}
		return songList;
	}

}
