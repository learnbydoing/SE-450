// Urvi Patel
// Phase 2 - Order.java
// Due Date: 10/1/2012

package tradable;

import price.Price;
import utils.InvalidArgumentException;
import utils.InvalidVolumeValueException;


/**
 * This class represents a request from the user BUY or SELL a specified quantity of a specified stock at
 * either a specified price or the market price.  It implements the Tradable interface.
 * 
 * @author Urvi
 *
 */

public class Order implements Tradable
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
     * @see #setId(String, String, Price)
     */
    private String id;
    
    /**
     * @see #getPrice()
     * @see #setPrice(Price)
     */
    private Price orderPrice;
    
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
     * Public constructor for Order object
     * @param user User name associated with the Order.
     * @param symbol Product associated with the Order.
     * @param price Price associated with the Order.
     * @param origVol Original volume associated with the Order.
     * @param s Side of of the Order.
     * @throws InvalidVolumeValueException if parameter origVol is negative.
     * @throws InvalidArgumentException if parameters user or symbol were null or empty. Or, if parameters priceor s are null, 
     */
	public Order(String user, String symbol, Price price, int origVol, Side s) throws InvalidVolumeValueException, InvalidArgumentException
	{
        setUser(user);
        setProduct(symbol);
        setPrice(price);
        setOriginalVolume(origVol);
        setSide(s);
        setRemainingVolume(origVol);
        setId(user, symbol, price);
	}
	

	/**
	 * Returns the user associated with the Order.
	 */

    public String getUser() 
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
     * Returns the unique id of the Order, created when Order is received by trading system.
     */
    
    public String getId()
    {
        return id;
    }
    
   /**
    * Returns the Price of the Order.
    */
    
    public Price getPrice()
    {
        return orderPrice;
    }
      
   /**
    * Returns the original quantity of an Order.
    */
    public int getOriginalVolume()
    {
        return originalVolume;
    }

    /**
     * Returns the remaining (working) quantity of an Order.
     */
    public int getRemainingVolume()
    {
        return remainingVolume;
    }
    
    /**
     * Returns cancelled quantity of an Order.
     */
    public int getCancelledVolume()
    {
        return cancelledVolume;
    }
    

    /**
     * Sets the Order's remaining quantity to the value passed in.
     * @param rVol Value to set Order's remaining quantity to. Cannot be negative or greater than the OriginalVolume.
     * @throws InvalidVolumeValueException if parameter remVol is negative or greater than the OriginalVolume.
     */
    public void setRemainingVolume(int rVol) throws InvalidVolumeValueException
    {
    	if(rVol < 0)
    	{
    		throw new InvalidVolumeValueException("Order.setRemainingVolume - Remaining Volume cannot be negative: " + rVol);
    	}
    	if(rVol > getOriginalVolume())
    	{
    		throw new InvalidVolumeValueException("Order.setRemainingVolume - Remaining Volume cannot be greater than Orignal Volume. Rem: " + rVol + " Orig: " + getOriginalVolume());
    	}   	
        remainingVolume = rVol;
    }
	
    
    
    /**
     * Sets the Order's cancelled quantity to the value passed in.
     * @param cVol The value to set the cancelled quantity to. Cannot be negative or greater than the OriginalVolume.
     * @throws InvalidVolumeValueException if parameter cVol is negative or greater than the OriginalVolume.
     */
    public void setCancelledVolume(int cVol) throws InvalidVolumeValueException
    {
    	if(cVol < 0)
    	{
    		throw new InvalidVolumeValueException("Order.setCancelledVolume - Cancelled Volume cannot be negative " + cVol);
    	}
    	
    	if(cVol > getOriginalVolume())
    	{
    		throw new InvalidVolumeValueException("Order.setCancelledVolume - Cancelled Volume cannot be greater than Orignal Volume.  Cxl'd: " + cVol + " Orig: " + getOriginalVolume());
    	}
        cancelledVolume = cVol;
    }


    /**
     * Return the side (BUY or SELL) of the Order.
     */
    public Side getSide()
    {
        return side;
    }

    /**
     * Determines if Tradable is part of a Quote.
     * @return False since Order is not a Quote.
     */
    public boolean isQuote()
    {
        return false;
    }
    
    /**
     * Provides the Order class with a user friendly string representation of an Order object.
     */
    public String toString()
    {

    	return getUser() + " order: " + getSide() + " "  + getRemainingVolume() + " " + getProduct() + " at " +
    			getPrice().toString() + " " + "(Original Vol: " + getOriginalVolume() + " , CXL'd Vol: " + getCancelledVolume() 
    			+ "), ID: " + getId();
    }
    
    // Private

    /**
     * Sets the user name associated with an Order to the value passed in.
     * @param name The value to set the user name to.  Cannot be null or empty.
     * @throws InvalidArgumentException if parameter name is null or empty.
     */
    
    private void setUser(String name) throws InvalidArgumentException
    {
    	if(name != null && name.isEmpty() == false)
    	{
    		userName = name;
    	}

    	else
    	{
    		throw new InvalidArgumentException("Order.setUser - Username cannot be null or empty");
    	}
    }
    
    /**
     * Sets the product name associated with an Order to the value passed in.
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
    		throw new InvalidArgumentException("Order.setProduct - Product name cannot be null or empty");
    	}
    }
    
    /**
     * Set a unique id for the Order to the concatenation of the user name, product symbol, price, and System.nanoTime().
     * 
     * @param un User name associated with the Order.
     * @param sym Product symbol associated with the Order.
     * @param p Price associated with the Order
     * @throws InvalidArgumentException if parameters un or sym are null or Empty, or if parameter p is null.
     */
    
    private void setId(String un, String sym, Price p) throws InvalidArgumentException
    {
    	if(un == null || un.isEmpty() == true)
    	{
    		throw new InvalidArgumentException("Order.setId - User name in id cannot be null or empty");
    	}
    	if(sym == null || sym.isEmpty() == true)
    	{
    		throw new InvalidArgumentException("Order.setId - Product name in id cannot be null or empty");
    	}
    	id = un.toString() + sym.toString() + p.toString() + System.nanoTime();
    }
    
    /**
     * Sets the Price of the Order to the value that was passed in.
     * @param p Price to set price of the Order to.  Cannot be null.
     * @throws InvalidArgumentException if Price parameter passed in is null.
     */
    private void setPrice(Price p) throws InvalidArgumentException
    {
    	if(p != null)
    	{
    		orderPrice = p;
    	}
    	else
    	{
    		throw new InvalidArgumentException("Order.setPrice - Price cannot be null");
    	}
    }
    
    /**
     * Sets the original quantity of the Order to the value passed in.
     * @param oVol The value to set the OriginalVolume to.
     * @throws InvalidVolumeValueException if the parameter ovol is negative.
     */
    
	private void setOriginalVolume(int oVol) throws InvalidVolumeValueException
	{
		if(oVol <= 0)
    	{
    		throw new InvalidVolumeValueException("Order.setOriginalVolume - Orignal Volume cannot be zero or negative: " + oVol);
    	}
		originalVolume = oVol;
	}
	
	/**
	 * Sets the side (BUY or SELL) of an Order to the value passed in.
	 * @param sde Enum whose valid values are BUY and SELL. Cannot be null.
	 * @throws InvalidArgumentException if parameter passed in is null.
	 */
	
    private void setSide(Side sde) throws InvalidArgumentException
    {
    	if(sde != null)
    	{
    		side = sde;
    	}
    	else
    	{
    		throw new InvalidArgumentException("Order.setSide - Side cannot be null");
    	}
    }    
}
