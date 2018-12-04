package com.creedfreak.spigot.container;

import com.google.common.primitives.UnsignedLong;
import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.professions.Profession;
import net.milkbowl.vault.economy.*;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/** [HUGE WIP]
 * This is the Player Wrapper for the CraftyProfessions Plugin in which stores all
 * of the information for a CraftyProfessions player.
 */
public class SpigotPlayer implements IPlayer
{
    // This is the players primary key within the Database
    private UnsignedLong mPlayerID;
    private Player mPlayer;

    // May not need the player pool if the wages are stored within the players Professions.
    private float mPlayerPool;
    private boolean mPrintEarnedWages;

    // The Users list of professions.
    private List<Profession> mProfessions;
    private Integer mPlayerLevel;
    private String mDBUsername;

    /**
     * This is the Default Constructor for the CraftyPlayer object in which
     * a player will be initialized from the Crafty Professions Database.
     *
     * @param
     */
    public SpigotPlayer (UnsignedLong dbID, String currentDBuname, Integer playerLevel)
    {
        mPlayerID = dbID;
        mPlayerPool = 0.0f;

		mPlayerLevel = playerLevel;
		mDBUsername = currentDBuname;

        mPrintEarnedWages = false;
    }
    /**
     * This method will return the UUID of the CPPlayer
     *
     * @return The Players UUID
     */
    public UUID getUUID ()
    {
        return mPlayer.getUniqueId ();
    }

	/**
	 * @return The username of the player
	 */
	public String getUsername ()
    {
    	return mPlayer.getName ();
    }

	/**
	 * @return the overall level of the user.
	 */
	public Integer getLevel ()
    {
    	return mPlayerLevel;
    }
    /**
     * This method will "Payout" the players current money ca, This method
     * should be called after x amount of ticks of the player receiving money and
     * right before the player logs off if the Player Pool is more than 0.
     */
    public float payoutPlayerPool ()
    {
    	float retVal = 0.0f;

        for (Profession prof : mProfessions)
        {
            if (mPrintEarnedWages)
            {
                // TODO: Implement custom currency symbols in the config file to allow different currencies to be displayed.
                mPlayer.sendMessage ("As a " + prof.getName () + " you earned $" + prof.pickupWages ());
            }

			retVal += prof.pickupWages ();
        }

        return retVal;
    }

    /**
     * This method will display the message to the Player wrapped within
     * the CraftyPlayer of this object, not "getting" a Player from the CraftyPlayer is
     * technically safer.
     *
     * @param message The message to send to the wrapped player.
     */
    public void sendMessage (String message)
    {
        mPlayer.sendMessage (message);
    }

    /**
     * This method checks the CraftyPlayers Permissions
     *
     * @param perm The permission to check against the player
     */
    public boolean checkPerms (final String perm)
    {
        return mPlayer.hasPermission (perm);
    }

    /**
     * Lists the current professions of the User. This will more than likely be a
     * command that the crafty player will be able to use in order to see all of
     * the current professions that they have.
     */
    public void listProfessions ()
    {
        if (null != mPlayer)
        {
            if (mProfessions.isEmpty ())
            {
                mPlayer.sendMessage ("You currently have no Professions");
            }
            else
            {
                // Iterate over the list and send the Profession name to the User.
	            mPlayer.sendMessage ("Your Current Professions Are:");
                for (Profession prof : mProfessions)
                {
                	mPlayer.sendMessage (prof.getName ());
                }
            }
        }
    }

	/**
     * Question: Can we have multiple blocks associated with multiple wage tables?
     * Infer: This could work if we use a map to store the various types of events,
     * Infer: place and break. Once the event occurs we grab the correct map and
     * Infer: hash the event element into that map.
     *
	 * Sets the print earned wages field to either true or false. This field indicates if
	 * the players wages should be printed out to them or not.
	 *
	 * Default Value for mPrintEarnedWages = false;
	 *
	 * @param printWages - true = Print wage values to them when they get paid.
	 *                     false = Don't print wage values when they get paid.
	 */
	public void setPrintEarnedWages (boolean printWages)
    {
    	mPrintEarnedWages = printWages;
    }

    public void doWork (String element)
    {
        int size = mProfessions.size ();
        // This algorithm currently has O(n) access time, this is not ideal.
        for (int i = 0; i < size; i++)
        {
            if (mProfessions.get (i).work (element))
            {
                // Exit the loop as soon as we find a match.
                i = size;
            }
        }
    }
}
