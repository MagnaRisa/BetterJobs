package com.creedfreak.common.exceptions;

/**
 * This Exception signifies if a command specified by the sender
 * is within the command map or not.
 */
public class CommandNotFoundException extends Exception {

	public CommandNotFoundException () {
		super ("No command exists with those arguments");
	}

	public CommandNotFoundException (String message) {
		super (message);
	}

}
