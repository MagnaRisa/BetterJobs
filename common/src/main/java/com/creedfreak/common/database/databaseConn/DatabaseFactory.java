package com.creedfreak.common.database.databaseConn;

import com.creedfreak.common.ICraftyProfessions;
import com.creedfreak.common.AbsConfigController;
import com.creedfreak.common.container.AbsPlayerFactory;
import com.creedfreak.common.utility.Logger;

import java.util.logging.Level;

public class DatabaseFactory
{
    public static Database buildDatabase (ICraftyProfessions plugin,
                                          AbsConfigController config, AbsPlayerFactory factory)
    {
        String dbType = config.getString ("DatabaseType");
        Database db = null;

        if (dbType.equalsIgnoreCase ("sqlite"))
        {
            Logger.Instance ().Info (Database.DATABASE_PREFIX, "Constructing SQLite Connection.");
            db = new SQLite_Conn (plugin, config);
        }
        else if (dbType.equalsIgnoreCase ("mysql"))
        {
            String host = config.getString ("MySQL.host");
            String database = config.getString ("MySQL.db_name");
            String user = config.getString ("MySQL.user");
            String identifier = config.getString ("MySQL.password");

            Logger.Instance ().Info (Database.DATABASE_PREFIX,"Constructing MySQL Connection.");
            db = new MySQL_Conn (plugin, config, host, database, user,
                identifier);
        }

        return db;
    }
}
