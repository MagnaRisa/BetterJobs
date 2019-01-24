package com.creedfreak.spigot.database;

import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.database.DAOs.AbsUsersDAO;
import com.creedfreak.common.database.databaseConn.Database;

import com.creedfreak.spigot.container.SpigotPlayer;
import com.google.common.primitives.UnsignedLong;
import org.bukkit.Bukkit;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.*;

public class SpigotUsersDAO extends AbsUsersDAO
{

	public SpigotUsersDAO (Database db)
	{
		super (db);
	}

	public IPlayer playerFactory (UnsignedLong playerID, String username, Integer playerLevel, UUID playerUUID)
	{
		return new SpigotPlayer (playerID, username, playerLevel, Bukkit.getPlayer (playerUUID));
	}

	private void loadProfessions (List<IPlayer> players) throws NotImplementedException
	{
		throw new NotImplementedException ();
	}

	private void loadAugments (UnsignedLong playerID) throws NotImplementedException
	{
		throw new NotImplementedException ();
	}
}