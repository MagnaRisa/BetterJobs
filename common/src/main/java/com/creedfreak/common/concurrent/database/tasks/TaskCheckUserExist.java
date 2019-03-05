package com.creedfreak.common.concurrent.database.tasks;

import com.creedfreak.common.database.connection.Database;
import com.creedfreak.common.database.queries.QueryLib;
import com.creedfreak.common.utility.Logger;
import com.creedfreak.common.utility.UuidUtil;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TaskCheckUserExist extends DBTask {

	private static final String PREFIX = "-CheckUserExist";
	private final UUID mUserID;

	public TaskCheckUserExist (UUID playerID) {
		super (DatabaseTaskType.Check);
		mUserID = playerID;
	}

	// DEBUG: Test this class to make sure it works.
	public void run () {
		String subSystemPrefix = Thread.currentThread ().getName () + PREFIX;
		DBTask postConditionTask;
		Connection connection = mDataPool.dbConnect ();
		PreparedStatement prepStmt = null;
		ResultSet resultSet = null;

		try {
			prepStmt = connection.prepareStatement (QueryLib.checkUserExist);
			prepStmt.setBytes (1, UuidUtil.toBytes (mUserID));
			resultSet = prepStmt.executeQuery ();

			if (resultSet.next ()) {
				// TODO: What happens if this fails? How to we queue it up again?
				postConditionTask = new TaskLoadPlayer (mUserID, resultSet.getLong ("UserID"));
			} else {
				// TODO: Where is the player initially loaded into the PlayerManager?
				postConditionTask = new TaskSavePlayer (mUserID, resultSet.getString ("Username"));
			}

			this.queuePostConditionTask (postConditionTask);
		}
		catch (SQLException except) {
			Logger.Instance ().Error (subSystemPrefix, "Error occurred while executing TaskCheckUserExist! "
				+ "Message: " + except.getMessage ());
		}
		finally {
			Database.CloseResources (resultSet, prepStmt);
			Database.CloseConnection (connection);
		}
	}
}
