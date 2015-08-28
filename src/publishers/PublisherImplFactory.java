package publishers;

import publishers.IPublisher.PubType;
import utils.InvalidArgumentException;

public class PublisherImplFactory
{

	/**
	 * Private constructor, since all methods are static
	 */
	private PublisherImplFactory() {}

	/**
	*  Returns the appropriate Impl (delgate) object
	*  @param type The type of publisher for which to create an Impl object: Valid values are  CurrentMarket, LastSale, Message, Ticker 
	*  @throws InvalidArgumentException if type is null
	*
	*/

	public static IPublisher getPublisher(PubType type) throws InvalidArgumentException
	{
		if(type == null)
		{
			throw new InvalidArgumentException("PublisherImplFactory.getPublisher - type cannot be null");
		}
		
		else if(type == PubType.CurrentMarket)
		{
			return new CurrentMarketPublisherImpl();
		}

		else if(type == PubType.LastSale)
		{
			return new LastSalePublisherImpl();
		}

		else if(type == PubType.Message)
		{
			return new MessagePublisherImpl();
		}

		else if(type == PubType.Ticker)
		{
			return new TickerPublisherImpl();
		}
			return null;
	}
}
