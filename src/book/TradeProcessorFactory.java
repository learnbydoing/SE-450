package book;

import book.TradeProcessor.AllocationStrategy;
import utils.InvalidArgumentException;

/**
 * Creates TradeProcessor objects for different allocation algorithms
 * 
 * @author Urvi
 *
 */
public class TradeProcessorFactory 
{
	/**
	 * Private constructor since all methods are static.
	 */
	private TradeProcessorFactory(){}
	
	/**
	 * Creates TradeProcessor object that uses the price-time algorithm
	 * @param strat The algorithm the TradeProcessor will use to execute trades (PRICETIME)
	 * @param bookSide ProductBookSide object this TradeProcessor belongs to
	 * @return a TradeProcessor object that uses the PRICETIME algorithm
	 * @throws InvalidArgumentException if invalid strategy is passed in
	 */
	public static TradeProcessor getPriceTimeProcessor(AllocationStrategy strat, ProductBookSide bookSide) throws InvalidArgumentException
	{
		if(strat == AllocationStrategy.PRICETIME)
		{
			return new TradeProcessorPriceTimeImpl(bookSide);
		}
		else
		{
			throw new InvalidArgumentException("TradeProcessorFactory.getTradeProcessor - AllocationStrategy cannot be null");
		}
	}
}
