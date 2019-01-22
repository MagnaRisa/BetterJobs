package com.creedfreak.common.database.DAOs;

import com.creedfreak.common.professions.Profession;
import com.google.common.primitives.UnsignedLong;
import com.creedfreak.common.database.databaseConn.Database;
import com.creedfreak.common.professions.IAugment;

import java.util.Collection;
import java.util.List;

public class AugmentsDAO implements IDaoBase<IAugment>
{
    private Database mDatabase;

    public AugmentsDAO (Database database)
    {
        mDatabase = database;
    }

    public void save (IAugment row)
    {
        // Save the augment here.
    }

    public void delete (UnsignedLong id)
    {
        // Delete the augment based on it's Id
    }

    public void update (IAugment row)
    {
        // Update a single row in the augments table.
    }

    public void updateAll (Collection<IAugment> augments)
    {
    	// Update a list of IAugments
    }

    public  List<IAugment> loadAll ()
    {
        return null;
    }

    public void fetchProfAugments (Profession prof, UnsignedLong userID)
    {

    }

    public IAugment load ()
    {
    	return null;
    }
}
