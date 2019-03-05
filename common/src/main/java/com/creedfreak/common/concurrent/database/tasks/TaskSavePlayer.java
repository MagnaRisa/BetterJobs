package com.creedfreak.common.concurrent.database.tasks;

import com.creedfreak.common.utility.Logger;
import com.creedfreak.common.utility.UuidUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class TaskSavePlayer extends DBTask {

	private static final String PREFIX = "-TaskSavePlayer";
	private static final String insertUser
			= "INSERT INTO Users (UUID, Username, DateOfCreation) "
			+ "VALUES (?, ?, CURRENT_TIMESTAMP)";

	private final UUID mPlayerID;
	private final String mUsername;

	public TaskSavePlayer (UUID playerUUID, String username) {
		super (DatabaseTaskType.Save);

		mPlayerID = playerUUID;
		mUsername = username;
	}

	// DEBUG: We need to debug this to make sure it works
	public void run () {
		final Logger logger = Logger.Instance ();
		final String subSystemPrefix = Thread.currentThread ().getName () + PREFIX;

		try (Connection conn = mDataPool.dbConnect ();
			PreparedStatement savePlayer = conn.prepareStatement (insertUser)) {
			
			savePlayer.setBytes (1, UuidUtil.toBytes (mPlayerID));
			savePlayer.setString (2, mUsername);

			if (savePlayer.execute ()) {
				logger.Debug (subSystemPrefix, "Inserted Player Successfully!");
			}
		}
		catch (SQLException except) {
			logger.Error (subSystemPrefix, "Cannot insert user! Reason: " + except.getMessage ());
		}
		catch (NullPointerException except) {
			if (mDataPool == null) {
				logger.Error (subSystemPrefix, "DataPool not set, can't execute task!");
			}
			except.printStackTrace ();
		}
	}
}
