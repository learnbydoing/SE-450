package publishers;

import publishers.IPublisher.PubType;
import utils.InvalidArgumentException;

/**
 * This class is creates a MessagePublisherImpl object
 * 
 * @author Urvi
 *
 */
public class MessagePublisherImplFactory 
{

	/**
	 * Private constructor since all methods are static
	 */
	private MessagePublisherImplFactory(){}
	
	/**
	 * Creates an IMessagePublisherImpl object
	 * @param type Type of the publisher to create (Message)
	 * @return Object of a class that implements IMessagePublisherImpl interface
	 * @throws InvalidArgumentException if type passed in is null
	 */
	public static IMessagePublisher getPublisher(PubType type) throws InvalidArgumentException
	{
		if(type == null)
		{
			throw new InvalidArgumentException("MessagePublisherImplFactory.getPublisher - type cannot be null");
		}
		
		else if(type == PubType.Message)
		{
			return new MessagePublisherImpl();
		}
		
		else
		{
			throw new InvalidArgumentException("Type " + type + " is not valid");
		}
	}

}
