package com.creedfreak.sponge.database;

import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.database.DAOs.AbsUsersDAO;
import com.creedfreak.common.database.databaseConn.Database;
import com.creedfreak.sponge.container.SpongePlayer;
import com.google.common.primitives.UnsignedLong;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpongeUsersDAO extends AbsUsersDAO
{
	public SpongeUsersDAO (Database db)
	{
		super (db);
	}

	public IPlayer playerFactory (UnsignedLong playerID, String username, Integer playerLevel, UUID playerUUID)
	{
		Optional<Player> spongeUser = Sponge.getServer ().getPlayer (playerUUID);
		IPlayer spongePlayer = null;

		if (spongeUser.isPresent ())
		{
			spongePlayer = new SpongePlayer (playerID, username, playerLevel, spongeUser.get ());
		}

		return spongePlayer;
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
