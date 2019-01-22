package com.creedfreak.common.database.DAOs;

import com.creedfreak.common.database.queries.queryLib;
import com.creedfreak.common.professions.Profession;
import com.creedfreak.common.utility.Logger;
import com.creedfreak.common.utility.UuidUtil;
import com.google.common.primitives.UnsignedLong;
import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.database.databaseConn.Database;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class AbsUsersDAO
{

	protected Database mDatabase;
	protected AugmentsDAO mAugmentDAO;
	protected ProfessionsDAO mProfessionsDAO;

	private static final String insertUser
			= "INSERT INTO Users (UUID, Username, DateOfCreation) "
			+ "VALUES (?, ?, CURRENT_TIMESTAMP)";

	private static final String updateUser
			= "UPDATE Users "
			+ "SET Username = ?, UserLevel = ? "
			+ "WHERE UserID = ?";

	private static final String checkUserExist
			= "SELECT UUID, UserID "
			+ "FROM Users "
			+ "WHERE UUID = ?";

	/**
	 * <p>This is the constructor for the Users database access object.</p>
	 *
	 * @param database The database in which to access.
	 */
	public AbsUsersDAO (Database database)
	{
		mDatabase = database;
		mAugmentDAO = new AugmentsDAO (database);
		mProfessionsDAO = new ProfessionsDAO (database);
	}

	/**
	 * <p>Save a player into the database.</p>
	 *
	 * @param playerID The ID of the player.
	 * @param username The username of the player.
	 */
	public void save (UUID playerID, String username)
	{
		Connection conn = mDatabase.dbConnect ();
		PreparedStatement savePlayer = null;

		try
		{
			savePlayer = conn.prepareStatement (insertUser);
			savePlayer.setBytes (1, UuidUtil.toBytes (playerID));
			savePlayer.setString (2, username);

			if (savePlayer.execute ())
			{
				Logger.Instance ().Debug ("UsersDAO", "Inserted Player Successfully!");
			}
		}
		catch (SQLException except)
		{
			Logger.Instance ().Error ("UsersDAO", "Cannot insert user! Reason: " + except.getMessage ());
		}
		finally
		{
			mDatabase.dbCloseResources (savePlayer);
			mDatabase.dbClose ();
		}
	}

	/**
	 * <p>Deletes a player from the Database. This includes all of the relationships that the player
	 * could potentially be a part of. Note: That the player can only get deleted if the proper password
	 * has been input into the command.</p>
	 *
	 * @param id The primary identifier within the database for the current player.
	 */
	public void delete (UnsignedLong id)
	{
		// Before we delete the user, add their current data to the archive so it is retrievable.

		// Delete the row whose rid == id
	}

	/**
	 * Updates the IPlayers information in the database.
	 *
	 * TODO: This should include the players Professions and Augments on those professions.
	 *
	 * @param player The player to update into the database
	 */
	public void update (IPlayer player)
	{
		// Update a single player.
		Connection conn = mDatabase.dbConnect ();
		PreparedStatement updatePlayer = null;

		try
		{
			updatePlayer = conn.prepareStatement (updateUser);

			updatePlayer.setString (1, player.getUsername ());
			updatePlayer.setInt (2, player.getLevel ());

			if (updatePlayer.execute ())
			{
				Logger.Instance ().Debug ("UsersDAO", "Updated Player Successfully!");
			}
		}
		catch (SQLException except)
		{
			Logger.Instance ().Error ("UsersDAO", "Cannot insert user! Reason: " + except.getMessage ());
		}
		finally
		{
			mDatabase.dbCloseResources (updatePlayer);
			mDatabase.dbClose ();
		}
	}

	/**
	 * Updates an entire collection of players to the database.
	 *
	 * @param players A collection of players to update.
	 */
	public void updateAll (Collection<IPlayer> players)
	{
		Connection conn = mDatabase.dbConnect ();
		PreparedStatement updatePlayer = null;
		int count = 0;

		try
		{
			updatePlayer = conn.prepareStatement (updateUser);

			for (IPlayer player : players)
			{
				updatePlayer.setString (1, player.getUsername ());
				updatePlayer.setInt (2, player.getLevel ());

				updatePlayer.executeUpdate ();
				count++;
			}
		}
		catch (SQLException except)
		{
			Logger.Instance ().Error ("UsersDAO", "While processing updateAll method something went wrong!");
			Logger.Instance ().Error ("UsersDAO", except.getMessage ());
		}
		finally
		{
			if (count == players.size ())
			{
				Logger.Instance ().Debug ("UsersDAO", "All players were updated successfully!");
			}

			mDatabase.dbCloseResources (updatePlayer);
			mDatabase.dbClose ();
		}
	}

	/**
	 * TODO: This could be condensed to use the <code>load<code/> function within the loop
	 *  what we have to be careful of is closing the statements appropriately while not
	 *  closing it every single loading of a player that is done. This is currently why
	 *  I don't call <code>load</code> within <code>loadSubset<code/>
	 *
	 * Loads a list of users from the database and returns a list
	 * of the IPlayers. If the collection passed into the method
	 * is not a list of players then null is returned..
	 *
	 * @param values The map of players to be loaded from the
	 *                                  database as IPlayers.
	 * @return A list of loaded players from the database.
	 */
	public List<IPlayer> loadSubset (Collection<UUID> values)
	{
		Connection conn = mDatabase.dbConnect ();

		List<IPlayer> retPlayers = new ArrayList<> ();
		PreparedStatement getPlayer = null;
		ResultSet resultSet = null;

		UnsignedLong playerID;
		UUID playerUUID;
		String username;
		Integer playerLevel;

		try
		{
			conn.setAutoCommit (false);
			getPlayer = conn.prepareStatement (queryLib.selectUserData);

			for (UUID uuid : values)
			{
				getPlayer.setBytes (1, UuidUtil.toBytes (uuid));
				resultSet = getPlayer.executeQuery ();

				if (resultSet.isFirst ())
				{
					playerID = UnsignedLong.valueOf (BigInteger.valueOf (resultSet.getLong ("UserID")));
					playerUUID = UuidUtil.fromBytes (resultSet.getBytes ("UUID"));
					username = resultSet.getString ("Username");
					playerLevel = resultSet.getInt ("UserLevel");

					if (playerUUID != uuid)
					{
						throw new SQLException ("Incorrect Row Retrieved!");
					}
					retPlayers.add (playerFactory (playerID, username, playerLevel));
				}
			}
			conn.commit ();
		}
		catch (SQLException except)
		{
			Logger.Instance ().Error ("AbsUsersDAO", "Something went wrong while processing loadAll. Reason: " + except.getMessage ());
		}
		catch (ClassCastException except)
		{
			Logger.Instance ().Error ("AbsUsersDAO", except.getMessage ());
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

			mDatabase.dbCloseResources (getPlayer, resultSet);
			mDatabase.dbClose ();
		}
		return retPlayers;
	}

	/**
	 * Loads a single user based on their UUID. Note that this
	 * method will only load the default players information soley
	 * from the
	 *
	 * @param userID The Users UUID
	 * @return a player object.
	 */
	public IPlayer load (UUID userID)
	{
		IPlayer retPlayer = null;
		PreparedStatement getPlayer = null;
		ResultSet resultSet = null;

		UnsignedLong playerID;
		UUID playerUUID;
		String username;
		Integer playerLevel;

		try
		{
			getPlayer = mDatabase.dbConnect ().prepareStatement (queryLib.selectUserData);

			getPlayer.setBytes (1, UuidUtil.toBytes (userID));

			resultSet = getPlayer.executeQuery ();

			if (resultSet.isFirst ())
			{
				playerID = UnsignedLong.valueOf (BigInteger.valueOf (resultSet.getLong ("UserID")));
				playerUUID = UuidUtil.fromBytes (resultSet.getBytes ("UUID"));
				username = resultSet.getString ("Username");
				playerLevel = resultSet.getInt ("UserLevel");

				if (playerUUID != userID)
				{
					throw new SQLException ("Incorrect Row Retrieved!");
				}
				retPlayer = playerFactory (playerID, username, playerLevel);
			}
		}
		catch (SQLException except)
		{
			Logger.Instance ().Error ("AbsUsersDAO", "Something wen't wrong while loading player! Reason: " + except.getMessage ());
		}
		finally
		{
			mDatabase.dbCloseResources (getPlayer, resultSet);
			mDatabase.dbClose ();
		}

		return retPlayer;
	}

	public IPlayer restoreUser (UUID userID)
	{
		return null;
	}

	/**
	 * TODO: Not Fully Implemented Yet.
	 * Checks to see if the user is within the database.
	 *
	 * @param userID - The Users unique id
	 *
	 * @return True - If the user is in the database
	 *         False - If the user is not in the database
	 */
	public boolean checkExist (UUID userID)
	{
		boolean retVal = false;

		PreparedStatement prepStmt = null;
		ResultSet resultSet = null;

		try
		{
			prepStmt = mDatabase.dbConnect ().prepareStatement (checkUserExist);
			prepStmt.setBytes (1, UuidUtil.toBytes (userID));
			resultSet = prepStmt.executeQuery ();

			if (resultSet.next ())
			{
				retVal = true;
			}
		}
		catch (SQLException except)
		{
			mDatabase.dbCloseResources (prepStmt, resultSet);
			mDatabase.dbClose ();
		}

		return retVal;
	}

	public void fetchUserProfessions (IPlayer player)
	{
		List<Profession> professions;

		professions = mProfessionsDAO.loadSubset (player.getDBIdentifier (), player.getUsername ());

//		for (Profession prof : professions)
//		{
//			// Grab the augments for each profession
//			mAugmentDAO.fetchProfAugments (prof, player.getDBIdentifier ());
//		}

		player.registerProfession (professions);
	}

	public abstract IPlayer playerFactory (UnsignedLong playerID, String username, Integer playerLevel);
}
