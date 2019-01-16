package com.creedfreak.sponge.container;

import com.creedfreak.common.container.IPlayer;
import com.google.common.primitives.UnsignedLong;
import org.spongepowered.api.entity.living.player.Player;

import java.util.UUID;

public class SpongePlayer implements IPlayer
{
	private Player mPlayer;

	private UnsignedLong mPlayerID ;
	private String  mDBUsername;
	private Integer mPlayerLevel;

	public SpongePlayer (UnsignedLong dbID, String currentDBuname, Integer playerLevel)
	{
		mPlayerID = dbID;
		mDBUsername = currentDBuname;
		mPlayerLevel = playerLevel;
	}

	public UUID getUUID ()
	{
		return mPlayer.getUniqueId ();
	}

<<<<<<< HEAD
	public UnsignedLong getDBIdentifier ()
	{
		return mPlayerID;
	}

=======
>>>>>>> 908cc8698a07640d82aa4b3010d3cc435454c222
	public String getUsername ()
	{
		return mPlayer.getName ();
	}

	public Integer getLevel  ()
	{
		return mPlayerLevel;
	}

	public void sendMessage (String message)
	{

	}

	public boolean checkPerms (final String perm)
	{
		return false;
	}

	public float payoutPlayerPool ()
	{
		return 0.0f;
	}

	public void listProfessions ()
	{

	}

	public void doWork (String elementName)
	{

	}
}
