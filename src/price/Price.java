// Urvi Patel
// SE 450 - Phase 1 - Price.java
// Due date:  9/24/2012


package price;

import utils.InvalidArgumentException;
import utils.InvalidPriceOperation;


/**
 * This class represents prices uses throughout the trading system.
 * 
 * @author Urvi
 *
 */


public class Price implements Comparable<Price>
{
		/**
		 * The price, as whole cents.
		 */
	private long price;
	
	/**
	 * Used to determine if Price is a market price.
	 */
	private boolean isMarketPrice;
	
	/**
	 * Constructor is protected to prevent Prices from being created by classes outside this package.
	 * This constructor is used to create limit prices.
	 * 
	 * @param value A long integer representation of the price, i.e., the price in whole cents.
	 */
	protected Price(long value)
	{
		price = value;	
		isMarketPrice = false;  //set to false for limit Price object
	}
	
	/**
	 * Constructor is protected to prevent Prices from being created by classes outside this package.
	 * This constructor is used to create market prices. Market prices have no actually value, they
	 * indicate that a user would like trade right now at the going market price. 
	 */
	
	protected Price()
	{
		isMarketPrice = true;  //set to true for limit Price object
	}
	
	/**
	 * Adds current price object to the price object passed in.
	 * @param p The Price object passed in
	 * @return A new Price object representing the sum of the two Price objects.
	 * @throws InvalidPriceOperation if either Price object is a Market Price.
	 * @throws InvalidArgumentException if Price object passed in is null
	 */
	public Price add(Price p) throws InvalidPriceOperation, InvalidArgumentException
	{
		if(p == null)
		{
			throw new InvalidArgumentException("Price.add - Price cannot be null");
		}
		
		if(this.isMarketPrice == true || p.isMarketPrice == true)
		{
			throw new InvalidPriceOperation("Cannot add a LIMIT price to a MARKET Price"); 
		}
		else
		{
			Price prce = PriceFactory.makeLimitPrice(this.price + p.price);
			return prce;
		}
	}
	
	/**
	 * Subtracts the the price object passed in from the current Price object.
	 * @param p The Price object passed in
	 * @return A new Price object representing the difference of the two Price objects.
	 * @throws InvalidPriceOperation if either Price object is a Market Price.
	 * @throws InvalidArgumentException if Price object passed in is null
	 */
	public Price subtract(Price p) throws InvalidPriceOperation, InvalidArgumentException
	{
		if(p == null)
		{
			throw new InvalidArgumentException("Price.subtract - Price cannot be null");
		}
		
		if(this.isMarketPrice == true || p.isMarketPrice == true) // check if either is a market price
		{
			throw new InvalidPriceOperation("Cannot subtract a LIMIT price from a MARKET Price");
		}
		else
		{
			Price prce = PriceFactory.makeLimitPrice(this.price - p.price);
			return prce;
		}
	}

	/**
	 * Multiplies the current Price object by the integer passed in.
	 * @param p The integer by which to multiply the current Price object. 
	 * @return A new Price object representing the multiplication of the 
	 * current Price object by the integer passed in
	 * @throws InvalidPriceOperation if teh current Price is a Market Price.
	 */
	
	public Price multiply(int p) throws InvalidPriceOperation
	{
		if(this.isMarketPrice == true)
		{
			throw new InvalidPriceOperation("Cannot multiply a MARKET price");
		}
		else
		{
			Price prce = new Price(this.price * p);
			return prce;
		}
	}
	
	/**
	 * Compares the current Price object to the Price object passed in.
	 * @param p The Price object to compare the current Price object to.
	 * @return 0 if Price objects are equal. Negative value if Price object
	 * passed in is greater than the current Price object.  Positive value if
	 * the Price object passed in is less than the current Price object.
	 */
	
	public int compareTo(Price p)
	{
		
		if(p.price == this.price)
		{
			return 0;
		}
		
		else if(p.price > this.price)
		{
			return -1;
		}
		
		else
			return 1;
	}
	
	
	/**
	 * Determines if the current Price object passed in is greater than
	 * or equal to the Price object passed in.
	 * @param p The Price object passed in
	 * @return True if current Price object is greater than or equal to
	 * the Price object passed in. If either is market price, return false.
	 * @throws InvalidArgumentException if p is null
	 */
	public boolean greaterOrEqual(Price p) throws InvalidArgumentException
	{
		if(p == null)
		{
			throw new InvalidArgumentException("Price.greaterOrEqual - Price cannot be null");
		}
		
		if(this.isMarketPrice == true || p.isMarketPrice == true)
		{
			return false;
		}
		if(p.compareTo(this) == -1 || p.compareTo(this) == 0)
			return true;
		else
			return false;			
	}

	/**
	 * Determines if the current Price object passed in is greater than
	 * the Price object passed in.
	 * @param p The Price object passed in
	 * @return True if current Price object is greater than 
	 * the Price object passed in. If either is market price, return false.
	 * @throws InvalidArgumentException if p is null
	 */
	
	public boolean greaterThan(Price p) throws InvalidArgumentException
	{
		if(p == null)
		{
			throw new InvalidArgumentException("Price.greaterThan - Price cannot be null");
		}
		
		if(this.isMarketPrice == true || p.isMarketPrice == true)
		{
			return false;
		}
		if(p.compareTo(this) == -1)
			return true;
		else
			return false;
	}
	
	/**
	 * Determines if the current Price object passed in is less than or equal
	 * to the Price object passed in.
	 * @param p The Price object passed in
	 * @return True if current Price object is less than or equal to
	 * the Price object passed in. If either is market price, return false.
	 * @throws InvalidArgumentException if p is null 
	 */
	public boolean lessOrEqual(Price p) throws InvalidArgumentException
	{
		if(p == null)
		{
			throw new InvalidArgumentException("Price.lessOrEqual - Price cannot be null");
		}
		if(this.isMarketPrice == true || p.isMarketPrice == true)
		{
			return false;
		}
		if(p.compareTo(this) == 1 || p.compareTo(this) == 0)
			return true;
		else
			return false;			
	}

	/**
	 * Determines if the current Price object passed in is less than
	 * the Price object passed in.
	 * @param p The Price object passed in
	 * @return True if current Price object is less than 
	 * the Price object passed in. If either is market price, return false.
	 * @throws InvalidArgumentException if p is null 
	 */
	public boolean lessThan(Price p) throws InvalidArgumentException 
	{
		if(p == null)
		{
			throw new InvalidArgumentException("Price.lessThan - Price cannot be null");
		}
		if(this.isMarketPrice == true || p.isMarketPrice == true)
		{
			return false;
		}
		if(p.compareTo(this) == 1)
			return true;
		else
			return false;
	}
	
	/**
	 * Determines if the current Price object passed in is equal to 
	 * the Price object passed in.
	 * @param p The Price object passed in
	 * @return True if current Price object is equal to
	 * the Price object passed in. 
	 * @throws InvalidArgumentException is null
	 */
	public boolean equals(Price p) throws InvalidArgumentException
	{
		if(p == null)
		{
			throw new InvalidArgumentException("Price.equals - Price cannot be null");
		}
		return (p.compareTo(this) == 0 && p.isMarketPrice == this.isMarketPrice);
	}

	/**
	 * Determines if Price is a market Price.
	 * @return True if Price is a market Price.
	 */
	public boolean isMarket()
	{
		return this.isMarketPrice;
	}
	
	/**
	 * Determines if the Price is negative.
	 * @return True if Price is negative. False if the Price
	 * is zero or positive.  If the price is a market Price,
	 * return false.
	 */
	public boolean isNegative()
	{
		if(this.isMarketPrice == true)
		{
			return false;
		}
		
		else
		{
			return(this.price < 0);
		}	
	}
	
	/**
	 * Provides the Price class with a user friendly string representation of an Price object
	 */
	public String toString()
	{
		if(this.isMarketPrice == true)
		{
			return "MKT";
		}
		return PriceFactory.longToCurrencyString(this.price);
	}

	
} //end Price


