package publishers;

import publishers.IPublisher.PubType;
import client.User;
import utils.AlreadySubscribedException;
import utils.InvalidArgumentException;
import utils.NotSubscribedException;


/**
 * This class consists of methods that allow a User to subscribe or unsubscibe to 
 * get a message when an order for a specified stock is cancelled, when an order for
 * a specified stock is filled, and a message providing the market state for a specified
 * stock.  
 * @author Urvi
 *
 */

public final class MessagePublisher 
{

	/**
	 * An instance to of the Message Publisher class.
	 * This is a private variable since only on instance is allowed in the system.
	 */
	private volatile static  MessagePublisher instance;
	
	/**
	 * An instance of the helper class that implements the methods to subscribe,
	 * unsubscribe and send message updates.
	 */
	
	private IMessagePublisher messagePublisherImpl;
	
	/**
	 * Returns the only instance of the MessagePublisher that
	 * will be used throughout the system.  
	 * 
	 * @return A MessagePublisher instance
	 * @throws InvalidArgumentException 
	 * @see MessagePublisherImplFactory#getPublisher
	 */
	
	public static MessagePublisher getInstance() throws InvalidArgumentException
	{
		if(instance == null)
		{
			synchronized(MessagePublisher.class)
			{
				if(instance == null)
					instance = new MessagePublisher();
			}
		}
		return instance;
	}
	
	/**
	 * Private constructor since only one instance of MessagePublisher class 
	 * can be created. An instance of helper class is created here.
	 * @throws InvalidArgumentException 
	 * @see MessagePublisherImplFactory#getPublisher
	 */
	
	private MessagePublisher() throws InvalidArgumentException 
	{
		messagePublisherImpl = MessagePublisherImplFactory.getPublisher(PubType.Message);;
	}
	
	/**
	 * Subscribes a user to receive message updates for a given stock.
	 * @param user User who wishes to subscribe.
	 * @param prod Product symbol for which User wants cancel, fill, or market messages.
	 * @throws AlreadySubscribedException if user is already subscribed to the product.
	 * @throws InvalidArgumentException if user or prod is null
	 * 
	 */
	
	public synchronized void subscribe(User user, String prod) throws AlreadySubscribedException, InvalidArgumentException
	{
		messagePublisherImpl.subscribe(user, prod);
	}
	
    /**
	 * Unsubscribes a user from receiving message updates for a given stock.
	 * @param user User who wishes to unsubscribe.
	 * @param prod Product symbol for which User no longer wants cancel, fill, or market messages.
	 * @throws NotSubscribedException if user was not subscribed to the product.
     * @throws InvalidArgumentException if user or prod is null
	 * 
	 */
	
	public synchronized void unSubscribe(User user, String prod) throws NotSubscribedException, InvalidArgumentException
	{
    	  messagePublisherImpl.unSubscribe(user, prod);
	}
	
	/**
	 * Sends out cancel messages for a specified stock to users.
	 * @param cm The cancel message to send to users.  
	 * @throws InvalidArgumentException if cm is null
	 */
	
	public synchronized void publishCancel(CancelMessage cm) throws InvalidArgumentException
	{
		messagePublisherImpl.publishCancel(cm);
	}
	
	/**
	 * Sends out fill messages for a specified stock to users.
	 * @param fm The fill message to send to users.  
	 * @throws InvalidArgumentException if fm is null 
	 */
	
	public synchronized void publishFill(FillMessage fm) throws InvalidArgumentException
	{
		messagePublisherImpl.publishFill(fm);
	}
	
	/**
	 * Sends out market messages for a specified stock to users.
	 * 
	 * @param mm The market message to send to users.  
	 * @throws InvalidArgumentException  if mm is null
	 */
	
	public synchronized void publishMarketMessage(MarketMessage mm) throws InvalidArgumentException
	{
		messagePublisherImpl.publishMarketMessage(mm);
	}
}
