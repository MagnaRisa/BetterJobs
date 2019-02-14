package com.creedfreak.common.exceptions;

public class CommandException extends Exception
{
	private static final long serialVersionUID = 3698331842717760769L;

	private final String CMD_PREFIX = "/prof";
	private final String[] mExecutedArgs;

	public CommandException (String message)
	{
		super (message);
		mExecutedArgs = null;
	}

	public CommandException (String message, final String[] executedCmd)
	{
		super (message);
		mExecutedArgs = executedCmd;
	}

	public String getExecutedCommand ()
	{
		StringBuilder builder = new StringBuilder ();

		if (null == mExecutedArgs)
		{
			return null;
		}
		else
		{
			builder.append (CMD_PREFIX);
			for (final String str : mExecutedArgs)
			{
				builder.append (" ");
				builder.append (str);
			}
		}

		return builder.toString ();
	}
}
