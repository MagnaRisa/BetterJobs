package com.creedfreak.common.database.databaseConn;

import com.creedfreak.common.AbsConfigController;
import com.creedfreak.common.ICraftyProfessions;
import com.creedfreak.common.utility.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQL_Conn extends Database
{
    private final String MYSQL_TABLE_STMTS = "resources/sql_files/mariadb_create_tables.sql";

    private String mHost;
    private String mDatabase;
    private String mDBUser;
    private String mDBIdentifier;

    /**************************************************************************
     * Constructor: MySQL_Conn
     *
     * Description: The primary constructor for a MySQL Connection
     *
     * Parameters:
     * @param plugin - The plugin in order to grab the Logger
     * @param hostName - The hostname of the database you would like to
     *                      connect to
     * @param db - The database name to use from the host
     * @param user - The user in which to login with in order to read/write
     *             from the database
     * @param identifier - The database user's password
     *
     * Return: None
     *************************************************************************/
    public MySQL_Conn (ICraftyProfessions plugin, AbsConfigController config,
                       String hostName, String db, String user, String identifier)
    {
        super (plugin, config);
        mHost = hostName;
        mDatabase = db;
        mDBUser = user;
        mDBIdentifier = identifier;
    }

    /**************************************************************************
     * Method: dbConnect
     *
     * Description: Creates a MySQL connection to the constructed MySQL_Conn
     *              Object. This method uses the information from the
     *              constructed object to make the connection to the database.
     *
     * @return The connection to the database
     *************************************************************************/
    public Connection dbConnect ()
    {
        try
        {
            if (mConnection == null || mConnection.isClosed ())
            {
                mConnection = DriverManager.getConnection ("jdbc:mysql://"
                    + mHost + "/" + mDatabase, mDBUser, mDBIdentifier);
            }
        }
        catch (SQLException exception)
        {
            mLogger.Error (Database.DATABASE_PREFIX,"database Connection Error: " + exception);
        }

        return mConnection;
    }

    /**************************************************************************
     * Method: testDBconnection
     *
     * Creates a MySQL connection to the constructed MySQL_Conn Object.
     * This method uses the information from the constructed object to make the
     * connection to the database.
     *
     * @return The connection to the database
     *************************************************************************/
    public List<Pair<Integer, String>> testDBconnection ()
    {
        List<Pair<Integer, String>> array = new ArrayList<> ();

        try
        {
            Statement statement = mConnection.createStatement ();
            ResultSet rSet;

            rSet = statement.executeQuery ("SELECT * FROM Test");

            while (rSet.next ())
            {
                array.add (new Pair<>(rSet.getInt ("TestID"),
                    rSet.getString ("TestingName")));
            }
        }
        catch (SQLException exception)
        {
            mLogger.Error (Database.DATABASE_PREFIX,"database Query Error: " + exception);
        }

        return array;
    }

    protected String getCreateTableStmts ()
    {
        return MYSQL_TABLE_STMTS;
    }
}