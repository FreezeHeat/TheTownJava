package Code;

import java.util.ArrayList;

/**
 * The database of each game instance
 */
public class GameDB {
    
    /**
     * States the current cycle:
     * <ul>
     * <li>true = day</li>
     * <li>false = night</li>
     * </ul>
     * @author Ben Gilad and Asaf Yeshayahu
     * @version %I%
     * @since 1.0
     */
    private boolean dayCycle = true; // true = day, false = night;
    
    /**
     * Holds the names of all current players in the game
     */
    private ArrayList<String> players = new ArrayList<String>();
    
    /**
     * Holds all the votes against other players
     * <p>In the game there's a phase where you vote against other players</p>
     */
    private ArrayList<String> votes = new ArrayList<String>();
    
}
