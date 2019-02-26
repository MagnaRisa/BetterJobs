package com.creedfreak.common.database.DAOs;

import com.creedfreak.common.database.databaseConn.Database;
import com.creedfreak.common.professions.Augment;
import com.creedfreak.common.professions.Profession;
import com.google.common.primitives.UnsignedLong;

import java.util.Collection;
import java.util.List;

public class AugmentsDAO {

	private Database mDatabase;

	public AugmentsDAO (Database database) {
		mDatabase = database;
	}

	public void save (Augment row) {
		// Save the augment here.
	}

	public void delete (UnsignedLong id) {
		// Delete the augment based on it's Id
	}

	public void update (Augment row) {
		// Update a single row in the augments table.
	}

	public void updateAll (Collection<Augment> augments) {
		// Update a list of IAugments
	}

	public List<Augment> loadAll () {
		return null;
	}

	public void fetchProfAugments (Profession prof, UnsignedLong userID) {

	}

	public Augment load () {
		return null;
	}
}
