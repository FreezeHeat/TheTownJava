/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Code.User;

/**
 *
 * <code>Killer</code> interface
 * @author Ben Gilad and Asaf Yeshayahu
 * @version %I%
 * @see Code.Killer
 * @since 1.0
 */
public interface IKiller{
    
    /**
     * Kill a specified User
     * @param user The user to be killed
     */
    public void kill(User user);
    
    /**
     * Send a message to other killers only
     * @param msg The message to be sent
     */
    public void sendMessageKiller(String msg);
}
