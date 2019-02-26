package com.creedfreak.common.professions;

/**
 * This Interface outlines the common uses of a Wage Table for the
 * various professions within the Plugin.
 * <p>
 * This class is the "Flyweight" within the flyweight pattern
 * implemented to avoid having the WageTableHandler handle
 * all of the generic requests
 */
public interface IWageTable {

	/**
	 * Enumeration
	 * <p>
	 * The possible states of a wage table.
	 */
	public enum State {
		Enabled,
		Disabled
	}

	/**
	 * <p> This Method is used in order to Hash a certain
	 * Item, Block, Crafting recipe etc.. In order to
	 * obtain the wage that that Object has associated
	 * with it within the Wage Table. </p>
	 *
	 * @param element    This object to be mapped into the Table
	 * @param profStatus The status of the players profession
	 * @return The Value stored at that Table Location or null
	 * if the Table does not have the desired Item.
	 */
	float mapItem (String element, String profStatus)
			throws NullPointerException;

	/**
	 * <p> This method will allow us to read the wage table
	 * from a specific file specified from the given
	 * TableType parameter. In general all of the wage
	 * table names should be in the format of
	 * profession_wage.yml</p>
	 *
	 * @param resource The file to read from
	 */
	boolean readTable (String resource);

	/**
	 * <p> writeTable will essentially take any modified data
	 * and write it back to the file who's name is stored
	 * within the wage table itself. This method will be
	 * called whenever the command to modify the table
	 * is called. </p>
	 *
	 * @param resource The file in which to write to.
	 */
	boolean writeTable (String resource);

	/**
	 * This method will return an internal boolean which will
	 * signify if the WageTable has changed after it has been read
	 * in from. This will allow for quicker writing times whenever
	 * saving the wage tables.
	 *
	 * @return True  - If the Table has been modified
	 * False - If the Table has not been modified
	 */
	boolean hasChanged ();

	/**
	 * This method will allow the modification of a specific value
	 * within the wage table.
	 *
	 * @param key   The key whose value is to be changed
	 * @param value The value in which to modify the previous value
	 * @return True  - If the operation was a success
	 * False - If the operation was a failure
	 */
	boolean modifyValue (String key, Double value);

	/**
	 * Sets the state of the wage table. The table can be either
	 * enabled or disabled. If a table is disabled it should be
	 * removed from the users Professions list. If the table then
	 * gets re-enabled, it needs to be retrieved from the WTH.
	 *
	 * @param state - The current state of the table.
	 */
	void setState (State state);

	/**
	 * @return the current state of the Wage Table.
	 */
	State getState ();
}
