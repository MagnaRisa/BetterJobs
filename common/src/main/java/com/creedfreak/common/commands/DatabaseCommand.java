package com.creedfreak.common.commands;

import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.database.databaseConn.Database;
import com.creedfreak.common.exceptions.CommandException;

/**
 * The abstract command that controls the database operations
 */
public abstract class DatabaseCommand implements ICommand {

	protected Database mDatabase;
	private CommandData mCommandData;

	protected DatabaseCommand (Database db, CommandData data) {
		mDatabase = db;
		mCommandData = data;
	}

	public String cmdName () {
		return mCommandData.getCmdName ();
	}

	/**
	 * Checks a users permissions
	 */
	public void checkPermission (IPlayer sender) throws CommandException {
		if (!(mCommandData.hasPerms (sender))) {
			throw new CommandException ("Insufficient Permissions!");
		}
	}

	/**
	 * Grabs the description of the command
	 */
	public String getDescription () {
		return mCommandData.getDescription ();
	}

	/**
	 * Grabs the usage of the command
	 */
	public String getUsage () {
		return mCommandData.getUsage ();
	}

	public boolean argLength (int maxArgs, int argLength) {
		return (maxArgs == argLength);
	}
}
