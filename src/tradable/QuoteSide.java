// Urvi Patel
// Phase 2 - QuoteSide.java
// Due Date: 10/1/2012

package tradable;

import price.Price;
import utils.InvalidVolumeValueException;
import utils.InvalidArgumentException;

public class QuoteSide implements Tradable
{
	/**
	 * @see #getUser()
	 * @see #setUser(String)
	 */
    private String userName;
    
    /**
     * @see #getProduct()
     * @see #setProduct(String)
     */
    private String productSymbol;
    
    /**
     * @see #getId()
     * @see #setId(String, String)
     */
    private String id;
    
    /**
     * @see #getPrice()
     * @see #setPrice(Price)
     */   
    private Price sidePrice;
    
    /**
     * @see #getOriginalVolume()
     * @see #setOriginalVolume(int)
     */
    private int originalVolume;
    
    /**
     * @see #getRemainingVolume()
     * @see #setRemainingVolume(int)
     */
    private int remainingVolume;
    
    /**
     * @see #getCancelledVolume()
     * @see #setCancelledVolume(int)
     */
    private int cancelledVolume;
    
    /**
     * @see #getSide()
     * @see #setSide(tradable.Tradable.Side)
     */
    private Side side;

    /**
     * Creates a QuoteSide object
     * @param name Name of user associated with the QuoteSide.
     * @param symbol Symbol associated with the QuoteSide.
     * @param price Price associated with the QuoteSide.
     * @param origVol OriginalQuantity associated with the QuoteSide.
     * @param s The side of the QuoteSide (BUY or SELL)
     * @throws InvalidVolumeValueException if origVol is negative.
     * @throws InvalidArgumentException if name or symbol are empty or null. Or, if price or s are null.
     */
	public QuoteSide(String name, String symbol, Price price, int origVol, Side s) throws InvalidVolumeValueException, InvalidArgumentException
	{
        setUser(name);
        setProduct(symbol);
        setPrice(price);
        setOriginalVolume(origVol);
        setRemainingVolume(origVol);
        setCancelledVolume(0);
        setSide(s);
        setId(name, symbol);
	}
	
	/**
	 * Copy constructor for QuoteSide object
	 * @param qs QuoteSide object to copied from
	 * @throws InvalidVolumeValueException if the qs's original volume is negative, or 
	 * if qs's remaining or cancelled volumes are negative or greater that qs's original volume. 
	 * @throws InvalidArgumentException
	 */

    public QuoteSide(QuoteSide qs) throws InvalidVolumeValueException, InvalidArgumentException
    {
    	if(qs == null)
    	{
    		throw new InvalidArgumentException("QuoteSide copy ctr - QuoteSide passed in cannot be null");
    	}
        setUser(qs.getUser());
        setProduct(qs.getProduct());
        setPrice(qs.getPrice());
        setOriginalVolume(qs.getOriginalVolume());
        setRemainingVolume(qs.getRemainingVolume());
        setCancelledVolume(qs.getCancelledVolume());
        setSide(qs.getSide());
        setId(qs.getUser(), qs.getProduct());
    }
    
	/**
	 * Returns the user associated with the QuoteSide.
	 */
    
    public String getUser()
    {
        return userName;
    }

 
    /**
     * Returns the product symbol associated with the QuoteSide.
     */
    
    public String getProduct()
    {
        return productSymbol;
    }
    
    /**
     * Returns the unique id of the QuoteSide, created when the QuoteSide is received by trading system.
     */
    
    public String getId()
    {
        return id;
    }
    
    /**
     * Returns the Price of the QuoteSide.
     */
    
    public Price getPrice()
    {
        return sidePrice;
    }

    /**
     * Returns the original quantity of the QuoteSide.
     */
    
    public int getOriginalVolume()
    {
        return originalVolume;
    }

    
    /**
     * Returns the remaining quantity of the QuoteSide.
     */
    
    public int getRemainingVolume()
    {
        return remainingVolume;
    }
    
    /**
     * Returns cancelled quantity of the Quote side.
     */
    
    public int getCancelledVolume()
    {
        return cancelledVolume;
    }
    
    /**
     * Sets the QuoteSide's remaining quantity to the value passed in.
     * @param rVol Value to set Order's remaining quantity to. Cannot be negative or greater than the OriginalVolume.
     * @throws InvalidVolumeValueException if parameter remVol is negative or greater than the OriginalVolume.
     */
    
    public void setRemainingVolume(int rVol) throws InvalidVolumeValueException
    {
    	if(rVol < 0)
    	{
    		throw new InvalidVolumeValueException("QuoteSide.setRemainingVolume - Remaining Volume cannot be negative");
    	}
    	
    	if(rVol > getOriginalVolume())
    	{
    		throw new InvalidVolumeValueException("QuoteSide.setRemainingVolume - Remaining Volume cannot be greater than Orignal Volume");
    	}
        remainingVolume = rVol;
    }
    
    /**
     * Sets the QuoteSide's cancelled quantity to the value passed in.
     * @param cVol The value to set the cancelled quantity to. Cannot be negative or greater than the OriginalVolume.
     * @throws InvalidVolumeValueException if parameter cVol is negative or greater than the OriginalVolume.
     */
    
    public void setCancelledVolume(int cVol) throws InvalidVolumeValueException
    {
    	if(cVol < 0)
    	{
    		throw new InvalidVolumeValueException("QuoteSide.setCancelledVolume - Cancelled Volume cannot be negative");
    	}
    	
    	if(cVol > getOriginalVolume())
    	{
    		throw new InvalidVolumeValueException("QuoteSide.setCancelledVolume - Cancelled Volume cannot be greater than Orignal Volume");
    	}
        cancelledVolume = cVol;
    }

    /**
     * Return the side (BUY or SELL) of the QuoteSide.
     */
    public Side getSide()
    {
        return side;
    }

    /**
     * Determines if QuotesSide is part of a Quote.
     * @return True since QuoteSide is indeed a Quote.
     */
    public boolean isQuote()
    {
        return true;
    }

    /**
     * Provides the QuoteSide class with a user friendly string representation of a QuoteSide object.
     */
    public String toString()
    { 
    	return getPrice().toString() + " x " + getOriginalVolume() + 
    			" (Original Vol: " + getOriginalVolume() + ", CXL'd Vol: " + getCancelledVolume() + ") [" + getId() + "]";
    }

    // Private

    
    /**
     * Sets the user name associated with the QuoteSide to the value passed in.
     * @param un The value to set the user name to.  Cannot be null or empty.
     * @throws InvalidArgumentException if parameter un is null or empty.
     */
    
    private void setUser(String un) throws InvalidArgumentException
    {
    	if(un != null && un.isEmpty() == false)
    	{
    		userName = un;
    	}
    	else
    	{
    		throw new InvalidArgumentException("QuoteSide.setUser -  Username cannot be null or empty");
    	}
    }
    
    /**
     * Sets the product name associated with the QuoteSide to the value passed in.
     * @param sym The value to set the product name to.  Cannot be null or empty.
     * @throws InvalidArgumentException if parameter sym is null or empty.
     */
    private void setProduct(String sym) throws InvalidArgumentException
    {
    	if(sym != null && sym.isEmpty() == false)
    	{
    		productSymbol = sym;
    	}
    	else
    	{
    		throw new InvalidArgumentException("QuoteSide.setProduct -  Product name cannot be null or empty");
    	}
    }
    
    /**
     * Set a unique id for the QuoteSide to the concatenation of the user name, product symbol, and System.nanoTime().
     * 
     * @param un User name associated with the QuoteSide.
     * @param sym Product symbol associated with the QuoteSide.
     * @throws InvalidArgumentException if parameters un or sym are null or Empty.
     */
    
    private void setId(String un, String sym) throws InvalidArgumentException
    {
    	if(un == null || un.isEmpty() == true)
    	{
    		throw new InvalidArgumentException("QuoteSide.setId -  User name in id cannot be null or empty");
    	}
    	if(sym == null || sym.isEmpty() == true)
    	{
    		throw new InvalidArgumentException("QuoteSide.setId -  Product name in id cannot be null or empty");
    	}
    	id = un.toString() + sym.toString() + System.nanoTime();
    }
    
    /**
     * Sets the Price of the QuoteSide to the value that was passed in.
     * @param p Price to set price of the QuoteSide to.  Cannot be null.
     * @throws InvalidArgumentException if Price parameter passed in is null.
     */
    
    private void setPrice(Price p) throws InvalidArgumentException
    {
    	if(p != null)
    	{
    		sidePrice = p;
    	}
    	else throw new InvalidArgumentException("QuoteSide.setPrice -  Price cannot be null");
    }
    
    /**
     * Sets the original quantity of the QuoteSide to the value passed in.
     * @param oVol The value to set the OriginalVolume to.
     * @throws InvalidVolumeValueException if the parameter ovol is negative.
     */
	private void setOriginalVolume(int oVol) throws InvalidVolumeValueException
	{
		if(oVol <= 0)
    	{
    		throw new InvalidVolumeValueException("QuoteSide.setPrice -  Orignal Volume cannot be zero or negative: " + oVol);
    	}
		originalVolume = oVol;
	}
    

	/**
	 * Sets the side (BUY or SELL) of an QuoteSide to the value passed in.
	 * @param sde Enum whose valid values are BUY and SELL. Cannot be null.
	 * @throws InvalidArgumentException if parameter passed in is null.
	 */
    private void setSide(Side sde) throws InvalidVolumeValueException
    {
    	if(sde != null)
    	{
    		side = sde;
    	}
    	else
    	{
    		throw new  InvalidVolumeValueException("QuoteSide.setSide -  side cannot be null.");
    	}
    }    
}