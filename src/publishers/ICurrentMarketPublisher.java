package publishers;

import utils.InvalidArgumentException;
import dto.MarketDataDTO;

/**
 * This interface serves as an extension to the IPublisher interface, adding a
 * method to publish current market updates.
 * 
 *  
 * @author Urvi
 *
 */
public interface ICurrentMarketPublisher extends IPublisher
{
	
	/**
	 * Sends out market updates to all users who have subscribed to a specified stock symbol.
	 * @param mdDTO The MarketDataDTO object that encapsulates all data related to the current market.
	 */
	void publishCurrentMarket(MarketDataDTO mdDTO) throws InvalidArgumentException;
}
