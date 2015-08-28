package publishers;

import price.Price;
import utils.InvalidArgumentException;


/**
 * This interface serves as an extension to the IPublisher interface, adding a 
 * method to publish ticker updates.
 *  
 * @author Urvi
 *
 */
public interface ITickerPublisher extends IPublisher
{
	/**
	 * Sends out ticker information to all subscribed users.  This method also saves the last
	 * prices for products and determines if the prices have gone up, down, or stayed the same.
	 * 
	 * @param prod Stock symbol for which User wants ticker updates.
	 * 
	 * @param p The current price of the stock.  This is compared to the saved price to 
	 * determine the direction of the stock price.
	 */
	
	void publishTicker(String prod, Price p) throws InvalidArgumentException;
}
