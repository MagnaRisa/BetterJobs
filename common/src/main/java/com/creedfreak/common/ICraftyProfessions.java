package com.creedfreak.common;

import com.creedfreak.common.commands.AbsCmdController;
import com.creedfreak.common.database.connection.Database;

import java.io.File;
import java.io.InputStream;

/**
 * This class is the main Abstraction layer for ICraftyProfessions
 * in which we can use multiple plugin architecture. For example
 * there is going to be a Spigot Version of ICraftyProfessions and
 * a Sponge Version of ICraftyProfessions. This will allow for
 * very minimal changes within the code when dealing with
 * different servers.
 * <p>
 * Planned Server Implementations: Spigot and Sponge
 * <p>
 * This interface outlines all of the common functionality
 * between both Spigot and Sponge implementations that
 * ICraftyProfessions will need to use throughout the
 * project.
 */
public interface ICraftyProfessions {

	/**
	 * This method outlines the economy hook setup
	 */
	boolean setupEconomy ();

	/**
	 * Get the Configuration of the Plugin
	 */
	AbsConfigController getConfigController ();

	AbsCmdController getCmdController ();

	/**
	 * Register the plugins listeners.
	 */
	void setupListeners ();

	/**
	 * Grab the database
	 */
	Database getDatabase ();

	void setupDatabase ();

	InputStream openResource (String resource);

	File getResourceFile ();
}
