package com.creedfreak.common.database;

import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.database.DAOs.AbsUsersDAO;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public class DatabaseTask
{
	private final DatabaseTaskType mType;
	private final String mQuery;
	
	public DatabaseTask (DatabaseTaskType type, String query)
	{
		mType = type;
		mQuery = query;
	}
	
	public DatabaseTaskType Type ()
	{
		return mType;
	}

	public String getQuery () { return mQuery; }

	public void execute (AbsUsersDAO dao)
	{
		String query = mQuery;

		
	}

	public IPlayer executeReturnable (AbsUsersDAO dao)
	{
		return null;
	}

	public boolean executeCheck (AbsUsersDAO dao)
	{
		return false;
	}

	public enum DatabaseTaskType
	{
		Save (false),
		Update (false),
		Delete (false),
		Insert (false),
		Query (true),
		Check (true),
		Poison (false);

		private boolean bReturnable;

		DatabaseTaskType (boolean returnable)
		{
			bReturnable = returnable;
		}

		public boolean getReturnable () { return bReturnable; }
	}
}
