package com.creedfreak.spigot.commands.ProfessionCommands;

import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.commands.ProfessionCommand;
import com.creedfreak.common.commands.CommandData;

/**
 * This command will handle the Profile command execution.
 */
public class CommandProfile extends ProfessionCommand
{
    public CommandProfile ()
    {
        super (new CommandData (
            "profile",
            // TODO: Implement the book feature.
            // "This command will give you a book that will display your profile in depth",
            "This command will give you a in text look at what your profile looks like.",
            "/prof profile",
            "craftyprofessions.use.profile"));
    }

    @Override
    public boolean execute (IPlayer sender, String... args)
    {
        sender.sendMessage ("You have just executed /prof profile");

        return true;
    }
}
