package client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import price.Price;
import price.PriceFactory;
import utils.InvalidArgumentException;
import utils.InvalidPriceOperation;
import tradable.Tradable.Side;

/**
 * This class is used to hold a user's profit and loss information, including how much they have spent buying stock, 
 * how much they gained or lost selling stock, and the value of the stock they currently own.
 * 
 * @author Urvi
 *
 */
public class Position 
{
	/**
	 * Public constructor to create Position object
	 */
	public Position() {}
	
	/**
	 * Stores the stock and the respective shares the user owns of that stock
	 */
	private HashMap<String, Integer> holdings = new HashMap<String, Integer>();
	
	/**
	 * Keeps a running balance between the money spent on stock purchases and money made
	 * on stock sales
	 */
	private Price accountCosts = PriceFactory.makeLimitPrice(0);
	
	/**
	 * Stores the current value of the stock a user owns.
	 */
	private HashMap<String, Price> lastSales = new HashMap<String, Price>();
	
	/**
	 * Updates holdings and account costs of a user when trading activity occurs
	 * @param product The stock symbol for which to update the position
	 * @param price The price of the the Tradable traded
	 * @param side The side of trade (BUY or SELL)
	 * @param volume The quantity that was traded
	 * @throws InvalidPriceOperation if a MKT Price is added or multiplied
	 * @throws InvalidArgumentException if side or price is null or if product is null or empty
	 */
	public void updatePosition(String product, Price price, Side side, int volume) throws InvalidPriceOperation, InvalidArgumentException
	{
		if(side == null)
		{
			throw new InvalidArgumentException("Position.updatePosition - side cannot be null");
		}
		
		if(price == null)
		{
			throw new InvalidArgumentException("Position.updatePosition - price cannot be null");
		}
		
		if(product == null || product.isEmpty())
		{
			throw new InvalidArgumentException("Position.updatePosition - product cannot be null or empty");
		}
		
		int adjVol;
		if(side == Side.BUY)
		{
			adjVol = volume;
		}
		else
		{
			adjVol = volume * -1;
		}

		if(holdings.containsKey(product) == false)
		{
			holdings.put(product, adjVol);
		}
		else
		{
			int currVol = holdings.get(product);
			currVol += adjVol;
			
			if(currVol == 0)
			{
				holdings.remove(product);
			}
			else
			{
				holdings.put(product, currVol);
			}
		}

		Price totalPrice = price.multiply(volume);
		
		if(side == Side.BUY)
		{
			accountCosts = accountCosts.subtract(totalPrice);
		}
		else
		{
			accountCosts = accountCosts.add(totalPrice);
		}
	}


	/**
	 * Adds the last sale of the specified stock to those the user owns
	 * @param product The stock symbol of the product for which to update the last sale
	 * @param price The last sale price of the stock
	 * @throws InvalidArgumentException if product is null or empty, or if Price is null
	 */
	public void updateLastSale(String product, Price price) throws InvalidArgumentException
	{
		if(price == null)
		{
			throw new InvalidArgumentException("Position.updateLastSale - price cannot be null");
		}
		
		if(product == null || product.isEmpty())
		{
			throw new InvalidArgumentException("Position.updateLastSale - product cannot be null or empty");
		}
		lastSales.put(product, price);
	}

	/**
	 * Returns the volume of the specified stock the user owns
	 * @param product The stock symbol for which to get volume information
	 * @return The volume of the stock
	 * @throws InvalidArgumentException if product is null or empty
	 */
	public int getStockPositionVolume(String product) throws InvalidArgumentException
	{
		if(product == null || product.isEmpty())
		{
			throw new InvalidArgumentException("Position.getStockPositionVolume - product cannot be null or empty");
		}
		
		if(holdings.containsKey(product) == false)
		{
			return 0;
		}
		else
		{
			return (holdings.get(product));		
		}
	}

	/**
	 * Returns list of stock user owns
	 * @return ArrayList of String objects containing the stock the user owns
	 */
	public ArrayList<String> getHoldings()
	{
		ArrayList<String> h = new ArrayList<String>(holdings.keySet());
		Collections.sort(h);
		return h;
	}
	
	
	/**
	 * Returns current value of the stock symbol passed in that user owns
	 * @param product The stock symbol for which to determine the position value
	 * @return Price object containing the value of the stock 
	 * @throws InvalidPriceOperation if Price is a market price
	 * @throws InvalidArgumentException if product is null or empty
	 */
	public Price getStockPositionValue(String product) throws InvalidPriceOperation, InvalidArgumentException
	{
		if(product == null || product.isEmpty())
		{
			throw new InvalidArgumentException("Position.getStockPositionValue - product cannot be null or empty");
		}
		
		if(holdings.containsKey(product) == false)
		{
			return PriceFactory.makeLimitPrice(0);
		}
		else
		{
			Price lastSalePrice = lastSales.get(product);
			if(lastSalePrice == null)
			{
				lastSalePrice = PriceFactory.makeLimitPrice(0);
			}
			int userVolume = holdings.get(product);
			Price posValue = lastSalePrice.multiply(userVolume);
			return posValue;
		}
	}

	/**
	 * Returns the account costs of the user
	 * @return  Returns the account costs of the user
	 */
	public Price getAccountCosts()
	{
		return accountCosts;
	}
	
	/**
	 * Returns the total current value of all stocks the user owns
	 * @return Price object containing the value of all stocks the user owns
	 * @throws InvalidPriceOperation if Price is a market price
	 * @throws InvalidArgumentException 
	 * @see  #getStockPositionValue(String)
	 */

	Price getAllStockValue() throws InvalidPriceOperation, InvalidArgumentException
	{
		Set<String> stockHoldings = holdings.keySet();
		Price totalPrice = PriceFactory.makeLimitPrice(0);
		
		for(String stock : stockHoldings)
		{
			Price p = getStockPositionValue(stock);
			totalPrice = p.add(totalPrice);
		}
		return totalPrice;
	}

	/**
	 * Returns the total current value of all stocks the user owns, plus account costs
	 * @return Total current value of all stocks the user owns, plus account costs
	 * @throws InvalidPriceOperation
	 * @throws InvalidArgumentException
	 * @see #getAllStockValue()
	 */
	public Price getNetAccountValue() throws InvalidPriceOperation, InvalidArgumentException
	{
		return getAllStockValue().add(accountCosts);	
	}
}
