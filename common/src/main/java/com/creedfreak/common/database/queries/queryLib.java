package com.creedfreak.common.database.queries;

/**
 * This class houses the static queries that will need to be called to
 * retrieve data from the database. These static strings will be used
 * whenever we need to interface into the database. This should prevent
 * any type of SQL Injection while keeping all of the queries in one
 * easy to read and modify class.
 */
public final class queryLib {

	public static final String checkUserExist
			= "SELECT UUID, UserID, Username "
			+ "FROM Users "
			+ "WHERE UUID = ?";

	// Grab the User and their internal DB ID.
	public static final String selectUserDataFromUUID
			= "SELECT * "
			+ "FROM Users "
			+ "WHERE UUID = ?";

	public static final String selectUserDataFromDatabaseID
			= "SELECT * "
			+ "FROM Users "
			+ "WHERE UserID = ?";

	// If they have any Careers or SideJobs we need to grab them.
	public static final String selectUserCareers
			= "SELECT * "
			+ "FROM Users, Careers, Professions "
			+ "WHERE Users.UserID = ? "
			+ "AND Users.UserID = Careers.UserID "
			+ "AND Professions.ProfessionID = Careers.ProfessionID";

	public static final String selectUserCareerNames
			= "SELECT ProfessionName "
			+ "FROM Users, Careers, Professions "
			+ "WHERE Users.UserID = ? "
			+ "AND Users.UserID = Careers.UserID "
			+ "AND Professions.ProfessionID = Careers.ProfessionID";

	public static final String selectUserSideJobs
			= "SELECT * "
			+ "FROM Users, SideJobs, SubProfessions "
			+ "WHERE Users.UserID = ? "
			+ "AND Users.UserID = SideJobs.UserID "
			+ "AND SubProfessions.SubProfessionID = SideJobs.SubProfessionID";

	// Select a (User, Profession) pair's augments.
	public static final String selectUserProfessionAugs
			= "SELECT * "
			+ "FROM UserProfHasAugments "
			+ "WHERE UserID = ? ";

	// Select a (User, SubProfession) pair's augments
	public static final String selectUserSideJobAugs
			= "SELECT * "
			+ "FROM SideJobs, UserSideJobHasAugments "
			+ "WHERE Careers.UserID = ? "
			+ "AND UserSideJobHasAugments.SubProfessionID = ? "
			+ "AND Careers.UserID = UserSideJobHasAugments.UserID "
			+ "AND Careers.SubProfessionID = UserSideJobHasAugments.SubProfessionID";
}
