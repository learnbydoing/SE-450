package utils;


/**
 * This class represents an exception that is thrown when a volume (quantity) of the
 * Tradable is not a valid value. For example, an original 
 * 
 * @author Urvi
 *
 */
public class InvalidVolumeValueException extends Exception
{
	public InvalidVolumeValueException(String msg) 
	{
		super(msg);
	}

}
