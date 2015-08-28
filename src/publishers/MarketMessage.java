package publishers;

import utils.InvalidArgumentException;

/**
 * This class contains data related to the state of the market.
 * 
 * @author Urvi
 *
 */
public class MarketMessage 
{
	/**
	 * Describes the state of the market, values are CLOSED, PREOPEN, and OPEN.
	 * @author Urvi
	 *
	 */
	public enum MarketState {CLOSED, PREOPEN, OPEN};
	
	/**
	 * Holds the state of the market (CLOSED, PREOPEN, OPEN)
	 */
	private MarketState state;
	
	
	/**
	 * Creates a MarketMessage object.
	 * 
	 * @param ms MarketState can be CLOSED, PREOPEN, or OPEN.
	 * @throws InvalidArgumentException  if ms parameter passed in is null.
	 */
	
	public MarketMessage(MarketState ms) throws InvalidArgumentException
	{
		setState(ms);
	}
	
	/**
	 * Returns the current market state
	 * 
	 * @return An enum of type MarketState whose valid values are CLOSED, PREOPEN, and OPEN.
	 */
	
	public MarketState getState()
	{
		return state;
	}
	
	/**
	 * Provides the MarketMessage with a user friendly string representation of a MarketMessage object.
	 */
	
	public String toString()
	{
		return getState().toString();
	}
	
	/**
	 * Sets the market state to the value that is passed in.
	 * This is a private method so that the input can be validated.
	 * 
	 * @param ste MarketState, valid values are CLOSED, PREOPEN, and OPEN.
	 * 
	 * @throws InvalidArgumentException if ste parameter passed in is null.
	 */
	private void setState(MarketState ste) throws InvalidArgumentException
	{
		if(ste != null)
		{
			state = ste;
		}
		else
		{
			throw new InvalidArgumentException("MarketMessage - MarketState cannot be null.");
		}
	}
}
