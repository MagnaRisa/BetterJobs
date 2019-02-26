package com.creedfreak.common.exceptions;

/**
 * This Exception signifies if a command specified by the sender
 * is within the command map or not.
 */
public class InvalidWageTableException extends Exception {

	public InvalidWageTableException (String location, String attemptTable) {
		super ("Invalid WageTable Casting From: " + attemptTable + "to" + location);
	}

	public InvalidWageTableException (String message) {
		super (message);
	}

}
