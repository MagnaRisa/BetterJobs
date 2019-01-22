package com.creedfreak.common.container;

import com.creedfreak.common.professions.Profession;
import com.creedfreak.common.professions.TableType;
import com.google.common.primitives.UnsignedLong;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface IPlayer
{
    /**
     * Returns the UUID of the current player.
     *
     * @return The players unique identifier
     */
    UUID getUUID ();

	/**
	 * Return the Username of the player.
	 *
	 * @return The players username
	 */
	String getUsername ();

	/**
	 * Return the players overall CP Level
	 *
	 * @return The level of the player
	 */
	Integer getLevel  ();

	/**
	 * @return The database identifier of the player.
	 */
	UnsignedLong getDBIdentifier ();

    /**
     * Sends a message to the target player.
     *
     * @param message The message to send to the target player.
     */
    void sendMessage (String message);

    /**
     * Checks the permission of the current player.
     *
     * @param perm The permission to test if the player currently has.
     *
     * @return True - If the player has the permission.
     *         False - If the player does not have that permission.
     */
    boolean checkPerms (final String perm);

    /**
     * Payout the players value pool to their economy account
     * If there is no economy then only add points to the players account.
     */
    float payoutPlayerPool ();

    /**
     * Displays the current professions the User currently have.
     */
    void listProfessions ();

	boolean registerProfession (Profession prof);

	void registerProfession (List<Profession> professions);

	boolean unregisterProfession (TableType prof);

	/**
     * TODO: It may be the case where we will have to map into a large table of blocks and then check if they have the correct job.
     * This method is used to generate revenue based on what the user has broken.
     *
     * @param elementName - The name of the block that was broken.
     */
    void doWork (String elementName);
}
