package publishers;

import java.util.HashMap;
import java.util.HashSet;

import price.Price;
import price.PriceFactory;
import utils.AlreadySubscribedException;
import utils.InvalidArgumentException;
import utils.NotSubscribedException;
import client.User;


/**
 * This is the helper class for the LastPublisher that implements methods to 
 * subscribe and unsubscribe to get information about last sale for a stock, and 
 * a method that sends out last sale updates.
 * 
 * @author Urvi
 *
 */ 

public class LastSalePublisherImpl implements ILastSalePublisher
{
	/**
	 * Constructs a LastSalePublisherImpl object
	 */
	
	public LastSalePublisherImpl(){}
	
	/**
	 * A HashMap to keep track of which users are subscribed to which products.
	*/
	
	private HashMap<String, HashSet<User>> lastSaleSubs = new HashMap<String, HashSet<User>>();
	
	/**
	 * Subscribes a user to receive last sale information for a given stock.
	 * @param u User who wishes to subscribe.
	 * @param prod Product symbol for which User wants current market information.
	 * @throws AlreadySubscribedException if user is already subscribed to the product..
	 * @throws InvalidArgumentException if u or prod is null
	 * 
	 */
	
	public synchronized void subscribe(User u, String prod) throws AlreadySubscribedException, InvalidArgumentException  
	{
		if(u == null)
		{
			throw new InvalidArgumentException ("LastSalePublisherImpl.subscribe - User cannot be null");
		}

		if(prod == null || prod.isEmpty())
		{
			throw new InvalidArgumentException ("LastSalePublisherImpl.subscribe - Product cannot be null or empty");
		}
		HashSet<User> hs = new HashSet<User>();
		if(lastSaleSubs.containsKey(prod) == true)
		{
			hs = lastSaleSubs.get(prod);
			if(hs.contains(u) == true)
			{
				throw new AlreadySubscribedException("User " + u.getUserName() + " already subscribed to " + prod + " for LastSalePublisher");
			}
		}
			hs.add(u);
			lastSaleSubs.put(prod, hs);
	}
	
   /**
	 * Unsubscribes a user from receiving last sale information for a given stock.
	 * @param u User who wishes to unsubscribe.
	 * @param prod Product symbol for which User no longer wants last sale information
	 * @throws NotSubscribedException if user is not subscribed to the product.
	 * @throws InvalidArgumentException if u or prod is null
	 * 
	 */
	
	public synchronized void unSubscribe(User u, String prod) throws NotSubscribedException, InvalidArgumentException
	{
		if(u == null)
		{
			throw new InvalidArgumentException ("LastSalePublisherImpl.unSubscribe - User cannot be null");
		}

		if(prod == null || prod.isEmpty())
		{
			throw new InvalidArgumentException ("LastSalePublisherImpl.unSubscribe - Product cannot be null or empty");
		}
		
		HashSet<User> hs = new HashSet<User>();
		hs = lastSaleSubs.get(prod);
		if(hs== null || hs.contains(u) == false)
		{
			throw new NotSubscribedException("User " + u.getUserName() + " not subscribed to " + prod + " for LastSalePublisher");
		}
		hs.remove(u);
		lastSaleSubs.put(prod, hs);
	}
	
    /**
     * Sends out last sale information to all users who have subscribed to a specified stock.
     * @param product Stock symbol user is subscribed to
     * @param p Last sale price of the stock symbol user is subscribed to
     * @param v Last volume for the stock symbol user is subscribed to
     * @throws InvalidArgumentException if p or v is null
     */
	
	public synchronized void publishLastSale(String product, Price p, int v) throws InvalidArgumentException
	{ 
		if(product == null || product.isEmpty())
		{
			throw new InvalidArgumentException ("LastSalePublisherImpl.publishLastSale - Product cannot be null or empty");
		}

		HashSet<User> hs = new HashSet<User>();
		
		if(lastSaleSubs.containsKey(product))
		{
			hs = lastSaleSubs.get(product);
			
			for(User u: hs)
			{
				if(p == null)
				{
					p = PriceFactory.makeLimitPrice(0);
				}
				u.acceptLastSale(product, p, v);
			}
			TickerPublisher.getInstance().publishTicker(product, p);
		}
	}

}
