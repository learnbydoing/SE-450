package client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import price.Price;
import publishers.CurrentMarketPublisher;
import publishers.LastSalePublisher;
import publishers.MessagePublisher;
import publishers.TickerPublisher;
import tradable.Order;
import tradable.Quote;
import tradable.Tradable.Side;
import utils.AlreadyConnectedException;
import utils.AlreadySubscribedException;
import utils.DataValidationException;
import utils.InvalidArgumentException;
import utils.InvalidConnectionIdException;
import utils.InvalidMarketStateException;
import utils.InvalidVolumeValueException;
import utils.NoSuchProductException;
import utils.NotSubscribedException;
import utils.OrderNotFoundException;
import utils.UserNotConnectedException;
import book.ProductService;
import dto.TradableDTO;

/**
 * Class that acts as a facade between a user and the trading system.
 * @author Urvi
 *
 */
public final class UserCommandService 
{
	/**
	 * Holds user names and connection ids
	 */
	private static HashMap<String, Long> connIds = new HashMap<String, Long>();
	
	/**
	 * Holds user name and User object pairs
	 */
	private static HashMap<String, User> connUsers = new HashMap<String, User>();
	
	/**
	 * Holds username and connection-time pairs
	 */
	private static HashMap<String, Long> connTime = new HashMap<String, Long>();
	
	/**
	 * Holds the instance of the UserCommandService class
	 */
	private volatile static UserCommandService instance;


	/**
	 * Creates the UserCommandService object if it hasn't been created 
	 * @return A UserCommandService object
	 */
	public static UserCommandService getInstance()
	{
		
		if(instance == null)
		{
			synchronized(UserCommandService.class)
			{
				if(instance == null)
					{
						return new UserCommandService();
					}
			}
		}
		return instance;
	}

	/**
	 * Private constructor since only instance of UserCommandService is allowed
	 */
	private UserCommandService() {}

	/**
	 * Method that verifies a valid username and connection id
	 * @param userName Name of the User that needs to be verified 
	 * @param connId Connection id of the User that needs to be verified
	 * @throws UserNotConnectedException if the User is not found by the trading system
	 * @throws InvalidConnectionIdException  if the connection id is not found by the trading system
	 * @throws InvalidArgumentException if username is null or empty
	 * 
	 */
	private void verifyUser(String userName, long connId) throws UserNotConnectedException, InvalidConnectionIdException, InvalidArgumentException
	{
		if(userName == null || userName.isEmpty())
		{
			throw new InvalidArgumentException("UserCommandService.verifyUser - username cannot be null or empty");
		}
		if(connIds.containsKey(userName) == false)
		{
			throw new UserNotConnectedException("UserCommandService.verifyUser - user not connected");
		}

		if(connId != connIds.get(userName))
		{
			throw new InvalidConnectionIdException("UserCommandService.verifyUser - connection id not found");
		}
	}


	/**
	 * Connects user to the trading system
	 * @param user The User to be connected
	 * @return A connection id once connection is established
	 * @throws AlreadyConnectedException if User has connected previously
	 * @throws InvalidArgumentException if User is null
	 */
	public synchronized long connect(User user) throws AlreadyConnectedException, InvalidArgumentException
	{
		if(user == null)
		{
			throw new InvalidArgumentException("UserCommandService.connect - User cannot be null");
		}
		if(connIds.containsKey(user))
		{
			throw new AlreadyConnectedException("UserCommandService.connect - user already connected");
		}

		connIds.put(user.getUserName(), System.nanoTime());
		connUsers.put(user.getUserName(), user);
		connTime.put(user.getUserName(), System.currentTimeMillis());
		return connIds.get(user.getUserName());
	}
	
	/**
	 * Disconnects User from the trading system
	 * @param username The name of the User to be disconnected
	 * @param connId Connection id of the User that needs to be verified
	 * @throws UserNotConnectedException if the User never connected
	 * @throws InvalidConnectionIdException if the connection id is not found by the trading system
	 * @throws InvalidArgumentException 
	 */
	public synchronized void disConnect(String username, long connId) throws UserNotConnectedException, InvalidConnectionIdException, InvalidArgumentException
	{
		verifyUser(username, connId);
		connIds.remove(username);
		connUsers.remove(username);
		connTime.remove(username);
	}

	/**
	 * After verifying the user, this method calls ProductService's getBookDepth() method
	 * @param username Name of the user to verify
	 * @param connId Connection id of the User that needs to be verified
	 * @param product Stock symbol for which depth is requested
	 * @return Result of call to ProductService.getBookDepth()
	 * @throws UserNotConnectedException 
	 * @throws InvalidConnectionIdException
	 * @throws NoSuchProductException
	 * @throws InvalidArgumentException
	 * @see #verifyUser(String, long)
	 * @see ProductService#getBookDepth(String)
	 */
	public String[][] getBookDepth(String username, long connId, String product) throws UserNotConnectedException, InvalidConnectionIdException, NoSuchProductException, InvalidArgumentException
	{
		verifyUser(username, connId);
		String[][] depth = (ProductService.getInstance().getBookDepth(product));
		return depth;
	}

	/** After verifying the user, this method calls ProductService's getMarketState() method
	 * 
	 * @param username Name of the user to verify
	 * @param connId Connection id of the User that needs to be verified
	 * @return A string representation of the MarketState {PREOPEN, OPEN, CLOSED}
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @throws InvalidArgumentException
	 * @see #verifyUser(String, long)
	 * @see ProductService#getMarketState()
	 */
	public String getMarketState(String username, long connId) throws UserNotConnectedException, InvalidConnectionIdException, InvalidArgumentException
	{
		verifyUser(username, connId);
		String state = (ProductService.getInstance().getMarketState().toString());
		return state;
	}

	/**
	 * After verifying the user, this method calls ProductService's getOrderWithRemainingQty(String, String) method
	 * @param username The name of the User to verify
	 * @param connId Connection id of the User that needs to be verified
	 * @param product The stock symbol for which remaining quantity is requested
	 * @return A list of TradableDTOs objects for the orders that have a remaining quantity
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @throws InvalidArgumentException
	 * @see #verifyUser(String, long)
	 * @see ProductService#getOrdersWithRemainingQty(String, String)
	 */
	public synchronized ArrayList<TradableDTO> getOrdersWithRemainingQty(String username, long connId, String product) throws UserNotConnectedException, InvalidConnectionIdException, InvalidArgumentException
	{
		ArrayList<TradableDTO> orders = new ArrayList<TradableDTO>();
		verifyUser(username, connId);
		orders = ProductService.getInstance().getOrdersWithRemainingQty(username, product);
		return orders;
	}
	
	/**
	 * After verifying the user, this method calls ProductService's getProductList() method
	 * @param username The name of the User to verify
	 * @param connId Connection id of the User that needs to be verified
	 * @return A list containing the stocks available in the system
	 * @throws InvalidArgumentException
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @see  #verifyUser(String, long)
	 * @see ProductService#getProductList()
	 */

	public synchronized ArrayList<String> getProducts(String username, long connId) throws InvalidArgumentException, UserNotConnectedException, InvalidConnectionIdException
	{
		verifyUser(username, connId);
		ArrayList<String> prods = ProductService.getInstance().getProductList();
		Collections.sort(prods);
		return prods;
	}

	/**
	 * After verifying the user, this method calls ProductService's submitOrder(Order) method
	 * @param username The name of the User to verify
	 * @param connId Connection id of the User that needs to be verified
	 * @param product The stock symbol for which to submit an order
	 * @param price The Price at which to place the order
	 * @param volume The quantity that user wishes to trade
	 * @param side The side of the trade (BUY or SELL)
	 * @return Order id of the submitted order
	 * @throws InvalidMarketStateException
	 * @throws NoSuchProductException
	 * @throws InvalidArgumentException
	 * @throws InvalidVolumeValueException
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @see #verifyUser(String, long)
	 * @see ProductService#submitOrder(Order)
	 * 
	 */
	public String submitOrder(String username, long connId, String product, Price price, int volume, Side side) 
			throws InvalidMarketStateException, NoSuchProductException, InvalidArgumentException, InvalidVolumeValueException, 
			UserNotConnectedException, InvalidConnectionIdException
	{
		verifyUser(username, connId);
		Order order = new Order(username, product, price, volume, side);
		String id = ProductService.getInstance().submitOrder(order);
		return id;
	}

	/**
	 * After verifying the user, this method calls ProductService's submitOrderCancel(String, Side, String) method
	 * @param username The name of the User to verify
	 * @param connId Connection id of the User that needs to be verified
	 * @param product The stock symbol for the order that user wishes to cancel
	 * @param side The side of the book represented by the order (BUY or SELL)
	 * @param orderId The id of the order to be cancelled
	 * @throws InvalidMarketStateException
	 * @throws NoSuchProductException
	 * @throws InvalidArgumentException
	 * @throws InvalidVolumeValueException
	 * @throws OrderNotFoundException
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @see ProductService#submitOrderCancel(String, book.ProductBookSide.BookSide, String)
	 * 
	 */
	public void submitOrderCancel(String username, long connId, String product, Side side, String orderId) throws InvalidMarketStateException, NoSuchProductException, InvalidArgumentException, InvalidVolumeValueException, OrderNotFoundException, UserNotConnectedException, InvalidConnectionIdException
	{
		verifyUser(username, connId);
		ProductService.getInstance().submitOrderCancel(product, side, orderId);
	}

	/**
	 * After verifying the user, this method calls ProductService's submitQuote(Quote) method
	 * @param username The name of the User to verify
	 * @param connId Connection id of the User that needs to be verified
	 * @param product The stock symbol of the Quote to be submitted
	 * @param bPrice The Price for the BUY side of the Quote
	 * @param bVolume The volume for the BUY side of the Quote
	 * @param sPrice The Price for the SELL side of the Quote
	 * @param sVolume The volume for the SELL side of the Quote
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @throws InvalidVolumeValueException
	 * @throws InvalidArgumentException
	 * @throws InvalidMarketStateException
	 * @throws NoSuchProductException
	 * @throws DataValidationException
	 * @see #verifyUser(String, long)
	 * @see ProductService#submitQuote(Quote)
	 */
	public void submitQuote(String username, long connId, String product, Price bPrice, int bVolume, Price sPrice, int sVolume) throws UserNotConnectedException, InvalidConnectionIdException, InvalidVolumeValueException, InvalidArgumentException, InvalidMarketStateException, NoSuchProductException, DataValidationException
	{
		verifyUser(username, connId);
		Quote quote = new Quote(username, product, bPrice, bVolume, sPrice, sVolume);
		ProductService.getInstance().submitQuote(quote);	
	}
	
	/**
	 * After verifying the user, this method calls ProductService's submitQuoteCancel(String, String) method
	 * @param username The name of the User to verify
	 * @param connId Connection id of the User that needs to be verified
	 * @param product The stock symbol of the Quote to be cancelled
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @throws InvalidMarketStateException
	 * @throws NoSuchProductException
	 * @throws InvalidArgumentException
	 * @throws InvalidVolumeValueException
	 * @see #verifyUser(String, long)
	 * @see ProductService#submitQuoteCancel(String, String)
	 */

	public void submitQuoteCancel(String username, long connId, String product) throws UserNotConnectedException, InvalidConnectionIdException, InvalidMarketStateException, NoSuchProductException, InvalidArgumentException, InvalidVolumeValueException
	{
		verifyUser(username, connId);
		ProductService.getInstance().submitQuoteCancel(username, product);	
	}

	/**
	 * After verifying the user, this method calls CurrentMarketPublisher's subscribe method
	 * @param username The name of the User to verify
	 * @param connId Connection id of the User that needs to be verified 
	 * @param product The stock symbol to subscribe for CurrentMarketPublisher updates
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @throws AlreadySubscribedException
	 * @throws InvalidArgumentException if username is null or empty
	 * @see #verifyUser(String, long)
	 * @see CurrentMarketPublisher#subscribe(User, String)
	 */
	public void subscribeCurrentMarket(String username, long connId, String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidArgumentException
	{
		if(username == null || username.isEmpty())
		{
			throw new InvalidArgumentException("UserCommandService.subscribeCurrentMarket - username cannot be null or empty");
		}
		verifyUser(username, connId);
		CurrentMarketPublisher.getInstance().subscribe(connUsers.get(username), product);
	}
	
	/**
	 * After verifying the user, this method calls LastSalePublisher's subscribe method
	 * @param username username The name of the User to verify
	 * @param connId Connection id of the User that needs to be verified
	 * @param product The stock symbol to subscribe for LastSalePublisher updates
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @throws AlreadySubscribedException
	 * @throws InvalidArgumentException if username is null or empty
	 * @see #verifyUser(String, long)
	 * @see LastSalePublisher#subscribe(User, String)
	 */

	public void subscribeLastSale(String username, long connId, String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidArgumentException
	{
		if(username == null || username.isEmpty())
		{
			throw new InvalidArgumentException("UserCommandService.subscribeLastSale - username cannot be null or empty");
		}
		verifyUser(username, connId);
		LastSalePublisher.getInstance().subscribe(connUsers.get(username), product);
	}

	/**
	 * After verifying the user, this method calls MessagePublisher's subscribe method
	 * @param username username The name of the User to verify
	 * @param connId Connection id of the User that needs to be verified
	 * @param product The stock symbol to subscribe for MessagePublisher updates
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @throws AlreadySubscribedException
	 * @throws InvalidArgumentException if username is null or empty
	 * @see #verifyUser(String, long)
	 * @see MessagePublisher#subscribe(User, String)
	 */
	public void subscribeMessages(String username, long connId, String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidArgumentException
	{
		if(username == null || username.isEmpty())
		{
			throw new InvalidArgumentException("UserCommandService.subscribeMessages - username cannot be null or empty");
		}
		verifyUser(username, connId);
		MessagePublisher.getInstance().subscribe(connUsers.get(username), product);
	}

	/**
	 * After verifying the user, this method calls TickerPublisher's subscribe method
	 * @param username username The name of the User to verify
	 * @param connId Connection id of the User that needs to be verified
	 * @param product The stock symbol to subscribe for TickerPublisher updates
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @throws AlreadySubscribedException
	 * @throws InvalidArgumentException if username is null or empty
	 * @see #verifyUser(String, long)
	 * @see TickerPublisher#subscribe(User, String)
	 */
	public void subscribeTicker(String username, long connId, String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidArgumentException
	{
		if(username == null || username.isEmpty())
		{
			throw new InvalidArgumentException("UserCommandService.subscribeTicker - username cannot be null or empty");
		}
		verifyUser(username, connId);
		TickerPublisher.getInstance().subscribe(connUsers.get(username), product);
	}
	
	/**
	 * After verifying the user, this method calls CurrentMarketPublisher's unSubscribe method
	 * @param username username The name of the User to verify
	 * @param connId Connection id of the User that needs to be verified
	 * @param product The stock symbol to unsubscribe from CurrentMarketPublisher updates
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @throws NotSubscribedException
	 * @throws InvalidArgumentException if username is null or empty
	 * @see #verifyUser(String, long)
	 * @see CurrentMarketPublisher#unSubscribe(User, String)
	 */
	public void unSubscribeCurrentMarket(String username, long connId, String product) throws UserNotConnectedException, InvalidConnectionIdException, NotSubscribedException, InvalidArgumentException
	{
		if(username == null || username.isEmpty())
		{
			throw new InvalidArgumentException("UserCommandService.unSubscribeCurrentMarket - username cannot be null or empty");
		}
		verifyUser(username, connId);
		CurrentMarketPublisher.getInstance().unSubscribe(connUsers.get(username), product);
	}
	

	/**
	 * After verifying the user, this method calls LastSalePublisher's unSubscribe method
	 * @param username username The name of the User to verify
	 * @param connId Connection id of the User that needs to be verified
	 * @param product The stock symbol to unsubscribe from LastSalePublisher updates
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @throws NotSubscribedException
	 * @throws InvalidArgumentException if username is null or empty
	 * @see #verifyUser(String, long)
	 * @see LastSalePublisher#unSubscribe(User, String)
	 */
	public void unSubscribeLastSale(String username, long connId, String product) throws UserNotConnectedException, InvalidConnectionIdException, NotSubscribedException, InvalidArgumentException
	{
		if(username == null || username.isEmpty())
		{
			throw new InvalidArgumentException("UserCommandService.unSubscribeLastSale - username cannot be null or empty");
		}
		verifyUser(username, connId);
		LastSalePublisher.getInstance().unSubscribe(connUsers.get(username), product);
	}

	/**
	 * After verifying the user, this method calls MessagePublisher's unSubscribe method
	 * @param username username The name of the User to verify
	 * @param connId Connection id of the User that needs to be verified
	 * @param product The stock symbol to unsubscribe from MessagePublisher updates
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @throws NotSubscribedException
	 * @throws InvalidArgumentException if username is null or empty
	 * @see #verifyUser(String, long)
	 * @see MessagePublisher#unSubscribe(User, String)
	 */
	
	public void unSubscribeMessages(String username, long connId, String product) throws UserNotConnectedException, InvalidConnectionIdException, NotSubscribedException, InvalidArgumentException
	{
		if(username == null || username.isEmpty())
		{
			throw new InvalidArgumentException("UserCommandService.unSubscribeMessages - username cannot be null or empty");
		}
		verifyUser(username, connId);
		MessagePublisher.getInstance().unSubscribe(connUsers.get(username), product);
	}

	/**
	 * After verifying the user, this method calls TickerPublisher's unSubscribe method
	 * @param username username The name of the User to verify
	 * @param connId Connection id of the User that needs to be verified
	 * @param product The stock symbol to unsubscribe from TickerPublisher updates
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @throws NotSubscribedException
	 * @throws InvalidArgumentException if username is null or empty
	 * @see #verifyUser(String, long)
	 * @see TickerPublisher#unSubscribe(User, String)
	 */
	public void unSubscribeTicker(String username, long connId, String product) throws UserNotConnectedException, InvalidConnectionIdException, NotSubscribedException, InvalidArgumentException
	{
		if(username == null || username.isEmpty())
		{
			throw new InvalidArgumentException("UserCommandService.unSubscribeTicker - username cannot be null or empty");
		}
		verifyUser(username, connId);
		TickerPublisher.getInstance().unSubscribe(connUsers.get(username), product);
	}

}
