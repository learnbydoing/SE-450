package book;

import java.util.ArrayList;
import java.util.HashMap;

import price.Price;
import publishers.FillMessage;
import tradable.Tradable;
import utils.InvalidArgumentException;
import utils.InvalidVolumeValueException;

/**
 * This Tradeprocessor implementer contains the functionality  needed to “execute” actual trades between 
 * Tradable objects in this book side using a price-time algorithm
 * 
 * @author Urvi
 *
 */
public class TradeProcessorPriceTimeImpl implements TradeProcessor
{
	/**
	 * Hold fill messages generated as a result of a trade
	 */
	private HashMap<String, FillMessage> fillMessages = new HashMap<String, FillMessage>();
	
	/**
	 *  ProductBookSide that this object that TradeProcessor object belongs to
	 */
	ProductBookSide bookSide;
	
	/**
	 * Public constructor for the TradeProcessorPriceTimeImpl class
	 * @param prodBookSide ProductBookSide that this object belong to
	 * @throws InvalidArgumentException if prodBookSide is null
	 */
	public TradeProcessorPriceTimeImpl(ProductBookSide prodBookSide) throws InvalidArgumentException
	{
		setBookSide(prodBookSide);
	}
	
	/**
	 * Creates a FillKey from the username, id, and Price of the FillMessage
	 * @param fm The FillMessage from which to generate a fill key
	 * @return A String holding the fill key that was created
	 * @throws InvalidArgumentException if fm is null
	 */
	private String makeFillKey(FillMessage fm) throws InvalidArgumentException
	{
		if(fm == null)
		{
			throw new InvalidArgumentException("TradeProcessorPriceTimeImpl.makeFillKey - FillMessage cannot be null");
		}
		
		String key = fm.getUser() + fm.getId() + fm.getPrice().toString();
		return key;
	}
	
	/**
	 * Checks to see if the FillMessage passed in is a fill message for an existing known trade or 
	 * if it is for a new previously unrecorded trade. 
	 * 
	 * @param fm The FillMessage to determine if existing or new
	 * @return True if it's a FillMessage for a new trade, false if it's a FillMessage for a old trade
	 * @throws InvalidArgumentException if fm is null
	 */
	private boolean isNewFill(FillMessage fm) throws InvalidArgumentException
	{	
		String key = makeFillKey(fm);
		
		if(fillMessages.containsKey(key) == false)
		{
			return true;  //New trade
		}
		
		FillMessage oldFill = fillMessages.get(key);
		
		if(oldFill.getSide() != fm.getSide())  //cond #1  Couldn't you OR #1 and #2??
		{
			return true; //New trade
		}
		
		if(oldFill.getId().equals(fm.getId()) == false) //cond #2
		{
			return true; //New trade
		}
		return false;
	}
	
	
	/**
	 * Adds a FillMessage to FillMessges received so far, if it is a new trade, or updates an existing fill message
	 * @param fm FillMessage to add
	 * @throws InvalidArgumentException
	 * @throws InvalidVolumeValueException 
	 * 
	 * @see FillMessage#setDetails(String)
	 * @see FillMessage#setVolume(int)
	 */
	private void addFillMessage(FillMessage fm) throws InvalidArgumentException, InvalidVolumeValueException
	{
		String key = makeFillKey(fm);;
		
		if(isNewFill(fm) == true)
		{
			fillMessages.put(key, fm);
		}
		else
		{
			FillMessage fillMsg = fillMessages.get(key);
			fillMsg.setVolume(fillMsg.getVolume() + fm.getVolume());
			fillMsg.setDetails(fm.getDetails());
		}
	}
	
/**
 *  Called when it has been determined that a Tradable (i.e., a Buy Order, a Sell QuoteSide, etc.) can trade against the content of the book.
 *  @param trd Tradable to be traded
 *  @return String trade identifiers, and their respective Fill Message objects
 *  @throws InvalidArgumentException if trd is null
 *  @see Tradable#setRemainingVolume(int)
 *  @see  #addFillMessage(FillMessage)
 */
	
	public HashMap<String, FillMessage> doTrade(Tradable trd) throws InvalidArgumentException, InvalidVolumeValueException 
	{
		
		if(trd == null)
		{
			throw new InvalidArgumentException("TradeProcessorPriceTimeImpl.doTrade - Tradable cannot be null");
		}
		
		fillMessages =  new HashMap<String, FillMessage>();
		ArrayList<Tradable> tradedOut = new ArrayList<Tradable>();
		
		ArrayList<Tradable> entriesAtPrice = bookSide.getProductBook().getEntriesAtTopOfBook(trd);
		
		for(Tradable t : entriesAtPrice)
		{
			if(trd.getRemainingVolume() == 0) //Yes path
			{
				break;
			}//end if remVol == 0
			else
			{
				Price tPrice;
				if(trd.getRemainingVolume() >= t.getRemainingVolume())
				{
					tradedOut.add(t);
					if(t.getPrice().isMarket() == true)
					{
						 tPrice = trd.getPrice();
					}
					else
					{
						 tPrice = t.getPrice();
					}					
					FillMessage fmT = new FillMessage(t.getUser(), t.getProduct(), tPrice, t.getRemainingVolume(), "leaving 0", t.getSide(), t.getId());
					addFillMessage(fmT);
					String details = "leaving " + (trd.getRemainingVolume() - t.getRemainingVolume());
					FillMessage fmTrd = new FillMessage(trd.getUser(), trd.getProduct(), tPrice, t.getRemainingVolume(), details, trd.getSide(), trd.getId());
					addFillMessage(fmTrd);
					trd.setRemainingVolume(trd.getRemainingVolume() - t.getRemainingVolume());
					t.setRemainingVolume(0);
					bookSide.getProductBook().addOldEntry(t);
				}
				else
				{
					int remainder = t.getRemainingVolume() - trd.getRemainingVolume();
					if(t.getPrice().isMarket() == true)
					{
						tPrice = trd.getPrice();
					}
					else
					{
						tPrice = t.getPrice();
					}
						FillMessage fmT1 = new FillMessage(t.getUser(), t.getProduct(), tPrice, trd.getRemainingVolume(), "leaving " + remainder, t.getSide(), t.getId());
						addFillMessage(fmT1);
						FillMessage fmTrd1 = new FillMessage(trd.getUser(), trd.getProduct(), tPrice, trd.getRemainingVolume(), "leaving 0", trd.getSide(), trd.getId());
						addFillMessage(fmTrd1);
						trd.setRemainingVolume(0);
						t.setRemainingVolume(remainder);
						bookSide.getProductBook().addOldEntry(trd);
						break;
					
				}
			}
		}//end foreach entriesAtPrice
		for(Tradable tradeOut : tradedOut)
		{
			entriesAtPrice.remove(tradeOut);
			if(entriesAtPrice.isEmpty() == true)
			{
				bookSide.getProductBook().clearIfEmpty(trd);
				return fillMessages;
			}
		}//end foreach tradedOut
		return fillMessages;
	}
	
	/**
	 * Set ProductBookSide object to the value passed in
	 * @param pbs ProductBookSide passed in
	 * @throws InvalidArgumentException if pbs is null
	 */
	private void setBookSide(ProductBookSide pbs) throws InvalidArgumentException
	{
		if(pbs == null)
		{
			throw new InvalidArgumentException("TradeProcessorImpl.setBookSide - ProductBookSide cannot be null");
		}
		else
		{
			bookSide = pbs;
		}
	}
}
