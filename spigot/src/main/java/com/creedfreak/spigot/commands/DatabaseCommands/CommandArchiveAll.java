package com.creedfreak.spigot.commands.DatabaseCommands;

import com.creedfreak.spigot.commands.DatabaseCommand;
import com.creedfreak.spigot.container.CommandData;
import com.creedfreak.spigot.container.SpigotPlayer;
import com.creedfreak.common.database.databaseConn.Database;

/**
 * This command will handle the backing up of players in one database to another database,
 * or if given the correct arguments, it will only backup the players within the PlayerManager.
 */
public class CommandArchiveAll extends DatabaseCommand
{
    public CommandArchiveAll (Database db)
    {
        super (db, new CommandData (
            "archiveall",
            "This command will archive all of the users within the database.",
            "/prof archiveall",
            "spigot_craftyprofessions.admin.archiveall"
        ));
    }

    @Override
    public boolean execute (SpigotPlayer sender, String... args)
    {
        sender.sendMessage ("You have issued the archiveall command");

        return true;
    }
}
