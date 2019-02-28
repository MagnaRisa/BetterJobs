package com.creedfreak.spigot.commands.DatabaseCommands;

import com.creedfreak.common.commands.CommandData;
import com.creedfreak.common.commands.DatabaseCommand;
import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.database.connection.Database;

/**
 * This command will archive a player to the archive database or multiple players based
 * on the command arguments
 */
public class CommandArchive extends DatabaseCommand {

	public CommandArchive (Database db) {
		super (db, new CommandData (
				"archive",
				"This command will send the specified player to be backed up within the Archive.",
				"/prof archive [PlayerName]",
				"craftyprofessions.admin.archive"
		));
	}

	@Override
	public boolean execute (IPlayer sender, String... args) {
		sender.sendMessage ("You have activated the /prof archive command");

		return true;
	}
}
