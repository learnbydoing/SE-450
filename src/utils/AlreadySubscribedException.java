package utils;

/**
 * 
 * This class represents an exception that is thrown when a user tries to subscribe to
 * receive data, (current market, last sale, ticker, or market messages), for a stock
 * but is already subscribed for that stock.
 * @author Urvi
 *
 */
public class AlreadySubscribedException extends Exception
{
	public AlreadySubscribedException(String msg)
	{
		super(msg);
	}
}
