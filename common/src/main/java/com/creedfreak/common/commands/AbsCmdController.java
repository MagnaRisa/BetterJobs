package com.creedfreak.common.commands;

import com.creedfreak.common.ICraftyProfessions;
import com.creedfreak.common.exceptions.CommandException;
import com.creedfreak.common.exceptions.CommandNotFoundException;

import java.util.HashMap;

public abstract class AbsCmdController {

	protected ICraftyProfessions mPlugin;
	private HashMap<String, ICommand> mCommandMap;

	/**
	 * Default constructor
	 */
	public AbsCmdController (ICraftyProfessions plugin) {
		mPlugin = plugin;
		mCommandMap = new HashMap<> ();
	}

	/**
	 * This method will setup all of the commands for the plugin and
	 * put them into the command map.
	 */
	protected abstract void initializeCommands ();

	/**
	 * Add the command to the Command Map
	 *
	 * @param cmd The command to add to the map
	 */
	protected void registerCommand (ICommand cmd) {
		mCommandMap.put (cmd.cmdName (), cmd);
	}

	/**
	 * This method checks to see if the arguments passed into it are
	 * valid for a CraftyProfession Command.
	 *
	 * @param args The Array of arguments to check the length of
	 * @return True If the argument length is > 0
	 * @throws CommandException This Argument is thrown when someone
	 *                          issues a command that has zero arguments.
	 */
	protected boolean checkArgsLength (String... args) throws CommandException {
		if (!(args.length > 0)) {
			throw new CommandException ("Too Few Arguments in command ", args);
		}

		return true;
	}

	/**
	 * This method pulls the reference to the command who's string identifier is
	 * the parameter arg.
	 *
	 * @param arg The string to hash to the command map and retrieve the command
	 * @return The ICommand with the specified command name.
	 * @throws CommandNotFoundException If mCommandMap.get returns null then we
	 *                                  know that the command is not in the map and that we need to throw
	 *                                  the exception.
	 */
	protected ICommand loadCommand (String arg) throws CommandNotFoundException {
		if (mCommandMap.get (arg) == null) {
			throw new CommandNotFoundException ();
		}

		return mCommandMap.get (arg);
	}
}
