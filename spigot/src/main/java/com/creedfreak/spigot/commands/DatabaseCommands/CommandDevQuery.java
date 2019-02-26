package com.creedfreak.spigot.commands.DatabaseCommands;

import com.creedfreak.common.commands.CommandData;
import com.creedfreak.common.commands.DatabaseCommand;
import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.database.databaseConn.Database;
import com.creedfreak.common.exceptions.CommandException;

import java.util.List;
import java.util.UUID;

/**
 * TODO: This is purely for testing while in game. Delete this before alpha release!
 */
public class CommandDevQuery extends DatabaseCommand {

	public CommandDevQuery (Database db) {
		super (db, new CommandData (
				"query",
				"This command will query the database with the input query.",
				"/prof query",
				"craftyprofessions.admin.query"
		));
	}

	@Override
	public boolean execute (IPlayer sender, String... args) throws CommandException {
		String query = args[1];
		List<StringBuilder> queryInfo;

		if (sender.getUUID ().equals (UUID.fromString ("72f46c44-9c4e-4590-85f2-c92c1de478f1"))) {
			if (null == query) {
				throw new CommandException ("The query type cannot be null!", args);
			}

			switch (query) {
				case "profession":
					// queryInfo =
				case "user":
					// queryInfo =
				default:
					queryInfo = null;
			}

			if (null == queryInfo) {
				throw new CommandException ("Invalid query! Options: profession, user, ... ");
			}

			for (StringBuilder builder : queryInfo) {
				sender.sendMessage (builder.toString ());
			}
		} else {
			throw new CommandException ("You are not allowed to use this command!", args);
		}

		return true;
	}
}
