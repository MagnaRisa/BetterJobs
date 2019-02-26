package com.creedfreak.common.container;

import com.creedfreak.common.professions.Profession;
import com.creedfreak.common.professions.TableType;
import com.google.common.primitives.UnsignedLong;
import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.Map;

;

/**
 * This class will copy a players state at a specific instance
 * of time. This will allow us to save a players information
 * based on a snapshot of their information. This will allow
 * us to use multi-threading to save the current active players
 * without significant synchronization on the IPlayer implementations.
 */
@Immutable
public class PlayerState {

	private final UnsignedLong mPlayerID;
	private final Integer mPlayerLevel;
	private final String mCurrentUsername;
	private final Map<TableType, Profession> mProfessionState;

	public PlayerState (UnsignedLong playerID, Integer playerLevel, String currentUsername,
	                    Map<TableType, Profession> profs) {
		mPlayerID = playerID;
		mPlayerLevel = playerLevel;
		mCurrentUsername = currentUsername;
		mProfessionState = profs;
	}
}
