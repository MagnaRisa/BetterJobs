package com.creedfreak.common.concurrent;

import com.creedfreak.common.database.DAOs.AbsUsersDAO;
import com.creedfreak.common.database.DatabaseTask;
import com.creedfreak.common.utility.Logger;

import java.util.concurrent.BlockingQueue;

public class DatabaseTaskConsumer implements Runnable
{
	private BlockingQueue<DatabaseTask> mWorkQueue;
	private AbsUsersDAO mUsersDAO;
	
	public DatabaseTaskConsumer (BlockingQueue<DatabaseTask> blockingQueue, AbsUsersDAO usersDAO)
	{
		mWorkQueue = blockingQueue;
		mUsersDAO = usersDAO;
	}
	
	@Override
	public void run ()
	{
		boolean running = true;
		DatabaseTask task;
		try
		{
			while (running)
			{
				// The thread will block here until tasks have entered the queue.
				task = mWorkQueue.take ();
				if (DatabaseTask.DatabaseTaskType.Poison == task.Type())
				{
					running = false;
					Logger.Instance ().Debug ("DB Thread", "Found poison pill, shutting down gracefully!");
				}
				else
				{
					if (task.Type ().getReturnable ())
					{
						// This means we need to return something
					}
					else
					{

					}
				}
			}
		}
		catch (InterruptedException except)
		{
			// Implement shutdown policy
			Thread.currentThread ().interrupt ();
			Logger.Instance().Warn (Thread.currentThread ().getName() + "-dbtask","Caught interrupted exception! Cleaning up thread and shutting down.");
		}
		finally
		{
			running = false;
		}
	}
}
