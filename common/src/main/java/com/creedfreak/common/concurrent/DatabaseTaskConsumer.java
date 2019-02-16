package com.creedfreak.common.concurrent;

import com.creedfreak.common.database.DatabaseTask;
import com.creedfreak.common.utility.Logger;

import java.util.concurrent.BlockingQueue;

public class DatabaseTaskConsumer implements Runnable
{
	private BlockingQueue<DatabaseTask> mWorkQueue;
	
	public DatabaseTaskConsumer (BlockingQueue<DatabaseTask> blockingQueue)
	{
		mWorkQueue = blockingQueue;
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
				
				/*
		
					Process the task here
				
				 */
				
				if (DatabaseTask.DatabaseTaskType.Poison == task.Type())
				{
					running = false;
					Logger.Instance ().Debug ("DB Thread", "Found poison pill, shutting down gracefully!");
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
