package com.creedfreak.spigot.container;

import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.container.IPlayerFactory;
import com.creedfreak.common.professions.Profession;
import com.creedfreak.common.utility.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class SpigotPlayerFactory implements IPlayerFactory
{
	private static final String PREFIX = "SpigotPlayerFactory";
	private static final String MESSAGE = "Cannot find player in Bukkit registry. Is the player offline?";

	// DEBUG: Debug and see if the `player` variable will always return null.
	//  Remember this runs on a separate thread!
	public IPlayer buildPlayer (Long playerID, Integer playerLevel, UUID playerUUID, String username)
	{
		Player entity = Bukkit.getPlayer (playerUUID);
		IPlayer player;

		if (entity != null)
		{
			player = new SpigotPlayer (playerID, username, playerLevel, entity);
		}
		else
		{
			Logger.Instance ().Error (PREFIX, MESSAGE);
			player = null;
		}
		return player;
	}

	public IPlayer buildPlayerWithProfessions (Long playerID, Integer playerLevel,
	                                           UUID playerUUID, String username,
	                                           List<Profession> professions)
	{
		IPlayer player = this.buildPlayer (playerID, playerLevel, playerUUID, username);

		if (player != null)
		{
			for (Profession prof : professions)
			{
				player.registerProfession (prof);
			}
		}
		else
		{
			Logger.Instance ().Error (PREFIX, MESSAGE);
		}
		
		return player;
	}
}
