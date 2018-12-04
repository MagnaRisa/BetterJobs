package com.creedfreak.sponge.container;

import com.creedfreak.common.container.AbsPlayerFactory;
import com.creedfreak.common.container.IPlayer;
import com.google.common.primitives.UnsignedLong;

public class SpongePlayerFactory extends AbsPlayerFactory
{
	public  IPlayer buildPlayer (UnsignedLong id, String username, Integer userLevel)
	{
		return new SpongePlayer (id, username, userLevel);
	}
}
