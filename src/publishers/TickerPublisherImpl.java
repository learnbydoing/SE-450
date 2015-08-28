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
 * This is the helper class for the TickerPublisher class that implements methods to 
 * subscribe and unsubscribe to get information about the ticker for a stock, and 
 * a method that sends out ticker updates.
 * 
 * @author Urvi
 *
 */ 

public class TickerPublisherImpl implements ITickerPublisher
{
	/**
	 * Constructs a TickerPublisherImpl object
	 */
	public TickerPublisherImpl() {}
	
	/**
	 * A HashMap to keep track of which users are subscribed to which products.
	*/
	private HashMap<String, HashSet<User>> tickerSubs = new HashMap<String, HashSet<User>>();
	
	/**
	 * A HashMap to keep track of the previous price of products.
	*/
	private HashMap<String, Price> tickerLastPrices = new HashMap<String, Price>();
	
	/**
	 * Subscribes a user to receive ticker updates for a given stock.
	 * 
	 * @param u User who wishes to subscribe.
	 * @param prod Product symbol for which User wants ticker updates.
	 * @throws AlreadySubscribedException if user is already subscribed to the product.
	 * @throws InvalidArgumentException if u or prod is null
	 * 
	 */
	
	public synchronized void subscribe(User u, String prod) throws AlreadySubscribedException, InvalidArgumentException  
	{
		if(u == null)
		{
			throw new InvalidArgumentException ("TickerPublisherImpl.subscribe - User cannot be null");
		}

		if(prod == null || prod.isEmpty())
		{
			throw new InvalidArgumentException ("TickerPublisherImpl.subscribe - Product cannot be null or empty");
		}
		HashSet<User> hs = new HashSet<User>();
			
		if(tickerSubs.containsKey(prod) == true)
		{
			hs = tickerSubs.get(prod);
			if(hs.contains(u) == true)
			{
				throw new AlreadySubscribedException("User " + u.getUserName() + " already subscribed to " + prod + " for TickerPublisher\n");
			}
		}
		hs.add(u);
		tickerSubs.put(prod, hs);
	}
	
    /**
	 * Unsubscribes a user from receiving ticker updates for a given stock.
	 * 
	 * @param u User who wishes to unsubscribe.
	 * @param prod Product symbol for which User no longer wants ticker updates.
	 * @throws NotSubscribedException if user was not subscribed to the product.
     * @throws InvalidArgumentException if u or prod is null
	 * 
	 */
	
	public synchronized void unSubscribe(User u, String prod) throws NotSubscribedException, InvalidArgumentException
	{
		if(u == null)
		{
			throw new InvalidArgumentException ("TickerPublisherImpl.unSubscribe - User cannot be null");
		}

		if(prod == null || prod.isEmpty())
		{
			throw new InvalidArgumentException ("TickerPublisherImpl.unSubscribe - Product cannot be null or empty");
		}
		
		HashSet<User> hs = new HashSet<User>();
		hs = tickerSubs.get(prod);
		if(hs == null || hs.contains(u) == false)
		{
			throw new NotSubscribedException("User " + u.getUserName() + " not subscribed to " + prod + " for TickerPublisher\n");
		}
		hs.remove(u);
		tickerSubs.put(prod, hs);
	}
	
	
	/**
	 * Sends out ticker information to all subscribed users.  This method also saves the last
	 * prices for products and determines if the prices have gone up, down, or stayed the same.
	 * 
	 * @param product Stock symbol for which User wants ticker updates.
	 * 
	 * @param priceIn The current price of the stock.  This is compared to the saved price to 
	 * determine the direction of the stock price.
	 * @throws InvalidArgumentException 
	 */
	
	public synchronized void publishTicker(String product, Price priceIn) throws InvalidArgumentException
	{
		if(product == null || product.isEmpty())
		{
			throw new InvalidArgumentException ("TickerPublisherImpl.publishTicker - Product cannot be null or empty");
		}

		 if(priceIn == null)
             priceIn = PriceFactory.makeLimitPrice(0);
		 
        char direction = '\0';
        Price prevPrice = tickerLastPrices.put(product, priceIn);
        HashSet<User> users = new HashSet<User>();
        
        if(tickerSubs.containsKey(product) == true)
        {
            direction = getDirection(prevPrice, priceIn);
            users = tickerSubs.get(product);

//            if(priceIn == null)
//               priceIn = PriceFactory.makeLimitPrice(0);

            for(User u: users)
            {
                u.acceptTicker(product, priceIn, direction);
            }
        }
	}
	
	/**
	 * Private method to determine the direction of the stock price.
	 * 
	 * @param oldPrice The previous price of the stock.
	 * 
	 * @param newPrice The current price of the stock.
	 * 
	 * @return A char representing the direction of the stock price.  If the
	 * price has gone up, a '>' is returned, if the price has gone down, a 
	 * '<' is returned, if the price has remained the same, a '=' is returned.
	 * If there is no previous price then a blank is returned.
	 * @throws InvalidArgumentException
	 * 
	 * @see Price#greaterThan
	 * @see Price#lessThan(Price)
	 * 
	 */
	private char getDirection(Price oldPrice, Price newPrice) throws InvalidArgumentException
	{
    	char dir = '\0';
        if(oldPrice == null)
            dir = ' ';
        else if(newPrice.greaterThan(oldPrice))
            dir = '>';
        else if(newPrice.lessThan(oldPrice))
            dir = '<'; 
        else // if(newPrice.equals(oldPrice))
            dir = '=';
        return dir;
	}
}
