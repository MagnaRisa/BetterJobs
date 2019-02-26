package com.creedfreak.common.professions;

public class ProfessionBuilder
{
    /**
     * Specifically builds a profession for a user who has information in the database.
     *
     * @return A Profession whose data is retrieved from the database.
     */
    public static Profession dbBuild (String internalName, Integer internalID, String profStatus,
                                      int level, int prestigeLevel, double expCurrent,
                                      double expTotal)
    {
        String lowerProfName = internalName.toLowerCase ();
        Profession newProf;

        switch (lowerProfName)
        {
            case "miner":
                newProf = new ProfMiner (internalID, profStatus, level, prestigeLevel, expCurrent, expTotal);
                break;
            default:
                newProf = null;
        }

        return newProf;
    }

    /**
     * Builds a profession for the first time for a user who doesn't have information in the database.
     *
     * @param profession - The Profession to Build
     *
     * @return The specified Profession
     */
    public static Profession buildDefault (String profession)
    {
        String lowerProfName = profession.toLowerCase ();
        Profession newProf;

        switch (lowerProfName)
        {
            case "miner":
                newProf = new ProfMiner ();
                break;
            default:
                newProf = null;
        }

        return newProf;
    }
}
