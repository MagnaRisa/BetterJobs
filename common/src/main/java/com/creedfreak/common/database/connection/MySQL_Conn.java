package com.creedfreak.common.database.connection;

import com.creedfreak.common.AbsConfigController;
import com.creedfreak.common.ICraftyProfessions;
import com.creedfreak.common.database.queries.QueryLib;
import com.creedfreak.common.utility.Pair;
import org.mariadb.jdbc.MariaDbPoolDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQL_Conn extends Database {

	private final String MYSQL_TABLE_STMTS = "sql_files/mariadb_create_tables.sql";
	// private final String CONNECTION_STRING;
	
	private MariaDbPoolDataSource mConnectionPool;

	/**
	 * The primary constructor for a MySQL/MariaDB Connection
	 *
	 * @param plugin - The plugin to grab resources from
	 * @param hostName - The hostname of the database
	 * @param port The port in which to access the database
	 * @param db - The database name to use from the host
	 * @param user - The user in which to login with
	 * @param identifier - The database user's password
	 */
	public MySQL_Conn (ICraftyProfessions plugin, AbsConfigController config,
	                   String hostName, int port, String db, String user, String identifier) {
		super (plugin, config);
		
		String url = "jdbc:mysql://" + hostName + ":" + port + "/" + db + "?maxPoolSize=8";
		mConnectionPool = new MariaDbPoolDataSource (url);

		try {
			mConnectionPool.setUser (user);
			mConnectionPool.setPassword (identifier);

		} catch (SQLException except) {
			mLogger.Error (Database.DATABASE_PREFIX, "Could not set the database " +
					"user or password. State: " + except.getSQLState ());
		}
	}

	/**
	 * Creates/fetches a MySQL connection to the constructed MySQL_Conn
	 *  Object. This method uses the information from the
	 *  constructed object to make the connection to the database.
	 *
	 * @return The connection to the database
	 */
	public Connection dbConnect () {
		Connection conn = null;
		try {
			synchronized (this) {
				conn = mConnectionPool.getConnection ();
			}
		}
		catch (SQLException exception) {
			mLogger.Error (Database.DATABASE_PREFIX, "Connection Error: " + exception);
		}
		return conn;
	}
	
	/**
	 * Closes down the internal connection pool.
	 */
	public void shutdown () {
		mConnectionPool.close ();
	}

	/**
	 * Tests the connection to the Database by selecting a testing table
	 *
	 * @return A List of testing data.
	 */
	@Deprecated
	public List<Pair<Integer, String>> testDBconnection () {
		List<Pair<Integer, String>> array = new ArrayList<> ();

		try (Connection conn = this.dbConnect ();
			PreparedStatement statement = conn.prepareStatement (QueryLib.checkSettings);
			ResultSet rSet = statement.executeQuery ()) {

			while (rSet.next ()) {
				array.add (new Pair<> (rSet.getInt ("SettingID"),
						rSet.getString ("SettingName")));
			}
		} catch (SQLException exception) {
			mLogger.Error (Database.DATABASE_PREFIX, "Query Error: " + exception);
		}
		return array;
	}

	/**
	 * Retrieve the create table statement file name.
	 */
	protected String getCreateTableStmts () {
		return MYSQL_TABLE_STMTS;
	}
}