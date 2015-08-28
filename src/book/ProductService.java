package book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


import tradable.Tradable.Side;
import dto.MarketDataDTO;
import dto.TradableDTO;
import publishers.MarketMessage;
import publishers.MarketMessage.MarketState;
import publishers.MessagePublisher;
import tradable.Order;
import tradable.Quote;
import utils.DataValidationException;
import utils.InvalidArgumentException;
import utils.InvalidMarketStateException;
import utils.InvalidMarketStateTransition;
import utils.InvalidVolumeValueException;
import utils.NoSuchProductException;
import utils.OrderNotFoundException;
import utils.ProductAlreadyExistsException;


/**
 * This class is the Facade to the entities that make up the Products (Stocks), and the Product Books (“booked” tradables on the Buy and Sell side). 
 * All interaction with the product books and the buy and sell sides of a stock’s book will go through this Facade
 * 
 * @author Urvi
 *
 */
public final class ProductService 
{
	/**
	 * The single instance of the ProductService class
	 */
	private volatile static ProductService instance;
	
	/**
	 * Structure that contains all product books, accessible by the stock symbol name. 
	 */
	private static HashMap<String, ProductBook> allBooks = new HashMap<String, ProductBook>();
	
	/**
	 *  Holds the current market state (CLOSED, PREOPEN, or OPEN). 
	 */
	private static MarketState state;
	
	/**
	 * Method used to create a single instance of the ProductService class 
	 * @return A ProductService object
	 * @throws InvalidArgumentException if an invalid MarketState value is passed in
	 */
	public static ProductService getInstance() throws InvalidArgumentException
	{
		if(instance == null)
		{
			synchronized(ProductService.class)
			{
				if(instance == null)
					instance = new ProductService();
			}
		}
		return instance;
	}
	
	/**
	 * Private constructor, creates a ProductService object and sets the MarketState to CLOSED
	 * @throws InvalidArgumentException if the value passed in is null
	 */
	private ProductService() throws InvalidArgumentException
	{
		setState(MarketState.CLOSED);
	}
	
	/**
	 * Private modifier to set MarketState
	 * @param ste State to set the market initially set the market to
	 * @throws InvalidArgumentException if ste is null
	 */
	private void setState(MarketState ste) throws InvalidArgumentException
	{
		if(ste == null)  //Overkill?
		{
			throw new InvalidArgumentException("ProductService.setState() - state cannot be null");
		}
		else
		{
			state = ste;
		}
	}
	
	/**Gets an ArrayList of TradableDTO’s containing information on all the orders in this ProductBookSide that have 
	 * remaining quantity for the specified user.  This is done via ProductBookSide.getOrdersWithRemainingQty(String).
	 * @throws InvalidArgumentException 
	 * @see ProductBook#getOrdersWithRemainingQty(String)
	 */
	public synchronized ArrayList<TradableDTO> getOrdersWithRemainingQty(String username, String product) throws InvalidArgumentException
	{
		if(product == null || product.isEmpty())
		{
			throw new InvalidArgumentException("ProductService.getOrdersWithRemainingQty - Product cannot be null or empty");
		}
		ProductBook prodBook = allBooks.get(product);
		ArrayList<TradableDTO> dtos = prodBook.getOrdersWithRemainingQty(username);
		return dtos;
	}
	
	/** Gets the best buy side price and volume.  This is done via a call to ProductBook.getMarketData()
	 * @param product Product for which to get price and volume information
	 * @throws InvalidArgumentException if product is null or empty
	 * @see ProductBook#getMarketData()
	 */
	public synchronized MarketDataDTO getMarketData(String product) throws InvalidArgumentException
	{
		if(product == null || product.isEmpty())
		{
			throw new InvalidArgumentException("ProductService.getOrdersWithRemainingQty - Product cannot be null or empty");
		}
		ProductBook prodBook = allBooks.get(product);
		MarketDataDTO mktDTO = prodBook.getMarketData();
		return mktDTO;
	}
	
	/**
	 * Return the current MarketState
	 * @return The current MarketState, CLOSED, PREOPEN, OPEN
	 */
	
	public synchronized MarketState getMarketState()
	{
		return state;
	}
	
	/** For each Price in the ProductBookSide, calculates the total volume at that price.  This is done via 
	 * a call to ProductBook.getBookDepth()
	 * @param product Product for which to get depth
	 * @throws InvalidArgumentException if product is null or empty
	 * 
	 * @see ProductBook#getBookDepth()
	 */
	public synchronized String[][] getBookDepth(String product) throws NoSuchProductException, InvalidArgumentException
	{
		if(product == null || product.isEmpty())
		{
			throw new InvalidArgumentException("ProductService.getBookDepth - Product cannot be null or empty");
		}
		
		if(allBooks.containsKey(product) == false)
		{
			throw new NoSuchProductException("ProductService.getBookDepth - Product does not exist " + product);
		}
		
		ProductBook prodBook = allBooks.get(product);
		String[][] depths = prodBook.getBookDepth();
		return depths;
	}
	
	/**
	 * Creates an Arraylist containing all product books
	 * @return  Arraylist containing all product books,
	 */
	public synchronized ArrayList<String> getProductList()
	{
		return new ArrayList<String>(allBooks.keySet());
	}
	
	//******** Manipulation Methods ********//
	
	/**
	 * Updates the market state to the new state passed in (CLOSED, PREOPEN, OPEN) and then creates a MarketMessage to be published.
	 * If the new state is OPEN then the market is opened for every product book, if the new state is CLOSED then the market is closed for every product book 
	 * @param ms new state of the market
	 * @throws InvalidArgumentException if ms is null
	 * @throws InvalidVolumeValueException
	 * @throws OrderNotFoundException 
	 * @throws InvalidMarketStateTransition if updating to the new state results in a transition that is not allowed. 
	 * The only valid transitions are CLOSED --> PREOPEN --> OPEN --> CLOSED	
	 * 
	 * @see ProductBook#closeMarket() 
	 * @see ProductBook#openMarket()
	 */
	
	public synchronized void setMarketState(MarketState ms) throws InvalidArgumentException, InvalidVolumeValueException, OrderNotFoundException, InvalidMarketStateTransition
	{
		if(ms == null)
		{
			throw new InvalidArgumentException("ProductService.setMarketState - State cannot be null");
		}
		
		if(state == MarketState.CLOSED && ms != MarketState.PREOPEN)
		{
			throw new InvalidMarketStateTransition("ProductService.setMarketState - Cannot go from CLOSED to " + ms.toString());
		}
		
		if(state == MarketState.PREOPEN && ms != MarketState.OPEN)
		{
			throw new InvalidMarketStateTransition("ProductService.setMarketState - Cannot go from PREOPEN to " + ms.toString());
		}
		
		if(state == MarketState.OPEN && ms != MarketState.CLOSED)
		{
			throw new InvalidMarketStateTransition("ProductService.setMarketState - Cannot go from OPEN to " + ms.toString());
		}
		
		state = ms;
		
		MarketMessage mm = new MarketMessage(ms);
		MessagePublisher.getInstance().publishMarketMessage(mm);
		Set<String> prods = allBooks.keySet();
		
		if(ms == MarketState.OPEN)
		{
			for(String p : prods)
			{
				ProductBook pb = allBooks.get(p);
				pb.openMarket();
			}
		}
		if(ms == MarketState.CLOSED)
		{
			for(String p : prods)
			{
				ProductBook pb = allBooks.get(p);
				pb.closeMarket();
			}
		}
	}
	
	
	/**
	 * Creates a new stock product that can be used for trading
	 * @param product Product to be created
	 * @throws InvalidArgumentException if product is null or empty
	 * @throws ProductAlreadyExistsException if product has already been created
	 */
	
	public synchronized void createProduct(String product) throws InvalidArgumentException, ProductAlreadyExistsException
	{
		if(product == null || product.isEmpty() == true)
		{
			throw new InvalidArgumentException("ProductService.createProduct - Product cannot be null or empty");
		}
		
		if(allBooks.containsKey(product) == true)
		{
			throw new ProductAlreadyExistsException("ProductService.createProduct - Product already exists: " + product);
		}
		
		ProductBook pb = new ProductBook(product);
		allBooks.put(product, pb);
	}
	
	/**
	 * Creates a new stock product that can be used for trading
	 * 
	 * @see ProductBook#addToBook(Quote)
	 * @param q The Quote to be sumbitted
	 * @throws InvalidMarketStateException
	 * @throws NoSuchProductException
	 * @throws DataValidationException if an attempt to submit a Quote is made when the market is CLOSED
	 * @throws InvalidVolumeValueException
	 * @throws InvalidArgumentException
	 */
	public synchronized void submitQuote(Quote q) throws InvalidMarketStateException, NoSuchProductException, DataValidationException, InvalidVolumeValueException, InvalidArgumentException
	{
		if(q == null)
		{
			throw new InvalidMarketStateException("ProductService.submitQuote - Quote cannot be null");
		}
		if(state == MarketState.CLOSED)
		{
			throw new InvalidMarketStateException("ProductService.submitQuote - Market is CLOSED, cannot submit Quote");
		}
		
		if(allBooks.containsKey(q.getProduct()) == false)
		{
			throw new NoSuchProductException("ProductService.getBookDepth - Product does not exist " + q.getProduct());
		}
		
		ProductBook pb = allBooks.get(q.getProduct());
		pb.addToBook(q);
	}
	
	/**
	 *  Adds the provided Order to the appropriate ProductSideBook.  This is done via a call to ProductBook.addToBook(Order).
	 * @see ProductBook#addToBook(Order)
	 * @param o The Order to be submitted
	 * @return The identifier of the Order that was added
	 * @throws InvalidMarketStateException if an attempt to submit an Order is made when the market is CLOSED
	 * @throws NoSuchProductException
	 * @throws InvalidArgumentException
	 * @throws InvalidVolumeValueException
	 */
	
	public synchronized String submitOrder(Order o) throws InvalidMarketStateException, NoSuchProductException, InvalidArgumentException, InvalidVolumeValueException
	{
		if(o == null)
		{
			throw new InvalidArgumentException("ProductService.submitOrder - Order cannot be null");
		}
		
		if(state == MarketState.CLOSED)
		{
			throw new InvalidMarketStateException("ProductService.submitQuote - Market is CLOSED, cannot submit Order");
		}
		
		if(state == MarketState.PREOPEN && o.getPrice().isMarket())
		{
			throw new InvalidMarketStateException("ProductService.submitQuote - Market is PREOPON, cannot submit Market order"); 
		}
		
		if(allBooks.containsKey(o.getProduct()) == false)
		{
			throw new NoSuchProductException("ProductService.getBookDepth - Product does not exist " + o.getProduct());
		}
		
		ProductBook pb = allBooks.get(o.getProduct());
		pb.addToBook(o);
		return o.getId();
	}
	
	/**
	 * Cancels an Order for given side of the book.  This is done via ProductBook.cancelOrder(BookSide, String)
	 * @see ProductBook#cancelOrder(ProductBookSide.BookSide, String)
	 * @param product Product symbol of Order to be cancelled
	 * @param side Side of the book (BUY or SELL) 
	 * @param orderId Identifier of the Order to be cancelled
	 * @throws InvalidMarketStateException if an attempt to cancel an Order is made when the market is CLOSED
	 * @throws NoSuchProductException if product is not a product in the book
	 * @throws InvalidArgumentException
	 * @throws InvalidVolumeValueException
	 * @throws OrderNotFoundException
	 */
	
	public synchronized void submitOrderCancel(String product, Side side, String orderId) throws InvalidMarketStateException, NoSuchProductException, InvalidArgumentException, InvalidVolumeValueException, OrderNotFoundException
	{
		if(product == null || product.isEmpty())
		{
			throw new InvalidArgumentException("ProductService.submitOrderCancel - Product cannot be null or empty");
		}
		
		if(state == MarketState.CLOSED)
		{
			throw new InvalidMarketStateException("ProductService.submitQuote - Market is CLOSED, cannot cancel Order");
		}
		
		if(allBooks.containsKey(product) == false)
		{
			throw new NoSuchProductException("ProductService.getBookDepth - Product does not exist " + product);
		}
		
		ProductBook pb = allBooks.get(product);
		pb.cancelOrder(side, orderId);
		
	}
	
	/**
	 * This method cancels a Quote for a username. This is done via a call to ProductBook.cancelQuote(String)
	 * @see ProductBook#cancelQuote(String)
	 * @param username
	 * @param product
	 * @throws InvalidMarketStateException if an attempt to cancel a Quote is made when the market is CLOSED
	 * @throws NoSuchProductException if product is not a product in the book
	 * @throws InvalidArgumentException
	 * @throws InvalidVolumeValueException
	 */
	public synchronized void submitQuoteCancel(String username, String product) throws InvalidMarketStateException, NoSuchProductException, InvalidArgumentException, InvalidVolumeValueException
	{
		if(product == null || product.isEmpty())
		{
			throw new InvalidArgumentException("ProductService.submitQuoteCancel - Product cannot be null or empty");
		}
		
		
		if(state == MarketState.CLOSED)
		{
			throw new InvalidMarketStateException("ProductService.submitQuote - Market is CLOSED, cannot cancel Order");
		}
		
		if(allBooks.containsKey(product) == false)
		{
			throw new NoSuchProductException("ProductService.getBookDepth - Product does not exist " + product);
		}
		
		ProductBook pb = allBooks.get(product);
		pb.cancelQuote(username);
	}
}//end ProductService
