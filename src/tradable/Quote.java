// Urvi Patel
// Phase 2 - Quote.java
// Due Date: 10/1/2012

package tradable;

import price.Price;
import tradable.Tradable.Side;
import utils.InvalidArgumentException;
import utils.InvalidVolumeValueException;


/**
 * This class represents the prices and volumes of certain stock that the user is willing to 
 * BUY or SELL shares.
 * 
 * @author Urvi
 *
 */

public class Quote
{
	/**
	 * @see #getUserName()
	 * @see #setUserName(String)
	 */
    private String userName;
    
    /**
     * @see #getProduct()
     * @see #setProduct(String)
     */
    private String productSymbol;
   
    /**
     * @see #getQuoteSide(tradable.Tradable.Side)
     * @see #setSide(String, String, Price, int, tradable.Tradable.Side)
     */
    private QuoteSide buySide;
    
    /**
     * @see #getQuoteSide(tradable.Tradable.Side)
     * @see #setSide(String, String, Price, int, tradable.Tradable.Side)
     */
  
    private QuoteSide sellSide;
    
    /**
     * Public constructor for Quote class.
     * @param name User name associated with the Quote
     * @param symbol Stock symbol associated with the Quote
     * @param bPrice Buy price associated with the Quote
     * @param bVol Buy quantity associated with the Quote
     * @param sPrice Sell price associated with the Quote
     * @param sVol Sell quantity associated with the Quote
     * @throws InvalidVolumeValueException if parameter bVol is negative.
     * @throws InvalidArgumentException if parameters name or symbol are null or empty.  Or, if parameters bPrice, sPrice are null.
     */
    
	public Quote(String name, String symbol, Price bPrice, int bVol, Price sPrice, int sVol) throws InvalidVolumeValueException, InvalidArgumentException
	{
        setUserName(name);
        setProduct(symbol);
        setSide(name, symbol, bPrice, bVol, Side.BUY);
        setSide(name, symbol, sPrice, sVol, Side.SELL);
	}

	/**
	 * Returns the user associated with the Quote.
	 */
	
    public String getUserName()
    {
        return userName;
    }
    
    /**
     * Returns the product symbol associated with the Order.
     */
	
    public String getProduct()
    {
        return productSymbol;
    }
    
    /**
     * Returns a copy of the BUY or SELL QuoteSide object
     * 
     * @param sideIn Side of the Quote, (BUY or SELL)
     * @return A copy of QuoteSide object, created using data elements that make up a QuoteSide
     * @throws InvalidVolumeValueException 
     * @throws InvalidArgumentException
     */
    public QuoteSide getQuoteSide(Side sideIn) throws InvalidVolumeValueException, InvalidArgumentException
    {
    	if(sideIn == null)
    	{
    		throw new InvalidArgumentException("Quote.getQuoteSide - Side cannot be null");
    	}
        if (sideIn == Side.BUY)
        {
            return new QuoteSide(buySide);
        }
        else
        {
            return new QuoteSide(sellSide);
        }
    }
    
    /**
     * Provides the Quote class with a user friendly string representation of a Quote object.
     */
    
    public String toString()
    { 
    	return  getUserName() + " quote: " + getProduct() + " " + buySide.getPrice().toString() + " x " + 
    			buySide.getRemainingVolume() + " (Orignal Vol: " + buySide.getOriginalVolume() + ", CXL'd Vol: " +
    			buySide.getCancelledVolume() + ") " + "[" + buySide.getId() + "]" +
 
    			" - " + 
    			
    			sellSide.getPrice().toString() + " x " +
    			sellSide.getRemainingVolume() + " (Orignal Vol: " + sellSide.getOriginalVolume() + ", CXL'd Vol: " +
    			sellSide.getCancelledVolume() + ") " + "[" + sellSide.getId() + "]";
    }

    // Private
    
    /**
     * Sets the user name associated with a Quote to the value passed in.
     * @param un The value to set the user name to.  Cannot be null or empty.
     * @throws InvalidArgumentException if parameter name is null or empty.
     */
    private void setUserName(String un) throws InvalidArgumentException
    {
    	if(un != null && un.isEmpty() == false)
    	{
    		userName = un;
    	}
    	else
    	{
    		throw new InvalidArgumentException("Quote.setUserName - Username cannot be null or empty");
    	}
    }
    
    /**
     * Sets the product name associated with a Quote to the value passed in.
     * @param prod The value to set the product name to.  Cannot be null or empty.
     * @throws InvalidArgumentException if parameter prod is null or empty.
     */
    
    private void setProduct(String prod) throws InvalidArgumentException
    {
    	if(prod != null && prod.isEmpty() == false)
    	{
    		productSymbol = prod;
    	}
    	else
    	{
    		throw new InvalidArgumentException("Quote.setProduct - Product name cannot be null or empty");
    	}
    }
    
	
/**
 * Creates a QuoteSide object, based on the side (BUY or SELL) that is passed in.
 * @param name Name of user associated with the QuoteSide
 * @param sym Product symbol associated with the QuoteSide
 * @param pce Price associated with the QuoteSide
 * @param vol Original volume associated with the QuoteSide
 * @param sde Side (BUY or SELL) associated with the QuoteSide
 * @throws InvalidVolumeValueException if parameter vol is negative
 * @throws InvalidArgumentException if parameters name or sym are null or empty.  Or, if parameters pce or sde are null.
 */
    
    private void setSide(String name, String sym, Price pce, int vol, Side sde) throws InvalidVolumeValueException, InvalidArgumentException
    {
    	if(name == null || name.isEmpty() == true)
    	{
    		throw new InvalidArgumentException("Quote.setSide - User name cannot be null or empty");
    	}
    	
    	if(pce == null)
    	{
    		throw new InvalidArgumentException("Quote.setSide - Price cannot be null");
    	}
    	
    	if(sde == null)
    	{
    		throw new InvalidArgumentException("Quote.setSide - Side cannot be null");
    	}
    	
    	if(sde == Tradable.Side.BUY)
    	{
    		buySide = new QuoteSide(name, sym, pce, vol, sde);
    	}
    	else
    	{
    		sellSide = new QuoteSide(name, sym, pce, vol, sde);
    	}
    } 
}

