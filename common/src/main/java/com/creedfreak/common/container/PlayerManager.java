package com.creedfreak.common.container;

import com.creedfreak.common.database.DAOs.AbsUsersDAO;
import com.creedfreak.common.database.DAOs.ProfessionsDAO;
import com.creedfreak.common.utility.Logger;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Player manager manages the Players associated with the Players who
 * are currently online. This is the portal to which all of the Profession
 * interactions will take place. This utilizes the Singleton pattern because
 * we should only have one of these in existence at any given time, this will
 * also allow us access to the players of the Manager throughout the plugin.
 */
public final class PlayerManager
{
    private static final String PM_PREFIX = "PlayerManager";

    private AbsUsersDAO mUsersDAO;
    private ProfessionsDAO mProfDAO;
    private Logger mLogger;

    private static PlayerManager mPlayerManager = null;

    // Change the UUID to the internal database ID
    private ConcurrentHashMap<UUID, IPlayer> mPlayerList;

    private PlayerManager ()
    {
        mLogger = Logger.Instance ();
    }

    /**
     * This is the central access point for the PlayerManager.
     *
     * @return The sole instance of the PlayerManager
     */
    public static PlayerManager Instance ()
    {
        if (mPlayerManager == null)
        {
            mPlayerManager = new PlayerManager ();
        }
        return mPlayerManager;
    }

    /**
     * This method is intended to initialize the Singleton after it's instantiation in order to
     * have the necessary data available to the PlayerManager being the Database in which
     * to access the Player Information and the Instance to the plugin so we cant have access
     * to things like the Logger of the plugin and other useful resources.
     *
     * @param usersDAO The interface between the players and the Database.
     */
    public void initializePlayerManager (AbsUsersDAO usersDAO)
    {
        mUsersDAO = usersDAO;
        mPlayerList = new ConcurrentHashMap<> ();
        mLogger.Debug (PM_PREFIX, "Initialization of the PlayerManager is completed!");
    }
    /**
     * This method will save all of the players to the database
     */
    public void saveAllPlayers ()
    {
        mUsersDAO.updateAll (mPlayerList.values ());
    }

    /**
     * This method will remove the player with the specified UUID from the
     * PlayerManager. Generally this will happen whenever a player logs
     * out of the game or a Disconnect is handled.
     *
     * @param dbID - The Player to remove from the Manager.
     */
    public void removePlayer (UUID dbID)
    {
    	mUsersDAO.update (mPlayerList.get (dbID));
        mPlayerList.remove (dbID);
    }

	/**
	 * Adds the player to the PlayerManager
	 *
	 * @param player - The player to add
	 */
	public void addPlayer (IPlayer player)
    {
    	mPlayerList.put (player.getUUID (), player);
    }

	/**
	 * Saves the player to the Database if it is not already there.
	 *
	 * @param uniqueID - The player to save to the database.
	 * @param username - The username of the player.
	 */
	public void savePlayer (UUID uniqueID, String username)
    {
        mUsersDAO.save (uniqueID, username);
    }

    /**
     * This method will load a player from the Database into the PlayerManager's internal
     * ConcurrentHashMap.
     *
     * @param playerUUID - The Player in which to load from the database
     */
    public boolean loadPlayer (UUID playerUUID)
    {
    	boolean succeed = false;

    	if (mUsersDAO.checkExist (playerUUID))
	    {
		    IPlayer player;
			player = mUsersDAO.load (playerUUID);
			mUsersDAO.fetchUserProfessions (player);

			mPlayerList.put (player.getUUID (), player);
			succeed = true;
	    }
    	return succeed;
    }

    /**
     * This method will hash the given UUID to the Hash Map in order to find and return
     * the specified player that is within the PlayerManager. We will also not decrement
     * the size here since we shall only be retrieving the IPlayer in order to use
     * that IPlayer elsewhere.
     *
     * @param playerID - The player to retrieve from the Hash Map
     *
     * @return The player specified by their Database ID
     */
    public IPlayer getPlayer (UUID playerID)
    {
        return mPlayerList.get (playerID);
    }
}
