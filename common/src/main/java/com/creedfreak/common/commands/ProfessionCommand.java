package com.creedfreak.common.commands;

import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.exceptions.CommandException;

/**
 * This is the Abstract class that implements an ICommand
 * this way I can keep all of the same responsibilities of a
 * command within here without recreating them in every command
 * class.
 */
public abstract class ProfessionCommand implements ICommand {

	protected CommandData mCommandData;

	protected ProfessionCommand (CommandData data) {
		mCommandData = data;
	}

	@Override
	public String cmdName () {
		return mCommandData.getCmdName ();
	}

	@Override
	public void checkPermission (IPlayer sender) throws CommandException {
		if (!(mCommandData.hasPerms (sender))) {
			throw new CommandException ("Insufficient Permissions!");
		}
	}

	@Override
	public String getDescription () {
		return mCommandData.getDescription ();
	}

	@Override
	public String getUsage () {
		return mCommandData.getUsage ();
	}

	@Override
	public boolean argLength (int maxArgs, int argLength) {
		return (maxArgs == argLength);
	}
}
