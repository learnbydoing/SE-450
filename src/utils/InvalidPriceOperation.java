// Urvi Patel
// SE 450 - Phase 1 - InvalidPriceOperation.java
// Due date:  9/24/2012

package utils;

/**
 * This class represents an exception that is thrown when an attempt is made to 
 * perform an operation that is not defined for Price objects. Namely, addition
 * and subtraction wherein the operands are a limit price and market price, or
 * to multiply a market price.
 * 
 * @author Urvi
 *
 */
public class InvalidPriceOperation extends Exception
{
	
	public InvalidPriceOperation(String msg)
	{
		  super(msg);
	}
}
