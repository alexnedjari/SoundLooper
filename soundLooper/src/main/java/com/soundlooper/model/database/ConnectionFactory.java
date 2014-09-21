/**
 *
 */
package com.soundlooper.model.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.soundlooper.system.preferences.SoundLooperProperties;

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
 * Provide method to manage connexion and get statements
 *
 * @author Alexandre NEDJARI
 * @since 28 juin 2011
 *
 *        ====================================================================
 */
public final class ConnectionFactory {

	/**
	 * Logger for this class
	 */
	private static Logger logger = Logger.getLogger(ConnectionFactory.class);

	/**
	 * Private constructor
	 */
	private ConnectionFactory() {
		//to avoid construction
	}

	/**
	 * The connexion
	 */
	private static Connection conn = null;

	/**
	 * Create a new statement (open connexion if needed
	 *
	 * @return the created new statement
	 * @throws SQLException
	 *             if a {@link SQLException} is threw
	 */
	public static Statement getNewStatement() throws SQLException {
		ConnectionFactory.logger.info("Demande nouveau statement");
		return ConnectionFactory.getConnection().createStatement();
	}

	/**
	 * get a prepared statement
	 * @param sql the sql
	 * @return the created statement
	 * @throws SQLException if a {@link SQLException} is threw
	 */
	public static PreparedStatement getNewPreparedStatement(String sql) throws SQLException {
		ConnectionFactory.logger.info("Demande préparation statement");
		return ConnectionFactory.getConnection().prepareStatement(sql);
	}

	/**
	 * Get the connexion (and open it on default database if needed)
	 *
	 * @return the connexion
	 * @throws SQLException
	 *             if an {@link SQLException} is threw
	 */
	private static Connection getConnection() throws SQLException {
		ConnectionFactory.logger.info("Demande nouvelle connexion jdbc:h2:data/datas");
		return ConnectionFactory.getConnection("jdbc:h2:data/datas");
	}

	/**
	 * Get the connexion (open it on the specified database if needed)
	 *
	 * @param databaseUrl
	 *            the database URL
	 * @return the connexion
	 * @throws SQLException
	 *             if an {@link SQLException} is threw
	 */
	public static Connection getConnection(String databaseUrl) throws SQLException {
		try {
			ConnectionFactory.logger.info("Demande connexion sur jdbc:h2:data/datas");
			if (ConnectionFactory.conn == null) {
				ConnectionFactory.logger.info("Create connection on " + databaseUrl);
				Class.forName("org.h2.Driver");
				ConnectionFactory.conn = DriverManager.getConnection(databaseUrl, "sa", "");
				ConnectionFactory.logger.info("Connection successfull on " + databaseUrl);
				ConnectionFactory.conn.setAutoCommit(false);
				//ConnectionFactory.executeScripts();
			}
			return ConnectionFactory.conn;
		} catch (ClassNotFoundException e) {
			throw new SQLException("Unable to create the connection", e);
		}
	}

	private static File getFile(String path) {
		URL resource = ConnectionFactory.class.getClassLoader().getResource(path);
		
		try {
			return new File(resource.toURI());
		} catch (URISyntaxException e) {
			logger.error("Unable to get resource '" + path + "'", e);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Update the db is needed
	 * @throws SQLException if an SQL Exception is threw
	 * @throws IOException If Exception is threw when acceding properties
	 */
	public static void updateDB() throws SQLException, IOException {
		ConnectionFactory.logger.info("Mise à jour de la base de données");
		if (!new File("data" + File.separator + "datas.h2.db").exists()) {
			//La base n'existe pas encore
			ConnectionFactory.logger.info("Création de la base de données");
			ConnectionFactory.executeScript(getFile("db/CREATE_TABLE_DB_UPDATE.sql"));
		}

		if (!SoundLooperProperties.getInstance().isDbToUpdate()) {
			ConnectionFactory.logger.info("La base de données n'a pas besoin d'être mise à jour");
			return;
		}
		ConnectionFactory.logger.info("La base de données doit être mise à jour");

		//Récupère la liste des scripts déjà exécutés
		Statement statement = ConnectionFactory.getNewStatement();
		ResultSet result = statement.executeQuery("SELECT filename FROM db_update");
		final List<File> listeFichierExecute = new ArrayList<File>();
		while (result.next()) {
			String nomFichier = result.getString("filename");
			ConnectionFactory.logger.info("le fichier '" + nomFichier + "' a déjà été exécuté");
			listeFichierExecute.add(getFile("db" + File.separator + nomFichier));
		}

		//Récupère la liste des fichiers à exécuter
		File[] sqlFiles = getFile("db").listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".sql") && !listeFichierExecute.contains(pathname);
			}
		});

		for (File file : sqlFiles) {
			ConnectionFactory.executeScript(file);
		}
		ConnectionFactory.logger.info("Commite les modifications");
		ConnectionFactory.commit();

		ConnectionFactory.logger.info("Met à jour les propriétés");
		SoundLooperProperties.getInstance().setDbToUpdate(false);
		SoundLooperProperties.getInstance().save();
		
	}


	/**
	 * Execute a script from a file
	 *
	 * @param scriptFile the file name
	 * @throws SQLException if an {@link SQLException} is threw
	 */
	private static void executeScript(File scriptFile) throws SQLException {
		try {
			ConnectionFactory.logger.info("Execute script : " + scriptFile);
			BufferedReader reader = new BufferedReader(new FileReader(scriptFile));
			try {
				StringBuffer completeQuery = new StringBuffer();
				String line = reader.readLine();
				while (line != null) {
					completeQuery.append(line);
					completeQuery.append('\n');
					line = reader.readLine();
				}
				ConnectionFactory.logger.info("Requête à exécuter : " + completeQuery);
				Statement statement = ConnectionFactory.getNewStatement();
				ConnectionFactory.logger.info("Exécute la requête");
				statement.execute(completeQuery.toString());
				ConnectionFactory.logger.info("Requête exécutée, mise à jour de db_update");
				statement.execute("INSERT INTO db_update (filename, dateexecution) VALUES ('" + scriptFile.getName() + "' , '" + SoundLooperDAO.getFormatedDate(new Date()) + "')");

				ConnectionFactory.logger.info("db_update à jour");
			} finally {
				reader.close();
			}
		} catch (IOException e) {
			ConnectionFactory.logger.info("Erreur à l'exécution : " + e.toString());
			throw new SQLException("Unable to create the connection", e);
		}

	}

	/**
	 * Make a commit on the connexion
	 *
	 * @throws SQLException
	 *             if a {@link SQLException} is threw
	 */
	public static void commit() throws SQLException {
		ConnectionFactory.getConnection().commit();

	}

	/**
	 * Make a rollback on the connexion
	 *
	 * @throws SQLException
	 *             if a {@link SQLException} is threw
	 */
	public static void rollback() throws SQLException {
		ConnectionFactory.getConnection().rollback();

	}

	/**
	 * close the connexion and prepare it for a new init
	 *
	 * @throws SQLException
	 *             if a {@link SQLException} is threw
	 */
	public static void close() throws SQLException {
		ConnectionFactory.getConnection().close();
		ConnectionFactory.conn = null;
	}
}
