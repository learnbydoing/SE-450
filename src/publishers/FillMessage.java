package publishers;

import price.Price;
import tradable.Tradable.Side;
import utils.InvalidArgumentException;
import utils.InvalidVolumeValueException;


/**
 * Contains data related to the fill of an order or quote-side.
 * 
 * It implements the Comparable interface.
 * 
 * @author Urvi
 *
 */

public class FillMessage implements Comparable<FillMessage>
{

	private Message message;
	
	/**
	 * Creates a FilllMessage object, given various information regarding the order that was filled. Namely,
	 * the user's name, the stock symbol, the price, volume, description of trade, and whether the stock was
	 * bought or sold.
	 * 
	 * @param uname The name of the User whose order or quote-side was filled.  Cannot be null or empty.
	 * @param sym The stock symbol of the filled order or quote-side.  Cannot be null or empty.
	 * @param prce The price the order or quote-side was filled.  Cannot be null.
	 * @param vlme The quantity of the order or quote-side that was filled.  Cannot be negative.
	 * @param dtls A description of the fill. Cannot be null.
	 * @param sde Denotes whether filled order or quote-side was a buy or sell.  Must be a valid value, cannot be null.
	 * @param id Unique identifier of the filled order or quote-side.  Cannot be null.
	 * @throws InvalidArgumentException if a null object is received or if a String is empty.
	 * @throws InvalidVolumeValueException if the volume is less than zero.
	 */
	
	public FillMessage(String uname, String sym, Price prce, int vlme, String dtls, Side sde, String id) throws InvalidArgumentException, InvalidVolumeValueException
	{
		message = MessageFactory.buildMessage(uname, sym, prce, vlme, dtls, sde, id);
		setUser(uname);
		setProduct(sym);
		setPrice(prce);
		setVolume(vlme);
		setDetails(dtls);
		setSide(sde);
		setId(id);
	}
	
	/**
	 * Returns the name of the user.
	 * @return The name of the user.
	 */
	
	public String getUser()
	{
		return message.getUser();
	}
		
	
	/**
	 * Returns the stock symbol for the filled order.
	 * @return The stock symbol for the filled order.
	 */
	
	public String getProduct()
	{
		return message.getProduct();
	}
	
	/**
	 * Returns the price of the stock that was filled.
	 * @return Price of the stock.
	 */
	
	public Price getPrice()
	{
		return message.getPrice();
	}	
	
	/**
	 * Returns the reason for the fill.
	 * @return A String that describes the reason for the fill.
	 */
	
	public int getVolume()
	{
		return message.getVolume();
	}
	
	/**
	 * Returns the description for the fill.
	 * @return A String that describes the reason for the cancellation.
	 */
	
	public String getDetails()
	{
		return message.getDetails();
	}
	
	/**
	 * Returns the side of the filled order or quote-side.
	 * @return The buy or sell side of the filled order or quote-side.
	 */
	
	public Side getSide()
	{
		return message.getSide();
	}
	
	/**
	 * Returns the id of the filled order.
	 * @return The unique id of the filled order.
	 */
	
	public String getId()
	{
		return message.getId();
	}
	
	/**
	 * Compares the prices in two FillMessage objects.
	 */
	
	public int compareTo(FillMessage fm)
	{
		return message.getPrice().compareTo(fm.getPrice());
	}
	
	/**
	 * Provides the FillMessage class with a user friendly string representation of a FilllMessage object.
	 */
	
	public String toString()
	{
		return "User: " + getUser() + ", Product: " + getProduct() + ", Price: " + getPrice() + ", Volume: " + getVolume() +
				", Details: " + getDetails() + ", Side: " + getSide();
	}
	
// Private sets()
	
	
	/**
	 * Sets the user name in the FillMessage to the value passed in.
	 * This is a private method so that the input can be validated.
	 * @param uname User name to specify in CancelMessage.
	 * @throws InvalidArgumentException if uname parameter is null or empty.  
	 */
	
	private void setUser(String uname) throws InvalidArgumentException
	{
		message.setUser(uname);
	}
	
	/**
	 * Sets the product name in the FillMessage to the value passed in.
	 * This is a private method so that the input can be validated.
	 * @param prod Product name to specify in the FillMessage.
	 * @throws InvalidArgumentException if prod parameter is null or empty.
	 */
	
	private void setProduct(String prod) throws InvalidArgumentException
	{
		message.setProduct(prod);
	}
	
	
	/**
	 * Sets the Price in the FillMessage to the value passed in.
	 * This is a private method so that the input can be validated.
	 * @param p Price to specify in the FillMessage.
	 * @throws InvalidArgumentException if value of the Price parameter passed in is null.
	 */
	
	private void setPrice(Price p) throws InvalidArgumentException
	{
		message.setPrice(p);
	}	
	
	/**
	 * Sets the volume in the FillMessage to the value passed in.
	 * This is a private method so that the input can be validated.
	 * @param v Volume to specify in the FillMessage
	 * @throws InvalidVolumeValueException volume passed in is negative.
	 */
	
	public void setVolume(int v) throws InvalidVolumeValueException
	{
		message.setVolume(v);
	}
	
	
	/**
	 * Sets the details in the FillMessage to the value passed in.
	 * This is a private method so that the input can be validated.
	 * @param reason Reason for cancellation to specify in the FillMessage.
	 * @throws InvalidArgumentException if reason parameter is null or empty.
	 */
	
	public void setDetails(String reason) throws InvalidArgumentException
	{
		message.setDetails(reason);
	}
	
	/**
	 * Sets the side (buy or sell) in the FillMessage to the value passed in.
	 * This is a private method so that the input can be validated.
	 * @param s Side to specify in the FillMessage
	 * @throws InvalidArgumentException if side is null.
	 */
	
	private void setSide(Side s) throws InvalidArgumentException
	{
		message.setSide(s);
	}
	
	/**
	 * 
	 * Sets the unique identifier in the FillMessage to the value passed in.
	 * This is a private method so that the input can be validated.
	 * @param id The id to specify in the FillMessage.
	 * @throws InvalidArgumentException if the id parameter is null or empty.
	 */
	
	private void setId(String id) throws InvalidArgumentException
	{
		message.setId(id);
	}
}














