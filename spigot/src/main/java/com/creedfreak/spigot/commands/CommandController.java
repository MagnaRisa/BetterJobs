package com.creedfreak.spigot.commands;

import com.creedfreak.common.ICraftyProfessions;
import com.creedfreak.common.commands.AbsCmdController;
import com.creedfreak.common.commands.ICommand;
import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.container.PlayerManager;
import com.creedfreak.common.database.databaseConn.Database;
import com.creedfreak.common.exceptions.CommandException;
import com.creedfreak.common.exceptions.CommandNotFoundException;
import com.creedfreak.spigot.commands.DatabaseCommands.*;
import com.creedfreak.spigot.commands.ProfessionCommands.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * The Command Controller is the central hub for all of the command
 * initialization and the disabling of commands. Note that some commands
 * will be unable to be disabled but in terms of feature commands there will
 * be disabling of those commands.
 */
public class CommandController extends AbsCmdController implements CommandExecutor {

	public CommandController (ICraftyProfessions plugin) {
		super (plugin);

		initializeCommands ();
	}

	/**
	 * Processes the commands for players issuing CraftyProfessions Commands
	 *
	 * @param sender The sender of the command
	 * @param cmd    The command that is being issued
	 * @param label  The label of the command
	 * @param args   The arguments of the command, this can range from few arguments to a couple.
	 * @return True  - If the command succeeds
	 * False - If the command fails
	 */
	@Override
	public boolean onCommand (CommandSender sender, Command cmd, String label, String... args) {
		boolean retVal;
		Player player = obtainBukkitPlayer (sender);
		IPlayer mPlayer;
		ICommand command;

		if (player == null) {
			sender.sendMessage ("Only players can run these commands!");
			retVal = false;
		} else {
			try {
				if (checkArgsLength (args)) {
					command = loadCommand (args[0]);

					// TODO: The issue: We have to have some way to retrieve the player by DB ID.
					mPlayer = PlayerManager.Instance ().getPlayerByUUID (player.getUniqueId ());

					try {
						command.execute (mPlayer, args);
					}
					catch (final CommandException ex) {
						String message = ex.getMessage ();
						String executedCommand = ex.getExecutedCommand ();

						if (null == executedCommand) {
							player.sendMessage (message);
						} else {
							player.sendMessage (message + " for command " + executedCommand);
						}
//						player.sendMessage (ex.getMessage ());
//						player.sendMessage (command.getDescription ());
//						player.sendMessage (command.getUsage ());
					}
				}
			}
			catch (final CommandException except) {
				player.sendMessage (except.getMessage () + except.getExecutedCommand ());
			}
			catch (final CommandNotFoundException ex) {
				// Load the Help command and display all usable commands for a regular user.
				//	        	mPlayer = PlayerManager.Instance ().getPlayer (player.getUniqueId ());
				//	        	command = loadCommand ();
				player.sendMessage (ex.getMessage ());
			}
			finally {
				retVal = true;
			}
		}

		return retVal;
	}

	protected void initializeCommands () {
		Database db = mPlugin.getDatabase ();

		/*Profession Command Registration*/
		registerCommand (new CommandJoin ());
		registerCommand (new CommandInfo ());
		registerCommand (new CommandList ());
		registerCommand (new CommandProfile ());
		registerCommand (new CommandStats ());

		/*database Command Registration*/
		registerCommand (new CommandDevQuery (db));

		registerCommand (new CommandArchive (db));
		registerCommand (new CommandArchiveAll (db));
		registerCommand (new CommandLookup (db));
		registerCommand (new CommandResetProfile (db));
		registerCommand (new CommandTest (db));
		registerCommand (new CommandReaderTest (db));


		/*Random Command Registration*/
		// mPlugin.getCommand ("shower").setExecutor (new CommandShower ());
	}

	/*Obtained from MobArena Code as the unwrapping of a CommandSender*/
	private Player obtainBukkitPlayer (CommandSender sender) {
		Player player = (Player) sender;
		UUID playerUUID = player.getUniqueId ();
		return Bukkit.getPlayer (playerUUID);
	}
}
