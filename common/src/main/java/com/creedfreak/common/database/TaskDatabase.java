package com.creedfreak.common.database;

import net.jcip.annotations.Immutable;

@Immutable
public class TaskDatabase
{

}

enum DatabaseTaskType
{
	Player (1),
	Admin (0);
	
	private final int mPriority;
	
	DatabaseTaskType (int priority) { mPriority = priority; }
	
	int Priority () { return mPriority; }
}
