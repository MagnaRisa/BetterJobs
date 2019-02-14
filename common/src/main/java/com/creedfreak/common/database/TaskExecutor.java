package com.creedfreak.common.database;

import com.creedfreak.common.database.DAOs.AbsUsersDAO;
import com.creedfreak.common.database.databaseConn.Database;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.PriorityBlockingQueue;

@ThreadSafe
public class TaskExecutor implements Runnable
{
	private ExecutingQueue mExecQueue;
	
	private AbsUsersDAO mUsersDAO;
	
	/**
	 * Default constructor for the TaskExecutor class.
	 */
	public TaskExecutor (AbsUsersDAO UsersDAO)
	{
		mExecQueue = new ExecutingQueue ();
		mUsersDAO = UsersDAO;
	}
	
	/**
	 * The purpose of TaskExecutor is to process Database
	 * tasks on a separate thread. The run override is used
	 * to process those commands. Sparing the Database tasks
	 * to this thread will I/O bound this thread and should
	 * increase throughput.
	 */
	public void run ()
	{
		try
		{
			while (true)
			{
			
			}
		}
		catch (InterruptedException except)
		{
			Thread.currentThread ().interrupt ();
		}
	}
	
	/**
	 * Adds a task to the underlying ExecutingQueue
	 *
	 * @param task
	 */
	public synchronized void addTask (TaskDatabase task)
	{
		mExecQueue.QueueTask (task);
	}
	
	/**
	 * [ExecutingQueue] - Static Nested Class
	 *
	 * This class is created whenever a TaskExecutor thread is created and ran. This
	 * will allow each thread to
	 */
	public static class ExecutingQueue
	{
		@GuardedBy("this")
		PriorityBlockingQueue<TaskDatabase> mProcessingQueue;
		
		synchronized void QueueTask (TaskDatabase task)
		{
			mProcessingQueue.put (task);
		}
		
	}
}
