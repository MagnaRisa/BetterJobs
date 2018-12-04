package com.creedfreak.spigot.commands;

import com.creedfreak.spigot.container.CommandData;
import com.creedfreak.spigot.container.SpigotPlayer;

/**
 * This is the Abstract class that implements an ICommand
 * this way I can keep all of the same responsibilities of a
 * command within here without recreating them in every command
 * class.
 */
public abstract class ProfessionCommand implements ICommand
{
    protected CommandData mCommandData;

    protected ProfessionCommand (CommandData data)
    {
        mCommandData = data;
    }

    /**
     * Returns the Command Name
     *
     * @return The command name
     */
    @Override
    public String cmdName ()
    {
        return mCommandData.getCommandArg ();
    }
    /**
     * Checks a users permissions by sending it into the
     * CommandData object associated with the command.
     * This way we aren't getting the permission and the
     * possibility to change it is zero.
     *
     * @param sender The command issuer
     *
     * @return True  - If the sender has the permission
     *         False - If the sender does not have the perms
     */
    @Override
    public boolean checkPermission (SpigotPlayer sender)
    {
        return mCommandData.hasPerms (sender);
    }

    @Override
    public String getDescription ()
    {
        return mCommandData.getDescription ();
    }

    @Override
    public String getUsage ()
    {
        return mCommandData.getUsage ();
    }
}
