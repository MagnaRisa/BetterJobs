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
	@GuardedBy ("this")
	
	// Consumer Thread Pool
	private ExecutorService mConsumers;
	private BlockingQueue<DatabaseTask> mWorkQueue;
	
	private AbsUsersDAO mUsersDAO;
	
	/**
	 * Default constructor for the DatabaseWorkerQueue class.
	 *
	 * @param UsersDAO The users database access object.
	 * @param numThreads The number of threads to be created in the thread pool.
	 */
	public DatabaseWorkerQueue (AbsUsersDAO UsersDAO, int numThreads)
	{
		mWorkQueue = new LinkedBlockingQueue<> ();
		mConsumers = Executors.newFixedThreadPool (numThreads, Executors.defaultThreadFactory ());
		mUsersDAO = UsersDAO;
		
		// launch the initial database thread
		mConsumers.execute (new DatabaseTaskConsumer (mWorkQueue));
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
			added = mWorkQueue.add (task);
		}
		catch (IllegalStateException except)
		{
			Logger.Instance ().Error ("DBTaskProcessor", "Something went wrong while adding a "
				+ "DB Task to the Queue! This should never happen! Please open an issue on our Github page with"
				+ "the stacktrace attached.");
			except.printStackTrace ();
			added = false;
		}
		return added;
	}
	
	/**
	 * safeShutdown employs the Poison Pill technique which lets the threads gracefully shutdown
	 * after all of the tasks in the queue have been consumed.
	 */
	public void safeShutdown ()
	{
		// foreach thread add a poison pill to the queue.
		// TODO: Finish Implementing
		mWorkQueue.add (new DatabaseTask (DatabaseTask.DatabaseTaskType.Poison, ""));
	}
	
	public void shutdown ()
	{
		if (!mConsumers.isTerminated ())
		{
			mConsumers.shutdown ();
		}
	}
	
//	/**
//	 * [ExecutingQueue] - Static Nested Class
//	 *
//	 * This class is created whenever a DatabaseWorkerQueue thread is created and ran. This
//	 * will allow each thread to
//	 */
//	public static class ExecutingQueue
//	{
//		@GuardedBy("this")
//		PriorityBlockingQueue<DatabaseTaskConsumer> mProcessingQueue;
//
//		synchronized void QueueTask (DatabaseTaskConsumer task)
//		{
//			mProcessingQueue.put (task);
//		}
//
//	}
}
