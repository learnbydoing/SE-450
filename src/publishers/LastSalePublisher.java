package publishers;

import price.Price;
import publishers.IPublisher.PubType;
import utils.AlreadySubscribedException;
import utils.InvalidArgumentException;
import utils.NotSubscribedException;
import client.User;


/**
 * This class consists of methods that allow a User to subscribe or unsubscibe to 
 * get information about last sale for a stock, and a method that sends out last 
 * sale updates.
 * 
 * @author Urvi
 *
 */
public final class LastSalePublisher 
{

	/**
	 * An instance to of the LastSaletPublisher class.
	 * This is a private variable since only on instance is allowed in the system.
	 */
	
	private volatile static LastSalePublisher instance;
	
	/**
	 * An instance of the helper class that implements the methods to subscribe,
	 * unsubscribe and send last sale updates.
	 */
	private ILastSalePublisher lastSalePublisherImpl;
	
	/**
	 * Returns the only instance of the LastSalePublisher that
	 * will be used throughout the system.  
	 * 
	 * @return A LastSalePublisher instance
	 * @throws InvalidArgumentException 
	 * @see LastSalePublisherImplFactory#getPublisher
	 */
	public static LastSalePublisher getInstance() throws InvalidArgumentException
	{
		if(instance == null)
		{
			synchronized(LastSalePublisher.class)
			{
				if(instance == null)
					instance = new LastSalePublisher();
			}
		}
		return instance;
	}
	
	/**
	 * Private constructor since only one instance of TickerPublisher class 
	 * can be created. An instance of helper class is created here.
	 * @throws InvalidArgumentException if type is null
	 * 
	 * @see LastSalePublisherImplFactory#getPublisher
	 */
	
	private LastSalePublisher() throws InvalidArgumentException 
    { 
		lastSalePublisherImpl = LastSalePublisherImplFactory.getPublisher(PubType.LastSale);
    }
	
	/**
	 * Subscribes a user to receive last sale information for a given stock.
	 * @param user User who wishes to subscribe.
	 * @param prod Product symbol for which User wants current market information
	 * @throws AlreadySubscribedException if user is already subscribed to the product
	 * @throws InvalidArgumentException if user or prod is null
	 * 
	 */
	
    public synchronized void subscribe(User user, String prod) throws AlreadySubscribedException, InvalidArgumentException
    {
        lastSalePublisherImpl.subscribe(user, prod);
    }

    /**
	 * Unsubscribes a user from receiving last sale information for a given stock.
	 * @param user User who wishes to unsubscribe.
	 * @param prod Product symbol for which User no longer wants last sale information
	 * @throws NotSubscribedException if user was not subscribed to the product
     * @throws InvalidArgumentException if user or prod is null
	 * 
	 */
    
    public synchronized void unSubscribe(User user, String prod) throws NotSubscribedException, InvalidArgumentException
    { 
        lastSalePublisherImpl.unSubscribe(user, prod);
    }
    
    /**
     * Sends out last sale information to all users who have subscribed to a specified stock.     * 
     * @param product Stock symbol user is subscribed to.
     * @param p Last sale price of the stock symbol user is subscribed to.
     * @param v Last volume for the stock symbol user is subscribed to.
     * @throws InvalidArgumentException if p or v is null
     */
    
    public synchronized void publishLastSale(String product, Price p, int v) throws InvalidArgumentException
	{ 
    	lastSalePublisherImpl.publishLastSale(product, p, v);
	}
}

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

