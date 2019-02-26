package com.creedfreak.spigot.commands.DatabaseCommands;

import com.creedfreak.common.commands.CommandData;
import com.creedfreak.common.commands.DatabaseCommand;
import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.database.databaseConn.Database;

public class CommandReaderTest extends DatabaseCommand {

	public CommandReaderTest (Database db) {
		super (db, new CommandData (
				"dbreadertest",
				"This command will test the SQLReader for SQL Files.",
				"/prof dbreadertest",
				"craftyprofessions.admin.debug"
		));
	}

	@Override
	public boolean execute (IPlayer sender, String... args) {
/*        mDatabase.dbConnect ();

        mDatabase.testReader (sender);*/

		return true;
	}
}