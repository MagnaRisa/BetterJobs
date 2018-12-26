package com.creedfreak.spigot.commands.ProfessionCommands;

import com.creedfreak.spigot.commands.ProfessionCommand;
import com.creedfreak.spigot.container.CommandData;
import com.creedfreak.spigot.container.SpigotPlayer;

/**
 * This command will handle the Profile command execution.
 */
public class CommandProfile extends ProfessionCommand
{
    public CommandProfile ()
    {
        super (new CommandData (
            "profile",
            "This command will give you a book that will display your profile in depth",
            "/prof profile",
            "craftyprofessions.use.profile"));
    }

    @Override
    public boolean execute (SpigotPlayer sender, String... args)
    {
        sender.sendMessage ("You have just executed /prof profile");

        return true;
    }
}
