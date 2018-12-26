package com.creedfreak.spigot.commands.DatabaseCommands;

import com.creedfreak.spigot.commands.DatabaseCommand;
import com.creedfreak.spigot.container.CommandData;
import com.creedfreak.spigot.container.SpigotPlayer;
import com.creedfreak.common.database.databaseConn.Database;
import com.creedfreak.common.database.databaseConn.MySQL_Conn;
import com.creedfreak.common.utility.Pair;

import java.util.List;

public class CommandTest extends DatabaseCommand
{
    public CommandTest (Database db)
    {
        super (db, new CommandData (
            "dbtest",
            "This command will test the connection to the database and  then query the database",
            "/prof dbtest",
            "craftyprofessions.admin.debug"
        ));
    }

    @Override
    public boolean execute (SpigotPlayer sender, String... args)
    {
        MySQL_Conn conn = (MySQL_Conn) mDatabase;
        mDatabase.dbConnect ();
        List<Pair<Integer, String>> array = conn.testDBconnection ();

        for (Pair<Integer, String> pair : array)
        {
            sender.sendMessage (pair.getFirst().toString () + " | " + pair.getSecond ());
        }

        return true;
    }
}
