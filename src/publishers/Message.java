package publishers;

import price.Price;
import tradable.Tradable.Side;
import utils.InvalidArgumentException;
import utils.InvalidVolumeValueException;

/**
 * This interface includes methods common to FillMessage and CancelMessage.  The methods
 * correspond to gets and sets of data members.
 * 
 * @author Urvi
 *
 */

public interface Message 
{

	/**
	 * Describes the type of message, valid values are Cancel and Fill.
	 * 
	 * @author Urvi
	 *
	 */
	enum MessageType { CANCEL, FILL };

	/**
	 * Returns the name of the user who will be receiving a Message of MessageType.
	 * @return The name of the user who will be receiving a Message of MessageType.
	 */
    String getUser();
    
    /**
     * Sets the name of the user who will be receiving a Message 
     * of MessageType to the value passed in.
     * 
     * @param uname The name of the user to specify in a Message of MessageType. Cannot be null or empty.
     * @throws InvalidArgumentException if the parameter uname is null or empty.
     */
    void setUser(String uname) throws InvalidArgumentException;

    /**
	 * Returns the name of the product for which a Message of MessageType was submitted.
	 * @return The stock symbol of the product.
	 */
    
    String getProduct();
    
    /**
     * Sets the name of the product for which a message of MessageType was 
     * submitted to the value passed in.
     * @param prod The name of the product for which a Message of MessageType was submitted.
     * @throws InvalidArgumentException if the parameter prod is null or empty.
     */
    
    void setProduct(String prod)throws InvalidArgumentException;
    
    /**
     * Returns the Price of an order in a Message of MessageType.
     * @return The Price of an order in a Message of MessageType.
     */
    
    Price getPrice();
    
    /**
     * Sets the Price of an order or quote-side in a Message of MessageType to the value passed in.
     * @param p The Price of an order in a Message of MessageType.
     * @throws InvalidArgumentException if value of Price parameter passed in is null
     */
    
    void setPrice(Price p) throws InvalidArgumentException;
    
    /**
     * Returns the quantity of the order or quote-side in a Message of MessageType.
     * @return Volume of the order in a Message of MessageType.
     */

    int getVolume();
    
    /**
     * Sets the quantity of the order or quote-side in a Message of MessageType to the value passed in.
     * @param vol Volume of the order or quote-side in a Message of MessageType. Cannot be negative.
     * @throws InvalidVolumeValueException if parameter vol is negative.
     */
    
    void setVolume(int vol) throws InvalidVolumeValueException;

    /**
     * Returns a description of the Message of MessageType.
     * @return String giving details in a Message of MessageType.
     */
    
    String getDetails();
    
   /**
    * Sets description of order or quote-side in a Message of MessageType to the value passed in.
    * @param dtails Text description of Message of MessageType.  Cannot be null or empty.
    * @throws InvalidArgumentException if dtails parameter is null or empty.
    */
    
    void setDetails(String dtails) throws InvalidArgumentException;

    /**
     * Returns the side (Buy or Sell) of an order or quote-side in a Message of MessageType.
     * @return The side of order or quote-side in a Message of MessageType.
     */
    Side getSide();
    
    /**
     * Sets the side (Buy or Sell) of an order or quote-side in a Message of MessageType to the value passed in.
     * @param s The side of order or quote-side in a Message of MessageType. Cannot be null.
     * @throws InvalidArgumentException if parameter passed in is null.
     */
    void setSide(Side s) throws InvalidArgumentException;

    /**
     * Returns the identifier of the order or quote-side in a Message of MessageType.
     * @return Identifier of the order or quote-side in a Message of MessageType.
     */
    String getId();
    
    /**
     * Sets the identifier of the order or quote-side in a Message of MessageType to the value passed in.
     * @param id Identifier of the order or quote-side in a Message of MessageType.  Cannot be null or empty.
     * @throws InvalidArgumentException if id parameter passed in is null or empty.
     */
    
    void setId(String id) throws InvalidArgumentException;
}
