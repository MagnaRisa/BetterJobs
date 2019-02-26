package com.creedfreak.common.utility;

public class LevelEquation {

	private static Double msBaseValue;

	// =(Base_Exp_Value*A2) * Prestige_Level
	public static Double calculateLevel (Double baseExp, int currentLevel,
	                                     Double prestigeLevel) {
		if (currentLevel == 1) {
			return baseExp;
		} else {
			return (baseExp * currentLevel) * prestigeLevel;
		}
	}
}
