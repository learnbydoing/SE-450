package publishers;

import publishers.IPublisher.PubType;
import utils.InvalidArgumentException;

/**
 * This class is creates a LastSalePublisherImpl object
 * 
 * @author Urvi
 *
 */

public class LastSalePublisherImplFactory 
{
	
	/**
	 * Private constructor since all methods are static
	 */
	private LastSalePublisherImplFactory(){}
	
	/**
	 * Creates an ILastSalePublisherImpl object
	 * @param type Type of the publisher to create (LastSale)
	 * @return Object of a class that implements ILastSalePublisherImpl interface
	 * @throws InvalidArgumentException if type passed in is null
	 */
	public static ILastSalePublisher getPublisher(PubType type) throws InvalidArgumentException
	{
		if(type == null)
		{
			throw new InvalidArgumentException("LastSalePublisherImplFactory.getPublisher - type cannot be null");
		}
		
		else if(type == PubType.LastSale)
		{
			return new LastSalePublisherImpl();
		}
		
		else
		{
			throw new InvalidArgumentException("Type " + type + " is not valid");
		}
	}

}
