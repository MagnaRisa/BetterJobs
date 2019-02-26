package com.creedfreak.common.professions;

import java.util.HashMap;

/**
 * This enum is used to primarily be a consolidation
 * of the wage table names so the only point of access
 * to the files names should be by the use of this enum.
 * This will cut down on misspelling of the wage table
 * names and overall a cleaner look within the code.
 */
public enum TableType {
	Miner ("miner_wage.json", "Miner_Payout", "Ore_Affinity", "Stone_Affinity"),
	Alchemy ("alchemy_wage.json", "Alchemy_Payout"),
	Angler ("angler_wage.json", "Angler_Payout"),
	Architect ("architect_wage.json", "Architect_Payout"),
	Crafter ("crafter_wage.json", "Crafter_Payout"),
	Enchanter ("enchanter_wage.json", "Enchanter_Payout"),
	Farmer ("farmer_wage.json", "Farmer_Payout"),
	Knight ("knight_wage.json", "Knight_Payout"),
	Lumberjack ("lumberjack_wage.json", "Lumberjack_Payout"),
	Merchant ("merchant_wage.json", "Merchant_Payout");

	private final String mFileName;
	private final String mDefaultTier;
	private HashMap<String, String> mTierMap = new HashMap<> ();

	/**
	 * The Default constructor for a TableType enumerated object.
	 * Assigns the name of the file to read and write to, defines
	 * the default tier for the wage table, and assigns all other
	 * non default tiers to the mTierMap to be used at a later date.
	 *
	 * @param fileName    - The name of the file to read and write from
	 * @param defaultTier - The default tier to use for payouts
	 * @param wageTiers   - Any other tier that is not Default.
	 */
	TableType (String fileName, String defaultTier, String... wageTiers) {
		mFileName = fileName;
		mDefaultTier = defaultTier;

		if (0 != wageTiers.length) {
			for (String key : wageTiers) {
				mTierMap.put (key, key);
			}
		}
	}

	/**
	 * Returns the file name of the wage table.
	 *
	 * @return mFileName
	 */
	public String getFileName () {
		return mFileName;
	}

	/**
	 * Grabs the Map of tiers for the TableType.
	 *
	 * @return mTierMap
	 */
	public HashMap<String, String> getTableTiers () {
		return mTierMap;
	}

	/**
	 * Return the default tier of the TableType
	 *
	 * @return mDefaultTier
	 */
	public String getDefaultTier () {
		return mDefaultTier;
	}
}


