/**
 *
 */
package com.soundlooper.model.database;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.soundlooper.exception.SoundLooperRuntimeException;
import com.soundlooper.system.preferences.SoundLooperProperties;

/**
 * ==================================================================== Sound
 * Looper is an audio player that allow user to loop between two points
 * Copyright (C) 2014 Alexandre NEDJARI
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
	private static Logger logger = LogManager.getLogger(ConnectionFactory.class);

	/**
	 * Private constructor
	 */
	private ConnectionFactory() {
		// to avoid construction
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
	public static Statement getNewStatement() {
		ConnectionFactory.logger.info("New statement request");
		try {
			return ConnectionFactory.getConnection().createStatement();
		} catch (SQLException e) {
			throw new SoundLooperRuntimeException("Unable to get new statement", e);
		}
	}

	/**
	 * get a prepared statement
	 * 
	 * @param sql
	 *            the sql
	 * @return the created statement
	 * @throws SQLException
	 *             if a {@link SQLException} is threw
	 */
	public static PreparedStatement getNewPreparedStatement(String sql) {
		ConnectionFactory.logger.info("Prepared statement request");
		try {
			return ConnectionFactory.getConnection().prepareStatement(sql);
		} catch (SQLException e) {
			throw new SoundLooperRuntimeException("Unable to get prepare statement", e);
		}
	}

	/**
	 * Get the connexion (and open it on default database if needed)
	 *
	 * @return the connexion
	 * @throws SQLException
	 *             if an {@link SQLException} is threw
	 */
	private static Connection getConnection() {
		ConnectionFactory.logger.info("new connexion request on jdbc:h2:data/datas");
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
	public static Connection getConnection(String databaseUrl) {
		try {
			ConnectionFactory.logger.info("Demande connexion sur jdbc:h2:data/datas");
			if (ConnectionFactory.conn == null) {
				ConnectionFactory.logger.info("Create connection on " + databaseUrl);
				Class.forName("org.h2.Driver");
				ConnectionFactory.conn = DriverManager.getConnection(databaseUrl, "sa", "");
				ConnectionFactory.logger.info("Connection successfull on " + databaseUrl);
				ConnectionFactory.conn.setAutoCommit(false);
				// ConnectionFactory.executeScripts();
			}
			return ConnectionFactory.conn;
		} catch (ClassNotFoundException | SQLException e) {
			throw new SoundLooperRuntimeException("Unable to create the database connexion", e);
		}
	}

	/**
	 * Update the db is needed
	 * 
	 * @throws SQLException
	 *             if an SQL Exception is threw
	 * @throws IOException
	 *             If Exception is threw when acceding properties
	 */
	public static void updateDB() {
		try {
			ConnectionFactory.logger.info("Update database");

			boolean isDatabaseExists = new File("data" + File.separator + "datas.h2.db").exists();
			if (!isDatabaseExists) {
				// La base n'existe pas encore
				ConnectionFactory.logger.info("Database creation");

				String createTableScriptName = "CREATE_TABLE_DB_UPDATE.sql";
				URL createTableScript = ConnectionFactory.class.getClassLoader().getResource(
						"db/" + createTableScriptName);

				ConnectionFactory.executeScript(createTableScriptName, createTableScript.openConnection()
						.getInputStream());

			}

			if (!SoundLooperProperties.getInstance().isDbToUpdate()) {
				ConnectionFactory.logger.info("The database doesn't need update");
				return;
			}
			ConnectionFactory.logger.info("The database need update");

			// Récupère la liste des scripts déjà exécutés
			Statement statement = ConnectionFactory.getNewStatement();
			ResultSet result = statement.executeQuery("SELECT filename FROM db_update");
			final List<String> listeFichierExecute = new ArrayList<String>();
			while (result.next()) {
				String nomFichier = result.getString("filename");
				ConnectionFactory.logger.info("File '" + nomFichier + "' was already executed");
				listeFichierExecute.add(new File(nomFichier).getName());
			}

			URL resource = ConnectionFactory.class.getClassLoader().getResource("db");

			if (StringUtils.startsWith(resource.toString(), "jar:")) {
				try {
					JarURLConnection connection = (JarURLConnection) resource.toURI().toURL().openConnection();
					JarFile jarFile = connection.getJarFile();
					Enumeration<JarEntry> entries = jarFile.entries();
					while (entries.hasMoreElements()) {
						JarEntry jarEntry = entries.nextElement();
						String fullName = jarEntry.getName();
						if (fullName.matches("db/.*sql")) {
							String name = fullName.substring(fullName.lastIndexOf("/") + 1);
							if (!listeFichierExecute.contains(name)) {
								executeScript(name, jarFile.getInputStream(jarEntry));
							}
						}
					}
				} catch (URISyntaxException e) {
					logger.error(e);
					throw new SoundLooperRuntimeException("Unable to transform URL to URI : " + resource, e);
				}
			} else {
				// We re not in a jar, developement environment
				// Récupère la liste des fichiers à exécuter
				File file;
				try {
					file = new File(resource.toURI());
					File[] sqlFiles = file.listFiles(new FileFilter() {
						@Override
						public boolean accept(File pathname) {
							return pathname.getName().endsWith(".sql")
									&& !listeFichierExecute.contains(pathname.getName());
						}
					});

					for (File sqlFile : sqlFiles) {
						ConnectionFactory.executeScript(sqlFile);
					}
				} catch (URISyntaxException e) {
					logger.error(e);
					throw new SoundLooperRuntimeException("Unable to transform URL to URI : " + resource, e);
				}
			}

			ConnectionFactory.logger.info("Commit changes");
			ConnectionFactory.commit();

			ConnectionFactory.logger.info("Update properties (set db to update to false)");
			SoundLooperProperties.getInstance().setDbToUpdate(false);
			SoundLooperProperties.getInstance().save();
		} catch (SQLException | IOException e) {
			throw new SoundLooperRuntimeException("Unable to update the database", e);
		}

	}

	private static void executeScript(File file) {
		try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file))) {
			executeScript(file.getName(), stream);
		} catch (IOException e) {
			throw new SoundLooperRuntimeException("Unable to execute script '" + file.getAbsolutePath() + "'", e);
		}
	}

	/**
	 * Execute a script from a file
	 *
	 * @param scriptFile
	 *            the file name
	 * @throws SQLException
	 *             if an {@link SQLException} is threw
	 */
	private static void executeScript(String fileName, InputStream stream) {
		try {
			ConnectionFactory.logger.info("Execute script : " + fileName);

			String completeQuery = IOUtils.toString(stream, Charset.forName("UTF-8"));

			ConnectionFactory.logger.info("Query to execute : " + completeQuery);
			Statement statement = ConnectionFactory.getNewStatement();
			ConnectionFactory.logger.info("Query execution...");
			statement.execute(completeQuery.toString());
			ConnectionFactory.logger.info("Query executed, update table db_update");
			statement.execute("INSERT INTO db_update (filename, dateexecution) VALUES ('" + fileName + "' , '"
					+ SoundLooperDAO.getFormatedDate(new Date()) + "')");

			ConnectionFactory.logger.info("db_update updated");
		} catch (IOException | SQLException e) {
			throw new SoundLooperRuntimeException("Unable to execute script " + fileName, e);
		}

	}

	/**
	 * Make a commit on the connexion
	 *
	 * @throws SQLException
	 *             if a {@link SQLException} is threw
	 */
	public static void commit() {
		try {
			ConnectionFactory.getConnection().commit();
		} catch (SQLException e) {
			throw new SoundLooperRuntimeException("Unable to commit transaction");
		}
	}

	/**
	 * Make a rollback on the connexion
	 *
	 * @throws SQLException
	 *             if a {@link SQLException} is threw
	 */
	public static void rollback() {
		try {
			ConnectionFactory.getConnection().rollback();
		} catch (SQLException e) {
			throw new SoundLooperRuntimeException("Unable to rollback transaction");
		}

	}

	/**
	 * close the connexion and prepare it for a new init
	 *
	 * @throws SQLException
	 *             if a {@link SQLException} is threw
	 */
	public static void close() {
		try {
			ConnectionFactory.getConnection().close();
			ConnectionFactory.conn = null;
		} catch (SQLException e) {
			throw new SoundLooperRuntimeException("Unable to close connection");
		}
	}
}
