package publishers;

import java.util.HashMap;
import java.util.HashSet;

import utils.AlreadySubscribedException;
import utils.InvalidArgumentException;
import utils.NotSubscribedException;
import client.User;

/**
 * This is the helper class for the MessagePublisher class that implements methods to 
 * subscribe and unsubscribe to get information about last sale for a stock, and 
 * a method that sends out last sale updates.
 * 
 * @author Urvi
 *
 */ 
public class MessagePublisherImpl implements IMessagePublisher
{	
	/**
	 * Constructs a MessagePublisherImpl object
	 */
	public MessagePublisherImpl(){}
	
	/**
	 * A HashMap to keep track of which users are subscribed to which products.
	 */
	
	private HashMap<String, HashSet<User>> messageSubs = new HashMap<String, HashSet<User>>();

	/**
	 * Subscribes a user to receive message updates for a given stock.
	 * @param u User who wishes to subscribe.
	 * @param prod Product symbol for which User wants cancel, fill, or market messages.
	 * @throws AlreadySubscribedException if user is already subscribed to the product.
	 * @throws InvalidArgumentException if u or prod is null
	 * 
	 */
	
	public synchronized void subscribe(User u, String prod) throws AlreadySubscribedException, InvalidArgumentException  
	{
		if(u == null)
		{
			throw new InvalidArgumentException ("MessagePublisherImpl.subscribe - User cannot be null");
		}

		if(prod == null || prod.isEmpty())
		{
			throw new InvalidArgumentException ("MessagePublisherImpl.subscribe - Product cannot be null is empty");
		}
		HashSet<User> hs = new HashSet<User>();
		if(messageSubs.containsKey(prod) == true)
		{
			hs = messageSubs.get(prod);
			if(hs.contains(u) == true)
			{
				throw new AlreadySubscribedException("User " + u.getUserName() + " already subscribed to " + prod + " for MessagePublisher");
			}
		}
		hs.add(u);
		messageSubs.put(prod, hs);
	}
	
    /**
	 * Unsubscribes a user from receiving message updates for a given stock.
	 * @param u User who wishes to unsubscribe.
	 * @param prod Product symbol for which User no longer wants cancel, fill, or market messages.
	 * @throws NotSubscribedException if user was not subscribed to the product
     * @throws InvalidArgumentException if u or prod is null
	 */
	
	public synchronized void unSubscribe(User u, String prod) throws NotSubscribedException, InvalidArgumentException
	{
		if(u == null)
		{
			throw new InvalidArgumentException ("MessagePublisherImpl.unSubscribe - User cannot be null");
		}

		if(prod == null || prod.isEmpty())
		{
			throw new InvalidArgumentException ("MessagePublisherImpl.unSubscribe - Product cannot be null is empty");
		}

		HashSet<User> hs = new HashSet<User>();
		hs = messageSubs.get(prod);
		if(hs == null || hs.contains(u) == false)
		{
			throw new NotSubscribedException("User " + u.getUserName() + " not subscribed to " + prod + " for MessagePublisher");
		}
		hs.remove(u);
		messageSubs.put(prod, hs);
			
	}
	
	/**
	 * Sends out cancel messages for a specified stock to users.
	 * @param cm The cancel message to send to users.  
	 * @throws InvalidArgumentException if cm is null
	 */
	
	public synchronized void publishCancel(CancelMessage cm) throws InvalidArgumentException
	{
		if(cm == null)
		{
			throw new InvalidArgumentException("MessagePublisherImpl.publishCancel - CancelMessage cannot be null");
		}
		
		HashSet<User> hs = new HashSet<User>();
		
		if(messageSubs.containsKey(cm.getProduct()))
		{
			hs = messageSubs.get(cm.getProduct());
			for(User u: hs)
			{
				if(u.getUserName() == cm.getUser())
				{
					u.acceptMessage(cm);
					break;
				}
			}
		}
	}
	
	/**
	 * Sends out fill messages for a specified stock to users.
	 * @param fm The fill message to send to users.  
	 * @throws InvalidArgumentException if fm is null
	 */
	
	public synchronized void publishFill(FillMessage fm) throws InvalidArgumentException
	{
		if(fm == null)
		{
			throw new InvalidArgumentException("MessagePublisherImpl.publishFill - FillMessage cannot be null");
		}
		
		HashSet<User> hs = new HashSet<User>();
		
		if(messageSubs.containsKey(fm.getProduct()))
		{
			hs = messageSubs.get(fm.getProduct());
			for(User u: hs)
			{
				if(u.getUserName() == fm.getUser())
				{
					u.acceptMessage(fm);
					break;
				}
			}
		}
	}
	
	/**
	 * Sends out market messages for a specified stock to users.
	 * 
	 * @param mm The market message to send to users.  
	 * @throws InvalidArgumentException if mm is null
	 */
	
	public synchronized void publishMarketMessage(MarketMessage mm) throws InvalidArgumentException
	{
        if (mm == null) {
            throw new InvalidArgumentException("MessagePublisherImpl.publishMarketMessage - MarketMessage cannot be null");
        }

        HashSet<User> hs = new HashSet<User>();

        for (String stock : messageSubs.keySet()) 
        {
            HashSet<User> hu = messageSubs.get(stock);
            hs.addAll(hu);
        }

        for (User u : hs) 
        {
            u.acceptMarketMessage(mm.toString());
        }
	}
}
