package publishers;

import price.Price;
import utils.InvalidArgumentException;

/**
 * This interface serves as an extension to the IPublisher interface, adding 
 * a method to publish last sale updates.
 * 
 * @author Urvi
 *
 */

public interface ILastSalePublisher extends IPublisher
{
    /**
     * Sends out last sale information to all users who have subscribed to a specified stock.
     * @param prod Stock symbol user is subscribed to
     * @param p Last sale price of the stock symbol user is subscribed to
     * @param volume Last volume for the stock symbol user is subscribed to
     */
    
	void publishLastSale(String prod, Price p, int volume) throws InvalidArgumentException;

}
