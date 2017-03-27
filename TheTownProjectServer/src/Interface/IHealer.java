/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Code.User;

/**
 *
 * {@code Healer} interface
 * @author Ben Gilad and Asaf Yeshayahu
 * @version %I%
 * @see Code.Healer
 * @since 1.0
 */
public interface IHealer{
    
    /**
     * Used to heal another user
     * @param user The user to be healed
     */
    public void heal(User user); // String or void should decide later
}
