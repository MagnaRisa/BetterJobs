package com.creedfreak.spigot.container;

import com.creedfreak.common.container.AbsPlayerFactory;
import com.creedfreak.common.container.IPlayer;
import com.google.common.primitives.UnsignedLong;

public class SpigotPlayerFactory extends AbsPlayerFactory
{
	public  IPlayer buildPlayer (UnsignedLong id, String username, Integer userLevel)
	{
		return new SpigotPlayer (id, username, userLevel);
	}
}
