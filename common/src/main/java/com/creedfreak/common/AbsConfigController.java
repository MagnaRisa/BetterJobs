package com.creedfreak.common;

public abstract class AbsConfigController {
	// This class should only handle the main config
	// file for the plugin. This should not touch
	// any of the wage files.
	// Implement the Commonality of Spigot and Sponge

	public abstract Integer getInt (String path);

	public abstract Double getDouble (String path);

	public abstract String getString (String path);
}
