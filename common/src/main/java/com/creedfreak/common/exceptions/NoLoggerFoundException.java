package com.creedfreak.common.exceptions;

public class NoLoggerFoundException extends Exception {

	public NoLoggerFoundException () {
		super ("The plugin logger was found to be null!");
	}
}
