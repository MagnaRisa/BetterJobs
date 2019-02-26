package com.creedfreak.common.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyUtil {

	private static final Integer SCALE = 2;

	public static BigDecimal formatDouble (Double toFormat) {
		return new BigDecimal (toFormat).setScale (SCALE, RoundingMode.DOWN);
	}

	public static BigDecimal formatFloat (Float toFormat) {
		return new BigDecimal (toFormat).setScale (SCALE, RoundingMode.DOWN);
	}
}
