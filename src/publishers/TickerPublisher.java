package publishers;

import price.Price;
import publishers.IPublisher.PubType;
import utils.AlreadySubscribedException;
import utils.InvalidArgumentException;
import utils.NotSubscribedException;
import client.User;


/**
 * This class consists of methods that allow a User to subscribe or unsubscibe to 
 * get ticher information stock, and a method that sends out last 
 * ticker updates.
 * 
 * @author Urvi
 *
 */
public final class TickerPublisher 
{
	/**
	 * An instance to of the TickerPublisher class.
	 * This is a private variable since only on instance is allowed in the system.
	 */
	
	private volatile static TickerPublisher instance;
	
	/**
	 * An instance of the helper class that implements the methods to subscribe,
	 * unsubscribe and send ticker updates.
	 */
	
	private ITickerPublisher tickerPublisherImpl;
	
	/**
	 * Returns the only instance of the TickerPublisher class that will be used throughout the system.  
	 * 
	 * @return A TickerPublisher instance
	 * @throws InvalidArgumentException 
	 * @see TickerPublisherImplFactory#getPublisher
	 */
	
	public static TickerPublisher getInstance() throws InvalidArgumentException
	{
		if(instance == null)
		{
			synchronized(TickerPublisher.class)
			{
				if(instance == null)
					instance = new TickerPublisher();
			}
		}
		return instance;
	}
	
	
	/**
	 * Private constructor since only one instance of TickerPublisher class 
	 * can be created. An instance of helper class is created here.
	 * @throws InvalidArgumentException if type sent to Factory is null
	 * 
	 * @see TickerPublisherImplFactory#getPublisher
	 */
	
	private TickerPublisher() throws InvalidArgumentException 
	{
		tickerPublisherImpl = TickerPublisherImplFactory.getPublisher(PubType.Ticker);
	}
	
	/**
	 * Subscribes a user to receive ticker updates for a given stock.
	 * 
	 * @param user User who wishes to subscribe.
	 * @param prod Product symbol for which User wants ticker updates.
	 * @throws AlreadySubscribedException if user is already subscribed to the product.
	 * @throws InvalidArgumentException if user or prod is null
	 * 
	 */
	
	public synchronized void subscribe(User user, String prod) throws AlreadySubscribedException, InvalidArgumentException
	{
		tickerPublisherImpl.subscribe(user, prod);
	}
	
    /**
	 * Unsubscribes a user from receiving ticker updates for a given stock.
	 * 
	 * @param user User who wishes to unsubscribe.
	 * @param prod Product symbol for which User no longer wants ticker updates.
	 * @throws NotSubscribedException if user was not subscribed to the product.
     * @throws InvalidArgumentException 
	 * 
	 */
	
	public synchronized void unSubscribe(User user, String prod) throws NotSubscribedException, InvalidArgumentException
	{
		tickerPublisherImpl.unSubscribe(user, prod);
	}

	/**
	 * Sends out ticker information to all subscribed users.  This method also saves the last
	 * prices for products and determines if the prices have gone up, down, or stayed the same.
	 * 
	 * @param product Stock symbol for which User wants ticker updates.
	 * @param priceIn The current price of the stock.  This is compared to the saved price to 
	 * determine the direction of the stock price.
	 * @throws InvalidArgumentException if product or priceIn is null
	 */

	public synchronized void publishTicker(String product, Price priceIn) throws InvalidArgumentException
	{
       tickerPublisherImpl.publishTicker(product, priceIn);
	}        
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

