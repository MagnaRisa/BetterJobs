package com.creedfreak.common.database.databaseConn;

import com.creedfreak.common.AbsConfigController;
import com.creedfreak.common.ICraftyProfessions;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite_Conn extends Database {

	private final String SQLITE_DB_NAME = "crafty_professions_SQLite.db";
	private final String SQLITE_TABLE_STMTS = "sql_files/sqlite_create_tables.sql";

	/**************************************************************************
	 * Constructor: SQLite_Conn
	 *
	 * Description: The primary constructor for an SQLite Connection
	 *
	 * Parameters:
	 * @param plugin - The plugin in order to grab the Logger
	 *
	 * Return: None
	 *************************************************************************/
	public SQLite_Conn (ICraftyProfessions plugin, AbsConfigController config) {
		super (plugin, config);
	}

	/**************************************************************************
	 * Method: dbConnect
	 *
	 * Description: This method will create and return a MySQL database
	 *              connection.
	 *
	 * Parameters: None
	 *
	 * Return:
	 * @return The connection to the database
	 *************************************************************************/
	public Connection dbConnect () {
		File dataFolder = new File (mPlugin.getResourceFile (), SQLITE_DB_NAME);

		if (!dataFolder.exists ()) {
			try {
				if (dataFolder.createNewFile ()) {
					mLogger.Info (Database.DATABASE_PREFIX,
							"database file not created, Creating it now...");
				}
			}
			catch (IOException e) {
				mLogger.Error (Database.DATABASE_PREFIX, "File write Error: " + SQLITE_DB_NAME);
			}
		}

		try {
			if (mConnection == null || mConnection.isClosed ()) {
				mConnection = DriverManager.getConnection ("jdbc:sqlite:"
						+ dataFolder);
			}

			return mConnection;
		}
		catch (SQLException e) {
			mLogger.Error (Database.DATABASE_PREFIX, "SQLite Exception on Initialization" + e);
		}

		return mConnection;
	}

	protected String getCreateTableStmts () {
		return SQLITE_TABLE_STMTS;
	}
}
