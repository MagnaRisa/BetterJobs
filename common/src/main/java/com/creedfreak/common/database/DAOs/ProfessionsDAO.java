package com.creedfreak.common.database.DAOs;

import com.creedfreak.common.database.queries.queryLib;
import com.creedfreak.common.professions.ProfessionBuilder;
import com.creedfreak.common.utility.Logger;
import com.google.common.primitives.UnsignedLong;
import com.creedfreak.common.database.databaseConn.Database;
import com.creedfreak.common.professions.Profession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProfessionsDAO implements IDaoBase<Profession>
{
    private Database mDatabase;

    public ProfessionsDAO (Database database)
    {
        mDatabase = database;
    }

    public void save (Profession row)
    {

    }

    public void delete (UnsignedLong id)
    {

    }

    public void update (Profession row)
    {

    }

    public void updateAll (Collection<Profession> profs)
    {

    }

    public Profession load ()
    {
    	return null;
    }

    public List<Profession> loadSubset (UnsignedLong userID, String username)
    {
        Connection conn = mDatabase.dbConnect ();

        List<Profession> loaded = new ArrayList<> ();
        PreparedStatement prepStatement = null;
        ResultSet resultSet = null;

        try
        {
            int PrestigeLevel, Level;
            double ExpCurrent, ExpTotal;
            String ProfName, ProfStatus;

            conn.setAutoCommit (false);
            prepStatement = conn.prepareStatement (queryLib.selectUserCareers);

            prepStatement.setLong (1, userID.longValue ());

            resultSet = prepStatement.executeQuery ();

            while (resultSet.next ())
            {
                ProfName = resultSet.getString ("ProfessionName");
                ProfStatus = resultSet.getString ("ProfStatus");

                ExpCurrent = resultSet.getDouble ("CurrentExp");
                ExpTotal = resultSet.getDouble ("TotalExp");

                Level = resultSet.getInt ("Level");
                PrestigeLevel = resultSet.getInt ("PrestigeLevel");

                loaded.add (ProfessionBuilder.dbBuild (ProfName, ProfStatus, Level, PrestigeLevel, ExpCurrent, ExpTotal));
            }
        }
        catch (SQLException except)
        {
            Logger.Instance ().Error ("ProfessionsDAO", "Error Code: " + except.getErrorCode () +
            " SQL State: " + except.getSQLState ());

            Logger.Instance ().Error ("ProfessionMiner could not be loaded for player named " + username + ". Is the "
                + "Database connected properly?");
        }
        finally
        {
            try
            {
                conn.setAutoCommit (true);
            }
            catch (SQLException except)
            {
                Logger.Instance ().Error ("AbsUsersDAO", "Could not set auto commit for Database: " + except.getSQLState ());
            }

            mDatabase.dbCloseResources (prepStatement, resultSet);
            mDatabase.dbClose ();
        }

        return loaded;
    }

    public List<Profession> loadAll ()
    {
        return null;
    }
}
