package com.creedfreak.common.container;

import com.creedfreak.common.exceptions.TableStateException;
import com.creedfreak.common.professions.BlockTable;
import com.creedfreak.common.professions.IWageTable;
import com.creedfreak.common.professions.TableType;
import com.creedfreak.common.utility.Logger;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Wage Table Handler is used to manage all of the wage tables
 * and their references. This includes retrieving, instantiating,
 * writing to file, and removing of the wage tables.
 * <p>
 * The Wage Table Handler implements the method GetWageTable which
 * serves as the flyweight factory for obtaining references to the
 * flyweight object when needed.
 */
public class WageTableHandler {

	public static final Type DEFAULT_WT_TYPE =
			new TypeToken<HashMap<String, HashMap<String, Float>>> () {
			}.getType ();
	private static final String WTH_PREFIX = "WageTableHandler";
	private static final String RESOURCE_DIR = "wage_data/";

	private static WageTableHandler Instance = new WageTableHandler ();

	private HashMap<TableType, IWageTable> mTableHandler;
	private HashMap<TableType, IWageTable> mDisabledTables;

	private Logger mLogger;

	/**
	 * The default constructor for the WageTableHandler. This is private to
	 * prevent any other instantiations elsewhere to uphold the
	 * Singleton Pattern.
	 */
	private WageTableHandler () {
		mTableHandler = new HashMap<> ();
		mLogger = Logger.Instance ();
	}

	/**
	 * Return the singular instance of the Wage Table Handler.
	 *
	 * @return Instance - The only Wage Table Handler
	 */
	public static WageTableHandler getInstance () {
		return Instance;
	}

	/**
	 * InitializeWageTables will use the default wage table name array to
	 * read in all of the wage tables and store them within the internal
	 * Table Handler object.
	 * <p>
	 * This method also doubles as the initialization of the Flyweight factory
	 * which handles all of the Wage Tables for dispersal at a later time after
	 * they have been initialized.
	 */
	public void InitializeWageTables (boolean debug) {
		// Initialize all block tables here. If they can't get read in. Delete them.
		mTableHandler.put (TableType.Miner, new BlockTable (TableType.Miner));
		// mTableHandler.put (TableType.Architect, new BlockTable (TableType.Architect));

		// Initialize all item related tables here.
		// mTableHandler.put (TableType.Alchemy, new AlchemyWage ());

		if (debug) {
			mLogger.Debug (WTH_PREFIX, "Initializing Wage Tables...");
		}

		// Read in all of the wage tables within the wage table handler.
		for (Map.Entry<TableType, IWageTable> entry : mTableHandler.entrySet ()) {
			if (!(entry.getValue ().readTable (RESOURCE_DIR + entry.getKey ().getFileName ()))) {
				mLogger.Error (WTH_PREFIX, "File " + entry.getKey ().getFileName ()
						+ " could not be read in! Disabling!");
				deleteTable (entry.getKey ());
			}
			if (debug) {
				mLogger.Debug (WTH_PREFIX, "Initialized " + entry.getKey ().getFileName ());
			}
		}

		if (debug) {
			mLogger.Debug ("Initialization of Wage Tables are complete!");
		}
	}

	/**
	 * WriteWageTables will write out all of the wage tables to their appropriate
	 * files. In the event of changes made to the files marked by a bit within the
	 * wage table we will write that table out to file. This should only occur when
	 * the onDisable event has been fired.
	 */
	public void WriteWageTables () {
		for (Map.Entry<TableType, IWageTable> entry : mTableHandler.entrySet ()) {
			if (!(entry.getValue ().writeTable (entry.getKey ().getFileName ()))) {
				// TODO: Can save it to another file named in the same dir for inspection later.
				mLogger.Error (WTH_PREFIX, "Cannot write to " + entry.getKey ().getFileName ()
						+ " no modified values will be saved!");
			}
		}
	}

	/**
	 * ModifiedTables will return a list of Wage Tables that have been modified
	 * since runtime and has not been written already to their respective file.
	 * This is mainly used to display the modified files to a user to track.
	 *
	 * @return modifiedTables - The list of modified tables.
	 */
	public List<TableType> ModifiedTables () {
		List<TableType> modifiedTables = new ArrayList<> ();

		for (Map.Entry<TableType, IWageTable> entry : mTableHandler.entrySet ()) {
			if (entry.getValue ().hasChanged ()) {
				modifiedTables.add (entry.getKey ());
			}
		}

		return modifiedTables;
	}

	/**
	 * The factory method to retrieve the Wage Tables according to the
	 * flyweight pattern.
	 *
	 * @param tableType - The type of table reference to return.
	 * @return - mTableHandler.get(tableType) - The specified Wage Table.
	 */
	public IWageTable GetWageTable (TableType tableType) {
		return mTableHandler.get (tableType);
	}

	/**
	 * Disables a table based on the type that is passed into the method.
	 *
	 * @param type  - The table type to disable.
	 * @param state - The state of the table, either enabled(true) or disabled(false).
	 */
	private void setState (TableType type, IWageTable.State state) throws TableStateException {
		if (IWageTable.State.Disabled == state) {
			IWageTable disableRef = mTableHandler.remove (type);
			if (null != disableRef) {
				disableRef.setState (state);
				mDisabledTables.put (type, disableRef);
			} else {
				// INFO: If this state change came from a command, then the output will be the player.
				throw new TableStateException ("The table is already disabled!");
			}
		} else {
			IWageTable enableRef = mDisabledTables.remove (type);

			if (null == enableRef) {
				// INFO: Unwind stack till the command issuer (reference above INFO: tag)
				throw new TableStateException ("The table is already enabled!");
			} else {
				enableRef.setState (state);
				mTableHandler.put (type, enableRef);
			}
		}
	}

	/**
	 * Deletes the table from the WTH. This method should only be called if a fatal error
	 * occurs and for some reason we cannot retrieve the table information. For example
	 * when we read in tables from file, if the read fails we need to remove the wage
	 * table from being used.
	 */
	private void deleteTable (TableType type) {
		mTableHandler.remove (type);
	}
}