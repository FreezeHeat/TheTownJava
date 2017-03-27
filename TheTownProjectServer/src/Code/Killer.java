package Code;

import static Code.ClientConnection.client;
import Interface.IKiller;
import java.io.IOException;
import java.io.Serializable;

/**
 * {@code Killer} class is a role within the game
 * <p>The killer need to kill every other player (Even other killers if he wants)
 * until no more citizens are left or he's the last one standing</p>
 * @author Ben Gilad and Asaf Yeshayahu
 * @version %I%
 * @since 1.0
 */
public class Killer extends User implements IKiller, Serializable{
    
    /**
     * Constructor for killer, convert a User to Killer
     * @param a The user to be converted
     */
    public Killer(User a){
        this.setUsername(a.getUsername());
        this.setPassword(a.getPassword());
        this.setHeales(a.getHeales());
        this.setKills(a.getKills());
        this.setLost(a.getLost());
        this.setWon(a.getWon());
    }
    
    /**
     * Kill a specified user (Killer option)
     * @param user The user to be killed
     */
    public void kill(User user){
        try {
            client.out.writeObject(Commands.KILL);
            client.out.writeObject(user);
            client.out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Send chat message only to other killers (Killer chat) for organized crime
     * @param msg The message to be sent
     */
    public void sendMessageKiller(String msg){
        try {
            client.out.writeObject(Commands.SEND_MESSAGE_KILLER);
            client.out.writeObject(getUsername() + ": " +msg);
            client.out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
