package com.creedfreak.common.concurrent.database.tasks;

import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.container.PlayerManager;
import com.creedfreak.common.database.connection.Database;
import com.creedfreak.common.database.queries.QueryLib;
import com.creedfreak.common.professions.Augment;
import com.creedfreak.common.professions.Profession;
import com.creedfreak.common.professions.ProfessionBuilder;
import com.creedfreak.common.utility.Logger;
import com.creedfreak.common.utility.UuidUtil;
import jdk.nashorn.internal.ir.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;

/**
 * This task will load an entire users crafty profession data from
 * the database. Once the data is retrieved the runnable builds
 * the player and adds it into the PlayerManager to be used.
 * <p>
 * The Data Retrieved:
 * - The user information from Users
 * - The user career information from Careers
 * - The user augments for their careers from UserProfHasAugments
 * <p>
 * Concurrency Policy:
 * All of the internal data can be read but never changed. Since the
 * TaskLoadPlayer state is immutable this class is Thread Safe.
 */
@ThreadSafe
@Immutable
public class TaskLoadPlayer extends DBTask {

	private static final String PREFIX = "-TaskLoadPlayer";
	private static final long INVALID_ID = -1;

	private final boolean mbUseDatabaseID;
	private final long mPlayerID;
	private final UUID mPlayerUUID;

	/**
	 * Default constructor which runs the load player task using
	 * the UUID of a player within the game.
	 */
	public TaskLoadPlayer (UUID playerUUID) {
		super (DatabaseTaskType.Query);
		mPlayerUUID = playerUUID;
		mPlayerID = INVALID_ID;
		mbUseDatabaseID = false;
	}

	/**
	 * A constructor that utilizes the internal database id of a user
	 * to make a quicker query of that users data from the database.
	 * When possible this type of constructor should be used for
	 * quicker retrieval of the users data.
	 */
	public TaskLoadPlayer (UUID playerUUID, long playerID) {
		super (DatabaseTaskType.Query);
		mPlayerUUID = playerUUID;
		mPlayerID = playerID;
		mbUseDatabaseID = (playerID > 0);
	}

	// DEBUG: This method needs to be tested!
	public void run () {
		String subSystemPrefix = Thread.currentThread ().getName () + PREFIX;
		Connection connection = mDataPool.dbConnect ();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		IPlayer retPlayer;

		Long playerID;
		UUID playerUUID;
		String username;
		Integer playerLevel;

		ExecutorService service = Executors.newSingleThreadExecutor ();
		FutureTask<List<Profession>> professions = new FutureTask<> (new TaskUserProfessions ());
		FutureTask<HashMap<Integer, List<Augment>>> augments = new FutureTask<> (new TaskUserAugments ());

		try {
			service.submit (professions);
			service.submit (augments);

			if (mbUseDatabaseID) {
				statement = connection.prepareStatement (QueryLib.selectUserDataFromDatabaseID);
				statement.setLong (1, mPlayerID);
			} else {
				statement = connection.prepareStatement (QueryLib.selectUserDataFromUUID);
				statement.setBytes (1, UuidUtil.toBytes (mPlayerUUID));
			}
			resultSet = statement.executeQuery ();

			if (resultSet.next ()) {
				List<Profession> playerProfList;
				HashMap<Integer, List<Augment>> playerProfAugMap;

				playerID = resultSet.getLong ("UserID");
				playerUUID = UuidUtil.fromBytes (resultSet.getBytes ("UUID"));
				username = resultSet.getString ("Username");
				playerLevel = resultSet.getInt ("UserLevel");

				try {
					// Wait on these two get methods until they finish then move ahead.
					playerProfList = professions.get ();
					playerProfAugMap = augments.get ();

					for (Profession prof : playerProfList) {
						List<Augment> profAugments = playerProfAugMap.get (prof.getIdentifier ());

						if (profAugments != null) {
							prof.attachAugments (profAugments);
						}
					}
					retPlayer = PlayerManager.Instance ().getPlayerFactory ().buildPlayerWithProfessions (playerID, playerLevel, playerUUID, username, playerProfList);
					PlayerManager.Instance ().addPlayer (retPlayer);
				}
				catch (ExecutionException except) {
					Logger.Instance ().Error (subSystemPrefix, "Execution Exception while retrieving a player!");
					except.printStackTrace ();
				}
				catch (InterruptedException except) {
					if (!Thread.currentThread ().isInterrupted ()) {
						// If the player is null on interrupt then the player wasn't received.
						// Thus we still need to get the player, queue up the same task again.
						if (PlayerManager.Instance ().getPlayer (mPlayerID) == null) {
							// TODO: DO more research here to see if this is valid.
							this.queuePostConditionTask (new TaskLoadPlayer (mPlayerUUID, mPlayerID));
						}
						Thread.currentThread ().interrupt ();
					}
				}
			}
		}
		catch (SQLException except) {
			Logger.Instance ().Error (subSystemPrefix, "Something went wrong while loading player! Reason: " + except.getMessage ());
		}
		finally {
			service.shutdown ();
			Database.CloseResources (resultSet, statement);
			Database.CloseConnection (connection);
		}
	}

	/**
	 * This callable will fetch all of a users professions to load onto a user.
	 * <p>
	 * Concurrency Policy:
	 * This class does not publish any internal data. Since there is no publication
	 * of internal data this class is Thread Safe.
	 */
	@ThreadSafe
	@Immutable
	private final class TaskUserProfessions implements Callable<List<Profession>> {

		public List<Profession> call () {
			String subSystemPrefix = Thread.currentThread ().getName () + PREFIX;
			List<Profession> professions = new LinkedList<> ();
			Connection connection = mDataPool.dbConnect ();

			PreparedStatement profStatement = null;
			ResultSet profResultSet = null;

			try {
				profStatement = connection.prepareStatement (QueryLib.selectUserCareers);
				profStatement.setLong (1, mPlayerID);
				profResultSet = profStatement.executeQuery ();

				while (profResultSet.next ()) {
					int level = profResultSet.getInt ("Level");
					int prestigeLevel = profResultSet.getInt ("PrestigeLevel");
					double currentExp = profResultSet.getDouble ("CurrentExp");
					double totalExp = profResultSet.getDouble ("TotalExp");
					Integer internalID = profResultSet.getInt ("ProfessionID");
					String internalName = profResultSet.getString ("InternalName");
					String profStatus = profResultSet.getString ("ProfStatus");
					
					professions.add (ProfessionBuilder.dbBuild (internalName, internalID,
							profStatus, level, prestigeLevel, currentExp, totalExp));
				}
			}
			catch (SQLException except) {
				Logger.Instance ().Error (subSystemPrefix, "SQL Error while retrieving player professions! SQLState: " + except.getSQLState ());
			}
			finally {
				Database.CloseResources (profResultSet, profStatement);
				Database.CloseConnection (connection);
			}

			return professions;
		}
	}

	/**
	 * The callable class that handles the fetching of a users augments. This callable
	 * will fetch every augment that a user has from the database. Once the
	 * augments are fetched we unpack them from the result set and bundle them
	 * into a Map of LinkedLists where each ProfessionID has a list of augments
	 * that are associated with it. If a profession doesn't have any augments the
	 * corresponding Map key that is associated with a ProfessionID will return null.
	 * <p>
	 * Concurrency Policy:
	 * This class does not publish any internal data. Since there is no publication
	 * of internal data this class is Thread Safe.
	 */
	@ThreadSafe
	@Immutable
	private final class TaskUserAugments implements Callable<HashMap<Integer, List<Augment>>> {

		public HashMap<Integer, List<Augment>> call () {
			String subSystemPrefix = Thread.currentThread ().getName () + PREFIX;
			HashMap<Integer, List<Augment>> augments = new HashMap<> ();
			Connection connection = mDataPool.dbConnect ();
			PreparedStatement statement = null;
			ResultSet resultSet = null;

			try {
				statement = connection.prepareStatement (QueryLib.selectUserProfessionAugs);
				statement.setLong (1, mPlayerID);
				resultSet = statement.executeQuery ();

				while (resultSet.next ()) {
					Augment augment = Augment.lookupAugment (resultSet.getInt ("AugmentID"));
					Integer professionID = resultSet.getInt ("ProfessionID");

					if (augments.containsKey (professionID)) {
						augments.get (professionID).add (augment);
					} else {
						List<Augment> newAugList = new ArrayList<> ();
						newAugList.add (augment);
						augments.put (professionID, newAugList);
					}
				}
			}
			catch (SQLException except) {
				Logger.Instance ().Error (subSystemPrefix, "Error while fetching user " +
						"profession augments! SQLState Error " + except.getSQLState ());
			}
			finally {
				Database.CloseResources (resultSet, statement);
				Database.CloseConnection (connection);
			}
			return augments;
		}
	}
}
