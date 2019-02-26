package com.creedfreak.common.concurrent.database.tasks;

import com.creedfreak.common.database.databaseConn.Database;
import com.creedfreak.common.utility.Logger;

import java.util.concurrent.ExecutorService;

public abstract class DBTask implements Runnable {

	private final DatabaseTaskType mType;
	protected Database mDataPool = null;
	private ExecutorService mConsumers;

	public DBTask (DatabaseTaskType type) {
		mType = type;
	}

	public DBTask (DatabaseTaskType type, DBTask postCondition) {
		mType = type;
	}

	public DatabaseTaskType getType () {
		return mType;
	}

	public void attachDataPool (Database dataPool) {
		mDataPool = dataPool;
	}

	public void attachService (ExecutorService service) {
		mConsumers = service;
	}

	/**
	 * Attach this tasks data pool and executor service to the task
	 * that was passed into it. This will allow us to transfer
	 * these devices to other post condition tasks without
	 * having to pass around those devices as parameters.
	 *
	 * @param task - The task to attach this objects devices to.
	 * @return The modified task.
	 */
	protected DBTask attachDevicesToTask (DBTask task) {
		task.attachDataPool (mDataPool);
		task.attachService (mConsumers);
		return task;
	}

	protected void queuePostConditionTask (Runnable task) {
		mConsumers.submit (task);
	}

	public enum DatabaseTaskType {
		Save (false),
		Update (false),
		Delete (false),
		Insert (false),
		Query (true),
		Check (true),
		Poison (false);

		private boolean bReturnable;

		DatabaseTaskType (boolean returnable) {
			bReturnable = returnable;
		}

		public boolean getReturnable () {
			return bReturnable;
		}
	}

	public final class PoisonPill extends DBTask {

		PoisonPill () {
			super (DatabaseTaskType.Poison);
		}

		public void run () {
			Logger.Instance ().Debug ("DB Thread", "Found poison pill, shutting down gracefully!");
		}
	}
}
