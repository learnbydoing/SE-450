package client;

import java.util.ArrayList;

import dto.TradableDTO;


import price.Price;
import publishers.CancelMessage;
import publishers.FillMessage;
import tradable.Tradable.Side;
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



/**
 * The User interface contains method declarations that must be implemented by a
 * class that want to be a "User"of DSX.  
 * 
 * The User interface provides a method which returns the user's name.
 * 
 * The user interface provides methods that give the user various types of
 * information about their stocks, which may sometimes be displayed on at GUI.
 * 
 * There is method that allows users to track information about sales of stock, 
 * which may or not be displayed to a GUI.
 * 
 * There is a method that gives the user information on an order that was traded.
 * 
 * There is a method that gives the user information on an order that was cancelled.
 * 
 * There is method gives the user information about a given stock, namely the current
 * price and an indication that the price reflects an increase or decrease since the 
 * last stock price. 
 * 
 * There is a method to give the user information for the current market (bid and offer)
 * for a stock.  This information includes the stock symbol, price, quantity for
 * the bid and offer. This may be displayed on a GUI.
 * 
 * There are methods to connect/disconnect from the trading system, 
 * 
 * There are methods to submit Orders and Quotes, and their respective cancels
 * 
 * There are methods to subscribe/unscribe from CurrentMarket, LastSale, Message, 
 * and Ticker publishers
 * 
 * There are methods to get profit/loss information
 * 
 * @author Urvi
 *
 */
public interface User 
{
	/**
	 * Returns the name of a User
	 * 
	 * @return The name of the user
	 */
	String getUserName();
	
	/**
	 * 
	 * Holds information about the last sale that is of interest to a User.  This 
	 * information is,namely the stock's symbol, the price that the stock traded, 
	 * and the quantity that was traded. 
	 * 
	 * @param prod The symbol of the stock
	 * @param p - The price of the stock
	 * @param v - The quantity of the last sale
	 * @throws InvalidArgumentException 
	 */
	void acceptLastSale(String prod, Price p, int v) throws InvalidArgumentException;
	
	/**
	 * Holds information about an order that was traded by a User
	 * 
	 * @param fm Contains information regarding an order that was traded
	 */
	void acceptMessage(FillMessage fm);
	
	/**
	 * Holds information about an order that was cancelled by a User
	 * 
	 * @param cm Contains information regarding an order that was cancelled
	 */
	void acceptMessage(CancelMessage cm);
	
	/**
	 * Holds information regarding the state of market that is of interest to a User
	 * 
	 * @param message Contains data related to the state of the market
	 */
	void acceptMarketMessage(String message);
	
	/**
	 * Holds information about a stock that is of interest to a User.  Namely,
	 * the stock symbol and price of that stock as well an indication as to whether 
	 * this price is greater than, less than, or equal to the last price. 
	 * 
	 * @param prod Symbol for the stock
	 * @param p Price of the stock
	 * @param direction Indicates if current price reflects an increase, decrease, or no 
	 * change from the last price.
	 */
	void acceptTicker(String prod, Price p, char direction);
	
	/**
	 * Holds information about the state of the market for a given stock, i.e, 
	 * where the stock is trading, that is of interest to a User.  The information 
	 * includes that name of the stock, and the bid and offer prices and quantities.
	 * 
	 * @param prod Symbol of the stock
	 * @param bp Buy price of the stock, i.e, the bid
	 * @param bv Buy volume of the stock i.e., how much is available for buying
	 * @param sp Selling price of the stock, i.e., the offer
	 * @param sv Sell volume of the stock, i.e., how much is available for selling
	 */
	void acceptCurrentMarket(String prod, Price bp, int bv, Price sp, int sv);

	/**
	 * This method will connect the user to the trading system.
	 * @throws InvalidArgumentException
	 * @throws AlreadyConnectedException
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @see UserCommandService#connect(User)
	 */
	void connect() throws InvalidArgumentException, AlreadyConnectedException, UserNotConnectedException, InvalidConnectionIdException;
	
	/**
	 *  This method will disconnect the user from the trading system. 
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @throws InvalidArgumentException 
	 * @see UserCommandService#disConnect(String, long)
	 */
	void disConnect() throws UserNotConnectedException, InvalidConnectionIdException, InvalidArgumentException;
	
	/**
	 * This method will activate the market display
	 * @throws UserNotConnectedException
	 * @see gui.UserDisplayManager#showMarketDisplay()
	 */
	void showMarketDisplay() throws UserNotConnectedException;
	
	/**
	 * This method forwards the new order request to the user command service and saves the resulting order id.
	 * @param product The name of the stock
	 * @param price The Price of the stock
	 * @param volume The quantity of stock to be traded
	 * @param side Side of the trade, BUY or SELL
	 * @return Id of Order that was submitted
	 * @throws InvalidMarketStateException
	 * @throws NoSuchProductException
	 * @throws InvalidArgumentException
	 * @throws InvalidVolumeValueException
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * 
	 * @see UserCommandService#submitOrder(String, long, String, price.Price, int, tradable.Tradable.Side)
	 * @see TradableUserData#TradableUserData(String, String, tradable.Tradable.Side, String)
	 */
	String submitOrder(String product, Price price, int volume, Side side) throws InvalidMarketStateException, NoSuchProductException, InvalidArgumentException, InvalidVolumeValueException, UserNotConnectedException, InvalidConnectionIdException;
	
	/**
	 * This method forwards the order cancel request to the user command service
	 * @param product The name of the stock
	 * @param side Side of the trade, BUY or SELL
	 * @param orderId Id of Order to be cancelled
	 * @throws InvalidMarketStateException
	 * @throws NoSuchProductException
	 * @throws InvalidArgumentException
	 * @throws InvalidVolumeValueException
	 * @throws OrderNotFoundException
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @see UserCommandService#submitOrderCancel(String, long, String, book.ProductBookSide.BookSide, String)
	 */
	void submitOrderCancel(String product, Side side, String orderId) throws InvalidMarketStateException, NoSuchProductException, InvalidArgumentException, InvalidVolumeValueException, OrderNotFoundException, UserNotConnectedException, InvalidConnectionIdException;
	
	
	/**
	 * This method forwards the new quote request to the user command service
	 * @param product The stock symbol of the Quote
	 * @param buyPrice The price of buy side of the Quote
	 * @param buyVolume The quantity for the buy side of the Quote
	 * @param sellPrice The price of the sell side of the Quote
	 * @param sellVolume The quantity for the sell side of the Quote
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @throws InvalidVolumeValueException
	 * @throws InvalidArgumentException
	 * @throws InvalidMarketStateException
	 * @throws NoSuchProductException
	 * @throws DataValidationException
	 * @see UserCommandService#submitQuote(String, long, String, Price, int, Price, int)
	 */
	void submitQuote(String product, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume) throws UserNotConnectedException, InvalidConnectionIdException, InvalidVolumeValueException, InvalidArgumentException, InvalidMarketStateException, NoSuchProductException, DataValidationException;
	
	/**
	 * This method forwards the quote cancel request to the user command service as follows:
	 * @param product The name of the stock of the Quote
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @throws InvalidMarketStateException
	 * @throws NoSuchProductException
	 * @throws InvalidArgumentException
	 * @throws InvalidVolumeValueException
	 * @see UserCommandService#submitQuoteCancel(String, long, String)
	 */
	void submitQuoteCancel(String product) throws UserNotConnectedException, InvalidConnectionIdException, InvalidMarketStateException, NoSuchProductException, InvalidArgumentException, InvalidVolumeValueException;
	
	/**
	 * This method forwards the current market subscription to the user command service
	 * @param product The name of the stock of the Quote
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @throws AlreadySubscribedException
	 * @throws InvalidArgumentException
	 * @see UserCommandService#subscribeCurrentMarket(String, long, String)
	 */
	void subscribeCurrentMarket(String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidArgumentException;
	
	/**
	 * This method forwards the last sale subscription to the user command service
	 * @param product The name of the stock of the Quote
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @throws AlreadySubscribedException
	 * @throws InvalidArgumentException
	 * @see UserCommandService#subscribeLastSale(String, long, String)
	 */
	void subscribeLastSale(String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidArgumentException;
	
	/**
	 * This method forwards the message subscription to the user command interface
	 * @param product
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @throws AlreadySubscribedException
	 * @throws InvalidArgumentException
	 * @see UserCommandService#subscribeMessages(String, long, String)
	 */
	void subscribeMessages(String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidArgumentException;
	
	/**
	 * This method forwards the ticker subscription to the user command service
	 * @param product
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @throws AlreadySubscribedException
	 * @throws InvalidArgumentException
	 * @see UserCommandService#subscribeTicker(String, long, String)
	 */
	void subscribeTicker(String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidArgumentException;
	
	/**
	 * Determines value of all the stock the User owns
	 * @return Returns of value of all stock the User owns
	 * @throws InvalidPriceOperation
	 * @throws InvalidArgumentException
	 * @see Position#getAllStockValue()
	 */
	Price getAllStockValue() throws InvalidPriceOperation, InvalidArgumentException;
	
	/**
	 * Determines the difference between cost of all stock purchases and stock sales
	 * @return Difference between cost of all stock purchases and stock sales
	 * @see Position#getAccountCosts()
	 */
	Price getAccountCosts();
	
	/**
	 * Determines the difference between current value of all stocks owned and account costs
	 * @return Difference between current value of all stocks owned and account costs
	 * @throws InvalidPriceOperation
	 * @throws InvalidArgumentException
	 * @see Position#getNetAccountValue()
	 */
	Price getNetAccountValue() throws InvalidPriceOperation, InvalidArgumentException;
	
	/**
	 * Allows the User object to submit a Book Depth request for the specified stock
	 * @param product The name of the stock symbol for which you want the depth
	 * @return The current book depth
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @throws NoSuchProductException
	 * @throws InvalidArgumentException
	 * @see UserCommandService#getBookDepth(String, long, String)
	 */
	String[][] getBookDepth(String product) throws UserNotConnectedException, InvalidConnectionIdException, NoSuchProductException, InvalidArgumentException;
	
	/**
	 * Allows User object to query the market state - OPEN, PREOPEN, CLOSED
	 * @return The current market state
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @throws InvalidArgumentException
	 * @see UserCommandService#getMarketState(String, long)
	 */
	String getMarketState() throws UserNotConnectedException, InvalidConnectionIdException, InvalidArgumentException;
	
	/**
	 * Returns order ids for orders a User has submitted
	 * @return A list of order ids
	 */
	ArrayList<TradableUserData> getOrderIds();
	
	/**
	 * Returns list of stocks available in the trading system
	 * @return List of stocks in the trading system
	 */
	ArrayList<String> getProductList();
	
	/**
	 * Returns value of the specified stock that a User owns
	 * @param sym The stock symbol for which the position value is requested
	 * @return Price object containing the value of the stock
	 * @throws InvalidPriceOperation
	 * @throws InvalidArgumentException 
	 * @see Position#getStockPositionValue(String)
	 */
	Price getStockPositionValue(String sym) throws InvalidPriceOperation, InvalidArgumentException;
	
	/**
	 * Returns volume of the specified stock that a User owns
	 * @param product The stock symbol for which the position volume is requested
	 * @return Volume of the specified stock
	 * @throws InvalidArgumentException 
	 * @see Position#getStockPositionVolume(String)
	 */
	int getStockPositionVolume(String product) throws InvalidArgumentException;
	
	/**
	 * Returns list of all stocks the User owns
	 * @return List of all stocks the User owns
	 * @see Position#getHoldings()
	 */
	ArrayList<String> getHoldings();
	
	/**
	 * Gets list of of DTOs containing information on all Orders for the user for the specified product with remaining volume
	 * @param product The stock symbol for which to get the remaining quantity
	 * @return Gets list of of DTOs containing information on all Orders for the user for the specified product with remaining volume
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdException
	 * @throws InvalidArgumentException
	 * @see UserCommandService#getOrdersWithRemainingQty(String, long, String)
	 */
	ArrayList<TradableDTO> getOrdersWithRemainingQty(String product) throws UserNotConnectedException, InvalidConnectionIdException, InvalidArgumentException;
	
	
	
}
