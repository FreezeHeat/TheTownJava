/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Code;

import java.io.Serializable;

/**
 *  This {@code ENUM} class holds all the constants used for roles in the game,
 * this is used by the server and client to identify and set roles in the game
 * @author Ben Gilad and Asaf Yeshayahus
 * @version %I%
 * @since 1.0
 */
public enum Roles implements Serializable{

    /**
     * Specifies that a user has the role of a CITIZEN
     */
    CITIZEN,

    /**
     * Specifies that a user has the role of a KILLER
     */
    KILLER,

    /**
     * Specifies that a user has the role of a HEALER
     */
    HEALER,

    /**
     * Specifies that a user has the role of a SNITCH
     */
    SNITCH,

    /**
     * Specifies that a user is DEAD
     */
    DEAD,

    /**
     * Specifies that the user is in the voting phase
     */
    VOTE;
}
