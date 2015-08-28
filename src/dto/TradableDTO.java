// Urvi Patel
// Phase 2 - TradableDTO.java
// Due Date: 10/1/2012

package dto;


import price.Price;
import tradable.Tradable.Side;

/**
 * This class encapsulates selected data values from a Tradable, used for data transfer.  
 * 
 * @author Urvi
 *
 */
public class TradableDTO
{

	/**
	 * Product symbol that works with Tradable.
	 */
    public String product;
    
    /**
     * Product symbol that works with Tradable.
     */
    public Price price;
    
    /**
     * The original volume of the Tradable.
     */
    public int originalVolume;
    
    /**
     * The remaining volume of the Tradable.
     */
    public int remainingVolume;
    
    /**
     * The cancelled vaolume of the Tradable.
     */
    public int cancelledVolume;
    
    /**
     * The user id associated with the Tradable.
     */
    public String user;
    
    /**
     * The side (BUY or SELL) of the Tradable.
     */
    public Side side;
    
    /**
     * True if Tradable is a Quote, false otherwise.
     */
    public boolean isQuote;
    
    /**
     * The unique id given to the Tradable.
     */
    public String id;


    /**
     * Public constructor used to create a TradableDTO, made from data values from the Tradable.
     * 
     * @param prod Product symbol that works with Tradable.
     * @param p Price of the Tradable.
     * @param origVol The original volume of the Tradable.
     * @param remVol The remaining volume of the Tradable.
     * @param cancVol The cancelled vaolume of the Tradable.
     * @param username The user id associated with the Tradable.
     * @param s The side (BUY or SELL) of the Tradable.
     * @param quote Set to true if Tradable is a quote, false otherwise.
     * @param ID The unique id given to the Tradable.
     */
	public TradableDTO(String prod, Price p, int origVol, int remVol, int cancVol, String username, Side s, boolean quote, String ID) //throws InvalidVolumeValueException, InvalidArgumentException
	{
       product = prod;
       price = p;
       originalVolume = origVol;
       remainingVolume = remVol;
       cancelledVolume = cancVol;
       user = username;
       side = s;
       isQuote = quote;
       id = ID;
	}
	
	/**
	 * Provides the TradableDTO class with a user friendly string representation of a TradableDTO object
	 */

	
	public String toString()
	{
		return "Product: " + product + ", " +
				"Price: " + price.toString() + ", " +
				"OriginalVolume: " + originalVolume+ ", " +
				"RemainingVolume: " + remainingVolume + ", " +
				"CancelledVolume: " + cancelledVolume + ", "  +
				"User: " + user + ", " +
				"Side: " + side  + ", " +
				"IsQuote: " + isQuote + ", " +
				"Id: " + id;
	}
}
