/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import client.User;
import price.Price;
import utils.UserNotConnectedException;

/**
 * A facade to the Market Display
 * @author Urvi
 *
 */
public class UserDisplayManager {

	/**
	 * The User connected to the trading system
	 */
    private User user;
    
    /**
     * Used to call appropriate methods in MarketDisplay
     */
    private MarketDisplay marketDisplay;

    /**
     * Public constructor, creates a UserDisplayManager object
     * @param u The User connected to the trading system
     */
    public UserDisplayManager(User u) {
        user = u;
        marketDisplay = new MarketDisplay(u, this);
    }

    /**
     * @see MarketDisplay#setVisible(boolean)
     */
    public void showMarketDisplay() throws UserNotConnectedException {
        marketDisplay.setVisible(true);
    }

    /**
     * @see MarketDisplay#updateMarketData(String, Price, int, Price, int)
     */
    public void updateMarketData(String product, Price bp, int bv, Price sp, int sv) {
        marketDisplay.updateMarketData(product, bp, bv, sp, sv);
    }
    
    /**
     * @see MarketDisplay#updateLastSale(String, Price, int)
     */
    public void updateLastSale(String product, Price p, int v) {
        marketDisplay.updateLastSale(product, p, v);
    }
    
    /**
     * @see MarketDisplay#updateTicker(String, Price, char)
     */
    public void updateTicker(String product, Price p, char direction) {
        marketDisplay.updateTicker(product, p, direction);
    }
    
    /**
     * @see MarketDisplay#updateMarketActivity(String)
     */
    public void updateMarketActivity(String activityText) {
        marketDisplay.updateMarketActivity(activityText);
    }
    
    /**
     * @see MarketDisplay#updateMarketState(String)
     */
    public void updateMarketState(String message) {
        marketDisplay.updateMarketState(message);
    }
}
