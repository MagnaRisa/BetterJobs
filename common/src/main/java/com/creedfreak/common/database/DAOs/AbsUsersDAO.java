package com.creedfreak.common.database.DAOs;

import com.creedfreak.common.database.queries.queryLib;
import com.google.common.primitives.UnsignedLong;
import com.creedfreak.common.container.IPlayer;
import com.creedfreak.common.database.databaseConn.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AbsUsersDAO implements IDaoBase<IPlayer>
{
    protected Database mDatabase;
    protected AugmentsDAO mAugmentDAO;
    protected ProfessionsDAO mProfessionsDAO;

    /**
     * <p>This is the constructor for the Users database access object.</p>
     *
     * @param database The database in which to access.
     */
    public AbsUsersDAO (Database database)
    {
        mDatabase = database;
        mAugmentDAO = new AugmentsDAO (database);
        mProfessionsDAO = new ProfessionsDAO (database);
    }

    /**
     * <p>Saves a players information into the database.</p>
     *
     * @param playerData The player to store within the database.
     */
    public void save (IPlayer playerData)
    {
        // Save the player and all of their information to the DB

    }

    /**
     * <p>Deletes a player from the Database. This includes all of the relationships that the player
     * could potentially be a part of. Note: That the player can only get deleted if the proper password
     * has been input into the command.</p>
     *
     * @param id The primary identifier within the database for the current player.
     */
    public void delete (UnsignedLong id)
    {
        // Before we delete the user, add their current data to the archive so it is retrievable.

        // Delete the row whose rid == id
    }

    public IPlayer update (IPlayer row)
    {
        return row;
    }

    public List<IPlayer> loadAll ()
    {
        return new ArrayList<> ();
    }

    public IPlayer load (UUID userID)
    {
        IPlayer retPlayer = null;

        // Query the database using the current players UUID
        // TODO Test this with actual database information specifically MariaDB
        // TODO This means I need to find my credentials for mariadb
        // queryLib.select
        // From the Result Set build the player from separate Queries.

        // Construct the new Player here then return
        // retPlayer = buildPlayer ();

        return retPlayer;
    }

    public IPlayer restoreUser (UUID userID)
    {
        return null;
    }
}
