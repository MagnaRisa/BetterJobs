package com.creedfreak.spigot.commands.ProfessionCommands;

import com.creedfreak.common.commands.CommandData;
import com.creedfreak.common.commands.ProfessionCommand;
import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.exceptions.CommandException;
import com.creedfreak.common.professions.Profession;
import com.creedfreak.common.professions.ProfessionBuilder;

/**
 * This is the Join command for a Profession. If a player issues this command
 * it will join them into a Profession.
 */
public class CommandJoin extends ProfessionCommand {

	private final int MAX_ARGS = 2;

	/**
	 * The Default Constructor is used to setup the information
	 * surrounding the Profession Join Command
	 */
	public CommandJoin () {
		super (new CommandData (
				"join",
				"Joins the user to the specified Profession",
				"/prof join [ProfessionName]",
				"craftyprofessions.use.join"));
	}

	/**
	 * This method executes the command for the given class.
	 *
	 * @param sender The Sender of the command
	 * @param args   The arguments of the command.
	 * @return True  - If the command succeeds in any way including having not enough arguments or
	 * if some exception was thrown.
	 * False - If the command fails all checks
	 */
	public boolean execute (IPlayer sender, String... args) throws CommandException {
		boolean registered;
		Profession newProf;
		String professionName = args[1];

		newProf = ProfessionBuilder.buildDefault (professionName);

		if (!(argLength (MAX_ARGS, args.length))) {
			throw new CommandException ("Too many arguments in command " + mCommandData.getCmdName (), args);
		}

		checkPermission (sender);

		if (null == newProf) {
			throw new CommandException ("Incorrect Profession Name. Run /prof list to see a list of available profession names.", args);
		}

		// Do Work
		registered = sender.registerProfession (newProf);
		if (registered) {
			sender.sendMessage ("You are already registered for this profession.");
		}
		return registered;
	}

	public String cmdName () {
		return mCommandData.getCmdName ();
	}
}
