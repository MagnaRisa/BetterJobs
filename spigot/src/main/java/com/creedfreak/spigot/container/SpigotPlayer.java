package com.creedfreak.spigot.container;

import com.creedfreak.common.container.PlayerManager;
import com.creedfreak.common.professions.TableType;
import com.creedfreak.common.utility.Logger;
import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.professions.Profession;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is the Player Wrapper for the CraftyProfessions Plugin in which stores all
 * of the information for a CraftyProfessions player.
 */
public class SpigotPlayer implements IPlayer
{
    // This is the players primary key within the database
    private final Long mPlayerID;
    private Player mPlayer;

    private float mPlayerPool;
    private boolean mPrintEarnedWages;

    // The Users list of professions.
    private ConcurrentHashMap<TableType, Profession> mProfessions;
    private Integer mPlayerLevel;
    private String mDBUsername;
    private String mCurrentUsername;

    /**
     * This is the Default Constructor for the CraftyPlayer object in which
     * a player will be initialized from the Crafty Professions database.
     *
     * @param internalID The players database ID, this is unique to the database.
     * @param currentDBuname The current players name stored in the database.
     * @param playerLevel The players overall Level retrieved from the database.
     */
    public SpigotPlayer (Long internalID, String currentDBuname,
                         Integer playerLevel, Player player)
    {
        mPlayerID = internalID;
        mProfessions = new ConcurrentHashMap<> ();

        mPlayerPool = 0.0f;
		mPlayerLevel = playerLevel;
		mDBUsername = currentDBuname;
	    mPlayer = player;

        if (!mDBUsername.equals (mPlayer.getName ()))
        {
        	mCurrentUsername = mPlayer.getName ();
        }

	    mPrintEarnedWages = false;
    }

	public SpigotPlayer (Long internalID, String currentDBuname,
	                     Integer playerLevel, Player player,
	                     List<Profession> professions)
	{
		this(internalID, currentDBuname, playerLevel, player);
		for (Profession prof : professions)
		{
			mProfessions.put (prof.type (), prof);
		}
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
	 * @return The database identifier of the player.
	 */
	public Long getInternalID ()
    {
    	return mPlayerID;
    }

    /**
     * Registering a profession will allow the user to start getting income from
     * the profession that is registered.
     *
     * @param prof - The profession to register with the players list of professions.
     */
    @Override
    public boolean registerProfession (Profession prof)
    {
        boolean registered;
        registered = mProfessions.containsKey (prof.type ());
        if (!registered)
        {
            mProfessions.put (prof.type (), prof);
            
            /*thread-task[Database Thread][Future]: Enqueue this database task since it's not time sensitive*/
	        // Insert the players profession into the database
	        
	        // Could use a modified flag, that sets the user into a "modified" state which then gets updated.
	        // mbModified = true;
	        
	        // Could just update the database here in this method until I can send requests to a thread.
	        PlayerManager.Instance ().updatePlayer (mPlayerID);
        }
        return registered;
    }

    public void registerProfession (List<Profession> professions)
    {
    	boolean registered;
		for (Profession prof : professions)
		{
			registered = registerProfession (prof);

			if (!registered)
			{
				Logger.Instance ().Warn ("SpigotPlayer", "Profession: " + prof.getName () + " was not registered for user " + mCurrentUsername);
			}
		}
    }

    public boolean unregisterProfession (TableType type)
    {
        boolean registered = mProfessions.containsKey (type);
        if (registered)
        {
            mProfessions.remove (type);
        }
        return registered;
    }

    /**
     * This method will "Payout" the players current accumulated money. This method
     * should be called after x amount of ticks of the player receiving money and
     * right before the player logs off if the Player Pool is more than 0.
     */
    public float payoutPlayerPool ()
    {
    	float retVal = 0.0f;

        for (Profession prof : mProfessions.values ())
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
                for (Profession prof : mProfessions.values ())
                {
                	mPlayer.sendMessage (prof.getName ());
                }
            }
        }
    }
    
    public Collection<Profession> getProfessionCollection ()
	{
		ConcurrentHashMap<TableType, Profession> copy = new ConcurrentHashMap<> (mProfessions);
		return copy.values ();
	}
	
	public Profession getProfession (TableType type)
	{
		return mProfessions.get (type).shallowCopy ();
	}

	/**
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
