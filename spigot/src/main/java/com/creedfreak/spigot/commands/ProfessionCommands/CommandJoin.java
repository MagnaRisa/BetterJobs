package com.creedfreak.spigot.commands.ProfessionCommands;

import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.container.PlayerManager;
import com.creedfreak.common.professions.ProfessionBuilder;
import com.creedfreak.spigot.commands.ProfessionCommand;
import com.creedfreak.spigot.container.CommandData;
import com.creedfreak.spigot.container.SpigotPlayer;

/**
 * This is the Join command for a Profession. If a player issues this command
 * it will join them into a Profession.
 */
public class CommandJoin extends ProfessionCommand
{
    /**
     * The Default Constructor is used to setup the information
     * surrounding the Profession Join Command
     */
    public CommandJoin ()
    {
       super (new CommandData (
            "join",
            "Joins the user to the specified Profession",
            "/prof join [ProfessionName]",
            "craftyprofessions.use.join"));
    }

    /**
     * This method executes the command for the given class.
     *
     * @param sender The Sender of the command
     *
     * @param args The arguments of the command.
     *
     * @return True  - If the command succeeds in any way including having not enough arguments or
     *                   if some exception was thrown.
     *         False - If the command fails all checks
     */
    public boolean execute (IPlayer sender, String... args)
    {
        sender.sendMessage ("You have just executed /prof join");
        if (checkPermission (sender))
        {
            if (sender.registerProfession (ProfessionBuilder.buildDefault (args[0])))
            {
                sender.sendMessage ("You are already registered for this profession.");
            }
        }

        return true;
    }

    public String cmdName ()
    {
        return mCommandData.getCommandArg ();
    }
}
