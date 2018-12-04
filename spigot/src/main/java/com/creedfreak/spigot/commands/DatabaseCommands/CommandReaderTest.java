package com.creedfreak.spigot.commands.DatabaseCommands;

import com.creedfreak.spigot.commands.DatabaseCommand;
import com.creedfreak.spigot.container.CommandData;
import com.creedfreak.spigot.container.SpigotPlayer;
import com.creedfreak.common.database.databaseConn.Database;

public class CommandReaderTest extends DatabaseCommand
{
    public CommandReaderTest (Database db)
    {
        super (db, new CommandData (
            "dbreadertest",
            "This command will test the SQLReader for SQL Files.",
            "/prof dbreadertest",
            "spigot_craftyprofessions.admin.debug"
        ));
    }

    @Override
    public boolean execute (SpigotPlayer sender, String... args)
    {
/*        mDatabase.dbConnect ();

        mDatabase.testReader (sender);*/

        return true;
    }
}