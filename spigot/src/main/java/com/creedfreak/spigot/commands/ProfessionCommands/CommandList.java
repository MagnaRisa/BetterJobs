package com.creedfreak.spigot.commands.ProfessionCommands;

import com.creedfreak.spigot.commands.ProfessionCommand;
import com.creedfreak.spigot.container.CommandData;
import com.creedfreak.spigot.container.SpigotPlayer;

/**
 * This command will list all of the available jobs that a player can join.
 */
public class CommandList extends ProfessionCommand
{
    public CommandList ()
    {
        super (new CommandData (
            "list",
            "Lists the professions available for a user to choose from",
            "/prof list",
            "spigot_craftyprofessions.use.list"));
    }

    @Override
    public boolean execute (SpigotPlayer sender, String... args)
    {
        sender.sendMessage ("You have just executed /prof list");

        return true;
    }
}
