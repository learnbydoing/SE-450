package book;

import java.util.HashMap;
import publishers.FillMessage;
import tradable.Tradable;
import utils.InvalidArgumentException;
import utils.InvalidVolumeValueException;


/**
 * Interface that defines the functionality needed to “execute” the actual trades between Tradable objects in this book side. There is only one method on this interface, doTrade(Tradable)
 * 
 * @author Urvi
 *
 */
public interface TradeProcessor
{
	/**
	 * Trades will be executed based on their price and the time they were submitted to they system.  This enumerator is used to dictate which strategy will be used at a given time.
	 * Other strategies may be used in the future so this enum may change.  
	 * 
	 * @author Urvi
	 *
	 */
	enum AllocationStrategy { PRICETIME };
	
	/**
	 * Method called when it has been determined that a Tradable (i.e., a Buy Order, a Sell QuoteSide, etc.) can trade against the content of the book. 
	 * 
	 * @param trd The Tradable to be traded
	 * @return HashMap containing String Tradable identifiers and a Fill Message object
	 * @throws InvalidArgumentException if trd is null
	 * @throws InvalidVolumeValueException if remaining volume of tradable is set to negative value after trade, 
	 */
	public HashMap<String, FillMessage> doTrade(Tradable trd) throws InvalidArgumentException, InvalidVolumeValueException ;
}
