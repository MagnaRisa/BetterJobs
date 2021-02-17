package com.creedfreak.spigot;

import com.creedfreak.common.AbsConfigController;
import com.creedfreak.common.ICraftyProfessions;
import com.creedfreak.common.commands.AbsCmdController;
import com.creedfreak.common.container.PlayerManager;
import com.creedfreak.common.container.WageTableHandler;
import com.creedfreak.common.database.connection.Database;
import com.creedfreak.common.database.connection.DatabaseFactory;
import com.creedfreak.common.utility.Logger;
import com.creedfreak.spigot.commands.CommandController;
import com.creedfreak.spigot.config.ConfigController;
import com.creedfreak.spigot.container.SpigotPlayerFactory;
import com.creedfreak.spigot.listeners.CoreListener;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Material;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.logging.Level;

/**
 * Authors: CreedTheFreak
 * <p>
 * This is the entry point class for the CraftyProfessions plugin.
 */
public class CraftyProfessionsSpigot extends JavaPlugin implements ICraftyProfessions {

	// Currently edited from here, will be a config option at a later date.
	private static boolean mDebug = true;

	private Logger mLogger = null;
	private static Economy mEconomy = null;
	private static Permission mPermissions = null;
	private static Chat mChat = null;
	private CoreListener mCoreListener;

	// Config Details
	private ConfigController mConfigController = null;

	// Command Controller
	private CommandController mCommandController;

	// database Information
	private Database mDatabase;

	// WageTables
	private WageTableHandler mTableHandler = null;

	/**
	 * @return The debug status of the plugin
	 */
	public static boolean debug () {
		return mDebug;
	}

	/**
	 * This method will do all of the plugins initialization of
	 * various objects like commands, events, etc...
	 */
	@Override
	public void onEnable () {
		// If the economy is not found then disable the plugin and stop.
		if (!setupEconomy ()) {
			getServer ().getPluginManager ().disablePlugin (this);
			return;
		}

		mLogger = Logger.Instance ();
		mLogger.initLogger (getLogger ());

		mConfigController = new ConfigController (this);
		mConfigController.createDefaultConfig ();
		mConfigController.registerConfigFiles ();

		// Setup debug logging if config value is true
		mLogger.debugActive(mConfigController.getDebug());

		setupDatabase ();
		this.registerListeners ();

		setupPermissions ();
		setupChat ();

		mCoreListener.setEconomyHook (mEconomy);

		mTableHandler = WageTableHandler.getInstance ();
		mTableHandler.InitializeWageTables (mConfigController.getDebug ());

		/* This does more than just initialize the PlayerManager. Please read the docs on PlayerManager
		 * to see all that is going on under the hood. */
		PlayerManager.Instance ().preparePlayerManager (mDatabase, new SpigotPlayerFactory (), 1);
		mLogger.Debug("TEST!!!!");
		mLogger.Debug ("Initialization of CraftyProfessions Completed!");
	}

	/**
	 * This method will call anything that needs to be disabled when
	 * the plugin gets disabled via command or on a server shutdown.
	 */
	@Override
	public void onDisable () {
		// Finish up Database Tasks and Shutdown Thread Pool
		PlayerManager.Instance ().cleanupPlayerManager ();
	}

	/**
	 * This method will test if Vault is installed and if there is a
	 * registered Economy Plugin. This method will only return true
	 * if there is an economy plugin that is registered and if Vault
	 * is installed and enabled.
	 */
	public boolean setupEconomy () {
		if (null == getServer ().getPluginManager ().getPlugin ("Vault")) {
			getLogger ().severe ("Vault is not Installed! Disabling Plugin");
			return false;
		}

		RegisteredServiceProvider<Economy> rsp = getServer ().getServicesManager ().getRegistration (Economy.class);

		if (null == rsp) {
			getLogger ().severe ("No Economy Plugin Found! Disabling Plugin");
			return false;
		}

		mEconomy = rsp.getProvider ();
		return mEconomy != null;
	}

	/**
	 * This method will return whether or not the chat provider
	 * has been setup or not.
	 */
	private boolean setupChat () {
		RegisteredServiceProvider<Chat> rsp = getServer ().getServicesManager ().getRegistration (Chat.class);

		if (null != rsp) {
			mChat = rsp.getProvider ();
		}

		return mChat != null;
	}

	/**
	 * This method will return whether or not the Permissions provider
	 * has been setup or not.
	 */
	private boolean setupPermissions () {
		RegisteredServiceProvider<Permission> rsp = getServer ().getServicesManager ().getRegistration (Permission.class);

		if (null != rsp) {
			mPermissions = rsp.getProvider ();
		}

		return (mPermissions != null);
	}

	/**
	 * Returns the Economy
	 */
	public static Economy getEconomy () {
		return mEconomy;
	}

	/**
	 * Returns the Permissions
	 */
	public static Permission getPermissions () {
		return mPermissions;
	}

	/**
	 * Returns the Chat
	 */
	public static Chat getChat () {
		return mChat;
	}

	/**
	 * Returns the database we are using
	 */
	public Database getDatabase () {
		return mDatabase;
	}

	/**
	 * This method registers all of the needed listeners for the Plugin
	 */
	private void registerListeners () {
		PluginManager pluginManager = this.getServer ().getPluginManager ();
		mCommandController = new CommandController (this);
		mCoreListener = new CoreListener (this);

		// Register the CraftyProfession Command Entry Point
		getCommand ("prof").setExecutor (mCommandController);

		// Register the Core Listener of CraftyProfessions
		pluginManager.registerEvents (mCoreListener, this);
	}

	/**
	 * NEED TO DELETE THIS LATER DEBUGGING ONLY
	 * This method is Purely for the Use of obtaining a list of Materials to access later
	 */
	private void writeMaterials () {
		final String FILE_NAME = "C:\\Users\\Logan Cookman\\Desktop\\materials.txt";

		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			fw = new FileWriter (FILE_NAME);
			bw = new BufferedWriter (fw);

			for (Material material : Material.values ()) {
				bw.write (material.toString () + "\n");
			}
		}
		catch (Exception e) {
			e.printStackTrace ();
		}
		finally {
			getLogger ().log (Level.INFO, "Writing Materials is DONE!");
			try {
				if (bw != null) {
					bw.close ();
				}
				if (fw != null) {
					fw.close ();
				}
			}
			catch (Exception e) {
				e.printStackTrace ();
			}
		}
	}

	/**
	 * Get the Configuration Object of the Plugin
	 */
	public AbsConfigController getConfigController () {
		return null;
	}

	/**
	 * Get the Command Controller of the Plugin
	 */
	public AbsCmdController getCmdController () {
		return null;
	}

	/**
	 * Register the plugins listeners.
	 */
	public void setupListeners () {

	}

	/**
	 * Sets up the database using the Spigot Config File
	 */
	public void setupDatabase () {
		getLogger ().info ("Setting up database connection and Checking for "
				+ "created database this might take awhile ...");

		mDatabase = DatabaseFactory.buildDatabase (this, mConfigController);

		if (!mDatabase.initializeDatabase ()) {
			Logger.Instance ().Error ("onEnable", "database didn't Initialize, Disabling Plugin!");
			getServer ().getPluginManager ().disablePlugin (this);
		}
	}

	/**
	 * Retrieve a resource of the plugin.
	 */
	public InputStream openResource (String resource) {
		return this.getResource (resource);
	}

	/**
	 * Retrieve the default directory of the plugin resource folder.
	 */
	public File getResourceFile () {
		return this.getDataFolder ();
	}
}
