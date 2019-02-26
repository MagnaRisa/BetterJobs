package com.creedfreak.common.professions;

import com.creedfreak.common.container.WageTableHandler;
import com.creedfreak.common.utility.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the Abstract Class for a Profession. This will be
 * the class in which all Professions are inherited from and
 * thus all of the shared data and responsibilities will be
 * encapsulated here.
 */
public abstract class Profession
{
    private final IWageTable mWageTable;
	private final Integer mProfessionID;

	private List<Augment> mAugments;
    private float mWagePool;

    protected Profession (TableType type, Integer internalID)
    {
    	mProfessionID = internalID;
        mWageTable = WageTableHandler.getInstance ().GetWageTable (type);

        if (null == mWageTable)
        {
            throw new NullPointerException ("The " + type.name () + " wage table is not enabled!");
        }
    }
    
    /**
     * The base profession copy constructor.
     */
    protected Profession (Profession prof)
    {
    	this.mProfessionID = prof.mProfessionID;
        this.mWageTable = prof.mWageTable;
        this.mWagePool = prof.mWagePool;
        this.mAugments = new ArrayList<> (prof.mAugments);
    }
    
    /**
     * Perform a shallowCopy of a profession.
     * This will allow the use the same WageTable object.
     */
    public abstract Profession shallowCopy ();

	/**
	 * Attach an augment list to the profession
	 */
	public void attachAugments (List<Augment> augList)
    {
    	mAugments = augList;
    }

	/**
	 * Return the internal identifier of the Profession.
	 */
	public Integer getIdentifier ()
    {
    	return mProfessionID;
    }
    /**
     * This method is used to return the Name of the Profession
     * to use in other places.
     *
     * @return The Name of the Profession
     */
    public abstract String getName ();

    /**
     * This method will essentially do "work" of the profession. If an event is
     * passed to this method the hope is that the corresponding block is hashed
     * and the monetary value of that block or item is returned to the player.
     *
     * Here when I mention "event" it's the literal event that the Minecraft/ServerAPI
     * is generating and thus is getting past along to this method.
     *
     * @param element - The block, item, or action that the event generates.
     *
     * @return - true or false, if the value retrieved from the HashMap was a null
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
    public abstract void upgradeStatus (String newStatus);

    public abstract String getStatus ();

    public abstract TableType type ();
}
