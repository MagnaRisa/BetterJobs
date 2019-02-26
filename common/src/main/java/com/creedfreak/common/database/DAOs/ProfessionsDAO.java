package com.creedfreak.common.database.DAOs;

import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.database.databaseConn.Database;
import com.creedfreak.common.database.queries.queryLib;
import com.creedfreak.common.professions.Profession;
import com.creedfreak.common.professions.TableType;
import com.creedfreak.common.utility.Logger;
import com.google.common.primitives.UnsignedLong;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProfessionsDAO {

	private Database mDatabase;

	private static final String insertCareer
			= "INSERT INTO Careers (UserID, ProfessionID) "
			+ "VALUES (?, ?)";

	private static final String updateCareer
			= "UPDATE Careers"
			+ "SET Level = ?, CurrentExp = ?, TotalExp = ?, PrestigeLevel = ?, ProfStatus = ? "
			+ "WHERE UserID = ?"
			+ "AND ProfessionID = ?";

	public ProfessionsDAO (Database database) {
		mDatabase = database;
	}

	/**
	 * Save a specific users profession to the database
	 *
	 * @param player
	 */
	public void save (IPlayer player, TableType type) {
		Connection conn = mDatabase.dbConnect ();
		PreparedStatement prepStmt = null;

		Profession prof = player.getProfession (type);

		try {
			prepStmt = conn.prepareStatement (insertCareer);

			prepStmt.setLong (1, player.getInternalID ().longValue ());
			// prepStmt.setLong (2, prof.);
		}
		catch (SQLException except) {

		}
		finally {
			mDatabase.dbCloseResources (prepStmt);
			mDatabase.dbClose ();
		}
	}

	public void saveSubset (IPlayer player) {

	}

	public void delete (UnsignedLong id) {

	}

	public void update (Profession row) {

	}

	public void updateAll (Collection<Profession> profs) {
//        Connection conn = mDatabase.dbConnect ();
//        PreparedStatement prepStmt;
//
//        try
//        {
//            prepStmt = conn.prepareStatement ()
//
//            // use prepStmt.batch to execute several queries making them more efficient.
//        }
	}

	public Profession load () {
		return null;
	}

	public List<Profession> loadSubset (UnsignedLong userID, String username) {
		Connection conn = mDatabase.dbConnect ();

		List<Profession> loaded = new ArrayList<> ();
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;

		try {
			int PrestigeLevel, Level;
			double ExpCurrent, ExpTotal;
			String ProfName, ProfStatus;

			conn.setAutoCommit (false);
			prepStatement = conn.prepareStatement (queryLib.selectUserCareers);

			prepStatement.setLong (1, userID.longValue ());

			resultSet = prepStatement.executeQuery ();

			while (resultSet.next ()) {
				ProfName = resultSet.getString ("ProfessionName");
				ProfStatus = resultSet.getString ("ProfStatus");

				ExpCurrent = resultSet.getDouble ("CurrentExp");
				ExpTotal = resultSet.getDouble ("TotalExp");

				Level = resultSet.getInt ("Level");
				PrestigeLevel = resultSet.getInt ("PrestigeLevel");

				// loaded.add (ProfessionBuilder.dbBuild (ProfName, ProfStatus, Level, PrestigeLevel, ExpCurrent, ExpTotal));
			}
		}
		catch (SQLException except) {
			Logger.Instance ().Error ("ProfessionsDAO", "Error Code: " + except.getErrorCode () +
					" SQL State: " + except.getSQLState ());

			Logger.Instance ().Error ("ProfessionMiner could not be loaded for player named " + username + ". Is the "
					+ "database connected properly?");
		}
		finally {
			try {
				conn.setAutoCommit (true);
			}
			catch (SQLException except) {
				Logger.Instance ().Error ("AbsUsersDAO", "Could not set auto commit for database: " + except.getSQLState ());
			}

			mDatabase.dbCloseResources (prepStatement, resultSet);
			mDatabase.dbClose ();
		}

		return loaded;
	}

	public List<Profession> loadAll () {
		return null;
	}
}
