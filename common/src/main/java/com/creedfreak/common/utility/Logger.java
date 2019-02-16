package com.creedfreak.common.utility;

import java.util.logging.Level;

/**
 * This is a wrapper for the Logger of the plugin. This is a singleton so we
 * are able to log from anywhere.
 */
public class Logger
{
	private static Logger ourInstance = new Logger ();

	private java.util.logging.Logger mLogger;
	private boolean mDebug;


	public static Logger Instance ()
	{
		return ourInstance;
	}

	private Logger ()
	{
		mLogger = null;
	}

	/**
	 * Initializes the Logger with the appropriate logger to use.
	 * This method is needed to set the logger of the Logger class
	 * so no matter what api we are using, whether it's sponge or
	 * spigot we can retrieve and use the correct Logger.
	 *
	 * @param logger - The correct Logger retrieved from a
	 *                  specific api call
	 */
	public void initLogger (java.util.logging.Logger logger)
	{
		if (mLogger != null)
		{
			mLogger.log (Level.WARNING, "Cannot reinitialize the Logger Class!");
		}
		else
		{
			mLogger = logger;
		}
	}

	/**
	 * Logs a message to the console
	 *
	 * If the Logger of the plugin is found to be NULL
	 * Then the plugin will retrieve the Logger from bukkit.
	 */
	private void Log (Level level, String message)
	{
		if (mLogger != null)
		{
			mLogger.log (level, message);
		}
	}

	private void Log (Level level, String subSystemPrefix, String message)
	{
		Log (level, "[" + subSystemPrefix + "] " + message);
	}

	private void DebugLog (Level level, String subSystemPrefix, String message)
	{
		Log (level, "[DEBUG] " + "[" + subSystemPrefix + "]" + message);
	}

	public void Info (String message)
	{
		this.Log (Level.INFO, message);
	}

	public void Info (String subSystemPrefix, String message)
	{
		this.Log (Level.INFO, subSystemPrefix, message);
	}

	public void Warn (String message)
	{
		this.Log (Level.WARNING, message);
	}

	public void Warn (String subSystemPrefix, String message)
	{
		this.Log (Level.WARNING, subSystemPrefix, message);
	}

	public void Error (String message)
	{
		this.Log (Level.SEVERE, message);
	}

	public void Error (String subSystemPrefix, String message)
	{
		this.Log (Level.SEVERE, subSystemPrefix, message);
	}

	public void Debug (String message)
	{
		if (mDebug)
		{
			Log (Level.INFO, "[DEBUG] " + message);
		}
	}

	public void Debug (String subSystemPrefix, String message)
	{
		if (mDebug)
		{
			this.DebugLog (Level.INFO, subSystemPrefix, message);
		}
	}

	/**
	 * Sets the mDebug flag to the value that is passed into the method.
	 */
	public void debugActive (boolean debug)
	{
		mDebug = debug;
	}
}
