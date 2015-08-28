package book;

import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


import dto.MarketDataDTO;
import dto.TradableDTO;

import price.Price;
import price.PriceFactory;
import publishers.CancelMessage;
import publishers.CurrentMarketPublisher;
import publishers.FillMessage;
import publishers.LastSalePublisher;
import publishers.MarketMessage.MarketState;
import publishers.MessagePublisher;
import tradable.Order;
import tradable.Quote;
import tradable.Tradable;
import tradable.Tradable.Side;
import utils.DataValidationException;
import utils.InvalidArgumentException;
import utils.InvalidVolumeValueException;
import utils.OrderNotFoundException;

/**
 * The ProductBook object maintains the Buy and Sell sides of a stock’s “book”. 
 * 
 * @author Urvi
 *
 */

public class ProductBook
{
	/**
	 * Stock symbol that this book represents 
	 */
	private String stockSymbol;
	
	/**
	 * ProductBookSide that maintains the Buy side of this book.
	 */
	private ProductBookSide buySide;
	
	/**
	 * ProductBookSide that maintains the Sell side of this book.
	 */
	private ProductBookSide sellSide;
	
	/**
	 * Results of the latest Market Data values (the prices and the volumes at the top of the buy and sell sides).
	 */
	private String lastCurrentMarket = "";
	
	/**
	 * List of the current quotes in this book for each user
	 */
	private HashSet<String> userQuotes = new HashSet<String>();
	
	/**
	 * Tradables that have been completely traded or cancelled 
	 */
	private HashMap<Price, ArrayList<Tradable>> oldEntries = new HashMap<Price, ArrayList<Tradable>>();


	/**
	 * Public constructor, creates ProductBook object.
	 * @param symbol Stock symbol this book represents. 
	 * @throws InvalidArgumentException if symbol is null or empty
	 */

public ProductBook(String symbol) throws InvalidArgumentException
{
	setProductSymbol(symbol);
	buySide = new ProductBookSide(this, Side.BUY);
	sellSide = new ProductBookSide(this, Side.SELL);
}//end ctr

/**
 * Sets the product symbol to the String passed in
 * @param sym The String passed in
 * @throws InvalidArgumentException if sym is null or empty
 */

private void setProductSymbol(String sym) throws InvalidArgumentException
{
 	if(sym != null && !sym.isEmpty())
	{
		stockSymbol = sym;
	}
	else
	{
		throw new InvalidArgumentException("ProductBook - Product symbol cannot be null");
	}
}//end setProductSymbol()

/**Gets an ArrayList of TradableDTO’s containing information on all the orders in this ProductBookSide that have 
 * remaining quantity for the specified user.  This is done via ProductBookSide.getOrdersWithRemainingQty(String).
 * @throws InvalidArgumentException  if username is null or empty
 * @see ProductBookSide#getOrdersWithRemainingQty(String)
 */

public synchronized ArrayList<TradableDTO> getOrdersWithRemainingQty(String username) throws InvalidArgumentException
{	
	ArrayList<TradableDTO> dtos = new ArrayList<TradableDTO>();
	dtos.addAll(buySide.getOrdersWithRemainingQty(username));
	dtos.addAll(sellSide.getOrdersWithRemainingQty(username));
	return dtos;
}//end getOrdersWithRemainingQty()

/**
 * Determine if it is too late to cancel an order (meaning it has already been traded out or cancelled), then creates a CancelMessage to be published
 * @param orderId Id for order that may have been traded or cancelled
 * @throws OrderNotFoundException if Order with orderId is not found
 * @throws InvalidArgumentException 
 * @throws InvalidVolumeValueException if orderId is null or empty 
 * @see CancelMessage#CancelMessage(String, String, Price, int, String, Tradable.Side, String)
 */
public synchronized void checkTooLateToCancel(String orderId) throws OrderNotFoundException, InvalidArgumentException, InvalidVolumeValueException
{
	if(orderId == null || orderId.isEmpty())
	{
		throw new InvalidArgumentException("ProductBook.checkTooLateToCancel - orderID cannot be null or empty");
	}
	
	Set<Price> prices = oldEntries.keySet();
	boolean found = false;

	for(Price p : prices)
	{
		if(found == false)
		{
			ArrayList<Tradable> trds = oldEntries.get(p);
			Iterator<Tradable> iter = trds.iterator();

			while(found == false && iter.hasNext())
			{
				Tradable t = iter.next();
				if(t.getId().equals(orderId))
				{
					found = true;
					CancelMessage cm = new CancelMessage(t.getUser(), t.getProduct(), t.getPrice(), t.getOriginalVolume(), "Too Late to Cancel", t.getSide(), t.getId());
					MessagePublisher.getInstance().publishCancel(cm);
				}
			}
		}
	}
	if(found == false)
	{
		throw new OrderNotFoundException("Product Book - Order with orderID: " + orderId + " was not found");
	}
}//end checkTooLateToCancel()

/**
 * Get the book depth via a call to ProductBookSide.getBookDepth()
 * @see ProductBookSide#getBookDepth()
 */

public synchronized String[][] getBookDepth()
{
	String[][] bd = new String[2][];  //O - Buy; 1 - Sell

	bd[0] = buySide.getBookDepth();
	bd[1] = sellSide.getBookDepth();
	return bd;
}//end getBookDepth()

/** Create a MarketDataDTO containing the best buy side price and volume
 * @see ProductBookSide#topOfBookPrice()
 * @see ProductBookSide#topOfBookVolume()
 * @return TradableDTO object that contains the best buy/sell prices and volumes
 */
public synchronized MarketDataDTO getMarketData()
{
	Price bestBuyPrice = buySide.topOfBookPrice();
	Price bestSellPrice = sellSide.topOfBookPrice();

	if(bestBuyPrice == null)
	{
		bestBuyPrice = PriceFactory.makeLimitPrice(0);
	}
	if(bestSellPrice == null)
	{
		bestSellPrice = PriceFactory.makeLimitPrice(0);
	}

	int bestBuyVol = buySide.topOfBookVolume();
	int bestSellVol = sellSide.topOfBookVolume();

	MarketDataDTO mktDTO = new MarketDataDTO(stockSymbol, bestBuyPrice, bestBuyVol, bestSellPrice, bestSellVol);
	return mktDTO;
}//end getMarketData()

//************** Manipulation methods ****************//

/**
 * Add the Tradable passed in to those that have been completely traded or cancelled
 * @param t Tradable to be added
 * @throws InvalidVolumeValueException
 * @throws InvalidArgumentException if t is null
 * 
 * @see Tradable#setCancelledVolume(int)
 */

public synchronized void addOldEntry(Tradable t) throws InvalidVolumeValueException, InvalidArgumentException
{
	if(t == null)
	{
		throw new InvalidArgumentException("ProductBook.addOldEntry - Tradable cannot be null");
	}
	
	if(oldEntries.containsKey(t.getPrice()) == false)
	{
		oldEntries.put(t.getPrice(), new ArrayList<Tradable>());
	}
		t.setCancelledVolume(t.getRemainingVolume());
		t.setRemainingVolume(0);
		ArrayList<Tradable> tList = oldEntries.get(t.getPrice());
		tList.add(t);
}//end addOldEntry()

/**
 * Opens the book for trading. Any resting Order and QuoteSides that are immediately tradable upon opening should be traded.
 * @throws InvalidVolumeValueException 
 * @throws InvalidArgumentException
 * 
 * @see #tryTrade(Tradable, ProductBookSide.BookSide) 
 */

public synchronized void openMarket() throws InvalidVolumeValueException, InvalidArgumentException
{
	Price buyPrice = buySide.topOfBookPrice();
	Price sellPrice = sellSide.topOfBookPrice();
	Price zeroPrice = PriceFactory.makeLimitPrice(0);

	if(buyPrice == null || sellPrice == null || buyPrice.equals(zeroPrice) || sellPrice .equals(zeroPrice)) //1 thru 3
	{
		return;
	}
	
	while( (buyPrice.greaterOrEqual(sellPrice)) || buyPrice.isMarket() || sellPrice.isMarket()) //5
	{
		ArrayList<Tradable> topBuys = new ArrayList<Tradable>(buySide.getEntriesAtPrice(buyPrice));
		HashMap<String, FillMessage> allFills = null;
		ArrayList<Tradable> toRemove = new ArrayList<Tradable>();

		for(Tradable t : topBuys)
		{
			allFills = tryTrade(t, Side.SELL);
			if(t.getRemainingVolume() == 0)
			{
				toRemove.add(t);
			}
		}//end foreach

		for(Tradable t: toRemove)
		{
			buySide.removeTradable(t);
		}//end foreach

		updateCurrentMarket();

		if(allFills.isEmpty() == false)
		{
			Price lastSalePrice = determineLastSalePrice(allFills); 
			int lastSaleVolume = determineLastSaleQuantity(allFills);
			LastSalePublisher.getInstance().publishLastSale(stockSymbol, lastSalePrice, lastSaleVolume);
		}
		

			buyPrice = buySide.topOfBookPrice();
			sellPrice = sellSide.topOfBookPrice();

		if(buyPrice == null || sellPrice == null) //1 thru 3
		{
			break;
		}
	}//end while
}//end openMarket()

/**
 * Cancels Orders and QuoteSides for BUY and SELL sides of the ProductBook upon market close.  This is done via ProuctBookSide.cancelAll().
 * @see ProductBookSide#cancelAll()
 */
public synchronized void closeMarket() throws InvalidArgumentException, InvalidVolumeValueException, OrderNotFoundException
{
	buySide.cancelAll();
	sellSide.cancelAll();
	updateCurrentMarket();
}//end closeMarket()

/**Cancels an Order for given side of the book.  This is done via a call to ProductBookSide.cancelOrder(String). 
 * This method takes a String which is the user name on the Order to be cancelled. 
 * @see ProductBookSide#submitOrderCancel(String)
 */

public synchronized void cancelOrder(Side side, String orderId) throws InvalidArgumentException, InvalidVolumeValueException, OrderNotFoundException
{
	if(side == null)
	{
		throw new InvalidArgumentException("ProductBook.cancelOrder - side cannot be null");
	}
	
	if(side == Side.BUY)
	{
		buySide.submitOrderCancel(orderId);
	}
	else
	{
		sellSide.submitOrderCancel(orderId);
	}
	updateCurrentMarket();
}//end cancelOrder()


/** Cancels a Quote for a given user via ProductBookSide.submitQuoteCancel(String).
 * @see ProductBookSide#submitQuoteCancel(String)
 */

public synchronized void cancelQuote(String username) throws InvalidArgumentException, InvalidVolumeValueException
{	
	buySide.submitQuoteCancel(username);
	sellSide.submitQuoteCancel(username);
	updateCurrentMarket();
}//end cancelQuote()

/**
 * Adds the provided Quote’s sides to the Buy and Sell ProductSideBooks.
 * @param q Quote to be added to the book
 * @throws DataValidationException if sell price of QuoteSide is less than buy price of QuoteSide or if sell or buy price of QuoteSide is zero
 * @throws InvalidVolumeValueException
 * @throws InvalidArgumentException 
 * 
 *  @see ProductBookSide#removeQuote(String)
 *  @see #addToBook(Tradable.Side, Tradable)
 */

public synchronized void addToBook(Quote q) throws DataValidationException, InvalidVolumeValueException, InvalidArgumentException
{
	if(q == null)
	{
		throw new InvalidArgumentException("ProductBook.addToBook - Quote cannot be null");
	}
	
	if(q.getQuoteSide(Side.SELL).getPrice().lessOrEqual(q.getQuoteSide(Side.BUY).getPrice()))
	{
		throw new DataValidationException("ProductBook.addtoBook() - Sell price is less than or equal to Buy price");
	}

	if(q.getQuoteSide(Side.BUY).getPrice().lessOrEqual(PriceFactory.makeLimitPrice(0)) || q.getQuoteSide(Side.SELL).getPrice().lessOrEqual(PriceFactory.makeLimitPrice(0)))
	{
		throw new DataValidationException("ProductBook.addtoBook() - Sell or Buy price is less than or equal to zero");
	}

	if(q.getQuoteSide(Side.BUY).getOriginalVolume() <= 0|| q.getQuoteSide(Side.SELL).getOriginalVolume() <=0)
	{
		throw new DataValidationException("ProductBook.addtoBook() - Sell or Buy volume is less than or equal to zero");
	}

	if(userQuotes.contains(q.getUserName()) == true)
	{
		buySide.removeQuote(q.getUserName());
		sellSide.removeQuote(q.getUserName());
		updateCurrentMarket();  //Output matches whether this is commented out or not
	}

	addToBook(Side.BUY, q.getQuoteSide(Side.BUY));
	addToBook(Side.SELL, q.getQuoteSide(Side.SELL));

	userQuotes.add(q.getUserName());
	updateCurrentMarket();
}//end addToBook(Quote)


/**
 * Method adds the provided Order to the appropriate ProductSideBook
 * @param o Order object be added
 * @throws InvalidArgumentException if o is null
 * @throws InvalidVolumeValueException
 * 
 * @see #addToBook(Tradable.Side, Tradable)
 * @see #updateCurrentMarket()
 */
public synchronized void addToBook(Order o) throws InvalidArgumentException, InvalidVolumeValueException
{
	if(o == null)
	{
		throw new InvalidArgumentException("ProductBook.addToBook - Order cannot be null");
	}
				
	addToBook(o.getSide(), o);
	updateCurrentMarket();
}//end addToBook(Order)


/**
 * Determines if the market for a stock product has been updated.  Once a change has been detected, a MarketDataDTO object
 * is created and a CurrentMarket message is published.
 * @throws InvalidArgumentException
 * @see CurrentMarketPublisher#publishCurrentMarket(MarketDataDTO)
 */
public synchronized void updateCurrentMarket() throws InvalidArgumentException
{
	String mkt = buySide.topOfBookPrice().toString() + buySide.topOfBookVolume() + sellSide.topOfBookPrice().toString() + sellSide.topOfBookVolume();

	if(lastCurrentMarket.equals(mkt) == false)
	{
		MarketDataDTO mktDTO = new MarketDataDTO(stockSymbol, buySide.topOfBookPrice(), buySide.topOfBookVolume(), sellSide.topOfBookPrice(), sellSide.topOfBookVolume());
		CurrentMarketPublisher.getInstance().publishCurrentMarket(mktDTO);
	}
	lastCurrentMarket = mkt;	
}//end updateCurrentMarket()

/**
 * Checks HashMap of FillMessages passed in and determines from the information it contains what the Last Sale price is
 * 
 * @param fills The HashMap of FillMessage objects that are passed in
 * @return The Price that corresponds to the last sale price  in the FillMessage HashMap
 * @throws InvalidArgumentException if fills is null
 */

private synchronized Price determineLastSalePrice(HashMap<String, FillMessage> fills) throws InvalidArgumentException
{
	if(fills == null)
	{
		throw new InvalidArgumentException("ProductBook.determineLastSalePrice - fills cannot be null");
	}
	
	ArrayList<FillMessage> msgs = new ArrayList<FillMessage>(fills.values());
	Collections.sort(msgs);
	return msgs.get(0).getPrice();
}//end determineLastSalePrice()


/**
 * Checks HashMap of FillMessages passed in and determines from the information it contains what the Last Sale quantity (volume) is.
 * @param fills The HashMap of FillMessage objects that are passed in
 * @return The volume that corresponds to the last sale volume in the FillMessage HashMap
 * @throws InvalidArgumentException if fills is null
 */
private synchronized int determineLastSaleQuantity(HashMap<String, FillMessage> fills) throws InvalidArgumentException
{
	if(fills == null)
	{
		throw new InvalidArgumentException("ProductBook.determineLastSaleQuantity - fills cannot be null");
	}
	
	ArrayList<FillMessage> msgs = new ArrayList<FillMessage>(fills.values());
	Collections.sort(msgs);
	return msgs.get(0).getVolume();
}//end determineLastSaleQuantity()


/**
 * Calls the ProductBookSide addToBook method, 
 * @param side
 * @param trd
 * @throws InvalidArgumentException
 * @throws InvalidVolumeValueException if side or trd is null
 * @see ProductBookSide#addToBook(tradable.Tradable)
 */
private synchronized void addToBook(Side side, Tradable trd) throws InvalidArgumentException, InvalidVolumeValueException
{
	if(side  == null)
	{
		throw new InvalidArgumentException("ProductBook.addToBook - side cannot be null");
	}
	
	if(ProductService.getInstance().getMarketState() == MarketState.PREOPEN)
	{
		 if(side  == Side.BUY)
		{
			buySide.addToBook(trd);
		}
		else
		{
			sellSide.addToBook(trd);
		}
		return;
	}

	HashMap<String, FillMessage> allFills = null;
	if(trd.getSide() == Side.BUY)
	{
		allFills = tryTrade(trd, Side.SELL);
	}
	else
	{
		allFills = tryTrade(trd, Side.BUY);
	}

	if(allFills != null && allFills.isEmpty() == false)
	{
		updateCurrentMarket();
		int tradedQuantity = trd.getOriginalVolume() - trd.getRemainingVolume();
		Price lastSalePrice = determineLastSalePrice(allFills);
		LastSalePublisher.getInstance().publishLastSale(stockSymbol, lastSalePrice, tradedQuantity);
	}


	if(trd.getRemainingVolume() > 0)
	{
		if(trd.getPrice().isMarket() == true)
		{
			CancelMessage cm = new CancelMessage(trd.getUser(), trd.getProduct(), trd.getPrice(), trd.getRemainingVolume(), "Cancelled", trd.getSide(), trd.getId());
			MessagePublisher.getInstance().publishCancel(cm);
		}
		else
		{
			if(side  == Side.BUY)
			{
				buySide.addToBook(trd);
			}
			else
			{
				sellSide.addToBook(trd);
			}
		}
	}
}//end addToBook()

/**
 * Returns an ArrayList of the Tradables that are at the best price.  This is done via a call to ProductBookSide.getEntriesAtTopOfBook()
 * @param trd The Tradable for which to get the entries, used to determine which ProductBookSide (BUY or SELL) is to be used
 * @return ArrayList of the Tradables that are at the best price
 * @throws InvalidArgumentException if trd is null
 */
public synchronized ArrayList<Tradable> getEntriesAtTopOfBook(Tradable trd) throws InvalidArgumentException
{
	if(trd  == null)
	{
		throw new InvalidArgumentException("ProductBook.getEntriesAtTopOfBook - tradable cannot be null");
	}
	
	
	ArrayList<Tradable> retTradableList = new ArrayList<Tradable>();
	if(trd.getSide() == Side.BUY)
	{
		retTradableList = sellSide.getEntriesAtTopOfBook();
	}
	else
	{
		retTradableList = buySide.getEntriesAtTopOfBook();
	}
	return 		retTradableList;
}//end getEntriesAtTopOfBook()

/**
 * Clears the appropriate side of the book (BUY or SELL).  This is done via a call to ProductBookSide.clearIfEmpty(Price).
 * @see ProductBookSide#clearIfEmpty(Price)
 * @param trd Tradable from which to determine which ProductBookSide (BUY or SELL) clear and from which to determine the Price associated with
 * the ArrayList to be removed
 * @throws InvalidArgumentException if trd is null
 */

public synchronized void clearIfEmpty(Tradable trd) throws InvalidArgumentException
{
	if(trd  == null)
	{
		throw new InvalidArgumentException("ProductBook.clearIfEmpty - tradable cannot be null");
	}
	
	
	if(trd.getSide() == Side.BUY)
	{
		sellSide.clearIfEmpty(topOfBookPrice(trd));
	}
	else
	{
		buySide.clearIfEmpty(topOfBookPrice(trd));
	}
}//end clearIfEmpty()

/** Determines the best Price for a BookSide (BUY or SELL) for the Tradable.  This is done via a call to 
 * ProductBookSide.topOfBookPrice()
 * @throws InvalidArgumentException if trd is null
 * 
 * @see ProductBookSide#topOfBookPrice()
 */

public synchronized Price topOfBookPrice(Tradable trd) throws InvalidArgumentException
{
	
	if(trd  == null)
	{
		throw new InvalidArgumentException("ProductBook.topOfBookPrice - tradable cannot be null");
	}
	
	Price p;
	if(trd.getSide() == Side.BUY)
	{
		p = sellSide.topOfBookPrice();
	}
	else
	{
		p = buySide.topOfBookPrice();
	}
	return p;
}//end topOfBookPrice()


/**
 * Tries a trade of the provided Tradable against entries in this ProductBookSide.
 * 
 * 
 * @param t Tradable to be traded
 * @param s Side of the tradable to be traded
 * @return HashMap of FillMessages
 * @throws InvalidVolumeValueException
 * @throws InvalidArgumentException if t or s are null
 * @see ProductBookSide#tryTrade(Tradable)
 */

public HashMap<String, FillMessage> tryTrade(Tradable t, Side s) throws InvalidVolumeValueException, InvalidArgumentException
{
	if(s == null)
	{
		throw new InvalidArgumentException("ProductBook.tryTrade - side cannot be null");
	}
	
   if(s  == Side.BUY)
   {
      return buySide.tryTrade(t);
   }
 else
   {
     return sellSide.tryTrade(t);
   }
}//end tryTrade()

}//end ProductBook class