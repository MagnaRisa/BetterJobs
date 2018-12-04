package com.creedfreak.common.database.DAOs;

import com.creedfreak.common.database.queries.queryLib;
import com.creedfreak.common.utility.Logger;
import com.creedfreak.common.utility.UuidUtil;
import com.google.common.primitives.UnsignedLong;
import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.database.databaseConn.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
	 * <p>Saves a players information into the database.</p>
	 *
	 * @param player The player to store within the database.
	 */
	public void save (IPlayer player)
	{
		Connection conn = mDatabase.dbConnect ();
		PreparedStatement savePlayer = null;

		try
		{
			savePlayer = conn.prepareStatement (insertUser);

			savePlayer.setBytes (1, UuidUtil.toBytes (player.getUUID ()));
			savePlayer.setString (2, player.getUsername ());

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
	 * Loads a list of users from the database and returns a list
	 * of the IPlayers. If the collection passed into the method
	 * is not a list of players then null is returned..
	 *
	 * @param values The map of players to be loaded from the
	 *                                  database as IPlayers.
	 * @return A list of loaded players from the database.
	 */
	public abstract  List<IPlayer> loadSubset (Collection<UUID> values);

	/**
	 * Loads a single user based on their UUID. Note that this
	 * method will only load the default players information soley
	 * from the
	 *
	 * @param userID The Users UUID
	 * @return a player object.
	 */
	public abstract IPlayer load (UUID userID);

	/**
	 *
	 * @param userID
	 * @return
	 */
	public IPlayer restoreUser (UUID userID)
	{
		return null;
	}
}
