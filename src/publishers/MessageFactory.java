package publishers;

import price.Price;
import tradable.Tradable.Side;
import utils.InvalidArgumentException;
import utils.InvalidVolumeValueException;


/**
 * This class creates an object of the helper class that implements the methods of the Message Interface.
 * @author Urvi
 *
 */
public class MessageFactory 
{	
	/**
	 * Private constructor since all methods are static
	 */
	private MessageFactory() {}
	
	/**
	 * Creates an appropriate MessageImpl object
	 * 
	 * @param user User name associated with Message to be created.
	 * @param sym  Stock symbol associated with Message to be created.
	 * @param prce Price object associated with Message to be created.
	 * @param vlme Volume associated with Message to be created.
	 * @param dtls Text description associated with Message to be created.
	 * @param sde Side associated with Message to be created.
	 * @param id Unique identifier associated with Message to be created.
	 * @return An object of the helper class (delegate) MessageImpl.
	 * @throws InvalidArgumentException if parameters user, sym, dtails or id are null or empty.  Or, if parameters prc or sde are null.
	 * @throws InvalidVolumeValueException
	 */
	public static Message buildMessage(String user, String sym, Price prce, int vlme, String dtls, Side sde, String id) throws InvalidArgumentException, InvalidVolumeValueException
	{
		return new MessageImpl(user, sym, prce, vlme, dtls, sde, id);
	}
}
