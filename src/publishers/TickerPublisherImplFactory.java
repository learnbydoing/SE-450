package publishers;

import publishers.IPublisher.PubType;
import utils.InvalidArgumentException;

/**
 * This class is creates a TickerPublisherImpl object
 * 
 * @author Urvi
 *
 */

public class TickerPublisherImplFactory 
{
	
	/**
	 * Private constructor since all methods are static
	 */
	private TickerPublisherImplFactory(){}
	
	/**
	 * creates a TickerPublisherImpl object
	 * @param type Type of the publisher to create (Ticker)
	 * @return  Object of a class that implements TickerPublisherImpl interface
	 * @throws InvalidArgumentException if type passed in is null
	 */
		public static ITickerPublisher getPublisher(PubType type) throws InvalidArgumentException
		{
			if(type == null)
			{
				throw new InvalidArgumentException("TickerPublisherImplFactory.getPublisher - type cannot be null");
			}
			
			else if(type == PubType.Ticker)
			{
				return new TickerPublisherImpl();
			}
			
			else
			{
				throw new InvalidArgumentException("Type " + type + " is not valid");
			}
		}

}
