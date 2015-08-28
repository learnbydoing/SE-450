package utils;


/**
 * 
 * This class represents an exception that is thrown when a user tries to unsubscribe 
 * from receiving data, (current market, last sale, ticker, or market messages), for a stock
 * but is not subscribed for that stock.
 * 
 * @author Urvi
 *
 */
public class NotSubscribedException extends Exception
{
	public NotSubscribedException(String msg)
	{
		super(msg);
	}
}
