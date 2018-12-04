package com.creedfreak.common.utility;

/**
 * TODO: REMOVE THIS CLASS AND ALL DEPENDANTS
 * This class is simply used to test the current function written  for
 * the database. This is to prevent the dependency with javafx.
 *
 * This will more than likely be removed at a later date
 *
 * @param <T1> Type number one to be stored
 * @param <T2> Type number two to be stored
 */
public class Pair <T1, T2>
{
    private T1 mFirst;
    private T2 mSecond;

    public Pair (T1 first, T2 second)
    {
        mFirst = first;
        mSecond = second;
    }

    public T1 getFirst ()
    {
        return mFirst;
    }

    public T2 getSecond ()
    {
        return mSecond;
    }
}
