package com.creedfreak.spigot.commands;

import com.creedfreak.common.AbsCmdController;
import com.creedfreak.common.ICraftyProfessions;
import com.creedfreak.spigot.commands.DatabaseCommands.*;
import com.creedfreak.spigot.commands.ProfessionCommands.*;
import com.creedfreak.common.container.PlayerManager;
import com.creedfreak.common.database.databaseConn.Database;
import com.creedfreak.common.exceptions.*;
import com.creedfreak.spigot.container.SpigotPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * The Command Controller is the central hub for all of the command
 * initialization and the disabling of commands. Note that some commands
 * will be unable to be disabled but in terms of feature commands there will
 * be disabling of those commands.
 */
public class CommandController extends AbsCmdController implements CommandExecutor
{
    private ICraftyProfessions mPlugin;
    private HashMap<String, ICommand> mCommandMap;

    /**
     * Default constructor
     */
    public CommandController (ICraftyProfessions craftyProf)
    {
        mPlugin = craftyProf;
        mCommandMap = new HashMap<> ();

        initializeCommands ();
    }

    /**
     * Processes the commands for players issuing CraftyProfessions Commands
     *
     * @param sender The sender of the command
     *
     * @param cmd The command that is being issued
     *
     * @param label The label of the command
     *
     * @param args The arguments of the command, this can range from few arguments to a few.
     *
     * @return True  - If the command succeeds
     *         False - If the command fails
     */
    @Override
    public boolean onCommand (CommandSender sender, Command cmd, String label, String... args)
    {
        Player player = obtainBukkitPlayer (sender);
        SpigotPlayer mPlayer;
        ICommand command = null;

        if (player == null)
        {
            return false;
        }

        try
        {
            if (checkArgsLength (args))
            {
                command = loadCommand (args[0]);

                // TODO: I need to add in the appropriate checking and arg trimming devices before I can use the commands.
                mPlayer = (SpigotPlayer) PlayerManager.Instance ().retrievePlayer (player.getUniqueId ());
                command.execute (mPlayer, args);
                return true;
            }
        }
        catch (TooFewArgumentException | CommandNotFoundException ex)
        {
            player.sendMessage (ex.getMessage ());
            return true;
        }

        return false;
    }

    /**
     * This method will setup all of the commands for the plugin and
     * put them into the command map.
     */
    private void initializeCommands ()
    {
         Database db = mPlugin.cpGetDatabase ();

        /*Profession Command Registration*/
        registerCommand (new CommandJoin ());
        registerCommand (new CommandInfo ());
        registerCommand (new CommandList ());
        registerCommand (new CommandProfile ());
        registerCommand (new CommandStats ());

        /*Database Command Registration*/
        registerCommand (new CommandArchive (db));
        registerCommand (new CommandArchiveAll (db));
        registerCommand (new CommandLookup (db));
        registerCommand (new CommandResetProfile (db));
        registerCommand (new CommandTest (db));
        registerCommand (new CommandReaderTest (db));


        /*Random Command Registration*/
        // mPlugin.getCommand ("shower").setExecutor (new CommandShower ());
    }

    /**
     * Add the command to the Command Map
     *
     * @param cmd The command to add to the map
     */
    private void registerCommand (ICommand cmd)
    {
        mCommandMap.put (cmd.cmdName (), cmd);
    }

    /**
     * This method checks to see if the arguments passed into it are
     * valid for a CraftyProfession Command.
     *
     * @param args The Array of arguments to check the length of
     *
     * @return True If the argument length is > 0
     *
     * @throws TooFewArgumentException This Argument is thrown when someone
     * issues a command that has zero arguments.
     */
    private boolean checkArgsLength (String... args) throws TooFewArgumentException
    {
        if (!(args.length > 0))
        {
            throw new TooFewArgumentException (ChatColor.DARK_AQUA + "Too Few Arguments in command");
        }

        return true;
    }

    /*Obtained from MobArena Code as the unwrapping of a CommandSender*/
    private Player obtainBukkitPlayer (CommandSender sender)
    {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId ();
        return Bukkit.getPlayer (playerUUID);
    }

    /**
     * This method pulls the reference to the command who's string identifier is
     * the parameter arg.
     *
     * @param arg The string to hash to the command map and retrieve the command
     *
     * @return The ICommand with the specified command name.
     *
     * @throws CommandNotFoundException If mCommandMap.get returns null then we
     *         know that the command is not in the map and that we need to throw
     *         the exception.
     */
    private ICommand loadCommand (String arg) throws CommandNotFoundException
    {
        if (mCommandMap.get (arg) == null)
        {
            throw new CommandNotFoundException ();
        }

        return mCommandMap.get (arg);
    }
}
