package com.creedfreak.common.container;

import com.creedfreak.common.concurrent.database.DatabaseWorkerQueue;
import com.creedfreak.common.concurrent.database.tasks.TaskCheckUserExist;
import com.creedfreak.common.concurrent.database.tasks.TaskSavePlayer;
import com.creedfreak.common.database.connection.Database;
import com.creedfreak.common.utility.Logger;
import net.jcip.annotations.GuardedBy;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Player manager manages the Players associated with the Players who
 * are currently online. This is the portal to which all of the Profession
 * interactions will take place. This utilizes the Singleton pattern because
 * we should only have one of these in existence at any given time, this will
 * also allow us access to the players of the Manager throughout the plugin.
 * <p>
 * Creation: The creation of this Singleton uses the Early Initialization
 * of the JVM to make sure that the static blocks are instantiated
 * in a synchronized environment as per the JLS 12.4.2 standard.
 */

// TODO: Mark this class as @ThreadSafe after I make it thread safe.
public final class PlayerManager {

	@GuardedBy ("this")
	private static final String PM_PREFIX = "PlayerManager";
	private static final PlayerManager mPlayerManager = new PlayerManager ();

	private Logger mLogger;
	private DatabaseWorkerQueue mWorkerQueue = null;
	private IPlayerFactory mPlayerFactory;

	private ConcurrentHashMap<Long, IPlayer> mPlayerList;
	private ConcurrentHashMap<UUID, Long> mInternalIDCache;

	private PlayerManager () {
	}

	/**
	 * Create the instance of PlayerManager.
	 */
	public static PlayerManager Instance () {
		return mPlayerManager;
	}

	/**
	 * Prepare the Singleton after it's instantiation in order to
	 * have the necessary data available to the PlayerManager.
	 * <p>
	 * This method will create the database task thread pool which will handle all of the
	 * threads associated with running tasks with the Database.
	 *
	 * @param factory The player factory interface.
	 */
	public synchronized void preparePlayerManager (Database dataPool, IPlayerFactory factory, int initialThreadCount) {
		mPlayerList = new ConcurrentHashMap<> ();
		mInternalIDCache = new ConcurrentHashMap<> ();
		mLogger = Logger.Instance ();

		mPlayerFactory = factory;
		mWorkerQueue = new DatabaseWorkerQueue (dataPool, initialThreadCount);

		mLogger.Debug (PM_PREFIX, "Initialization of the PlayerManager is completed!");
	}

	public IPlayerFactory getPlayerFactory () {
		return mPlayerFactory;
	}

	/**
	 * This method will save all of the players to the database
	 */
	public synchronized void updatePlayers () {
		for (IPlayer player : mPlayerList.values ()) {
			// TODO: Loop and update all players in the PlayerManager
			// mWorkerQueue.addTask ();
		}
	}

	/**
	 * This method will remove the player with the specified UUID from the
	 * PlayerManager. Generally this will happen whenever a player logs
	 * out of the game or a Disconnect is handled.
	 *
	 * @param internalID - The Player to remove from the Manager.
	 */
	public synchronized void removePlayer (Long internalID) {
		IPlayer player = mPlayerList.get (internalID);

		// TODO: Send task to DB Thread

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
	public synchronized void addPlayer (IPlayer player) {
		mInternalIDCache.put (player.getUUID (), player.getInternalID ());
		mPlayerList.put (player.getInternalID (), player);
	}

	/**
	 * Saves the player to the database if it is not already there.
	 *
	 * @param uniqueID - The player to save to the database.
	 * @param username - The username of the player.
	 */
	public void savePlayer (UUID uniqueID, String username) {
		mWorkerQueue.addTask (new TaskSavePlayer (uniqueID, username));
	}

	/**
	 * This method will load a player into the internal Player Map
	 *
	 * @param playerUUID - The player uuid of the player to load.
	 */
	public synchronized void loadPlayer (UUID playerUUID) {
		// This will check to see if the player exists and handles the
		// saving or loading of a player depending on the outcome.
		mWorkerQueue.addTask (new TaskCheckUserExist (playerUUID));
	}

	/**
	 * Allows the updating of a single player from within the PlayerManager.
	 *
	 * @param internalID The player uuid of the player to load
	 */
	public synchronized void updatePlayer (Long internalID) {
		IPlayer player = mPlayerList.get (internalID);
		if (player != null) {
			// TODO: Queue up a new TaskUpdatePlayer
		}
	}

	/**
	 * This method will hash the given UUID to the Hash Map in order to find and return
	 * the specified player that is within the PlayerManager. We will also not decrement
	 * the size here since we shall only be retrieving the IPlayer in order to use
	 * that IPlayer elsewhere.
	 *
	 * @param internalID The players internal ID
	 * @return The player specified by their database ID
	 */
	public IPlayer getPlayer (Long internalID) {
		return mPlayerList.get (internalID);
	}

	public IPlayer getPlayerByUUID (UUID playerUUID) {
		return mPlayerList.get (mInternalIDCache.get (playerUUID));
	}

	public void cleanupPlayerManager () {
		if (mWorkerQueue != null) {
			mWorkerQueue.safeShutdown ();
		}
	}
}
