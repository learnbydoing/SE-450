package client;

import gui.UserDisplayManager;

import java.sql.Timestamp;
import java.util.ArrayList;

import price.Price;
import publishers.CancelMessage;
import publishers.FillMessage;
import utils.AlreadyConnectedException;
import utils.AlreadySubscribedException;
import utils.DataValidationException;
import utils.InvalidArgumentException;
import utils.InvalidConnectionIdException;
import utils.InvalidMarketStateException;
import utils.InvalidPriceOperation;
import utils.InvalidVolumeValueException;
import utils.NoSuchProductException;
import utils.OrderNotFoundException;
import utils.UserNotConnectedException;

import dto.TradableDTO;
import tradable.Tradable.Side;

/**
 * Implementation of the User interface, representing a real user in the trading system
 * @author Urvi
 *
 */
public class UserImpl implements User
{
	/**
	 * The name of the user using the trading system
	 */
	private String username;
	
	/**
	 * The id the User is given when connecting to the trading system
	 */
	private long connectionId;
	
	/**
	 * List of the stocks available in the trading system
	 */
	private ArrayList<String> stocks = new ArrayList<String>();
	
	/**
	 * Contains information on the orders a User has submitted
	 */
	private ArrayList<TradableUserData> orderData = new ArrayList<TradableUserData>();
	
	/**
	 * Holds values of users' stock, costs, etc
	 */
	private Position position;
	
	/**
	 * Acts as facade between user and market display
	 */
	private UserDisplayManager displayManager;

	/**
	 * Public constructor that creates a UserImpl object and calls the Position ctr to create Position object
	 * @param name The User's name
	 * @throws InvalidArgumentException
	 * @see #setUserName(String)
	 */
	public UserImpl(String name) throws InvalidArgumentException
	{
		setUserName(name);
		position = new Position();	
	}


	/**
	 * Sets the username to the uname parameter passed in
	 * @param uname The name of the user
	 * @throws InvalidArgumentException if uname is null or empty
	 */
	private void setUserName(String uname) throws InvalidArgumentException
	{
		if(uname == null || uname.isEmpty() == true)
		{
			throw new InvalidArgumentException("TradableuserData.setUserName - username cannot be null or empty");
		}
		username = uname;
	} 

	/**
	 * Returns the user name
	 */
	public String getUserName()
	{
		return username;
	}	

	/**
	 * Returns the id the user was given upon connecting to the trading system
	 * @return  Id the user was given upon connecting to the trading system
	 */
	public long getConnectionID()
	{
		return connectionId;
	}

	/**
	 * @throws InvalidArgumentException 
	 * @see UserDisplayManager#updateLastSale(String, Price, int)
	 * @see Position#updateLastSale(String, Price)
	 */
	public void acceptLastSale(String product, Price price, int volume) throws InvalidArgumentException
	{
			displayManager.updateLastSale(product, price, volume);
			position.updateLastSale(product, price);
	}

	/**
	 * @see UserDisplayManager#updateMarketActivity(String)
	 * @see Position#updatePosition(String, Price, tradable.Tradable.Side, int)
	 */
	public void acceptMessage(FillMessage fm) 
	{
		try
		{
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			String summary = "{" + ts + "} FillMessage: " + fm.getSide() + " " + fm.getVolume() + " " + fm.getProduct() + " at " + fm.getPrice().toString() + " " + fm.getDetails() + "[TradableId: " + fm.getId() + "]";
			displayManager.updateMarketActivity(summary);
			position.updatePosition(fm.getProduct(), fm.getPrice(), fm.getSide(), fm.getVolume());
		}
		catch(InvalidPriceOperation e)
		{
			System.out.println(e.getMessage());
		}
		catch(InvalidArgumentException e)
		{
			System.out.println(e.getMessage());
		}
	}

	/**
	 * @see UserDisplayManager#updateMarketActivity(String)
	 */
	public void acceptMessage(CancelMessage cm) 
	{
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		String summary = "{" + ts + "} CancelMessage: " + cm.getSide() + " " + cm.getVolume() + " " + cm.getProduct() + " at " + cm.getPrice().toString() + " " + cm.getDetails() + " [TradableId: " + cm.getId() + "]";
		displayManager.updateMarketActivity(summary);
	}

	/**
	 * @see UserDisplayManager#updateMarketState(String)
	 */
	public void acceptMarketMessage(String message)
	{
		displayManager.updateMarketState(message);
	}

	/**
	 * @see UserDisplayManager#updateTicker(String, Price, char)
	 */
	public void acceptTicker(String product, Price p, char dir)
	{
			displayManager.updateTicker(product, p, dir);
	}

	/**
	 * @see UserDisplayManager#updateMarketData(String, Price, int, Price, int)
	 */
	public void acceptCurrentMarket(String product, Price bPrice, int bVolume, Price sPrice, int sVolume)
	{	
			displayManager.updateMarketData(product, bPrice, bVolume, sPrice, sVolume);
	}

	/**
	 * @see UserCommandService#connect(User)
	 * @see UserCommandService#getProducts(String, long)
	 */
	public void connect() throws InvalidArgumentException, UserNotConnectedException, InvalidConnectionIdException, AlreadyConnectedException 
	{	
		connectionId = UserCommandService.getInstance().connect(this);
		stocks = UserCommandService.getInstance().getProducts(getUserName(), getConnectionID());
	}

	/**
	 * @throws InvalidArgumentException 
	 * @see UserCommandService#disConnect(String, long)
	 */
	public void disConnect() throws UserNotConnectedException, InvalidConnectionIdException, InvalidArgumentException
	{
			UserCommandService.getInstance().disConnect(getUserName(), getConnectionID());
	}

	/**
	 * @see UserDisplayManager#showMarketDisplay()
	 */
	public void showMarketDisplay() throws UserNotConnectedException
	{
		if(stocks == null)
		{
			throw new UserNotConnectedException("UserImpl.showMarketDisplay - list of stocks cannot be null");
		}

		if(displayManager == null)
		{
			displayManager = new UserDisplayManager(this);
		}
		
		displayManager.showMarketDisplay();
	}

	/** 
	 * @see UserCommandService#submitOrder(String, long, String, price.Price, int, tradable.Tradable.Side)
	 * @see TradableUserData#TradableUserData(String, String, tradable.Tradable.Side, String)
	 */
	public String submitOrder(String product, Price p, int volume, Side side) throws InvalidMarketStateException, NoSuchProductException, InvalidArgumentException, InvalidVolumeValueException, UserNotConnectedException, InvalidConnectionIdException
	{
			String id = UserCommandService.getInstance().submitOrder(getUserName(), getConnectionID(), product, p, volume, side);
			TradableUserData trdUserData = new TradableUserData(getUserName(), product, side, id);
			orderData.add(trdUserData);
			return id;
	}

	/**
	 * @see UserCommandService#submitOrderCancel(String, long, String, book.ProductBookSide.BookSide, String)
	 */
	public void submitOrderCancel(String product, Side side, String orderId) throws InvalidMarketStateException, NoSuchProductException, InvalidArgumentException, InvalidVolumeValueException, OrderNotFoundException, UserNotConnectedException, InvalidConnectionIdException
	{
			UserCommandService.getInstance().submitOrderCancel(getUserName(), getConnectionID(), product, side, orderId);
	}

	/**
	 * @see UserCommandService#submitQuote(String, long, String, Price, int, Price, int)
	 */
	public void submitQuote(String product, Price bPrice, int bVolume, Price sPrice, int sVolume) throws UserNotConnectedException, InvalidConnectionIdException, InvalidVolumeValueException, InvalidArgumentException, InvalidMarketStateException, NoSuchProductException, DataValidationException
	{
			UserCommandService.getInstance().submitQuote(getUserName(), getConnectionID(), product, bPrice, bVolume, sPrice, sVolume);
	}

	/**
	 * @see UserCommandService#submitQuoteCancel(String, long, String)
	 */
	public void submitQuoteCancel(String product) throws UserNotConnectedException, InvalidConnectionIdException, InvalidMarketStateException, NoSuchProductException, InvalidArgumentException, InvalidVolumeValueException
	{
			UserCommandService.getInstance().submitQuoteCancel(getUserName(), getConnectionID(), product);
	}

	/**
	 * @see UserCommandService#subscribeCurrentMarket(String, long, String)
	 */
	public void subscribeCurrentMarket(String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidArgumentException
	{
			UserCommandService.getInstance().subscribeCurrentMarket(getUserName(), getConnectionID(), product);
	}

	/**
	 * @see UserCommandService#subscribeLastSale(String, long, String)
	 */
	public void subscribeLastSale(String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidArgumentException
	{
		UserCommandService.getInstance().subscribeLastSale(getUserName(), getConnectionID(), product);
	}

	/**
	 * @see UserCommandService#subscribeMessages(String, long, String)
	 */
	public void subscribeMessages(String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidArgumentException
	{
		UserCommandService.getInstance().subscribeMessages(getUserName(), getConnectionID(), product);
	}
	
	/**
	 * @see UserCommandService#subscribeTicker(String, long, String)
	 */
	public void subscribeTicker(String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidArgumentException
	{
		UserCommandService.getInstance().subscribeTicker(getUserName(), getConnectionID(), product);
	}

	/**
	 * @see Position#getAllStockValue()
	 */
	public Price getAllStockValue() throws InvalidPriceOperation, InvalidArgumentException
	{
		return position.getAllStockValue();
	}

	/**
	 * @see Position#getAccountCosts()
	 */
	public Price getAccountCosts()
	{
		return position.getAccountCosts();
	}
	
	/**
	 * @see Position#getNetAccountValue()
	 */
	public Price getNetAccountValue() throws InvalidPriceOperation, InvalidArgumentException
	{
		return position.getNetAccountValue();
	}

	/**
	 * @see UserCommandService#getBookDepth(String, long, String)
	 */
	public String[][] getBookDepth(String product) throws UserNotConnectedException, InvalidConnectionIdException, NoSuchProductException, InvalidArgumentException
	{
		return UserCommandService.getInstance().getBookDepth(getUserName(), getConnectionID(), product);
	}

	/**
	 * @see UserCommandService#getMarketState(String, long)
	 */
	public String getMarketState() throws UserNotConnectedException, InvalidConnectionIdException, InvalidArgumentException
	{
		return UserCommandService.getInstance().getMarketState(getUserName(), getConnectionID());
	}
	
	/**
	 * @see User#getOrderIds()
	 */
	public ArrayList<TradableUserData> getOrderIds()
	{
		return orderData;
	}

	/**
	 * @see User#getProductList()
	 */
	public ArrayList<String> getProductList()
	{
		return stocks;
	}
	
	/**
	 * @throws InvalidArgumentException 
	 * @see Position#getStockPositionValue(String)
	 */
	public Price getStockPositionValue(String product) throws InvalidPriceOperation, InvalidArgumentException
	{
		return position.getStockPositionValue(product);
	}
	
	/**
	 * @throws InvalidArgumentException 
	 * @see Position#getStockPositionVolume(String)
	 */
	public int getStockPositionVolume(String product) throws InvalidArgumentException
	{
		return position.getStockPositionVolume(product);
	}

	/**
	 * @see Position#getHoldings()
	 */
	public ArrayList<String> getHoldings()
	{
		return position.getHoldings();
	}

	/**
	 * @see User#getOrdersWithRemainingQty(String)
	 */
	public ArrayList<TradableDTO> getOrdersWithRemainingQty(String product) throws UserNotConnectedException, InvalidConnectionIdException, InvalidArgumentException
	{
		return UserCommandService.getInstance().getOrdersWithRemainingQty(getUserName(), getConnectionID(), product);
	}
	//end UserImpl()

}
