package Code;

import static Code.ClientConnection.client;
import Interface.ISnitch;
import java.io.IOException;
import java.io.Serializable;

/**
 * {@code Snitch} class is a role within the game
 * <p>The snitch has the ability to know the identity of another player
 * he can then snitch on them through the chat and try and convince others
 * to hang someone!</p>
 * @author Ben Gilad and Asaf Yeshayahu
 * @version %I%
 * @since 1.0
 */
public class Snitch extends User implements ISnitch, Serializable{
    
    /**
     * Snitch constructor, convert a User to a Snitch
     * @param a The user to be converted
     */
    public Snitch(User a){
        this.setUsername(a.getUsername());
        this.setPassword(a.getPassword());
        this.setHeales(a.getHeales());
        this.setKills(a.getKills());
        this.setLost(a.getLost());
        this.setWon(a.getWon());
    }
    
    /**
     * Snitch on a User (Snitch option)
     * @param user The user to snitch on
     */
    public void snitch(User user){
        try {
            client.out.writeObject(Commands.DETECT);
            client.out.writeObject(user);
            client.out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
