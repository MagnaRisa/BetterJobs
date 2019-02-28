package com.creedfreak.common.professions;

public class ProfessionBuilder {

	/**
	 * Specifically builds a profession for a user who has information in the database.
	 *
	 * @return A Profession whose data is retrieved from the database.
	 */
	public static Profession dbBuild (String internalName, Integer internalID, String profStatus,
	                                  int level, int prestigeLevel, double expCurrent,
	                                  double expTotal) {
		String lowerProfName = internalName.toLowerCase ();
		Profession newProf;

		switch (lowerProfName) {
			case "miner":
				newProf = new ProfMiner (internalID, profStatus, level, prestigeLevel, expCurrent, expTotal);
				break;
			case "angler":
				newProf = null;
				break;
			case "lumberjack":
				newProf = null;
				break;
			case "knight":
				newProf = null;
				break;
			case "architect":
				newProf = null;
				break;
			case "farmer":
				newProf = null;
				break;
			default:
				newProf = null;
		}

		return newProf;
	}

	/**
	 * Builds a profession for the first time for a user who doesn't have information in the database.
	 *
	 * @param internalName - The Profession to Build
	 * @return The specified Profession
	 */
	public static Profession buildDefault (String internalName) {
		String lowerProfName = internalName.toLowerCase ();
		Profession newProf;

		switch (lowerProfName) {
			case "miner":
				newProf = new ProfMiner ();
				break;
			case "angler":
				newProf = null;
				break;
			case "lumberjack":
				newProf = null;
				break;
			case "knight":
				newProf = null;
				break;
			case "architect":
				newProf = null;
				break;
			case "farmer":
				newProf = null;
				break;
			default:
				newProf = null;
		}

		return newProf;
	}
}
