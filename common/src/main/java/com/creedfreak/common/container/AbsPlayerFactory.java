package com.creedfreak.common.container;

import com.google.common.primitives.UnsignedLong;

public abstract class AbsPlayerFactory
{
	abstract public IPlayer buildPlayer (UnsignedLong id, String username, Integer userLevel);
}
