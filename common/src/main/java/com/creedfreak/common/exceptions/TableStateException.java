package com.creedfreak.common.exceptions;

public class TableStateException extends Exception {

	public TableStateException () {
		super ("Table state error in Wage Table Handler");
	}

	public TableStateException (String message) {
		super (message);
	}
}
