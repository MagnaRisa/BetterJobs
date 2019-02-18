package com.creedfreak.common.container;

import com.creedfreak.common.concurrent.DatabaseWorkerQueue;
import com.creedfreak.common.database.DAOs.AbsUsersDAO;
import com.creedfreak.common.utility.Logger;
import com.google.common.primitives.UnsignedLong;
import net.jcip.annotations.GuardedBy;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Player manager manages the Players associated with the Players who
 * are currently online. This is the portal to which all of the Profession
 * interactions will take place. This utilizes the Singleton pattern because
 * we should only have one of these in existence at any given time, this will
 * also allow us access to the players of the Manager throughout the plugin.
 *
 * Creation: The creation of this Singleton uses the Early Initialization
 * of the JVM to make sure that the static blocks are instantiated
 * in a synchronized environment as per the JLS 12.4.2 standard.
 */

// TODO: Mark this class as @ThreadSafe after I make it thread safe.
public final class PlayerManager
{
	@GuardedBy("this")
    private static final String PM_PREFIX = "PlayerManager";
	private static final PlayerManager mPlayerManager = new PlayerManager ();

    private AbsUsersDAO mUsersDAO;
    private Logger mLogger;
    
    // TODO: Create new thread pool here to handle database

	private DatabaseWorkerQueue mWorkerQueue;
    private ConcurrentHashMap<UnsignedLong, IPlayer> mPlayerList;
    private ConcurrentHashMap<UUID, UnsignedLong> mInternalIDCache;

    private PlayerManager () { }

    /**
     * Create the instance of PlayerManager.
     */
    public static PlayerManager Instance ()
    {
	    return mPlayerManager;
    }

    /**
     * Prepare the Singleton after it's instantiation in order to
     * have the necessary data available to the PlayerManager.
     *
     * This method will create the database task thread pool which will handle all of the
     * threads associated with running tasks with the Database.
     *
     * @param usersDAO The interface between the players and the database.
     */
    public synchronized void preparePlayerManager (AbsUsersDAO usersDAO, int initialThreadCount)
    {
        mUsersDAO = usersDAO;
        mPlayerList = new ConcurrentHashMap<> ();
        mInternalIDCache = new ConcurrentHashMap<> ();
	    mLogger = Logger.Instance ();
	    
	    mWorkerQueue = new DatabaseWorkerQueue (mUsersDAO, initialThreadCount);
	    
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
     * @param internalID - The Player to remove from the Manager.
     */
    public synchronized void removePlayer (UnsignedLong internalID)
    {
    	IPlayer player = mPlayerList.get (internalID);
    	
    	// TODO: Send task to DB Thread
    	mUsersDAO.update (player);
    	
    	mInternalIDCache.remove (player.getUUID ());
        mPlayerList.remove (internalID);
    }

	/**
	 * Adds the player to the PlayerManager
	 * This method should never get called from
	 * another thread since it should only ever add players
	 * once the onJoin event handler is called.
	 *
	 * @param player - The player to add.
	 */
	public synchronized void addPlayer (IPlayer player)
    {
    	mInternalIDCache.put (player.getUUID (), player.getInternalID ());
    	mPlayerList.put (player.getInternalID (), player);
    }

	/**
	 * Saves the player to the database if it is not already there.
	 *
	 * @param uniqueID - The player to save to the database.
	 * @param username - The username of the player.
	 */
	public void savePlayer (UUID uniqueID, String username)
    {
    	// TODO: Send this task up to the database.
        mUsersDAO.save (uniqueID, username);
    }

    /**
     * This method will load a player from the database into the PlayerManager's internal
     * ConcurrentHashMap.
     *
     * TODO: We can spare the creation of the player to the DB thread as a Future task. Once the future is done we simple load the player into the manager.
     *
     * @param playerUUID - The Player in which to load from the database
     */
    public synchronized boolean loadPlayer (UUID playerUUID)
    {
    	boolean succeed = false;
    	if (mUsersDAO.checkExist (playerUUID))
	    {
		    IPlayer player;
			player = mUsersDAO.load (playerUUID);
			mUsersDAO.fetchUserProfessions (player);

			mInternalIDCache.put (player.getUUID (), player.getInternalID ());
			mPlayerList.put (player.getInternalID (), player);
			succeed = true;
	    }
    	return succeed;
    }
	
	/**
	 * Allows the updating of a single player from within the PlayerManager.
	 *
	 * @param internalID The internal database ID assigned to the player.
	 *
	 * @return Whether or not the update is successful.
	 */
	public synchronized boolean updatePlayer (UnsignedLong internalID)
    {
    	IPlayer player = mPlayerList.get (internalID);
    	boolean bUpdate = (null != player);
    	
    	if (bUpdate)
	    {
	    	mUsersDAO.update (player);
	    }
	    return bUpdate;
    }

    /**
     * This method will hash the given UUID to the Hash Map in order to find and return
     * the specified player that is within the PlayerManager. We will also not decrement
     * the size here since we shall only be retrieving the IPlayer in order to use
     * that IPlayer elsewhere.
     *
     * @param internalID The players internal ID
     *
     * @return The player specified by their database ID
     */
    public IPlayer getPlayer (UnsignedLong internalID)
    {
        return mPlayerList.get (internalID);
    }
}
