package com.creedfreak.common.professions;

import java.util.HashMap;

public enum Augment {
	MinExpBonus (1),
	StdExpBonus (2),
	ImpExpBonus (3),
	MaxExpBonus (4),
	MinIncomeBonus (5),
	StdIncomeBonus (6),
	ImpIncomeBonus (7),
	MaxIncomeBonus (8),
	MinTokenBonus (9),
	StdTokenBonus (10),
	ImpTokenBonus (11),
	MaxTokenBonus (12);

	private static HashMap<Integer, Augment> mLookup = new HashMap<> ();
	private Integer mAugmentID;

	static {
		for (Augment aug : Augment.values ()) {
			mLookup.put (aug.mAugmentID, aug);
		}
	}

	Augment (Integer augID) {
		mAugmentID = augID;
	}

	public static Augment lookupAugment (Integer augmentID) {
		return mLookup.get (augmentID);
	}

}
