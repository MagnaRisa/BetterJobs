package com.creedfreak.common.professions;

import com.creedfreak.common.container.WageTableHandler;
import com.creedfreak.common.utility.Logger;

/**
 * This is the Abstract Class for a Profession. This will be
 * the class in which all Professions are inherited from and
 * thus all of the shared data and responsibilities will be
 * encapsulated here.
 */
public abstract class Profession
{
    private IWageTable mWageTable;
    private float mWagePool;

    protected Profession (TableType type)
    {
        mWageTable = WageTableHandler.getInstance ().GetWageTable (type);

        if (null == mWageTable)
        {
            throw new NullPointerException ("The " + type.name () + " wage table is not enabled!");
        }
    }

    /**
     * This method is used to return the Name of the Profession
     * to use in other places.
     *
     * @return The Name of the Profession
     */
    abstract public String getName ();

    /**
     * This method will essentialy do "work" of the profession. If an event is
     * passed to this method the hope is that the corresponding block is hashed
     * and the monetary value of that block or item is returned to the player.
     *
     * Here when I mention "event" it's the literal event that the Minecraft/ServerAPI
     * is generating and thus is getting past along to this method.
     *
     * @param element - The block, item, or action that the event generates.
     *
     * @return - true or false, if the value retrived from the HashMap was a null
     *              value or not. If null is encountered then
     */
    public boolean work (String element)
    {
        boolean retVal = true;

        try
        {
            mWagePool += mWageTable.mapItem (element, this.getStatus ());
        }
        catch (NullPointerException except)
        {
            Logger.Instance ().Error ("PROFESSION", except.getMessage ());
            Logger.Instance ().Debug ("[REMOVE THIS] THIS IS A TEST, DOES THE ERROR GET LOGGED OR THROWN...");
            retVal = false;
        }
        return retVal;
    }

    /**
     * Retrieves the value of the current players earned wages of a specific profession.
     * The wage is then cleared back to zero to start calculating again until next
     * pay period.
     *
     * @return - The current wage of this profession.
     */
    public float pickupWages ()
    {
        float retVal = mWagePool;
        mWagePool = 0.0f;

        return retVal;
    }

    /**
     * This method is used to update the professions
     * status. For example from the "Miner_Payout" to
     * the "Stone_Affinity" profession status.
     *
     * Likewise calling this method will also set the
     * Miner Wage payout table to the corresponding
     * upgraded version. Miner_Payout -> Stone_Affinity
     */
    abstract void upgradeStatus (String newStatus);

    abstract String getStatus ();

    // Some way to pay a player or hold the money in a central location
    // Some way to generate the revenue
    // Some way to add and or remove XP gained for Professions
    // Some sort of Modifier method to track and update Modifiers
}
