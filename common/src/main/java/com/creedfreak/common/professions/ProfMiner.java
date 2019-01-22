package com.creedfreak.common.professions;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class ProfMiner extends Profession
{
    public static final String PROF_NAME = "Miner";
    private final TableType mType = TableType.Miner;

    // This is the Users current Miner Status i.e. Miner_Payout, Ore_Affinity, etc...
    private String mMinerStatus;

    // These are the Augments a user has for this profession.
    // Note that these are not used to calculate the modifiers
    // after this list is initialized.
    private List<IAugment> mAugments;

    int mProfLevel;
    int mPrestigeLevel;
    double mCurrentExp;
    double mTotalExp;

    // TODO: Calculate these when the augments are fetched.
    private float mExpierenceBonus;
    private float mIncomeBonus;
    private float mTokenBonus;

    // TODO: References Issue #27
    //private float mWagePool;

    /**
     * Constructs the default ProfessionMiner. This is used primarily when
     * the user selects a job for the first time and they have no stats
     * in the profession yet.
     */
    public ProfMiner ()
    {
        super (TableType.Miner);

        mMinerStatus = "default";
        mProfLevel = 0;
        mPrestigeLevel = 0;
        mCurrentExp = 0D;
        mTotalExp = 0D;

        mExpierenceBonus = 0f;
        mIncomeBonus = 0f;
        mTokenBonus = 0f;

        mAugments = null;
    }

    /**
     * Constructs the database related profession with a specific user. This is
     * used if the player is already in the database with stats in the profession.
     *
     * @param status - The current status of the profession
     * @param level - The current level of the user in this profession
     * @param prestigeLevel - The prestige level of the user in this profession
     * @param currentExp - The current exp of the current level the user has in this profession
     * @param totalExp -  The total amount of exp total the user has gained in this profession
     * @throws NullPointerException
     */
    public ProfMiner (String status, int level, int prestigeLevel, double currentExp, double totalExp) throws NullPointerException
    {
        super(TableType.Miner);

        mMinerStatus = status;

        mProfLevel = level;
        mPrestigeLevel = prestigeLevel;
        mCurrentExp = currentExp;
        mTotalExp = totalExp;
    }

    public String getName ()
    {
        return PROF_NAME;
    }

    /**
     * This method is called when the user upgrades their Miner profession status.
     * This will upgrade the users current states to their new status which will
     * point to a different wage table to be used for payment.
     *
     * @param newStatus - The new Miner status. i.e. Ore_Affinity or Stone_Affinity
     */
    public void upgradeStatus (String newStatus)
    {
        mMinerStatus = newStatus;
    }

    public String getStatus ()
    {
        return mMinerStatus;
    }

    public TableType type ()
    {
        return mType;
    }

    public void loadAugments ()
    {
        throw new NotImplementedException ();
    }
}
