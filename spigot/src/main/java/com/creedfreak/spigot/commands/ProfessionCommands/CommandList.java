package com.creedfreak.spigot.commands.ProfessionCommands;

import com.creedfreak.common.commands.CommandData;
import com.creedfreak.common.commands.ProfessionCommand;
import com.creedfreak.common.container.IPlayer;

/**
 * This command will list all of the available jobs that a player can join.
 */
public class CommandList extends ProfessionCommand {

	public CommandList () {
		super (new CommandData (
				"list",
				"Lists the professions available for a user to choose from",
				"/prof list",
				"craftyprofessions.use.list"));
	}

	@Override
	public boolean execute (IPlayer sender, String... args) {
		sender.sendMessage ("You have just executed /prof list");

		return true;
	}
}
