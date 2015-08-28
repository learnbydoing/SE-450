// Urvi Patel
// SE 450 - Phase 1 - PriceFactory.java
// Due date:  9/24/2012


package price;

import java.util.HashMap;

/**
 * This class represents the globally accessible factory that will be used to create Price
 * object, and ensures that only Price objects with unique prices are created.
 * 
 * @author Urvi
 *
 */
public class PriceFactory 
{
	/**
	 * A HashMap to keep track of Price objects that have been created by the factory.
	 */
	static HashMap<String, Price> prices = new HashMap<String, Price>();
	
	/**
	 * Creates a limit Price object from a String value.  
	 * @param value The price for the Price object, represented as string value.
	 * @return A "limit price" Price object.
	 */
	public static Price makeLimitPrice(String value)
	{
		long p = stringToLong(value);
		String currString = longToCurrencyString(p);
		
		if(prices.containsKey(currString) == true && prices.get(currString).isMarket() == false)  // Vfy price objects are equal
		{
			return(prices.get(currString));
		}
		else
		{
			Price price = new Price(p);
			prices.put(currString, price);
			return price;
		}
	}
	
	
	/**
	 * Creates a limit price from a long value.
	 * @param value The price for the Price object, represented as a long integer.
	 * @return A "limit price" Price object.
	 */
	
	public static Price makeLimitPrice(long value)
	{
		String currString = longToCurrencyString(value);
		
		if(prices.containsKey(currString) == true && prices.get(currString).isMarket() == false)  // Vfy price objects are equal
		{
			return(prices.get(currString));
		}
		else
		{
			Price price = new Price(value);
			prices.put(currString, price);
			return price;
		}
	}
	
	/**
	 * Create a market price object.  No values are passed in since this is to create a "market price" Price object.
	 * @return A "market price" Price object.
	 */
	public  static Price makeMarketPrice()
	{
		String currString = "MKT";
		
		if(prices.containsKey(currString) == true && prices.get(currString).isMarket() == true)
		{
			return(prices.get("MKT"));
		}
		else
		{
			Price price = new Price();
			prices.put(currString, price);
			return price;
		}
	}
	
	/**
	 * Converts a long to a String representing traditional US currency format. For example, 1234 --> $12.34
	 * @param num The value to convert to currency format.
	 * @return A String in traditional US currency format.
	 */
	
	public static String longToCurrencyString(long num)
	{
			double d = (double) num / 100.0;
			String resultString = String.format("$%,.2f", d);
			return resultString;
	}

	/**
	 * Converts a String to a long value by performing various String manipulations on the input string.
	 * @param s The String to be converted
	 * @return A long integer representation of the string passed in.
	 */
	
	public static long stringToLong(String s)
    {
        boolean isNeg = false;
        long dollar = 0;
        long cents = 0;
        long price = 0;

        String[] components;

        if(s.indexOf('$') != -1)  //Find and remove all dollar signs.
        {
            s = s.replace("$", "");
        }

        if(s.indexOf(',') != -1) //Find and remove all decimals.
        {
        	s = s.replace(",", "");
        }

        if(s.indexOf('-') != -1) //Find and remove negative sign, but remember if number is negative.
        {
        	s = s.replace("-", "");
            isNeg = true;
        }

        if(s.indexOf('.') != -1)  //Split input string at the decimal point.
        {
            components = s.split("\\.");

            if(components[0].isEmpty())
            {
                dollar = Long.parseLong("0"); //The first half is the dollar amount.  If it's empty, price is fraction of a dollar.
            }
            else
            {
                dollar = Long.parseLong(components[0]) * 100;  //If not empty, multiply it by 100 to get the number of cents in dollar amount. 
            }
            if(isNeg)
            {
                dollar = dollar * -1; //If there was a negative sign, multiply the dollar amount by -1.
            }

            if(components.length != 1 )
            {
                if(components[1].length() == 1)
                {
                	components[1] = components[1].concat("0");  //If there was no cents denoted, put 0 for placeholder.
                }
                if(components[1].length() > 2)  //Truncate to 2 decimal places if needed.
                {
                	s.substring(0, 2);

                }
                cents = Long.parseLong(components[1]); //Extract the cents value
            }
             
                price = isNeg ? (dollar - cents) : (dollar + cents);  // If number was neg, subtract cents, else add cents.
        }
        else
        {
            price = isNeg ? Long.parseLong(s) * -100 : Long.parseLong(s) * 100; //If there were no decimals, simply multipy by -100 or 100.
        }
        return price;
    } //end String to Long

	/**
	 * Private constructor since all methods are static
	 */
	
	private PriceFactory() {}
}
