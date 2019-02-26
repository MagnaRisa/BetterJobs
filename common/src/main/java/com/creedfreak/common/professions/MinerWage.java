package com.creedfreak.common.professions;

import java.math.BigDecimal;

// Convert this to Json


/**
 * TODO: Is this class still needed? We only really need a block map now. The table type is defined by a TableType.
 * This Class is the implementation for the MinerWage Table
 * used to fetch the wage of a specific block mined or broken
 * based on that blocks name.
 */
@Deprecated
public class MinerWage extends BlockTable {

	public static final String MINER_PAYOUT = "Miner_Payout";
	private static final String STONE_AFFINITY = "Stone_Affinity";
	private static final String ORE_AFFINITY = "Ore_Affinity";
	private static final String MINER_WAGE_TABLE = "MinerWage";

	public MinerWage () {
		super (TableType.Miner);
	}

	/**
	 * TODO: This needs Syncronization before it can function appropriately
	 * The goal of this method is to be able to modify a value within the
	 * Wage Table from a command or by other means.
	 *
	 * @param value The new value to modify the WageTable with
	 * @param path  The path to the key whose value you want to change
	 * @return True  - If the value change was successful
	 * False - If the value change was unsuccessful
	 */

	public boolean modifyValue (BigDecimal value, String... path) {
//        Map<String, BigDecimal> map = mBlockMap.get (path[0]);
//        if (map == null)
//        {
//            return false;
//        }
//
//        BigDecimal oldValue = map.get (path[1]);
//        if (oldValue == null)
//        {
//            return false;
//        }
//
//        mBlockMap.get(path[0]).put (path[1], value);
		return true;
	}
}
