package com.creedfreak.common.database;

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
	
	public enum DatabaseTaskType
	{
		Save,
		Update,
		Delete,
		Insert,
		Poison
	}
}
