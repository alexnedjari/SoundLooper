/**
 *
 */
package com.soundlooper.model.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.soundlooper.exception.SoundLooperDatabaseException;
import com.soundlooper.model.SoundLooperObject;
import com.soundlooper.model.mark.Mark;
import com.soundlooper.model.song.Song;

/**
 * ====================================================================
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
 * Provide methods to managed marks in database
 *
 * @author Alexandre NEDJARI
 * @since 28 juin 2011
 *
 * ====================================================================
 */
public final class MarkDatabaseAccess {

	/**
	 * The instance
	 */
	private static MarkDatabaseAccess instance;

	/**
	 * Get the instance
	 *
	 * @return the instance
	 */
	public static MarkDatabaseAccess getInstance() {
		if (MarkDatabaseAccess.instance == null) {
			MarkDatabaseAccess.instance = new MarkDatabaseAccess();
		}
		return MarkDatabaseAccess.instance;
	}

	/**
	 * Constructor
	 */
	private MarkDatabaseAccess() {
		// private to avoid construction
	}

	/**
	 * Create a new mark in the song
	 *
	 * @param mark
	 *            the mark to persist
	 * @throws SoundLooperDatabaseException
	 *             if a {@link SoundLooperDatabaseException} is threw
	 * @throws SQLException
	 *             if a {@link SQLException} is threw
	 */
	public void createNewMark(Mark mark) throws SoundLooperDatabaseException, SQLException {
		Song song = mark.getSong();
		if (song.getId() == SoundLooperObject.ID_NOT_INITIALIZED) {
			throw new SoundLooperDatabaseException("Cannot persist a mark of an unpersited song", SoundLooperDatabaseException.ERROR_CODE_SONG_ID_DOES_NOT_EXISTS_IN_DATABASE);
		}

		// check that a mark with this name does not exists in the database for
		// this song
		try {
			// Check that there is no marks with this name
			ResultSet checkQuery = ConnectionFactory.getNewStatement().executeQuery("SELECT ID FROM MARK WHERE NAME = '" + mark.getName() + "' AND SONG_ID=" + song.getId());
			if (checkQuery.next()) {
				// there is already a song for this path
				throw new SoundLooperDatabaseException("Mark with this name already exists in database for the song : " + song.getDescription() + "\nThe mark is : "
						+ mark.getDescription(), SoundLooperDatabaseException.ERROR_CODE_MARK_NAME_ALREADY_EXISTS_FOR_THIS_SONG);
			}

			// insert mark in the song
			Statement updateStatement = ConnectionFactory.getNewStatement();
			updateStatement.executeUpdate("INSERT INTO MARK (SONG_ID, BEGIN_TIME, END_TIME, NAME) VALUES(" + song.getId() + ", " + mark.getBeginMillisecond() + ", "
					+ mark.getEndMillisecond() + ", '" + mark.getName() + "')");

			ResultSet generatedKeys = updateStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				mark.setId(generatedKeys.getLong(1));
			} else {
				throw new SoundLooperDatabaseException("No ID generated when persinting song : " + song.getDescription(),
						SoundLooperDatabaseException.ERROR_CODE_NO_ID_GENERATED_FOR_NEW_MARK);
			}

			ConnectionFactory.commit();
		} catch (SQLException e) {
			ConnectionFactory.rollback();
			throw e;
		} catch (SoundLooperDatabaseException e) {
			ConnectionFactory.rollback();
			throw e;
		}
	}

	/**
	 * Update a mark
	 *
	 * @param mark
	 *            the mark to update
	 * @throws SQLException
	 *             if an {@link SQLException} is threw
	 * @throws SoundLooperDatabaseException
	 *             if a {@link SoundLooperDatabaseException} is threw
	 */
	public void updateMark(Mark mark) throws SQLException, SoundLooperDatabaseException {
		try {
			// check that the mark with this ID exists
			ResultSet checkQueryID = ConnectionFactory.getNewStatement().executeQuery("SELECT ID FROM MARK WHERE ID = '" + mark.getId() + "'");
			if (!checkQueryID.next()) {
				// there is no song with this ID
				throw new SoundLooperDatabaseException("Mark with this ID is not persisted : " + mark.getDescription(),
						SoundLooperDatabaseException.ERROR_CODE_MARK_ID_DOES_NOT_EXISTS_IN_DATABASE);
			}

			// check that the name of the updated mark is not already used in
			// this song
			ResultSet checkQueryPath = ConnectionFactory.getNewStatement().executeQuery(
					"SELECT ID FROM MARK WHERE ID != '" + mark.getId() + "' AND NAME='" + mark.getName() + "' AND SONG_ID=" + mark.getSong().getId());
			if (checkQueryPath.next()) {
				// There is a mark that have already the new name
				throw new SoundLooperDatabaseException("Mark with this name already exists in database : " + mark.getDescription() + " for the song "
						+ mark.getSong().getDescription(), SoundLooperDatabaseException.ERROR_CODE_MARK_NAME_ALREADY_EXISTS_FOR_THIS_SONG);
			}

			// update the mark attributes for this song
			Statement updateStatement = ConnectionFactory.getNewStatement();
			updateStatement.executeUpdate("UPDATE MARK SET NAME='" + mark.getName() + "', BEGIN_TIME=" + mark.getBeginMillisecond() + ", END_TIME=" + mark.getEndMillisecond()
					+ " WHERE ID = '" + mark.getId() + "'");

			if (updateStatement.getUpdateCount() != 1) {
				throw new SoundLooperDatabaseException("Update mark query must update 1 row and it update " + updateStatement.getUpdateCount() + " for the mark "
						+ mark.getDescription());
			}
			ConnectionFactory.commit();

		} catch (SQLException e) {
			ConnectionFactory.rollback();
			throw e;
		} catch (SoundLooperDatabaseException e) {
			ConnectionFactory.rollback();
			throw e;
		}
	}

	/**
	 * Delete a mark
	 *
	 * @param mark
	 *            the mark to delete
	 * @throws SoundLooperDatabaseException
	 *             if a {@link SoundLooperDatabaseException} is threw
	 * @throws SQLException
	 *             if a {@link SQLException} is threw
	 */
	public void deleteMark(Mark mark) throws SoundLooperDatabaseException, SQLException {
		try {
			// check that the mark with this ID exists
			ResultSet checkQueryID = ConnectionFactory.getNewStatement().executeQuery("SELECT ID FROM MARK WHERE ID = '" + mark.getId() + "'");
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
		} catch (SQLException e) {
			ConnectionFactory.rollback();
			throw e;
		} catch (SoundLooperDatabaseException e) {
			ConnectionFactory.rollback();
			throw e;
		}
	}

	//	/**
	//	 * init the mark list in a song
	//	 * @param song the song
	//	 * @throws SQLException if an {@link SQLException} is threw
	//	 * @throws SoundLooperDatabaseException if a {@link SoundLooperDatabaseException} is threw
	//	 * @throws SoundLooperObjectAlreadyExistsException If a mark is duplicated in the song
	//	 */
	//	public void initMarksList(Song song) throws SQLException, SoundLooperDatabaseException, SoundLooperObjectAlreadyExistsException {
	//		// Get list of marks for a song
	//		ResultSet marksQuery = ConnectionFactory.getNewStatement().executeQuery("SELECT ID, NAME, BEGIN_TIME, END_TIME FROM MARK WHERE SONG_ID=" + song.getId());
	//		while (marksQuery.next()) {
	//			long id = marksQuery.getLong("ID");
	//			long beginTime = marksQuery.getLong("BEGIN_TIME");
	//			long endTime = marksQuery.getLong("END_TIME");
	//			String name = marksQuery.getString("NAME");
	//
	//			Mark mark = new Mark(song, name, beginTime, endTime);
	//			mark.setId(id);
	//		}
	//
	//	}
}
