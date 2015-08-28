package utils;

/**
 * This class represents an exception that is thrown when a method is passed an invalid
 * argument.  That is, if argument is null, or out of the range of valid values for the
 * given argument.
 * 
 * @author Urvi
 *
 */
public class InvalidArgumentException extends Exception
{
	public InvalidArgumentException(String msg)
	{
		super(msg);
	}

}
