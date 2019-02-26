package com.creedfreak.common.commands;

import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.exceptions.CommandException;

/**
 * TODO: I need to do what the below comment states
 * <p>
 * I need to either add in the PlayerManager Reference to handle players
 * or use the Player Manager to retrieve the CraftyPlayer to pass into the
 * command rather than the Whole Manager.
 */
public interface ICommand {

	/**
	 * This is the primary method for executing a command.
	 */
	boolean execute (IPlayer sender, String... args) throws CommandException;

	/**
	 * This method returns the Name of the command.
	 */
	String cmdName ();

	/**
	 * Checks a users permissions
	 */
	void checkPermission (IPlayer player) throws CommandException;

	/**
	 * Grabs the description of the command
	 */
	String getDescription ();

	/**
	 * Grabs the usage of the command
	 */
	String getUsage ();

	/**
	 * Tests the argument length for a given command. If there are too many
	 * arguments then we return false.
	 */
	boolean argLength (int maxArgs, int argLength);
}
