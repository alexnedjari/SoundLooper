/**
 *
 */
package com.soundlooper.model.mark;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.soundlooper.exception.SoundLooperDatabaseException;
import com.soundlooper.exception.SoundLooperException;
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
public class MarkDAO extends SoundLooperDAO<Mark> {

	/**
	 * The instance
	 */
	private static MarkDAO instance;

	/**
	 * Private constructor
	 */
	private MarkDAO() {
		// To avoid construction
	}

	/**
	 * Get the instance
	 *
	 * @return the instance
	 */
	public static MarkDAO getInstance() {
		if (MarkDAO.instance == null) {
			MarkDAO.instance = new MarkDAO();
		}
		return MarkDAO.instance;
	}

	@Override
	public Mark createNew() {
		return new Mark();
	}

//	@Override
//	public Mark getById(long id) throws SoundLooperException {
//		// get a mar from his id
//		try {
//			ResultSet marksQuery = ConnectionFactory.getNewStatement().executeQuery("SELECT id, id_song, name begintime, endtime FROM mark WHERE id='" + id + "'");
//			if (marksQuery.next()) {
//				String name = marksQuery.getString("name");
//				int beginTime = marksQuery.getInt("begintime");
//				int endTime = marksQuery.getInt("endtime");
//				Song song = SongDAO.getInstance().getById(marksQuery.getLong("id"));
//
//				Mark mark = this.createNew();
//				mark.setId(id);
//				mark.setName(name);
//				mark.setBeginMillisecond(beginTime);
//				mark.setEndMillisecond(endTime);
//				mark.setSong(song);
//				return mark;
//			}
//		} catch (SQLException e) {
//			throw new SoundLooperDatabaseException("Error when trying to get mark with i='" + id + "'", e);
//		}
//
//		throw new SoundLooperRecordNotFoundException("chanson", "id = '" + id + "'");
//	}

	@Override
	protected void insert(Mark mark) {
		try {
			// Check that there is no mark with this name
			PreparedStatement statementCheckName = ConnectionFactory.getNewPreparedStatement("SELECT id FROM mark WHERE name = ? and id_song=?");
			statementCheckName.setString(1, mark.getName());
			statementCheckName.setLong(2, mark.getSong().getId());
			ResultSet checkQuery = statementCheckName.executeQuery();
			if (checkQuery.next()) {
				// there is already a mark for this name and this song
				throw new SoundLooperDatabaseException("Mark with this name for this song already exists in database : " + mark.getDescription(),
						SoundLooperDatabaseException.ERROR_CODE_MARK_NAME_ALREADY_EXISTS_FOR_THIS_SONG);
			}

			// crée un nouveau marqueur dans la base de données, et rempli le
			// champ ID
			PreparedStatement updateStatement = ConnectionFactory.getNewPreparedStatement("INSERT INTO mark (id_song, begintime, endtime, name) VALUES(?,?,?,?)");
			updateStatement.setLong(1, mark.getSong().getId());
			updateStatement.setInt(2, mark.getBeginMillisecond());
			updateStatement.setInt(3, mark.getEndMillisecond());
			updateStatement.setString(4, mark.getName());
			updateStatement.executeUpdate();

			ResultSet generatedKeys = updateStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				mark.setId(generatedKeys.getLong(1));
			} else {
				throw new SoundLooperDatabaseException("No ID generated when persisting mark : " + mark.getDescription(),
						SoundLooperDatabaseException.ERROR_CODE_NO_ID_GENERATED_FOR_NEW_MARK);
			}

			ConnectionFactory.commit();
		} catch (SQLException e) {
			this.rollbackCurrentTransaction();
			throw new SoundLooperDatabaseException("Error when creating new mark", e);
		} catch (SoundLooperDatabaseException e) {
			this.rollbackCurrentTransaction();
			throw e;
		}
	}

	@Override
	protected void update(Mark mark) {
		try {
			// check that the mark with this ID exists
			ResultSet checkQueryID = ConnectionFactory.getNewStatement().executeQuery("SELECT id FROM mark WHERE id = '" + mark.getId() + "'");
			if (!checkQueryID.next()) {
				// there is no mark with this ID
				throw new SoundLooperDatabaseException("Mark with this ID is not persisted : " + mark.getDescription(),
						SoundLooperDatabaseException.ERROR_CODE_MARK_ID_DOES_NOT_EXISTS_IN_DATABASE);
			}

			// check that the path of the updated mark is not already used
			PreparedStatement checkNameStatement = ConnectionFactory.getNewPreparedStatement("SELECT id FROM mark WHERE id != ? AND name=? and id_song= ?");
			checkNameStatement.setLong(1, mark.getId());
			checkNameStatement.setString(2, mark.getName());
			checkNameStatement.setLong(3, mark.getSong().getId());
			ResultSet checkQueryPath = checkNameStatement.executeQuery();
			if (checkQueryPath.next()) {
				// There is a mark that have already this name
				throw new SoundLooperDatabaseException("Mark with this name already exists for this song : " + mark.getDescription(),
						SoundLooperDatabaseException.ERROR_CODE_MARK_NAME_ALREADY_EXISTS_FOR_THIS_SONG);
			}

			// modifie les attributs d'un marqueur en fonction de son ID
			PreparedStatement updateStatement = ConnectionFactory.getNewPreparedStatement("UPDATE mark SET name=?, begintime=?, endtime=?, id_song=? WHERE id =?");
			updateStatement.setString(1, mark.getName());
			updateStatement.setInt(2, mark.getBeginMillisecond());
			updateStatement.setInt(3, mark.getEndMillisecond());
			updateStatement.setLong(4, mark.getSong().getId());
			updateStatement.setLong(5, mark.getId());
			updateStatement.executeUpdate();

			if (updateStatement.getUpdateCount() != 1) {
				throw new SoundLooperDatabaseException("Update mark query must update 1 row and it update " + updateStatement.getUpdateCount() + " for the mark "
						+ mark.getDescription());
			}
			ConnectionFactory.commit();

		} catch (SQLException e) {
			this.rollbackCurrentTransaction();
			throw new SoundLooperDatabaseException("Error when trying to update mark " + mark.getDescription(), e);
		} catch (SoundLooperDatabaseException e) {
			this.rollbackCurrentTransaction();
			throw e;
		}
	}

	@Override
	public Mark delete(Mark mark) {
		try {
			// check that the mark with this ID exists
			ResultSet checkQueryID = ConnectionFactory.getNewStatement().executeQuery("SELECT id FROM mark WHERE id = '" + mark.getId() + "'");
			if (!checkQueryID.next()) {
				// there is no mark with this ID
				throw new SoundLooperDatabaseException("Mark with this ID is not persisted : " + mark.getDescription(),
						SoundLooperDatabaseException.ERROR_CODE_MARK_ID_DOES_NOT_EXISTS_IN_DATABASE);
			}

			Statement updateStatement = ConnectionFactory.getNewStatement();
			updateStatement.executeUpdate("DELETE FROM MARK WHERE ID = '" + mark.getId() + "'");

			if (updateStatement.getUpdateCount() != 1) {
				throw new SoundLooperDatabaseException("Delete mark query must update 1 row and it delete " + updateStatement.getUpdateCount() + " for the mark "
						+ mark.getDescription());
			}

			ConnectionFactory.commit();
			mark.setId(SoundLooperObject.ID_NOT_INITIALIZED);
			Song song = mark.getSong();
			song.getMarks().remove(mark.getName());
			return mark;
		} catch (SQLException e) {
			this.rollbackCurrentTransaction();
			throw new SoundLooperDatabaseException("Error when deleting mark", e);
		} catch (SoundLooperDatabaseException e) {
			this.rollbackCurrentTransaction();
			throw e;
		}
	}

//	@Override
//	public ArrayList<Mark> getList() throws SoundLooperException {
//		ArrayList<Mark> markList = new ArrayList<Mark>();
//		try {
//			// récupère la liste des chansons créées
//			ResultSet marksQuery = ConnectionFactory.getNewStatement().executeQuery("SELECT ID, name, begintime,endtime,id_song FROM mark");
//			while (marksQuery.next()) {
//				long id = marksQuery.getLong("id");
//				String name = marksQuery.getString("name");
//				int beginTime = marksQuery.getInt("begintime");
//				int endTime = marksQuery.getInt("endtime");
//				Song song = SongDAO.getInstance().getById(marksQuery.getLong("id"));
//
//				Mark mark = this.createNew();
//				mark.setId(id);
//				mark.setName(name);
//				mark.setBeginMillisecond(beginTime);
//				mark.setEndMillisecond(endTime);
//				mark.setSong(song);
//				markList.add(mark);
//			}
//		} catch (SQLException e) {
//			throw new SoundLooperDatabaseException("Error when get the marks list", e);
//		}
//		return markList;
//	}

	/**
	 * Get the list of marks for a song
	 * @param song the song
	 * @return the list of marks
	 * @throws SoundLooperException if an error occured
	 */
	public ArrayList<Mark> getList(Song song) throws SoundLooperException {
		ArrayList<Mark> markList = new ArrayList<Mark>();
		try {
			// récupère la liste des chansons créées
			ResultSet marksQuery = ConnectionFactory.getNewStatement().executeQuery(
					"SELECT ID, name, begintime,endtime,id_song FROM mark WHERE id_song=" + song.getId() + " ORDER BY begintime");
			while (marksQuery.next()) {
				long id = marksQuery.getLong("id");
				String name = marksQuery.getString("name");
				int beginTime = marksQuery.getInt("begintime");
				int endTime = marksQuery.getInt("endtime");

				Mark mark = this.createNew();
				mark.setId(id);
				mark.setBeginMillisecond(beginTime);
				mark.setEndMillisecond(endTime);
				mark.setSong(song);
				mark.setName(name);
				markList.add(mark);
			}
		} catch (SQLException e) {
			throw new SoundLooperDatabaseException("Error when get the marks list", e);
		}
		return markList;
	}
}
