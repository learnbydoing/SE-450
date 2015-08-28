package publishers;

import publishers.IPublisher.PubType;
import utils.AlreadySubscribedException;
import utils.InvalidArgumentException;
import utils.NotSubscribedException;
import client.User;
import dto.MarketDataDTO;


/**
 * This class consists of methods that allow a User to subscribe or unsubscribe to 
 * get updates on current market data, and a method that sends out market updates. 
 * 
 * @author Urvi
 *
 */
public final class CurrentMarketPublisher
{
	/**
	 * An instance to of the CurrentMarketPublisher class.
	 * This is a private variable since only on instance is allowed in the system.
	 */
	private volatile static CurrentMarketPublisher instance;  // This is volatile so that the value can be read from main memory
	
	/**
	 * An instance of the helper class that implements the methods to 
	 * subscribe, unsubscribe, and send out market updates.
	 */
	private ICurrentMarketPublisher currentMarketPublisherImpl;
	
	
/**
 * Returns the only instance of the CurrentMarketPublisher that
 * will be used throughout the system. 
 * @return An ICurrentMarketPublisher instance
 * @throws InvalidArgumentException 
 *
 * @see CurrentMarketPublisherImplFactory#getPublisher
 */
	public static CurrentMarketPublisher getInstance() throws InvalidArgumentException
	{
		if(instance == null)
		{ 
			synchronized (CurrentMarketPublisher.class) 
			{
				if(instance == null)
					instance = new CurrentMarketPublisher();
			}
		}
		return instance;
	}
	
	/**
	 * Private constructor since only one instance of CurrentMarketPublisher class 
	 * can be created. An instance of helper class is created here.  A factory
	 * is used, which takes as a parameter, the type of publisher to create.
	 * @throws InvalidArgumentException if null is passed for a publisher type
	 * @see CurrentMarketPublisherImplFactory#getPublisher
	 */
	private CurrentMarketPublisher() throws InvalidArgumentException
	{
		currentMarketPublisherImpl = CurrentMarketPublisherImplFactory.getPublisher(PubType.CurrentMarket);
	}
	
	/**
	 * Subscribes a user to receive current market data updates for a given stock.
	 * 
	 * @param user User who wishes to subscribe.
	 * @param prod Product symbol for which User wants current market information
	 * @throws AlreadySubscribedException if user is already subscribed to the product
	 * @throws InvalidArgumentException if user or prod is null
	 * 
	 */
	public synchronized void subscribe(User user, String prod) throws AlreadySubscribedException, InvalidArgumentException
	{
          currentMarketPublisherImpl.subscribe(user, prod);
	}
	
	
	/**
	 * Unsubscribes a user from current market data.
	 * @param user User who wishes to unsubscribe.
	 * @param prod Product symbol for which User no longer wants current market information
	 * @throws NotSubscribedException if user was not subscribed to the product
	 * @throws InvalidArgumentException if user or prod is null 
	 * 
	 */
	
	public synchronized void unSubscribe(User user, String prod) throws NotSubscribedException, InvalidArgumentException
	{
          
          currentMarketPublisherImpl.unSubscribe(user, prod);
	}
      
	/**
	 * Sends out market updates to all users who have subscribed to a specified stock symbol.
	 * @param mdto The MarketDataDTO object that encapsulates all data related to the current market.
	 * @throws InvalidArgumentException if mdto is null
	 */
	
  	public synchronized void publishCurrentMarket(MarketDataDTO mdto) throws InvalidArgumentException
  	{
  		currentMarketPublisherImpl.publishCurrentMarket(mdto);
  	}
}