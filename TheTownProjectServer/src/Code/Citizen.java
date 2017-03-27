package Code;

import java.io.Serializable;

/**
 * {@code Citizen} is a role within the game
 * <p>The citizen's goal is to stay alive and find the killers</p>
 * @author Ben Gilad and Asaf Yeshayahu
 * @version %I%
 * @since 1.0
 */
public class Citizen extends User implements Serializable{
    
    /**
     * Constructor for Citizen, Given a User will create a citizen (Casting)
     * @param a The user to be converted
     */
    public Citizen(User a){
        this.setUsername(a.getUsername());
        this.setPassword(a.getPassword());
        this.setHeales(a.getHeales());
        this.setKills(a.getKills());
        this.setLost(a.getLost());
        this.setWon(a.getWon());
    }
}
