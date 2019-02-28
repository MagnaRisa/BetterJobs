package com.creedfreak.common.database.connection;

import com.creedfreak.common.AbsConfigController;
import com.creedfreak.common.ICraftyProfessions;
import com.creedfreak.common.utility.Logger;
import com.creedfreak.common.utility.SQLReader;
import com.creedfreak.common.utility.TimeUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

public abstract class Database {

	static final String DATABASE_PREFIX = "Database";
	private static final String SQL_INSERT_STATEMENTS = "sql_files/insert_stmts.sql";
	private static final String SQL_CREATE_STMT = "CREATE TABLE IF NOT EXISTS";
	private static final String SQL_INSERT_STMT = "INSERT INTO";

	static Logger mLogger = Logger.Instance ();
	final ICraftyProfessions mPlugin;
	final AbsConfigController mConfiguration;

	private int mNumTables;
	private int mNumInsertsRan;
	private Double mTotalTimeElapsed;

	/**
	 * The primary constructor for a database connection.
	 *
	 * @param plugin The plugin to obtain resources from.
	 * @param config The configurations of the plugin.
	 */
	public Database (ICraftyProfessions plugin, AbsConfigController config) {
		mPlugin = plugin;
		mConfiguration = config;

		mNumTables = 0;
		mNumInsertsRan = 0;
		mTotalTimeElapsed = 0D;
	}

	/**
	 * Returns a connection to the database from the internal connection pool.
	 *
	 * @return The connection to the database.
	 */
	public abstract Connection dbConnect ();

	/**
	 * Retrieves the implementation specific create tables file name.
	 *
	 * @return The connection specific create table statement file name.
	 */
	protected abstract String getCreateTableStmts ();

	/**
	 * The method will close the connection/s of the database.
	 */
	public abstract void dbClose ();

	/**
	 * Closes the prepared statement and result set passed in.
	 *
	 * @param stmt The statement to close.
	 * @param set The result set to close.
	 */
	public static void dbCloseResources (PreparedStatement stmt, ResultSet set) {
		try {
			if (set != null) {
				set.close ();
			}
		}
		catch (SQLException exception) {
			mLogger.Error (DATABASE_PREFIX, "Failed to close result set " + exception.getMessage ());
		}

		try {
			if (stmt != null) {
				stmt.close ();
			}
		}
		catch (SQLException exception) {
			mLogger.Error (DATABASE_PREFIX, "Failed to close prepared statement " + exception.getMessage ());
		}
	}

	/**
	 * Closes the prepared statement passed in.
	 *
	 * @param stmt The statement to close.
	 */
	public static void dbCloseResources (PreparedStatement stmt) {
		try {
			if (stmt != null) {
				stmt.close ();
			}
		}
		catch (SQLException exception) {
			mLogger.Error (DATABASE_PREFIX, "Failed to close prepared statement " + exception.getMessage ());
		}
	}

	/**
	 * This method will setup the database if it has not been setup. If any errors
	 *  occur while executing this method, false will be returned to notify
	 *  the plugin to be disabled.
	 *
	 * @return true or false depending on if the database has been initialized with no errors.
	 * */
	public boolean initializeDatabase () {
		boolean createSuccess, insertSuccess, retVal = false;
		long initialTime = System.nanoTime ();
		DecimalFormat timeFormat = new DecimalFormat ("#0.00");

		if (!checkDBExists ()) {
			createSuccess = createTables ();

			if (createSuccess) {
				mLogger.Info (DATABASE_PREFIX, "Total number of create table statements ran: " + mNumTables);

				insertSuccess = insertIntoTables ();

				if (insertSuccess) {
					mLogger.Info (DATABASE_PREFIX, "The database has been created and the required data has been inserted!");
				}
				retVal = insertSuccess;
			}
			mTotalTimeElapsed = TimeUtil.toSeconds (System.nanoTime () - initialTime);
			mLogger.Info (DATABASE_PREFIX, "Total time elapsed for database construction: " + timeFormat.format (mTotalTimeElapsed) + "sec");
		} else {
			mLogger.Info (DATABASE_PREFIX, "Database found! Setup not necessary.");
			retVal = true;
		}

		return retVal;
	}

	/**
	 * This method will create the database tables/entities.
	 */
	protected boolean createTables () {
		Connection connection = this.dbConnect ();
		SQLReader reader = new SQLReader ();
		PreparedStatement statement = null;
		boolean retVal;
		String sqlStmt;

		try {
			reader.openReader (mPlugin.openResource (this.getCreateTableStmts ()));

			sqlStmt = reader.readStatement ();

			while (!sqlStmt.equals (SQLReader.EOF)) {
				statement = connection.prepareStatement (sqlStmt);
				statement.execute ();

				// executeStatement (sqlStmt, connection);
				if (sqlStmt.contains (SQL_CREATE_STMT)) {
					mNumTables++;
				}
				sqlStmt = reader.readStatement ();
			}
			retVal = true;
		}
		catch (IOException | SQLException exception) {
			mLogger.Error (DATABASE_PREFIX, "Could not create database tables: " + exception.getMessage ());
			retVal = false;
		}
		finally {
			dbCloseResources (statement);
			dbClose ();
			reader.closeReader ();
		}
		return retVal;
	}

	/**
	 * This method will insert the needed data into the database.
	 *
	 * @return True or false depending of if the operation was successful.
	 */
	private boolean insertIntoTables () {
		boolean retVal;
		Connection connection = this.dbConnect ();
		SQLReader reader = new SQLReader ();
		PreparedStatement statement = null;
		String sqlStmt;

		try {
			reader.openReader (mPlugin.openResource (SQL_INSERT_STATEMENTS));

			sqlStmt = reader.readStatement ();

			while (!sqlStmt.equals (SQLReader.EOF)) {
				if (sqlStmt.contains (SQL_INSERT_STMT)) {
					mNumInsertsRan++;
				}
				statement = connection.prepareStatement (sqlStmt);
				statement.execute ();

				sqlStmt = reader.readStatement ();
			}
			retVal = true;
			mLogger.Info (DATABASE_PREFIX, "Total number of Insertion statements ran: " + mNumInsertsRan);
		}
		catch (IOException | SQLException exception) {
			mLogger.Error (DATABASE_PREFIX, "Couldn't insert initialization data into the database. Reason: " + exception.getMessage ());
			retVal =  false;
		}
		finally {
			dbCloseResources (statement);
			dbClose ();
			reader.closeReader ();
		}
		return retVal;
	}

	/**
	 * This method will check to see if the database tables are already setup.
	 *  If the database is already setup the database won't be reinitialized.
	 *
	 * @return True or false depending of if the database exists or not.
	 * */
	private boolean checkDBExists () {
		boolean tablesExist = false;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection conn = this.dbConnect ();
		String sqlQuery =
				"SELECT SettingName, Active " +
				"FROM Settings " +
				"WHERE SettingName = 'Setup'";

		try {
			statement = conn.prepareStatement (sqlQuery);
			resultSet = statement.executeQuery ();
			tablesExist = resultSet.next ();
		}
		catch (SQLException exception) {
			mLogger.Info (DATABASE_PREFIX, "Could not locate tables! Database has not been created yet, this is normal.");
		}
		finally {
			dbCloseResources (statement, resultSet);
			dbClose ();
		}
		return tablesExist;
	}
}