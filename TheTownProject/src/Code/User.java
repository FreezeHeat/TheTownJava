package Code;

import Interface.UserMethods;
import java.io.Serializable;
import java.util.ArrayList;
import static Code.ClientConnection.client;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * {@code User} class, holds the information about each user in the database
 * @author Ben Gilad and Asaf Yeshayahu
 * @version %I%
 * @since 1.0
 */
public class User implements Serializable, UserMethods {

    /**
     * The user's username
     * <p><b>Note:</b> PK in the database, so it's unique</p>
     */
    private String username;
    
    /**
     * The user's password
     */
    private String password;
    
    /**
     * <i>Statistics</i>, how many games the user has won
     */
    private long won;
    
     /**
     * <i>Statistics</i>, how many games the user has lost
     */
    private long lost;
    
     /**
     * <i>Statistics</i>, how many players the user has killed
     */
    private long kills;
    
     /**
     * <i>Statistics</i>, how many players the user has healed
     */
    private long heales;
    
     /**
     * States if the user is alive or dead
     * <ul>
     * <li> true = player's alive</li>
     * <li> false = players' dead</li>
     * </ul>
     */
    private boolean isAlive;

    /**
     * This constructor creates an empty <code>AbstractUser</code>
     */
    public User() {
    }

    /**
     * This constructor is used to initialize an <code>AbsractUser</code>
     *
     * @param username The username
     * @param password The username's password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.won = 0;
        this.lost = 0;
        this.kills = 0;
        this.heales = 0;
        this.isAlive = true;
    }

    /**
     * Gets the username
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username
     *
     * @param username Username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password
     *
     * @return Password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password
     *
     * @param password Password to be set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets how many games the user has won
     *
     * @return Games won
     */
    public long getWon() {
        return won;
    }

    /**
     * Sets how many games were won by the user
     *
     * @param won Games won
     */
    public void setWon(long won) {
        this.won = won;
    }

    /**
     * Gets how many games the user lost
     *
     * @return Games lost
     */
    public long getLost() {
        return lost;
    }

    /**
     * Sets how many games the user lost
     *
     * @param lost Games lost
     */
    public void setLost(long lost) {
        this.lost = lost;
    }

    /**
     * Gets Kills done by the user
     *
     * @return Kills done
     */
    public long getKills() {
        return kills;
    }

    /**
     * Sets Kills done by the user
     *
     * @param kills Kills done
     */
    public void setKills(long kills) {
        this.kills = kills;
    }

    /**
     * Gets heals done by the user
     *
     * @return Heals done
     */
    public long getHeales() {
        return heales;
    }

    /**
     * Sets heals done by the user
     *
     * @param heales Heals done
     */
    public void setHeales(long heales) {
        this.heales = heales;
    }

    /**
     * Check if the user is alive in the game
     *
     * @return status
     */
    public boolean IsAlive() {
        return isAlive;
    }

    /**
     * Set if user is alive or not
     *
     * @param isAlive status
     */
    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    /**
     * Vote against another user
     *
     * @param user user to vote against
     */
    public void vote(User user) {
        try {
            client.out.writeObject(Commands.VOTE);
            client.out.writeObject(user);
            client.out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Send a message to the chat
     *
     * @param msg the message
     */
    public void sendMessage(String msg) {
        try {
            client.out.writeObject(Commands.SEND_MESSAGE);
            client.out.writeObject(getUsername() + ": " +msg);
            client.out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Send a message to all the dead people in the chat
     * @param msg the message
     */
    public void sendMessageDead(String msg){
        try {
            client.out.writeObject(Commands.SEND_MESSAGE_DEAD);
            client.out.writeObject(getUsername() + ": " +msg);
            client.out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Join a game
     */
    public void joinGame() {
        try {
            client.out.writeObject(Commands.READY);
            client.out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Logout of the game
     */
    public void logout() {
        try {
            client.out.writeObject(Commands.DISCONNECT);
            client.out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Checks equality by comparing username AND password
     *
     * @param o User object
     * @return <code>True/False</code> Equal/Different
     */
    @Override
    public boolean equals(Object o) {
        User au = (User) o;

        if (au.username.equals(this.username)
                && au.password.equals(this.password)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * A second form of <code>toString()</code> without colors
     * @return The formatted string
     */
    public String toStringTwo(){
        
       return 
                this.getUsername() + "\n"
                +"Won: "+ " "+ this.getWon()+"\n"
                +"Lost: "+ " "+ this.getLost()+"\n" 
                +"Kills: "+ " "+ this.getKills() +"\n" 
                +"Heals: "+ " "+ this.getHeales()+"\n\n" ;
    }
    
    /**
     * A color coded version (using html) of <code>toString()</code>
     * @return The color formatted string
     */
    @Override
    public String toString(){
        
        
        return "<html><p style=background-color:white><b><font size=15>"
                +"Won: "+ " "+ this.getWon()+ "<br>"
                +"Lost: "+ " "+ this.getLost() + "<br>"
                +"Kills: "+ " "+ this.getKills() + "<br>"
                +"Heals: "+ " "+ this.getHeales() +"</font></b></p></html>";
              
    }
}
