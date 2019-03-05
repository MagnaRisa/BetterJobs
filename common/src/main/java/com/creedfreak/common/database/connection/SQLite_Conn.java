package com.creedfreak.common.database.connection;

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

	private Connection mConnection;

	/**
	 * The default constructor.
	 *
	 * @param plugin The plugin to retrieve resources from.
	 */
	public SQLite_Conn (ICraftyProfessions plugin, AbsConfigController config) {
		super (plugin, config);
	}

	/**
	 * This method fetches the singular database connection for SQLite.
	 *
	 * @return The connection to the database
	 */
	public synchronized Connection dbConnect () {
		File dataFolder = new File (mPlugin.getResourceFile (), SQLITE_DB_NAME);

		if (!dataFolder.exists ()) {
			try {
				if (dataFolder.createNewFile ()) {
					mLogger.Info (Database.DATABASE_PREFIX,
							"Database file not created, creating it now...");
				}
			}
			catch (IOException except) {
				mLogger.Error (Database.DATABASE_PREFIX, "File write error " + SQLITE_DB_NAME + ": "
						+ except.getMessage ());
			}
		}

		try {
			if (mConnection == null || mConnection.isClosed ()) {
				mConnection = DriverManager.getConnection ("jdbc:sqlite:" + dataFolder);
			}

			return mConnection;
		}
		catch (SQLException except) {
			mLogger.Error (Database.DATABASE_PREFIX, "Could not fetch SQLite connection: " + except.getMessage ());
		}

		return mConnection;
	}
	
	public void shutdown () {
		try {
			if (mConnection != null) {
				if (!mConnection.isClosed ()) {
					mConnection.close ();
				}
			}
		} catch (SQLException except) {
			mLogger.Error (Database.DATABASE_PREFIX, "Something went wrong while disabling the database: " + except.getMessage ());
		}
	}

	/**
	 * Retrieves the create table statements of this type of database driver.
	 */
	protected String getCreateTableStmts () {
		return SQLITE_TABLE_STMTS;
	}
}
