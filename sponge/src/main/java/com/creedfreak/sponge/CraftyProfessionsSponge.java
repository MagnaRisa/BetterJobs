package com.creedfreak.sponge;

import com.creedfreak.common.utility.BuildConfig;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import javax.inject.Inject;

@Plugin (id = "craftyprofessions", name = "Crafty Professions", version = BuildConfig.SPONGE_VERSION,
		description = "The Sponge implementation of the CraftyProfessions Plugin")
public class CraftyProfessionsSponge {

	@Inject
	private Logger mLogger;

	@Listener
	public void onServerStart (GameStartedServerEvent event) {
		mLogger.info ("Spigot Crafty Professions is currently Running!");
	}
}
