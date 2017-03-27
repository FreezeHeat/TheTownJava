package Code;

import static Code.ClientConnection.client;
import Interface.IHealer;
import java.io.IOException;
import java.io.Serializable;

/**
 * {@code Healer} class a role within the game
 * <p>The healer heals other users and keeps them from dying</p>
 * @author Ben Gilad and Asaf Yeshayahu
 * @version %I%
 * @since 1.0
 */
public class Healer extends User implements IHealer, Serializable{
    
    /**
     * Constructor for Healer, convert a User to Healer
     * @param a The user to be converted
     */
    public Healer(User a){
        this.setUsername(a.getUsername());
        this.setPassword(a.getPassword());
        this.setHeales(a.getHeales());
        this.setKills(a.getKills());
        this.setLost(a.getLost());
        this.setWon(a.getWon());
    }
    
   /**
    * Heal the target specified (Healer option)
    * @param user The user to be healed
    */
    public void heal(User user){
        try {
            client.out.writeObject(Commands.HEAL);
            client.out.writeObject(user);
            client.out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
