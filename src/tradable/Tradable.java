// Urvi Patel
// Phase 2 - Tradable.java
// Due Date: 10/1/2012

package tradable;

import price.Price;
import utils.InvalidVolumeValueException;

/**
 * This class is a generic type for entities that represent a BUY or SELL request that
 * can be traded.
 * 
 * @author Urvi
 *
 */

public interface Tradable
{
	/**
	 * This is used to indicate whether is Tradable is a BUY or a SELL.
	 * 
	 * @author Urvi
	 *
	 */
    enum Side { BUY, SELL };
    
    /**
     * Returns the product symbol associated with the Tradable.
     * @return The product symbol associated with the Tradable.
     */
    String getProduct();
    
    /**
     * Returns the price of the Tradable.
     * @return The price of the Tradable.
     */
    Price getPrice();
    
    /** 
     * Returns the original quantity of the Tradable.
     * @return the original quantity of the Tradable.
     */
    int getOriginalVolume();
    
    /**
     * Returns the remaining quantity of the Tradable.
     * @return The remaining quantity of the Tradable.
     */
    
    int getRemainingVolume();
    
    /**
     * Returns the cancelled quantity of the Tradable.
     * 
     * @return The cancelled quantity of the Tradable.
     */
    int getCancelledVolume();
    
    /**
     * Sets the Tradable's cancelled quantity to the value passed in.
     * @param cancVol The value to set the cancelled quantity to. Cannot be negative or greater than the OriginalVolume.
     * @throws InvalidVolumeValueException if parameter cancVol is negative or greater than the OriginalVolume.
     */
    
    void setCancelledVolume(int cancVol) throws InvalidVolumeValueException;
    
    /**
     * Sets the Tradable's remaining quantity to the value passed in.
     * @param remVol Value to set Tradable's remaining quantity to. Cannot be negative or greater than the OriginalVolume.
     * @throws InvalidVolumeValueException if parameter remVol is negative or greater than the OriginalVolume.
     */
    void setRemainingVolume(int remVol) throws InvalidVolumeValueException;
    
    /**
     * Returns user associated with the Tradable.
     * @return The user name of the Tradable.
     */
    String getUser();
    
    /**
     * Return the side (BUY or SELL) of the Tradable.
     * @return the side (BUY or SELL) of the Tradable.
     */
    Side getSide();
    
    /**
     * Determines if Tradable is part of a Quote.
     * @return True if Tradable is part of a Quote, false otherwise.
     */
    boolean isQuote();
    
    /**
     * Returns ATradable's unique id.
     * @return A Tradable's unique id.
     */
    String getId();
}

