package com.creedfreak.common.database.DAOs;

import com.google.common.primitives.UnsignedLong;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IDaoBase<T>
{
    void save (T row);

    void delete (UnsignedLong id);

    void update (T row);

    void  updateAll (Collection<T> rows);

    List<T> loadAll ();

    T load ();
}
