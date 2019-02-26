package com.creedfreak.common.professions;

import com.creedfreak.common.container.WageTableHandler;
import com.creedfreak.common.utility.JsonWrapper;

import java.io.IOException;
import java.util.HashMap;

/**
 * This implementation of a Wage Table is based around the Blocks
 * of Minecraft, this way any Professions that revolve around
 */
public class BlockTable implements IWageTable {

	/**
	 * So my intended purpose here is that whenever someone breaks or places
	 * a block which could be a possibility for any jobs if configured for it
	 * is to be able to handle multiple types of actions. So for instance If you
	 * have the Miner Job and your able to break iron and place it for Money, we
	 * will be able to have both types of actions both placing and breaking being
	 * handled within this table. For Blocks this may not be too big of a deal since
	 * you only really want to either place a block for a job or break it. But when handling
	 * actions like breaking and planting crops this type of table should work nicely.
	 */
	private HashMap mBlockMap;
	private TableType mTableType;

	private State mbState;
	private boolean mbHasChanged;

	public BlockTable (TableType tableType) {
		mTableType = tableType;
		mbHasChanged = false;
		mbState = State.Enabled;
	}

	@Override
	public boolean readTable (String resource) {
		boolean retVal = true;
		JsonWrapper wrapper = new JsonWrapper ();

		try {
			mBlockMap = wrapper.readJson (WageTableHandler.DEFAULT_WT_TYPE, resource);
		}
		catch (IOException except) {
			retVal = false;
		}

		return retVal;
	}

	/**
	 * This method will map the given item into the mBlockMap
	 * this will return a float value if the element is found
	 * or null if the item is not found within the Map.
	 *
	 * @param element The item to look for the in the BlockMap
	 * @return The BigDecimal Value that the Item maps to within mBlockMap
	 * @throws NullPointerException - If one of the hash map retrievals return
	 *                              null then when the method tries to access them we should get
	 *                              a null pointer exception. We need to deal with it wherever
	 *                              mapItem is called.
	 */
	@Override
	@SuppressWarnings ("unchecked")
	public float mapItem (String element, String profStatus) throws NullPointerException {
		// The Block may will always be in the form of HashMap<String, Double>
		HashMap<String, Float> internalMap = (HashMap<String, Float>) mBlockMap.get (profStatus);

		if (null == internalMap) {
			throw new NullPointerException ("Could not mapItem! Retrieval of Block Map returned Null");
		}

		return internalMap.get (element);
	}

	/**
	 * CURRENTLY UNTESTED! NEED TO REPLICATE THIS IN THE READING OF THE FILE AS WELL
	 * This method will write the table specified by the internal parents
	 * protected mTableType field.
	 *
	 * @param resource The file to write the json to.
	 */
	@Override
	public boolean writeTable (String resource) {
		boolean retVal = true;

		if (mbHasChanged) {
			JsonWrapper wrapper = new JsonWrapper ();

			try {
				wrapper.writeJson (mBlockMap, WageTableHandler.DEFAULT_WT_TYPE, resource);
			}
			catch (IOException except) {
				retVal = false;
			}
		}
		return retVal;
	}

	/**
	 * @return if the table has been changed or not.
	 */
	@Override
	public boolean hasChanged () {
		return mbHasChanged;
	}

	/**
	 * TODO: Intended only for Admins so there needs to be some sort of protection surrounding this method at the command level.
	 * TODO: Before we can implement this method the whole wage table structure needs to be synchronized.
	 *
	 * @param key   The key whose value is to be changed
	 * @param value The value in which to modify the previous value
	 * @return If the modification was valid or not.
	 */
	public boolean modifyValue (String key, Double value) {
		return false;
	}

	/**
	 * @return get the current state of this table.
	 */
	public State getState () {
		return mbState;
	}

	/**
	 * Sets the current state of the wage table.
	 *
	 * @param state - Is the table disabled(false) or enabled(true).
	 */
	public void setState (State state) {
		mbState = state;
	}
}
