package com.creedfreak.common.commands;

import com.creedfreak.common.container.IPlayer;

import java.util.HashMap;

/**
 * This class stores all of the information for a Command.
 *
 * Name: The name of the command
 * Description: The Description of the command
 * Usage: The usage of the command, For example: /prof [CommandName]
 * Permission: The Permission node this user requires to use the command.
 */
public class CommandData
{
    private final HashMap<String, String> mData = new HashMap<> ();

    /**
     * This method is the default constructor for the CommandData class for
     * a command which stores data according to the class header comment.
     */
    public CommandData (String name, String desc, String usage, String perm)
    {
        mData.put("name", name);
        mData.put("description", desc);
        mData.put("usage", usage);
        mData.put("permission", perm);
    }

    /**
     * This method is used to test a players permission to the command
     * this object is attached to.
     *
     * @param sender The person to test against the permission within mData.
     *
     * @return True  - If the CommandSender has access to this permission
     *         False - If the CommandSender does not have access to this permission
     */
    public boolean hasPerms (IPlayer sender)
    {
        return sender.checkPerms (mData.get("permission"));
    }

    /**
     * This method returns the command postfix of the attached
     *  after the command /prof is issued.
     */
    public String getCmdName ()
    {
        return mData.get("name");
    }

    /**
     * This method returns the description of the command.
     */
    public String getDescription ()
    {
        return mData.get ("description");
    }

    /**
     * Returns the usage of the command
     */
    public String getUsage ()
    {
        return mData.get("usage");
    }
}
