package com.creedfreak.common.database.queries;

import com.google.common.primitives.UnsignedLong;
import com.creedfreak.common.database.databaseConn.Database;

import java.sql.ResultSet;

/**
 * This class houses the static queries that will need to be called to
 * retrieve data from the database. These static strings will be used
 * whenever we need to interface into the database. This should prevent
 * any type of SQL Injection while keeping all of the queries in one
 * easy to read and modify class.
 */
public final class queryLib
{
    // onUserLogin
    // Grab the User and their internal DB ID.
    private static final String selectUserData
            = "SELECT * "
            + "FROM Users "
            + "WHERE UUID = ?";

    // If they have any Careers or SideJobs we need to grab them.
    private static final String selectUserCareers
            = "SELECT * "
            + "FROM Users, Careers, Professions "
            + "WHERE Users.UserID = ? "
            + "AND Users.UserID = Careers.UserID "
            + "AND Professions.ProfessionID = Careers.ProfessionID";

    private static final String selectUserSideJobs
            = "SELECT * "
            + "FROM Users, SideJobs, SubProfessions "
            + "WHERE Users.UserID = ? "
            + "AND Users.UserID = SideJobs.UserID "
            + "AND SubProfessions.SubProfessionID = SideJobs.SubProfessionID";

    // Grab the Level information from a LevelID
    // db-test: Query marked for testing.
    private static final String selectLevelInfo
            = "SELECT * "
            + "FROM Levels "
            + "WHERE LevelID = ?";

    // Select a (User, Profession) pair's augments.
    // db-test: Query marked for testing.
    private static final String selectUserProfessionAugs
            = "SELECT * "
            + "FROM Careers, UserProfHasAugments "
            + "WHERE Careers.UserID = ? "
            + "AND UserProfHasAugments.ProfessionID = ? "
            + "AND Careers.UserID = UserProfHasAugments.UserID "
            + "AND Careers.ProfessionID = UserProfHasAugments.ProfessionID";

    // Select a (User, SubProfession) pair's augments
    // db-test: Query maked for testing.
    private static final String selectUserSideJobAugs
            = "SELECT * "
            + "FROM SideJobs, UserSideJobHasAugments "
            + "WHERE Careers.UserID = ? "
            + "AND UserSideJobHasAugments.SubProfessionID = ? "
            + "AND Careers.UserID = UserSideJobHasAugments.UserID "
            + "AND Careers.SubProfessionID = UserSideJobHasAugments.SubProfessionID";

//    private static final String updateUserData
//            = "UPDATE ";

    // TODO: Implement this properly. Us the DB and ID to find the users careers.
    public static ResultSet queryUserCareers (Database db, UnsignedLong userID)
    {
        return null;
    }

    public static ResultSet queryUser (Database db, UnsignedLong userID)
    {
        ResultSet retunSet;

        db.dbConnect ();


        db.dbClose();

        return null;
    }
}
