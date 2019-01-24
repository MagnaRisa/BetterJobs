package com.creedfreak.common.database.databaseConn;

import com.creedfreak.common.AbsConfigController;
import com.creedfreak.common.ICraftyProfessions;
import com.creedfreak.common.utility.Logger;
import com.creedfreak.common.utility.SQLReader;
import com.creedfreak.common.utility.TimeUtil;

import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;

public abstract class Database
{

	public static final String DATABASE_PREFIX = "database";
	private static final String SQL_INSERT_STATEMENTS = "sql_files/insert_stmts.sql";
	private static final String SQL_CREATE_STMT = "CREATE TABLE IF NOT EXISTS";
	private static final String SQL_INSERT_STMT = "INSERT INTO";

	protected Logger mLogger;
	protected Connection mConnection;
	protected ICraftyProfessions mPlugin;
	protected AbsConfigController mConfiguration;

	private int mNumTables;
	private int mNumInsertsRan;
	private Double mTotalTimeElapsed;

	/**************************************************************************
	 * Constructor: database
	 *
	 * Description: The primary constructor for a database connection
	 *
	 * Parameters:
	 * @param plugin - The plugin the database interfaces with.
	 *
	 * Return:      None
	 *************************************************************************/
	public Database (ICraftyProfessions plugin, AbsConfigController config)
	{
		mLogger = Logger.Instance ();

		mPlugin = plugin;
		mConfiguration = config;

		mNumTables = 0;
		mNumInsertsRan = 0;
		mTotalTimeElapsed = 0D;
	}

	/**************************************************************************
	 * Method:      dbConnect
	 *
	 * Description: The common method that all extensions of this database
	 *              abstract object will need to implement.
	 *
	 * Parameters:  None
	 *
	 * Return:      The Specific connection of the implemented database.
	 *************************************************************************/
	public abstract Connection dbConnect ();

	/**************************************************************************
	 * Method:      getCreateTableStmts
	 *
	 * Description: Retrieves the implementation specific create tables
	 *              file name
	 *
	 * Parameters: None
	 *
	 * Return:
	 * @return The connection specific create table statement file name.
	 *************************************************************************/
	protected abstract String getCreateTableStmts ();

	/**************************************************************************
	 * Method:      dbClose
	 *
	 * Description: The method will close the prep statement and result set
	 *              passed into the method. If either of these are already
	 *              null, then they will not be closed.
	 *
	 * Parameters:  None
	 *
	 * Return:      None
	 *************************************************************************/
	public void dbClose ()
	{
		try
		{
			if (mConnection != null)
			{
				mConnection.close ();
			}
		}
		catch (SQLException exception)
		{
			mLogger.Warn (DATABASE_PREFIX, "Could not close database Connection: " + exception.getMessage ());
		}
	}

	/**************************************************************************
	 * Method: dbCloseResources
	 *
	 * Description: Closes the prepared statement and result set
	 *                          passed in.
	 *
	 * Parameters:
	 * @param stmt - The statement to close
	 * @param set - The result set to close
	 *
	 * Return: None
	 *************************************************************************/
	public void dbCloseResources (PreparedStatement stmt, ResultSet set)
	{
		try
		{
			if (set != null)
			{
				set.close ();
			}
		}
		catch (SQLException exception)
		{
			mLogger.Error (DATABASE_PREFIX, "Failed to close SQL ResultSet " + exception.getMessage ());
		}

		try
		{
			if (stmt != null)
			{
				stmt.close ();
			}
		}
		catch (SQLException exception)
		{
			mLogger.Error (DATABASE_PREFIX, "Failed to close SQL Prep Statment " + exception.getMessage ());
		}
	}

	/**************************************************************************
	 * Method: dbCloseResources
	 *
	 * Description: Closes the prepared statement passed in.
	 *
	 * Parameters:
	 * @param stmt - The statement to close
	 *
	 * Return: None
	 *************************************************************************/
	public void dbCloseResources (PreparedStatement stmt)
	{
		try
		{
			if (stmt != null)
			{
				stmt.close ();
			}
		}
		catch (SQLException exception)
		{
			mLogger.Error (DATABASE_PREFIX, "Failed to close SQL Prep Statement " + exception.getMessage ());
		}
	}

	/**************************************************************************
	 * Method:      initializeDatabase
	 *
	 * Description: This method will setup the entire database if it has
	 *              already not been setup. If any errors occur while
	 *              executing this method we will return false to notify
	 *              the main plugin class that we need to disable the plugin.
	 *
	 * Parameters:  None
	 *
	 * Return:
	 * @return True  - If the database gets initialized successfully.
	 *         False - If the database initialization fails at some point.
	 *************************************************************************/
	public boolean initializeDatabase ()
	{
		boolean createSuccess, insertSuccess, retVal = false;
		long initialTime = System.nanoTime ();
		DecimalFormat timeFormat = new DecimalFormat ("#0.00");

		if (!checkDBExists ())
		{
			createSuccess = createTables ();

			if (createSuccess)
			{
				mLogger.Info (DATABASE_PREFIX, "Total number of Create Table statements ran: " + mNumTables);

				insertSuccess = insertIntoTables ();

				if (insertSuccess)
				{
					mLogger.Info (DATABASE_PREFIX, "database has been created and the required data has been inserted!");
				}

				// This automatically means that createSuccess was true.
				retVal = insertSuccess;
			}

			mTotalTimeElapsed = TimeUtil.toSeconds (System.nanoTime () - initialTime);
			mLogger.Info (DATABASE_PREFIX, "Total time elapsed for database Construction: " + timeFormat.format (mTotalTimeElapsed) + "sec");
		}
		else
		{
			mLogger.Info (DATABASE_PREFIX, "database found! Setup not necessary");
			retVal = true;
		}

		return retVal;
	}

	/**************************************************************************
	 * Method:      createTables
	 *
	 * Description: This method will read the CreateTables.sql file one
	 *              SQL statement at a time and then execute that statement
	 *              until the Crafty Professions database is setup.
	 *
	 * Parameters:  None
	 *
	 * Return:      None
	 *************************************************************************/
	protected boolean createTables ()
	{
		Connection connection = dbConnect ();
		SQLReader reader = new SQLReader ();
		String sqlStmt;

		try
		{
			reader.openReader (mPlugin.openResource (this.getCreateTableStmts ()));

			sqlStmt = reader.readStatement ();

			while (!sqlStmt.equals (SQLReader.EOF))
			{
				executeStatement (sqlStmt, connection);
				if (sqlStmt.contains (SQL_CREATE_STMT))
				{
					mNumTables++;
				}
				sqlStmt = reader.readStatement ();
			}
		}
		catch (IOException | SQLException exception)
		{
			mLogger.Error (DATABASE_PREFIX, "Could not Create database Tables: " + exception);
			return false;
		}
		catch (Exception exception)
		{
			mLogger.Error (DATABASE_PREFIX, "Unhandled Exception: " + exception);
			return false;
		}
		finally
		{
			dbClose ();
			reader.closeReader ();
		}
		return true;
	}

	/**************************************************************************
	 * Method:      insertIntoTables
	 *
	 * Description: This method will insert the initialization data into the
	 *              database. This will allow the plugin to start handling
	 *              users when they log into the server.
	 *
	 * Parameters:  None
	 *
	 * Return:
	 * @return True  - If the table insertions succeed
	 *         False - If the table insertions fail
	 *************************************************************************/
	private boolean insertIntoTables ()
	{
		Connection connection = dbConnect ();
		SQLReader reader = new SQLReader ();
		String sqlStmt;

		try
		{
			reader.openReader (mPlugin.openResource (SQL_INSERT_STATEMENTS));

			sqlStmt = reader.readStatement ();

			while (!sqlStmt.equals (SQLReader.EOF))
			{
				if (sqlStmt.contains (SQL_INSERT_STMT))
				{
					mNumInsertsRan++;
				}
				executeStatement (sqlStmt, connection);
				sqlStmt = reader.readStatement ();
			}
		}
		catch (IOException | SQLException exception)
		{
			mLogger.Error (DATABASE_PREFIX, "Could Not Insert Initialization Data Into database: " + exception);
			return false;
		}
		finally
		{
			dbClose ();
			reader.closeReader ();
			mLogger.Info (DATABASE_PREFIX, "Total number of Insertion statements ran: " + mNumInsertsRan);
	}
		return true;
	}

	/**************************************************************************
	 *  TODO This method needs to be updated to use the settings table from the DB
	 *  Method:      checkDBExists
	 *
	 * Description: This method will check to see if the Crafty professions
	 *              tables are already setup. If they are then we will not
	 *              rerun the database setup Queries.
	 *
	 * Parameters:  None
	 *
	 * Return:
	 * @return True -  If the tables were created.
	 *         False - If the tables were already initialized.
	 *************************************************************************/
	private boolean checkDBExists ()
	{
		boolean bTablesExist, test;
		Connection conn = dbConnect ();
		String sqlQuery = "SELECT ProfessionID FROM Professions";

		try
		{
			PreparedStatement prepStmt = conn.prepareStatement (sqlQuery);
			ResultSet rSet = prepStmt.executeQuery ();

			bTablesExist = rSet.next ();

			dbCloseResources (prepStmt, rSet);
		}
		catch (SQLException exception)
		{
			mLogger.Error (DATABASE_PREFIX, "Could not locate tables within checkDBExists! Defaulting return type to false. State: " + exception.getSQLState ());
			return false;
		}
		finally
		{
			dbClose ();
		}
		return bTablesExist;
	}

	/**************************************************************************
	 * Method:      executeStatement
	 *
	 * Description: This method executes sql statements that do not have a
	 *              return value.
	 *
	 * Exceptions:
	 * @throws SQLException - If an error occurs here we are throwing the
	 *                        exception to be handled where this method
	 *                        is called.
	 *
	 * Parameters:
	 * @param sqlStatement - An SQL statement that will be executed.
	 * @param conn - The connection to the database.
	 *
	 * Return: None
	 *************************************************************************/
	private void executeStatement (String sqlStatement, Connection conn) throws SQLException
	{
		PreparedStatement statement = conn.prepareStatement (sqlStatement);
		statement.execute ();
	}
}
