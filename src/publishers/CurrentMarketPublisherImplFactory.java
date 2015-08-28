package publishers;

import publishers.IPublisher.PubType;
import utils.InvalidArgumentException;


/**
 * This class is creates a CurrentMarketPublisherImpl object
 * 
 * @author Urvi
 *
 */
public class CurrentMarketPublisherImplFactory 
{
	/**
	 * Private constructor since all methods are static
	 */
	private CurrentMarketPublisherImplFactory(){}
	
	/**
	 * Ceates an ICurrentMarketPublisherImpl object
	 * @param type Type of the publisher to create (CurrentMarket)
	 * @return Object of a class that implements ICurrentMarketPublisherImpl interface
	 * @throws InvalidArgumentException if type passed in is null
	 */
	public static ICurrentMarketPublisher getPublisher(PubType type) throws InvalidArgumentException
	{
		if(type == null)
		{
			throw new InvalidArgumentException("CurrentMarketPublisherImplFactory.getPublisher - type cannot be null");
		}
		
		else if(type == PubType.CurrentMarket)
		{
			return new CurrentMarketPublisherImpl();
		}
		
		else
		{
			throw new InvalidArgumentException("Type " + type + " is not valid");
		}
	}

}
