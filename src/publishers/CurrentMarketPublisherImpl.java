package publishers;

import java.util.HashMap;
import java.util.HashSet;

import price.PriceFactory;
import utils.AlreadySubscribedException;
import utils.InvalidArgumentException;
import utils.NotSubscribedException;
import client.User;
import dto.MarketDataDTO;


/**
 * This is the helper class for the CurrentMarketPublisher that implements methods to 
 * subscribe, unsubscribe, and send out market updates.
 * 
 * @author Urvi
 *
 */
public class CurrentMarketPublisherImpl implements ICurrentMarketPublisher, IPublisher
{
	
	/**
	 * Constructs a CurrentMarketPublisherImpl object
	 */
	public CurrentMarketPublisherImpl() {}
	
	/**
	 * A HashMap to keep track of which users are subscribed to which products.
	 */
	
	private HashMap<String, HashSet<User>> currentMktSubs = new HashMap<String, HashSet<User>>();

	/**
	 * Subscribes a user to receive current market data updates for a given stock
	 * @param u User who wishes to subscribe.
	 * @param prod Product symbol for which User wants current market information.
	 * @throws AlreadySubscribedException if user is already subscribed to the product.
	 * @throws InvalidArgumentException if u or prod is null
	 * 
	 */

	public synchronized void subscribe(User u, String prod) throws AlreadySubscribedException, InvalidArgumentException  
	{
		if(u == null)
		{
			throw new InvalidArgumentException ("CurrentMarketPublisherImpl.subscribe - User cannot be null");
		}

		if(prod == null || prod.isEmpty())
		{
			throw new InvalidArgumentException ("CurrentMarketPublisher.subscribe - Product cannot be null or empty");
		}
		HashSet<User> hs = new HashSet<User>();
		if(currentMktSubs.containsKey(prod) == true)
		{
			hs = currentMktSubs.get(prod);
			if(hs.contains(u) == true)
			{
				throw new AlreadySubscribedException("User " + u.getUserName() + " already subscribed to " + prod + " for CurrentMarketPublisher");
			}
		}
		hs.add(u);
		currentMktSubs.put(prod, hs);
	}
	
	/**
	 * Unsubscribes a user from current market data.
	 * @param u User who wishes to unsubscribe.
	 * @param prod Product symbol for which User no longer wants current market information
	 * @throws NotSubscribedException if user is not subscribed to the product.
	 * @throws InvalidArgumentException if u or prod is null
	 * 
	 */
	
	public synchronized void unSubscribe(User u, String prod) throws NotSubscribedException, InvalidArgumentException
	{
		if(u == null)
		{
			throw new InvalidArgumentException ("CurrentMarketPublisherImpl.unSubscribe - User cannot be null");
		}

		if(prod == null || prod.isEmpty())
		{
			throw new InvalidArgumentException ("CurrentMarketPublisher.unSubscribe - Product cannot be null or empty");
		}
		HashSet<User> hs = new HashSet<User>();

		hs = currentMktSubs.get(prod);
		if(hs == null || hs.contains(u) == false)
		{
			throw new NotSubscribedException("User " + u.getUserName() + " not subscribed to " + prod + " for CurrentMarketPublisher");
		}
		hs.remove(u);
		currentMktSubs.put(prod, hs);
	}
	
    
	/**
	 * Sends out market updates to all users who have subscribed to a specified stock symbol.
	 * @param mdto The MarketDataDTO object that encapsulates all data related to the current market.
	 * @throws InvalidArgumentException if mdto is null
	 */
	
	public synchronized void publishCurrentMarket(MarketDataDTO mdto) throws InvalidArgumentException
	{
		if(mdto == null)
		{
			throw new InvalidArgumentException ("CurrentMarketPublisher.publishCurrentMarket - MarketDataDTO cannot be null");
		}
		
		String stock = mdto.product;
		HashSet<User> usersSubbedToStock = new HashSet<User>();
		
		if(currentMktSubs.containsKey(stock))
		{
			usersSubbedToStock = currentMktSubs.get(stock);
			
			for(User u : usersSubbedToStock)
			{
				if(mdto.buyPrice == null)
				{
					mdto.buyPrice = PriceFactory.makeLimitPrice(0);
				}
				if(mdto.sellPrice == null)
				{
					mdto.sellPrice = PriceFactory.makeLimitPrice(0);
				}
				u.acceptCurrentMarket(mdto.product, mdto.buyPrice, mdto.buyVolume, mdto.sellPrice, mdto.sellVolume);
			}
		}
	}
}
