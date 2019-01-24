package com.creedfreak.spigot.listeners;

import com.creedfreak.common.ICraftyProfessions;
import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.database.DAOs.AbsUsersDAO;
import com.creedfreak.spigot.CraftyProfessionsSpigot;
import com.creedfreak.common.container.PlayerManager;
import com.creedfreak.spigot.database.SpigotUsersDAO;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Author:  CreedTheFreak
 * Date:    4/21/2017
 *
 * This is the Core Listener for the CraftyProfessions Plugin
 */
public class CoreListener implements Listener
{
    private CraftyProfessionsSpigot mCraftyProf;
    private AbsUsersDAO mUserDAO;
    private Economy mEconomy;

    public CoreListener (ICraftyProfessions professions)
    {
	    mCraftyProf = (CraftyProfessionsSpigot) professions;
		mUserDAO = new SpigotUsersDAO (mCraftyProf.getDatabase ());
    }


    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event)
    {
        Player eventPlayer = event.getPlayer ();

        if (!PlayerManager.Instance ().loadPlayer (eventPlayer.getUniqueId ()))
        {
        	PlayerManager.Instance ().savePlayer (eventPlayer.getUniqueId (), eventPlayer.getName ());
        }
    }

    @EventHandler
    public void onPlayerLogout (PlayerQuitEvent event)
    {
        Player eventPlayer = event.getPlayer ();

        PlayerManager.Instance ().removePlayer (eventPlayer.getUniqueId ());
    }

    @EventHandler
    public void onBlockBreak (BlockBreakEvent event)
    {
        IPlayer eventFocus;
        Player player = event.getPlayer ();

        /*
        player.sendMessage (event.getPlayer ().getDisplayName () + ChatColor.RED + " Broke " + event.getBlock ().getType () + ":" + event.getBlock ().getData ());
        player.sendMessage (event.getPlayer ().getDisplayName () + ChatColor.GOLD + " Broke " + event.getBlock ().getType ().toString ());

        if (mEconomy != null)
        {
           player.sendMessage (Double.toString (mEconomy.getBalance (player)));
        }
        */

        // TODO: This needs testing before moving on
        eventFocus = PlayerManager.Instance ().getPlayer (player.getUniqueId ());
        // eventFocus.doWork (event.getBlock ().getType ().name ());

        player.sendMessage ("Block Broken was " + event.getBlock ().getType ().name ());

        // INFO: The below implementation of retrieving a wage table is deprecated and needs to be removed.
/*        IWageTable wageTable = mProfessions.getWageTable ("Miner_Wage");
        if (wageTable == null)
        {
            player.sendMessage ("Wage Table is NULL");
            return;
        }

        BigDecimal value = wageTable.mapItem (event.getBlock (), "Miner_Payout");
        if (value == null)
        {
            player.sendMessage ("Block does not map to Miner_Wage Table value returned null");
            return;
        }

        player.sendMessage (event.getBlock().getType () + " is Worth " + value.toString ());*/
    }

    @EventHandler
    public void onBlockPlaced (BlockPlaceEvent event)
    {
        Player player = event.getPlayer ();

        player.sendMessage (player.getDisplayName() + " Placed " + event.getBlockPlaced ().toString ());

        if (mEconomy != null)
        {
            mEconomy.depositPlayer (player, 100D);
            mEconomy.getBalance (player);
        }
    }

    /**
     * THIS METHOD IS FOR TESTING ONLY AND WILL NOT BE USED IN THE FINAL PRODUCTION
     */
    @EventHandler
    public void onEntityPickup (EntityPickupItemEvent event)
    {
        Entity player = event.getEntity ();

        if (player instanceof Player)
        {
            player.sendMessage (event.getItem ().getName ());
            // TODO: Use the below way to retrieve the Material Type from an Item.
            player.sendMessage (event.getItem ().getItemStack ().getType ().toString ());
            player.sendMessage (event.getItem ().getItemStack ().getData ().toString ());
            player.sendMessage (event.getItem ().getItemStack ().getItemMeta ().toString ());
        }
    }



    /**
     * WILL PROBABLY HAVE TO REMOVE THIS AT SOME POINT
     * I DONT LIKE SETTERS.
     *
     * This method will place the economy into the lister so that
     * we can obtain the correct economy after the construction of this
     * object.
     * @param econ
     */
    public void setEconomyHook (Economy econ)
    {
        mEconomy = econ;
    }
}
