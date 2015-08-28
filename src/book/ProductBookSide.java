package book;

import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import book.TradeProcessor.AllocationStrategy;
import dto.TradableDTO;
import price.Price;
import price.PriceFactory;
import publishers.CancelMessage;
import publishers.FillMessage;
import publishers.MessagePublisher;
import tradable.Tradable;
import tradable.Tradable.Side;
import utils.InvalidArgumentException;
import utils.InvalidVolumeValueException;
import utils.OrderNotFoundException;

/**
 * Maintains the content of one side (Buy or Sell) of a Stock (product) “book”.
 * 
 * @author Urvi
 *
 */

public class ProductBookSide
{
	/**
	 * Hold valid values for the side that the current ProductBookSide represents.
	 * 
	 * @author Urvi
	 *
	 */
	//public enum BookSide { BUY, SELL } ;
	
	/**
	 * Denote which side the current ProductBookSide represents, BUY or SELL
	 */
	private Side side;
	
	/**
	 * Collection of book entries for this side, organized by Price, and then by time of arrival. 
	 */
	private HashMap<Price, ArrayList<Tradable>> bookEntries = new HashMap<Price, ArrayList<Tradable>>();
	
	/**
	 *  Used to execute trades against this book side. 
	 */
	private TradeProcessor tradeProcessor;
	
/**
 *  The ProductBook object that this ProductBookSide belongs to
 */
	private ProductBook prodBook;


/**
 * Public constructor, creates a ProductBookSide object.
 * 
 * @param pBook Reference to the ProductBook object that this ProductBookSide belongs to
 * @param bSide The side, BUY or SELL, of this ProductBookSide object
 * @throws InvalidArgumentException if pBook or pSide is null
 */
public ProductBookSide(ProductBook pBook, Side bSide) throws InvalidArgumentException
{
	tradeProcessor = TradeProcessorFactory.getPriceTimeProcessor(AllocationStrategy.PRICETIME, this);
	setProductBook(pBook);
	setBookSide(bSide);
}//end ProductBookSide ctr

/**
 * Returns the ProductBook object that this ProductBookSide belongs to
 * @return ProductBook object that this ProductBookSide belongs to
 */

public ProductBook getProductBook()
{
	return prodBook;
}

/**
 * Sets the ProductBookSide side (BUY or SELL) to the parameter passed in
 * @param s The BookSide of the ProductBookSide that is passed in
 * @throws InvalidArgumentException if s is null
 */

private void setBookSide(Side s) throws InvalidArgumentException
{
	if(s == null)
	{
		throw new InvalidArgumentException("ProductBookSide.setBookSide - BoodSide cannot be null");
	}
	else
	{
		side = s;
	}
}

/**
 * Sets the ProductBook that this ProductBookSide belongs to. 
 * @param pb The ProductBook that is passed in
 * @throws InvalidArgumentException if pb is null
 */

private void setProductBook(ProductBook pb) throws InvalidArgumentException
{
	if(pb != null)
	{
		prodBook = pb;
	}
	else
	{
		throw new InvalidArgumentException("ProductBookSide.setProductBook() - ProductBook cannot be null");
	}
}//end setProductBook()

/**
 * Gets an ArrayList of TradableDTO’s containing information on all the orders in this ProductBookSide that have remaining quantity for the specified user.
 * @param username User for which to generate ArrayList of TradableDTO’s containing information on all the orders in this ProductBookSide that have remaining quantity
 * @return ArrayList of TradableDTO’s containing information on all the orders in this ProductBookSide that have remaining quantity
 * @throws InvalidArgumentException if username is null or empty
 */
public synchronized ArrayList<TradableDTO> getOrdersWithRemainingQty(String username) throws InvalidArgumentException
{
	if(username == null || username.isEmpty())
	{
		throw new InvalidArgumentException("ProductBookSide.getOrdersWithRemainingQty - username cannot be null or empty");
	}
	
	ArrayList<TradableDTO> dtoList = new ArrayList<TradableDTO>();
	ArrayList<Tradable> tradablesList = new ArrayList<Tradable>();
	Set<Price> prices = bookEntries.keySet();
	
	for(Price p : prices)
	{
		tradablesList = bookEntries.get(p);
		
		for(Tradable t : tradablesList)
		{
		  if(t.getUser().equals(username) && t.getRemainingVolume() > 0)
		  {
			  TradableDTO tDTO = new TradableDTO(t.getProduct(), t.getPrice(), t.getOriginalVolume(), t.getRemainingVolume(), t.getCancelledVolume(), t.getUser(), t.getSide(), t.isQuote(), t.getId());
			  dtoList.add(tDTO);
		  }
		}
	}
	return dtoList;
}//end getOrdersWithRemainingQty()

/**
 * Gets an ArrayList of the Tradables that are at the best price in the “bookEntries” HashMap.
 * @return  ArrayList of the Tradables that are at the best price in the “bookEntries” HashMap.
 */
synchronized ArrayList<Tradable> getEntriesAtTopOfBook()
{
	if(bookEntries.isEmpty() == true)
	{
		return null;
	}
 	
	ArrayList<Price> sorted = new ArrayList<Price>(bookEntries.keySet()); //Get prices
	
	Collections.sort(sorted);  //Sort in ascending order

	if(side == Side.BUY)
	{
		Collections.reverse(sorted); //Sort in descending order
	}
	return bookEntries.get(sorted.get(0));
}//end getEntriesAtTopOfBook()

/**
 * For each Price in the ProductBookSide, calculates the total volume at that price
 * @return Two dimensional String array, where each entry contains the Price and the total volume at that price
 */

public synchronized String[] getBookDepth()
{
	
	if(bookEntries.isEmpty()) //look this up
	{
		return new String[] { "<Empty>" };
	}

	int size = bookEntries.size();
	String[] retString = new String[size];

	ArrayList<Price> sorted = new ArrayList<Price>(bookEntries.keySet()); //Get prices
	Collections.sort(sorted); //Sort in ascending order
	if(side == Side.BUY)
	{
		Collections.reverse(sorted); //Sort in descending order
	}

	int i = 0;
	int totRemVol = 0;
	for(Price p: sorted)
	{
		ArrayList<Tradable> tList = bookEntries.get(p);
		totRemVol = 0;
		for(Tradable t : tList)
		{
			totRemVol += t.getRemainingVolume();
		}
		String s = p.toString() + " x " + totRemVol;
		retString[i] = s;
		i++;
	}
	return retString;
}//end getBookDepth()

/**
 * Return all the Tradables in this book side at the specified price
 * @param price The price for which to get all Tradables
 * @return Return ArrayList of all the Tradables in this book side at the specified price
 * @throws InvalidArgumentException if price is null
 */

synchronized ArrayList<Tradable> getEntriesAtPrice(Price price) throws InvalidArgumentException
{
	if(price == null)
	{
		throw new InvalidArgumentException("ProductBookSide.getEntriesAtPrice - price cannot be nul");
	}
	
	if(bookEntries.get(price) == null)
	{
		return null;
	}
	else
	{
		return bookEntries.get(price);
	}
}//end getEntriesAtPrice

/**
 * Determines if the product book (the “bookEntries” HashMap) contains a Market Price
 * 
 * @return True if the product book (the “bookEntries” HashMap) contains a Market Price, false otherwise
 */
public synchronized boolean hasMarketPrice()
{
	return ( bookEntries.containsKey(PriceFactory.makeMarketPrice()) == true); 
}//end hasMarketPrice()

/**
 * Determines the ONLY Price in this product’s book is a Market Price
 * @return  Returns true if there is only one key in the “bookEntries” HashMap and it is a Market Price
 */

public synchronized boolean hasOnlyMarketPrice()
{
	return ( bookEntries.size() == 1 && bookEntries.containsKey(PriceFactory.makeMarketPrice()) == true);
}//end hasOnlyMarketPrice()

/**
 * Determines the best Price in the ProductBookSide
 * @return  The best Price in the ProductBookSide
 */

public synchronized Price topOfBookPrice()
{
	ArrayList<Price> sorted = new ArrayList<Price>(bookEntries.keySet()); //Get prices
	
	if(bookEntries.isEmpty())
	{
		Price p = PriceFactory.makeLimitPrice(0);
		return p;
	}
	else
	{
		
		Collections.sort(sorted); //Sort in ascending order
		if(side == Side.BUY)
		{
			Collections.reverse(sorted); //Sort in descending order
		}
	}
	return sorted.get(0);
}//end topOfBookPrice()

/**
 *  Determines the volume associated with the best Price in the ProductBookSide
 * @return The volume associated with the best Price in the ProductBookSide
 */

public synchronized int topOfBookVolume()
{
	ArrayList<Price> sorted = new ArrayList<Price>(bookEntries.keySet()); //Get prices
	ArrayList<Tradable> trds = new ArrayList<Tradable>();
	
	if(bookEntries.isEmpty())
	{
		return 0;
	}
	else
	{
		
		Collections.sort(sorted); //Sort in ascending order
		if(side == Side.BUY)
		{
			Collections.reverse(sorted); //Sort in descending order
		}
	}
	trds = bookEntries.get((sorted).get(0));
	int tSum = 0;
	for(Tradable t : trds)
	{
		tSum += t.getRemainingVolume();
	}
	return tSum;
}//end topOfBookVolume()

/**
 * Determine if there are no products in the product book
 * @return True if there are no products in the product book
 */
public synchronized boolean isEmpty()
{
	return bookEntries.isEmpty();
}//end isEmpty()


//********** Manipulation methods *************//

/**
 * Cancel every Order and QuoteSide in the product book
 * @see #submitOrderCancel(String)
 * @see #submitQuoteCancel(String)
 */

public synchronized void cancelAll() throws InvalidArgumentException, InvalidVolumeValueException, OrderNotFoundException
{
	HashSet<Price> pSet = new HashSet<Price>(bookEntries.keySet());  //Copy of price keys
	
	for(Price p : pSet)
	{
		ArrayList<Tradable> tList = new ArrayList<Tradable>(bookEntries.get(p));
		for(Tradable t : tList)
		{
			if(t.isQuote() == true)
			{
				submitQuoteCancel(t.getUser());
			}
			if(t.isQuote() == false)	
			{
				submitOrderCancel(t.getId());
			}
		}
	}
}//end cancelAll()

//public CancelMessage(String uname, String sym, Price prce, int vlme, String dtls, Side sde, String id)

/**
 * Remove the Quote from the book, belonging to specified user
 * @param user The User whose Quote is to be removed
 * @return TradableDTO using data from the QuoteSide to be removed
 * @throws InvalidArgumentException if user is null or empty
 */

public synchronized TradableDTO removeQuote(String user) throws InvalidArgumentException
{
	if(user == null || user.isEmpty())
	{
		throw new InvalidArgumentException("ProductBookSide.removeQuote - user cannot be null or empty");
	}
	HashSet<Price> pSet = new HashSet<Price>(bookEntries.keySet()); 

	
	for(Price p : pSet)
	{
		ArrayList<Tradable> tList = new ArrayList<Tradable>(bookEntries.get(p));
		for(Tradable t : tList)
		{
			if(t.isQuote() && t.getUser().equals(user))
			{
				bookEntries.get(p).remove(t);
				TradableDTO dto = new TradableDTO(t.getProduct(), t.getPrice(), t.getOriginalVolume(), t.getRemainingVolume(), t.getCancelledVolume(), t.getUser(), t.getSide(), t.isQuote(), t.getId());
				if(bookEntries.get(p).size() == 0)
				{
					bookEntries.remove(p);
				}
					return dto;
				}//end if
		}//end foreach Tradable
	}//end foreach Price;
	return null;
}//end removeQuote()

/**
 * Cancel the Order (if possible) that has the specified identifier and create CancelMessage to be published and adds Tradable to list old entries.
 * If no order is found, ProductBook's checkTooLateToCancel method is called 
 * @param orderId Id of order to be cancelled
 * @throws InvalidArgumentException  
 * 
 * @see #addOldEntry(Tradable)
 * @see ProductBook#checkTooLateToCancel(String)
 */

public synchronized void submitOrderCancel(String orderId) throws InvalidArgumentException, InvalidVolumeValueException, OrderNotFoundException
{
	if(orderId == null || orderId.isEmpty())
	{
		throw new InvalidArgumentException("ProductBookSide.submitOrderCancel - orderId cannot be null or empty");
	}
	
	HashSet<Price> pSet = new HashSet<Price>(bookEntries.keySet()); 
	boolean found = false;
	
	for(Price p : pSet)
	{
	  if(found == false)
	  {
		ArrayList<Tradable> tList = new ArrayList<Tradable>(bookEntries.get(p));
		for(Tradable t : tList)
		{
			if(t.getId().equals(orderId))
			{
				found = true;
				bookEntries.get(p).remove(t);
				CancelMessage cm = new CancelMessage(t.getUser(), t.getProduct(), t.getPrice(), t.getRemainingVolume(), "Order Cancelled", t.getSide(), t.getId());
				MessagePublisher.getInstance().publishCancel(cm);
				addOldEntry(t);
				if(bookEntries.get(p).size() == 0)
				{
					bookEntries.remove(p);
				}
				return;
			}//end if
		}//end while
	  }//end if found
	}//end for Price
	if(found == false)
		prodBook.checkTooLateToCancel(orderId);
}//end submitOrderCancel()

/**
 * Cancels the QuoteSide (if possible) that has the specified userName and creates a CancelMessage to be published
 * 
 * @param username User whose Quote is to be cancelled
 * @throws InvalidArgumentException 
 * @throws InvalidVolumeValueException if CancelMessage ctr is sent a negative volume
 * 
 * @see #removeQuote(String)
 */

public synchronized void submitQuoteCancel(String username) throws InvalidArgumentException, InvalidVolumeValueException
{
	if(username == null || username.isEmpty())
	{
		throw new InvalidArgumentException("ProductBookSide.submitQuoteCancel - username cannot be null or empty");
	}
	
	TradableDTO dto = removeQuote(username);
	if(dto != null)
	{
		CancelMessage cm = new CancelMessage(dto.user, dto.product, dto.price,  dto.remainingVolume, /*dto.cancelledVolume,*/ "Quote " + dto.side + "-Side Cancelled", dto.side, dto.id);
		MessagePublisher.getInstance().publishCancel(cm);
	}
	else
	{
		return;
	}
}//end submitQuoteCancel()

/**
 * Adds the Tradable passed in to the “parent” product book’s “old entries” list. 
 * @param t Tradable to be added to the “parent” product book’s “old entries” list.
 * @throws InvalidVolumeValueException 
 * @throws InvalidArgumentException if t is null
 * 
 * @see ProductBook#addOldEntry(Tradable)
 */

public void addOldEntry(Tradable t) throws InvalidVolumeValueException, InvalidArgumentException
{
	prodBook.addOldEntry(t);
}//end addOldEntry()


/**
 * Adds the Tradable passed in to the book 
 * @param trd  The Tradable to be added to the book
 * @throws InvalidArgumentException if trd is null
 */

public void addToBook(Tradable trd) throws InvalidArgumentException
{
	if(trd == null)
	{
		throw new InvalidArgumentException("ProductBookSide.addToBook - Tradable cannot be null");
	}
	
	if(bookEntries.containsKey(trd.getPrice()) == false)
	{
		ArrayList<Tradable> tList = new ArrayList<Tradable>();
		bookEntries.put(trd.getPrice(), tList);
	}
	
	bookEntries.get(trd.getPrice()).add(trd);
}

/**
 * Attempts a trade the provided Tradable against entries in this ProductBookSide
 * 
 * @param t Tradable to be traded against ProductBookSide entries
 * @return HashMap holding the fills
 * @throws InvalidVolumeValueException
 * @throws InvalidArgumentException if t is null
 * 
 * @see #tryBuyAgainstSellSideTrade(Tradable)
 * @see #trySellAgainstBuySideTrade(Tradable)
 */

public HashMap<String, FillMessage> tryTrade(Tradable t) throws InvalidVolumeValueException, InvalidArgumentException
{
	if(t == null)
	{
		throw new InvalidArgumentException("ProductBookSide.tryTrade - Tradable cannot be null");
	}
	
	HashMap<String, FillMessage> fills = new HashMap<String, FillMessage>();
	
	if(side == Side.BUY)
	{
		fills = trySellAgainstBuySideTrade(t);
	}
	else //if(side == BookSide.SELL)
	{
		fills = tryBuyAgainstSellSideTrade(t);
	}
	
	Set<String> values = fills.keySet();
	
	for(String s : values)
	{
		FillMessage fm = fills.get(s);
		MessagePublisher.getInstance().publishFill(fm);
	}
	return fills;
}//end tryTrade()

/**
 * Attempts to fill the SELL side Tradable passed in against the content of the book. 
 * @param trd The SELL side Tradable to fill
 * @return HashMap of FillMessages for the trade
 * @throws InvalidVolumeValueException    
 * @throws InvalidArgumentException if trd is null
 * 
 * @see TradeProcessor#doTrade(Tradable)
 * @see #mergeFills(HashMap, HashMap)
 */

public synchronized HashMap<String,FillMessage> trySellAgainstBuySideTrade(Tradable trd) throws InvalidVolumeValueException, InvalidArgumentException
{
	if(trd == null)
	{
		throw new InvalidArgumentException("ProductBookSide.trySellAgainstBuySideTrade - Tradable cannot be null");
	}
	
	HashMap<String, FillMessage> allFills = new HashMap<String, FillMessage>();
	HashMap<String, FillMessage> fillMsgs = new HashMap<String, FillMessage>();
	
	while( (trd.getRemainingVolume() > 0 && isEmpty() == false && trd.getPrice().lessOrEqual(topOfBookPrice())) || (trd.getRemainingVolume() > 0 && isEmpty() == false && trd.getPrice().isMarket()) )
	{
		HashMap<String, FillMessage> msgs = tradeProcessor.doTrade(trd);
		fillMsgs = mergeFills(fillMsgs, msgs);
	}
	allFills.putAll(fillMsgs);
	return allFills;
}//end trySellAgainstBuySideTrade()

/**
 * 
 * Merge multiple fill messages together into one consistent list.  If user receives partial fills, only one fill message should be send.
 * @param current Current HashMap of fills
 * @param newFills Fills to be merged with current fills
 * @return HashMap of FillMessages, merging all fills
 * @throws InvalidVolumeValueException
 * @throws InvalidArgumentException if current or newFills is null
 * 
 *  @see FillMessage#setVolume(int)
 *  @see FillMessage#setDetails(String)
 */

private HashMap<String, FillMessage> mergeFills(HashMap<String, FillMessage> current, HashMap<String, FillMessage> newFills) throws InvalidVolumeValueException, InvalidArgumentException
{
	if(current == null)
	{
		throw new InvalidArgumentException("ProductBookSide.mergeFills - current cannot be null");
	}
	
	if(newFills == null)
	{
		throw new InvalidArgumentException("ProductBookSide.mergeFills - newFils cannot be null");
	}
	
	if(current.isEmpty())
	{
		return new HashMap<String, FillMessage>(newFills);
	}

	else
	{
		HashMap<String, FillMessage> retHashMap = new HashMap<String, FillMessage>(current);

		for(String key : newFills.keySet())
		{
			if(!current.containsKey(key))
			{
				retHashMap.put(key, newFills.get(key));
			}
			else
			{
				FillMessage fm = retHashMap.get(key);
				fm.setVolume(newFills.get(key).getVolume());
				fm.setDetails(newFills.get(key).getDetails());
			}
		}
		return retHashMap;
	}
}//end mergeFills()


/**
 * Attempt to fill the BUY side Tradable passed in against the content of the book. 
 * @param trd The BUY side Tradable to fill
 * @return HashMap of FillMessages for the trade
 * @throws InvalidVolumeValueException 
 * @throws InvalidArgumentException
 * 
 * @see TradeProcessor#doTrade(Tradable)
 * @see #mergeFills(HashMap, HashMap)
 */

public synchronized HashMap<String, FillMessage> tryBuyAgainstSellSideTrade(Tradable trd) throws InvalidVolumeValueException, InvalidArgumentException
{
	if(trd == null)
	{
		throw new InvalidArgumentException("ProductBookSide.tryBuyAgainstSellSideTrade - Tradable cannot be null");
	}
	HashMap<String, FillMessage> allFills = new HashMap<String, FillMessage>();
	HashMap<String, FillMessage> fillMsgs = new HashMap<String, FillMessage>();
	
	while( (trd.getRemainingVolume() > 0 && isEmpty() == false && trd.getPrice().greaterOrEqual(topOfBookPrice())) || (trd.getRemainingVolume() > 0 && isEmpty() == false && trd.getPrice().isMarket()) )
	{
		HashMap<String, FillMessage> msgs = tradeProcessor.doTrade(trd);
		fillMsgs = mergeFills(fillMsgs, msgs);
	}
	allFills.putAll(fillMsgs);
	return allFills;
}//end tryBuyAgainstSellSide()

/**
 * Removes a key/value pair from the book (the “bookEntries” HashMap) if the ArrayList associated with the Price passed in is empty
 * @param p The Price associated with the ArrayList to be removed
 * @throws InvalidArgumentException if p is null
 */

public synchronized void clearIfEmpty(Price p) throws InvalidArgumentException
{
	if(p == null)
	{
		throw new InvalidArgumentException("ProductBookSide.clearIfEmtpy - price cannot be null");
	}
	ArrayList<Tradable> trds = bookEntries.get(p);
	if(trds.isEmpty()) 
	{
		bookEntries.remove(p);
	}
}//end clearIfEmpty()


/**
 * Remove the Tradable passed in from the book when it has been traded or cancelled.
 * @param t The Tradable to be removed from the book
 * @throws InvalidArgumentException if t is null
 */
public synchronized void removeTradable(Tradable t) throws InvalidArgumentException
{
	if(t == null)
	{
		throw new InvalidArgumentException("ProductBookSide.removeTradable - Tradable cannot be null");
	}
	ArrayList<Tradable> entries = bookEntries.get(t.getPrice());
	if(entries == null)
	{
		return;
	}

	boolean result = entries.remove(t);

	if(result == false)
	{
		return;
	}

	if(entries.size() == 0)
	{
		clearIfEmpty(t.getPrice());
	}
}//end removeTradable()

}//end ProductBookSide class