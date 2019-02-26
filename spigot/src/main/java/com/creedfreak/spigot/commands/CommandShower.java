package com.creedfreak.spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The Shower command is the first command I have created for the CraftyProfessions
 * Plugin and in essence is the Most Important command within the Plugin...
 * <p>
 * Just sayin...
 */
public class CommandShower implements CommandExecutor {

	/**
	 * This method is called when someone runs the command
	 */
	@Override
	public boolean onCommand (CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			sender.sendMessage ("#showersquad");
		}

		return true;
	}
}
