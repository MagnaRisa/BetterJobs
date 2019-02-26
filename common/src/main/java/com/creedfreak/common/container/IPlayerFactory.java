package com.creedfreak.common.container;

import com.creedfreak.common.professions.Profession;

import java.util.List;
import java.util.UUID;

public interface IPlayerFactory
{
	IPlayer buildPlayer (Long playerID, Integer playerLevel, UUID playerUUID,
	                     String username);

	IPlayer buildPlayerWithProfessions (Long playerID, Integer playerLevel,
	                                    UUID playerUUID, String username,
	                                    List<Profession> professions);
}
