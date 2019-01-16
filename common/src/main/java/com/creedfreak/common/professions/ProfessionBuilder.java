package com.creedfreak.common.professions;

public class ProfessionBuilder
{
	public static Profession buildProfession (String profession)
	{
		Profession construct = null;
		String check = profession.toLowerCase ();

//		switch (check)
//		{
//			case "miner":
//				construct = new ProfMiner ();
//			default:
//
//		}

		return construct;
	}
}
