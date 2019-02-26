package com.creedfreak.sponge.container;

import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.professions.Profession;
import com.creedfreak.common.professions.TableType;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class SpongePlayer implements IPlayer {

	private Player mPlayer;

	private Long mPlayerID;
	private String mDBUsername;
	private Integer mPlayerLevel;

	public SpongePlayer (Long dbID, String currentDBuname, Integer playerLevel, Player player) {
		mPlayerID = dbID;
		mDBUsername = currentDBuname;
		mPlayerLevel = playerLevel;
		mPlayer = player;
	}

	public UUID getUUID () {
		return mPlayer.getUniqueId ();
	}

	public Long getInternalID () {
		return mPlayerID;
	}

	public String getUsername () {
		return mPlayer.getName ();
	}

	public Integer getLevel () {
		return mPlayerLevel;
	}

	public void sendMessage (String message) {

	}

	public boolean checkPerms (final String perm) {
		return false;
	}

	public float payoutPlayerPool () {
		return 0.0f;
	}

	@Override
	public boolean registerProfession (Profession prof) {
		return false;
	}

	@Override
	public void registerProfession (List<Profession> professions) {

	}

	@Override
	public boolean unregisterProfession (TableType prof) {
		return false;
	}

	public void listProfessions () {

	}

	@Override
	public Collection<Profession> getProfessionCollection () {
		return null;
	}

	@Override
	public Profession getProfession (TableType type) {
		return null;
	}

	public void doWork (String elementName) {

	}
}
