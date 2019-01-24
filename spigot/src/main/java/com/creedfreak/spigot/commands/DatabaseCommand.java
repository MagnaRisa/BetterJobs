package com.creedfreak.spigot.commands;

import com.creedfreak.common.container.IPlayer;
import com.creedfreak.spigot.container.CommandData;
import com.creedfreak.common.database.databaseConn.Database;

/**
 * The abstract command that controls the database operations
 */
public abstract class DatabaseCommand implements ICommand
{
    protected Database mDatabase;
    private CommandData mCommandData;

    protected DatabaseCommand (Database db, CommandData data)
    {
        mDatabase = db;
        mCommandData = data;
    }

    public String cmdName ()
    {
        return mCommandData.getCommandArg ();
    }

    /**
     * Checks a users permissions
     */
    public boolean checkPermission (IPlayer player)
    {
        return mCommandData.hasPerms (player);
    }

    /**
     * Grabs the description of the command
     */
    public String getDescription ()
    {
        return mCommandData.getDescription ();
    }

    /**
     * Grabs the usage of the command
     */
    public String getUsage ()
    {
        return mCommandData.getUsage ();
    }
}
