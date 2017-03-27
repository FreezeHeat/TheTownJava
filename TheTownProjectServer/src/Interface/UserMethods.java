package Interface;

import Code.User;
import java.io.IOException;

/**
 * <code>User</code> 's interface
 * @author Ben Gilad and Asaf Yeshayahu
 * @version %I%
 * @see Code.User
 * @since 1.0
 */
public interface UserMethods {

    /**
     * Vote against another user
     * @param user user to vote against
     */
    public void vote(User user);

    /**
     * Send a message to the chat
     * @param msg the message
     */
    public void sendMessage(String msg);

    /**
     * Join a game
     */
    public void joinGame();

    /**
     * Logout of the game
     */
    public void logout();
}
