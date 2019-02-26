package com.creedfreak.common.exceptions;

/**
 * This exception signifies a command that is given with
 * too few arguments.
 */
public class TooFewArgumentException extends Exception {

	public TooFewArgumentException (String message) {
		super (message);
	}
}
