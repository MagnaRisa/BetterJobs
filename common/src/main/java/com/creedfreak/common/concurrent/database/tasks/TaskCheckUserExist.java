package com.creedfreak.common.concurrent.database.tasks;

import com.creedfreak.common.database.queries.queryLib;
import com.creedfreak.common.utility.UuidUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TaskCheckUserExist extends DBTask
{
	private final UUID mUserID;

	public TaskCheckUserExist (UUID playerID)
	{
		super (DatabaseTaskType.Check);
		mUserID = playerID;
	}

	// DEBUG: Test this class to make sure it works.
	public void run ()
	{
		DBTask postConditionTask;
		PreparedStatement prepStmt = null;
		ResultSet resultSet = null;

		try
		{
			prepStmt = mDataPool.dbConnect ().prepareStatement (queryLib.checkUserExist);
			prepStmt.setBytes (1, UuidUtil.toBytes (mUserID));
			resultSet = prepStmt.executeQuery ();

			if (resultSet.next ())
			{
				// TODO: What happens if this fails? How to we queue it up again?
				postConditionTask = new TaskLoadPlayer (mUserID, resultSet.getLong ("UserID"));
			}
			else
			{
				// TODO: Where is the player initially loaded into the PlayerManager?
				postConditionTask = new TaskSavePlayer (mUserID, resultSet.getString ("Username"));
			}

			this.queuePostConditionTask (postConditionTask);
		}
		catch (SQLException except)
		{
			mDataPool.dbCloseResources (prepStmt, resultSet);
			mDataPool.dbClose ();
		}
	}
}
