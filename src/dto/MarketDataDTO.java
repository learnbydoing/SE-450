package dto;

import price.Price;

/**
 * This class is used to contain data elements that capture the values that make up the current market.
 * This will be used throughout the trading system so a DTO is used to facilitate more efficient data
 * transfer between objects.
 * 
 * @author Urvi
 *
 */
public class MarketDataDTO 
{
	/**
	 * The stock symbol.
	 */
	public String product;
	
	/**
	 * The current price at which the stock can be bought.
	 */
	
	public Price buyPrice;
	
	/**
	 * The current quantity of stock that is available to be bought.
	 * 
	 */
	
	public int buyVolume;
	
	/**
	 * 
	 * The current price at which the stock can be sold.
	 */
	public Price sellPrice;
	
	/**
	 * The current quantity of stock that is available to be sold.
	 */
	public int sellVolume;
	
	/**
	 * Creates a MarketDataDTO object, using elements that make up the current market.
	 * 
	 * @param sym The stock symbol.
	 * @param bPrice The current buy side price of the stock.
	 * @param bVol The current buy side volume of the stock.
	 * @param sPrice The current sell side price of the stock.
	 * @param sVol The current sell side volume of the stock.
	 */
	
	public MarketDataDTO(String sym, Price bPrice, int bVol, Price sPrice, int sVol)
	{
		product = sym;
		buyPrice = bPrice;
		buyVolume = bVol;
		sellPrice = sPrice;
		sellVolume = sVol;
	}
	
	/**
	 *  Provides the MarketDataDTO class with a user friendly string representation of a MarketDataDTO object.
	 */
	
	public String toString()
	{
		return "Product: " + product + " BuyPrice: " + buyPrice + " BuyVolume: " + 
				buyVolume + " SellPrice: " + sellPrice + " SellVolume: " + sellVolume;
	}
}