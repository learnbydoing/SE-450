package publishers;

import price.Price;
import tradable.Tradable.Side;
import utils.InvalidArgumentException;
import utils.InvalidVolumeValueException;


/**
 * This is the delegate class that contains the implementations of the methods in the Message interface, namely gets and set of 
 * data members.
 * 
 * @author Urvi
 *
 */
public class MessageImpl implements Message 
{
	/**
	 * @see #getUser()
	 * @see #setUser(String)
	 */
	private String user;
	
	/**
	 * @see #getProduct()
	 * @see #setProduct(String)
	 */
	private String product;
	
	/**
	 * @see #getPrice()
	 * @see #setPrice(Price)
	 */
	private Price price;
	
	/**
	 * @see #getVolume()
	 * @see #setDetails(String)
	 */
	private int volume;
	
	/**
	 * @see #getDetails()
	 * @see #setDetails(String)
	 */
	private String details;
	
	/**
	 * @see #setSide(tradable.Tradable.Side)
	 * @see #getSide()
	 */
	private Side side;
	
	/**
	 * @see #getId()
	 * @see #setId(String)
	 */
	private String id;
	
	/**
	 * Public constructor for the Message helper class
	 * @param un User name associated with the Message.
	 * @param prod Symbol associated with the Message.
	 * @param prc Price associated with the Message.
	 * @param vol Quantity associated with the Message.
	 * @param dtails Text that describes the Message.
	 * @param sde Side associated with the Message.
	 * @param id Unique id associated with the Message.
	 * @throws InvalidArgumentException if parameters un, prod, id, or dtails are null or empty.  Or, if prc or side are null.
	 * @throws InvalidVolumeValueException if vol is negative
	 */
	
	public MessageImpl(String un, String prod, Price prc, int vol, String dtails, Side sde, String id) throws InvalidArgumentException, InvalidVolumeValueException 
	{
		setUser(un);
		setProduct(prod);
		setPrice(prc);
		setVolume(vol);
		setDetails(dtails);
		setSide(sde);
		setId(id);
	}
	

	/**
	 * Returns the name of the user who will be receiving a Message of MessageType. 
	 */
	
	public String getUser()
	{
		return user;
	}
	
    /**
	 * Returns the name of the product for which a Message of MessageType was submitted..
	 */
	
	public String getProduct()
	{
		return product;
	}
	
	 /**
     * Returns the Price of an order in a Message of MessageType.
     */
	
	public Price getPrice()
	{
		return price;
	}
	
	/**
	 * Returns the quantity of the order or quote-side in a Message of MessageType.
	 */
	public int getVolume()
	{
		return volume;
	}
	
	/**
	 * Returns a description of the Message of MessageType.
	 */
	
	public String getDetails()
	{
		return details;
	}
	
	/**
	 * Sets the side (Buy or Sell) of an order or quote-side in a Message of MessageType to the value passed in.
	 */
	
	public Side getSide()
	{
		return side;
	}
	
	
	/**
	 * Returns the identifier of the order or quote-side in a Message of MessageType.
	 */
	public String getId()
	{
		return id;
	}
	
	// Sets
	
    /**
     * Sets the name of the user who will be receiving a Message 
     * of MessageType to the value passed in.
     * 
     * @param u The name of the user to specify in a Message of MessageType. Cannot be null or empty.
     * @throws InvalidArgumentException if the parameter passed in is null or empty.
     */
	
	public void setUser(String u) throws InvalidArgumentException
	{
		if( u != null)
		{
			user = u;
		}
		else
		{
			throw new InvalidArgumentException("MessageImpl.setUser - User cannot be null");
		}
	}
	
    /**
     * Sets the name of the product for which a message of MessageType was 
     * submitted to the value passed in.
     * @param prod The name of the product for which a Message of MessageType was submitted.
     * @throws InvalidArgumentException if the parameter prod is null or empty.
     */
	
	public void setProduct(String prod) throws InvalidArgumentException
	{
		if(prod != null && prod.isEmpty() == false)
		{
			product = prod;
		}
		else
		{
			throw new InvalidArgumentException("MessageImpl.setProduct - Product cannot be null or empty");
		}
	}
	
    /**
     * Sets the Price of an order or quote-side in a Message of MessageType to the value passed in.
     * @param p The Price of an order in a Message of MessageType.
     * @throws InvalidArgumentException if value of Price parameter passed in is null
     */
    
	public void setPrice(Price p) throws InvalidArgumentException 
	{
		if(p != null)
		{
			price = p;
		}
		else
		{
			throw new InvalidArgumentException("MessageImpl - Price cannot be null");
		}
	}
	
    /**
     * Sets the quantity of the order or quote-side in a Message of MessageType to the value passed in.
     * @param v Volume of the order or quote-side in a Message of MessageType. Cannot be negative.
     * @throws InvalidVolumeValueException if parameter v is negative.
     */
	
	public void setVolume(int v) throws InvalidVolumeValueException
	{
		if(v >= 0)
		{
			volume = v;
		}
		else 
		{
			throw new InvalidVolumeValueException("MessageImpl.setVolume - Volume has an invalid volume:" + v);
		}
	}
	
	   /**
	    * Sets description of order or quote-side in a Message of MessageType to the value passed in.
	    * @param d Text description of Message of MessageType.  Cannot be null or empty.
	    * @throws InvalidArgumentException if d parameter is null or empty.
	    */
	
	public void setDetails(String d) throws InvalidArgumentException
	{
		if(d != null && d.isEmpty() == false)
		{
			details = d;
		}
		else
		{
			throw new InvalidArgumentException("MessageImpl.setDetails - Details cannot be null or empty");
		}
	}
	
	 /**
     * Sets the side (Buy or Sell) of an order or quote-side in a Message of MessageType to the value passed in.
     * @param s The side of order or quote-side in a Message of MessageType. Cannot be null.
     * @throws InvalidArgumentException if parameter passed in is null.
     */
	
	public void setSide(Side s) throws InvalidArgumentException
	{
		if(s != null)
		{
			side = s;
		}
		else
		{
			throw new InvalidArgumentException("MessagImple.setSide - Side cannot be null");
		}
	}
	
    /**
     * Sets the identifier of the order or quote-side in a Message of MessageType to the value passed in.
     * @param ID Identifier of the order or quote-side in a Message of MessageType.  Cannot be null or empty.
     * @throws InvalidArgumentException if id parameter passed in is null or empty.
     */
	
	public void setId(String ID) throws InvalidArgumentException
	{
		if(ID != null && ID.isEmpty() == false)
		{
			id = ID;
		}
		else 
		{
			throw new InvalidArgumentException("MessageImpl.setId - ID cannot be null");
		}
	}
}
