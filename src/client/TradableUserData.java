package client;


import utils.InvalidArgumentException;
import tradable.Tradable.Side;

/**
 * This class will hold selected data elements related to the Tradables a user has submitted to the system.
 * @author Urvi
 *
 */
public class TradableUserData 
{
	/**
	 * The name of the user who submitted the Tradable
	 */
	private String username;
	
	/**
	 * The stock symbol of the Tradable
	 */
	private String stockSymbol;
	
	/**
	 * The side of the Tradable (BUY or SELL)
	 */
	private Side side;

	/**
	 * The order id of the Tradable
	 */
	private String orderId;

	/**
	 * Creates a TradableUserData object
	 * @param name Name of the user who submitted the Tradable
	 * @param product The stock symbol of the Tradable that was traded
	 * @param s The side of the trade (BUY or SELL)
	 * @param orderID The id of the order that was traded
	 * @throws InvalidArgumentException if name, product, side or orderID are null or empty
	 */
	public TradableUserData(String name, String product, Side s, String orderID) throws InvalidArgumentException
	{
		setUserName(name);
		setStockSymbol(product);
		setSide(s);
		setOrderId(orderID);
	}

	/**
	 * Returns name of the user who submitted the Tradable 
	 * @return  The name of the user who submitted the Tradable
	 */
	public String getUserName()
	{
		return username;
	}	


	/**
	 * Returns The stock symbol of the Tradable that was traded
	 * @return The String stock symbol of the Tradable that was traded
	 */
	public String getStockSymbol()
	{
		return stockSymbol;
	}
	
	/**
	 * Returns The side of the trade (BUY or SELL)
	 * @return The Side {BUY or SELL} of the trade 
	 */

	public Side getSide()
	{
		return side;
	}

	/**
	 * Returns the id of the order that was traded
	 * @return The String id of the order that was traded
	 */
	public String getOrderId()
	{
		return orderId;
	}

	/**
	 * Sets the name of the user for the Tradable to the parameter passed in
	 * @param uname The name to set the username to
	 * @throws InvalidArgumentException if uname is null or empty
	 */
	private void setUserName(String uname) throws InvalidArgumentException
	{

		if(uname == null || uname.isEmpty() == true)
		{
			throw new InvalidArgumentException("TradableUserData.setUserName - username cannot be null or empty");
		}
		username = uname;
	}

	/**
	 * Sets the stock symbol for the Tradable to the parameter passed in
	 * @param sym The value to set the stock symbol to
	 * @throws InvalidArgumentException if sym is null
	 */
	private void setStockSymbol(String sym) throws InvalidArgumentException
	{

		if(sym == null || sym.isEmpty() == true)
		{
			throw new InvalidArgumentException("TradableUserData.setStockSymbol - Stock symbol cannot be null or empty");
		}
		stockSymbol = sym;
	}

	/**
	 * Sets the side of the Tradable to the parameter passed in
	 * @param s The value to set the side of the Tradable to: {BUY, SELLL}
	 * @throws InvalidArgumentException if sde is null
	 */
	private void setSide(Side s) throws InvalidArgumentException
	{

		if(s == null)
		{
			throw new InvalidArgumentException("TradableUserData.setSide - side cannot be null");
		}
		side = s;
	}

	/**
	 * Sets the order id for the Tradable to the parameter that is passsed in
	 * @param id The value to set the order id to
	 * @throws InvalidArgumentException if id is null or empty
	 */
	private void setOrderId(String id) throws InvalidArgumentException
	{

		if(id == null || id.isEmpty() == true)
		{
			throw new InvalidArgumentException("TradableuserData.setOrderId - id cannot be null or empty");
		}
		orderId = id;
	}
	
	/**
	 *  Provides the TradableUserData class with a user friendly string representation of a TradableUserData object.
	 */
	public String toString()
	{
		return "User " + username + ", " + side + " " + stockSymbol + "(" + orderId + ")"; 
	}
}
