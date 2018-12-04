package com.creedfreak.spigot.Database;

import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.database.DAOs.AbsUsersDAO;
import com.creedfreak.common.database.databaseConn.Database;
import com.creedfreak.common.database.queries.queryLib;
import com.creedfreak.common.utility.Logger;
import com.creedfreak.common.utility.UuidUtil;
import com.creedfreak.spigot.container.SpigotPlayer;
import com.google.common.primitives.UnsignedLong;
import jdk.jshell.spi.ExecutionControl;
import org.bukkit.entity.Player;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SpigotUsersDAO extends AbsUsersDAO
{

	public SpigotUsersDAO (Database db)
	{
		super (db);
	}

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
				retPlayer = new SpigotPlayer (playerID, username, playerLevel);
			}
		}
		catch (SQLException except)
		{
			Logger.Instance ().Error ("UsersDAO", "Something wen't wrong while loading player! Reason: " + except.getMessage ());
		}
		finally
		{
			mDatabase.dbCloseResources (getPlayer, resultSet);
			mDatabase.dbClose ();
		}

		return retPlayer;
	}

	/**
	 * TODO: This could be a prime spot to implement reflection.
	 *  Load a subset of items from the database. In terms of this
	 *  class the only thing that should be passed in should be
	 *  a Spigot/Bukkit player. Because we are dealing with two
	 *  API's with two different Players that do not easily inherit a
	 *  similar object we can just cast to a generic collection to
	 *  a player collection.
	 *
	 *  TODO: Reflection Research
	 *  Can reflection be used here to dynamically call a spigot method
	 *  specifically to grab a Bukkit Player?
	 *
	 *  What about dynamic methods? Can methods use both a
	 *  Sponge and Spigot player simultaneously with reflection?
	 * @param uniqueIDs The list of player ID's to load.
	 * @return A list of loaded players as IPlayers from the Database
	 */
	@Override
	public  List<IPlayer> loadSubset (Collection<UUID> uniqueIDs)
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

			for (UUID uuid : uniqueIDs)
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
					retPlayers.add (new SpigotPlayer (playerID, username, playerLevel));
				}
			}
			conn.commit ();
		}
		catch (SQLException except)
		{
			Logger.Instance ().Error ("SpigotUsersDAO", "Something went wrong while processing loadAll. Reason: " + except.getMessage ());
		}
		catch (ClassCastException except)
		{
			Logger.Instance ().Error ("SpigotUsersDAO", except.getMessage ());
		}
		finally
		{
			try
			{
				conn.setAutoCommit (true);
			}
			catch (SQLException except)
			{
				Logger.Instance ().Error ("SpigotUsersDAO", "Count not set auto commit for Database: " + except.getSQLState ());
			}

			mDatabase.dbCloseResources (getPlayer, resultSet);
			mDatabase.dbClose ();
		}
		return retPlayers;
	}

	private void loadProfessions (List<IPlayer> players) throws ExecutionControl.NotImplementedException
	{
		throw new ExecutionControl.NotImplementedException ("Not Implemented Yet!");
	}

	private void loadAugments (UnsignedLong playerID) throws ExecutionControl.ExecutionControlException
	{
		throw new ExecutionControl.NotImplementedException ("Not Implmented Yet!");
	}
}