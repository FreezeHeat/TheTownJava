/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Code.User;

/**
 *
 * {@code Snitch} interface
 * @author Ben Gilad and Asaf Yeshayahu
 * @version %I%
 * @see Code.Snitch
 * @since 1.0
 */
public interface ISnitch{
    
    /**
     * Used to snitch on a user
     * @param user The user to snitch on
     */
    public void snitch(User user);
}
