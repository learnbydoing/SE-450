package publishers;

import utils.AlreadySubscribedException;
import utils.InvalidArgumentException;
import utils.NotSubscribedException;
import client.User;


/**
 * This interface contains methods common to all publishers, namely subscribe and unSubscribe.
 * 
 * @author Urvi
 *
 */
public interface IPublisher 
{
	/**
	 * Enum that denotes the type of publisher
	 *
	 */
		enum PubType { CurrentMarket, LastSale, Message, Ticker };

	/**
	
	/**
	 * This method subscribes a user to receive data (i.e., current market, last sale, ticker,
	 * and market messages) for a stock symbol.
	 *  
	 * @param user The user who wishes to subscribe.
	 * 
	 * @param prod The product the user wishes to subscribe to.
	 * 
	 * @throws AlreadySubscribedException if the user is already subscribed to the product in prod parameter
	 * 
	 */
	void subscribe(User user, String prod) throws AlreadySubscribedException, InvalidArgumentException;
	
	/**
	 * This method unsubscribes a user to receive data (i.e., current market, last sale, ticker,
	 * and market messages) for a stock symbol.
	 *  
	 * @param user The user who no longer wishes to subscribe.
	 * 
	 * @param prod The product the user no longer wishes to subscribe to.
	 * 
	 * @throws AlreadySubscribedException if the user is not subscribed to the product in prod parameter
	 * 
	 */
	void unSubscribe(User user, String prod) throws NotSubscribedException, InvalidArgumentException;	
}
