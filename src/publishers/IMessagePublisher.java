package publishers;

import utils.InvalidArgumentException;


/**
 * This interface serves as an extension to the IPublisher interface, adding  
 * methods to publish FillMessages, CancelMessages and MarketMessages.
 * 
 * @author Urvi
 *
 */

public interface IMessagePublisher extends IPublisher
{

	/**
	 * Sends out cancel messages for a specified stock to users.
	 * 
	 * @param cm The cancel message to send to users.  
	 */
	
	void publishCancel(CancelMessage cm) throws InvalidArgumentException;
	
	/**
	 * Sends out fill messages for a specified stock to users.
	 * 
	 * @param fm The fill message to send to users.  
	 */
	
	void publishFill(FillMessage fm) throws InvalidArgumentException;
	
	/**
	 * Sends out market messages for a specified stock to users.
	 * 
	 * @param mm The market message to send to users.  
	 */
	
	void publishMarketMessage(MarketMessage mm) throws InvalidArgumentException;
}
