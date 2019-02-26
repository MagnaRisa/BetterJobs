package com.creedfreak.common.concurrent.database;

import com.creedfreak.common.concurrent.database.tasks.DBTask;
import com.creedfreak.common.database.databaseConn.Database;
import com.creedfreak.common.utility.Logger;
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
	private final int THREAD_TERMINATE = 15;
	private final int MIN_THREADS = 1;
	private final int MAX_THREADS = Runtime.getRuntime ().availableProcessors ();
	private final int mMaxUsableThreads;

	private ExecutorService mConsumers;
	private Database mDataPool;
	
	/**
	 * Default constructor for the DatabaseWorkerQueue class.
	 *
	 * @param dataPool The database to access.
	 * @param numThreads The number of threads to be created in the thread pool.
	 */
	public DatabaseWorkerQueue (Database dataPool, int numThreads)
	{
		mDataPool = dataPool;

		// Bounds check the number of threads passed in.
		// TODO: This may change in the future.
		if (numThreads > (MAX_THREADS / 2))
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
		mConsumers = Executors.newFixedThreadPool (mMaxUsableThreads);
	}
	
	/**
	 * Adds a task to the task Queue. This also includes the attachments
	 * of the DataPool to the task as well as
	 *
	 * @param task The task to submit to the internal executor service.
	 */
	public void addTask (DBTask task)
	{
		task.attachDataPool (mDataPool);
		task.attachService (mConsumers);
		mConsumers.submit (task);
	}
	
	/**
	 * Shutdown the consumer thread pool
	 */
	public void safeShutdown ()
	{
		mConsumers.shutdown ();
		try
		{
			mConsumers.awaitTermination (THREAD_TERMINATE, TimeUnit.SECONDS);
		}
		catch (InterruptedException except)
		{
			mConsumers.shutdownNow ();
			Thread.currentThread ().interrupt ();
		}
	}
}
