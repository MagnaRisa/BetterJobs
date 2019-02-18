package com.creedfreak.common.concurrent;

import com.creedfreak.common.database.DAOs.AbsUsersDAO;
import com.creedfreak.common.database.DatabaseTask;
import com.creedfreak.common.utility.Logger;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.*;

/**
 * This class utilizes the Producer/Consumer design pattern.
 * A Producer in this instance is an IPlayer in most cases or
 * the PlayerManager itself when an Update All task is requested.
 * The consumers are the worker threads which are managed from the
 * mTaskPool.
 *
 * All tasks are submitted to the mTaskQueue which is a blocking
 * queue. As tasks are added into the queue the worker threads
 * will retrieve those tasks and run them.
 */
@ThreadSafe
public class DatabaseWorkerQueue
{
	private final String SYSTEM_PREFIX = "DBTaskProcessor";
	private final int MIN_THREADS = 1;
	private final int MAX_THREADS = Runtime.getRuntime ().availableProcessors ();
	private final int QUEUE_THRESHOLD = 250;

	private final int mMaxUsableThreads;

	private volatile boolean mbIsAlive;

	private BlockingQueue<DatabaseTask> mWorkQueue = new LinkedBlockingQueue<> ();
	private BlockingDeque<Thread> mConsumers = new LinkedBlockingDeque<> ();

	private AbsUsersDAO mUsersDAO;
	
	/**
	 * Default constructor for the DatabaseWorkerQueue class.
	 *
	 * @param usersDAO The users database access object.
	 * @param numThreads The number of threads to be created in the thread pool.
	 */
	public DatabaseWorkerQueue (AbsUsersDAO usersDAO, int numThreads)
	{
		mbIsAlive = true;
		mUsersDAO = usersDAO;

		// Bounds check the number of threads passed in.
		// TODO: This may change in the future.
		if (numThreads > MAX_THREADS)
		{
			mMaxUsableThreads = MAX_THREADS;
		}
		else if (numThreads < MIN_THREADS)
		{
			mMaxUsableThreads = MIN_THREADS;
		}
		else
		{
			mMaxUsableThreads = numThreads;
		}

		mConsumers.push (new Thread (new DatabaseTaskConsumer (mWorkQueue, mUsersDAO)));
	}
	
	/**
	 * Adds a task to the task Queue.
	 *
	 * The reason we have used the offer() method in this instance rather
	 * than the put() method is simply that offer() won't block the
	 * producer thread/s. When we get inputs from a Player we don't know
	 * where that event it coming from whether it's on it's own thread
	 * or on the main thread since we are getting the event from an
	 * abstracted back-end. As such if we were to block a producer
	 * of the database tasks it could be blocking an event thread
	 * or main thread which we need to keep responsive. Instead we will
	 * log the error and throw an exception. That being said this queue
	 * should never fill up since it's "unbounded" to Integer.MAX_VALUE
	 *
	 * @param task The task to submit to the queue.
	 */
	public boolean addTask (DatabaseTask task)
	{
		boolean added;
		try
		{
			synchronized (this)
			{
				if (mWorkQueue.size () > QUEUE_THRESHOLD)
				{
					mConsumers.push (new Thread (new DatabaseTaskConsumer (mWorkQueue, mUsersDAO)));
				}
			}
			added = mWorkQueue.add (task);
		}
		catch (IllegalStateException except)
		{
			Logger.Instance ().Error (SYSTEM_PREFIX, "Something went wrong while adding a "
				+ "DB Task to the Queue! This should never happen! Please open an issue on our Github page with"
				+ "the stacktrace attached.");
			except.printStackTrace ();
			added = false;
		}
		return added;
	}
	
	/**
	 * The safe shutdown employs the Poison Pill technique which lets the threads gracefully shutdown
	 * after all of the tasks in the queue have been consumed.
	 *
	 * Usage: safeShutdown should only be used when cleaning up or shutting down the plugin.
	 *
	 */
	public void safeShutdown ()
	{
		try
		{
			for (Thread thread : mConsumers)
			{
				if (!thread.isInterrupted ())
				{
					mWorkQueue.add (new DatabaseTask (DatabaseTask.DatabaseTaskType.Poison, ""));
					thread.join ();
				}
			}
		}
		catch (InterruptedException except)
		{
			Logger.Instance ().Error (Thread.currentThread ().getName(), "Current thread threw Interrupt " +
					"Exception while waiting for Database Threads to terminate! Attempting a forced shutdown!");

			forceShutdown ();
		}
	}

	/**
	 * A forced shutdown should only occur as a last resort.
	 * This will manually interrupt each thread and force their interrupt policy.
	 */
	private void forceShutdown ()
	{
		for (Thread thread : mConsumers)
		{
			if (!thread.isInterrupted ())
			{
				thread.interrupt ();
			}
		}
	}
}
