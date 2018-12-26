package com.creedfreak.spigot.commands.ProfessionCommands;

import com.creedfreak.spigot.commands.ProfessionCommand;
import com.creedfreak.spigot.container.CommandData;
import com.creedfreak.spigot.container.SpigotPlayer;

/**
 * This command when executed will display the users stats
 * to the chat. Note that this is the basic version and will
 * not display some information whereas its counterpart
 * command of CommandProfile will give a book with your
 * full statistics within.
 */
public class CommandStats extends ProfessionCommand
{
    private CommandData mCommandData;

    public CommandStats ()
    {
        super (new CommandData (
            "stats",
            "This command will display a simple version of the players stats",
            "/prof stats",
            "craftyprofessions.use.stats"
        ));
    }

    public boolean execute (SpigotPlayer sender, String... args)
    {
        sender.sendMessage ("You have issued the command /prof stats");

        return true;
    }


}
