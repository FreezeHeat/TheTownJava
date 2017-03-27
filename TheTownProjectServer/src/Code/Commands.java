/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Code;

import java.io.Serializable;

/**
 *
 * includes commands that are required for the server/client output/input
 * actions.
 * @author Ben Gilad and Asaf Yeshayahus
 * @version %I%
 * @since 1.0
 */
public enum Commands implements Serializable{
    /**
     * Used with the server thread 
     * <p>Specifies the user wishes to login</p>
     */
    LOGIN,
    
     /**
     * Used with the server thread 
     * <p>Specifies the user entered wrong details in the login process</p>
     */
    WRONG_DETAILS,
    
     /**
     * Used with the server thread 
     * <p>Specifies the user wishes to register</p>
     */
    REGISTER,
    
     /**
     * Used with the server thread 
     * <p>Specifies the user wishes to disconnect from the game / lobby</p>
     */
    DISCONNECT,
    
     /**
     * Used with the server thread 
     * <p>Specifies that the user entered in login process is already
       connected</p>
     */
    ALREADY_CONNECTED,
    
     /**
     * Used with the server thread 
     * <p>Specifies that the user entered in register process already exists</p>
     */
    ALREADY_EXISTS,
    
     /**
     * Used with the server thread 
     * <p>Specifies that there was an error in the connection to the server</p>
     */
    CONNECTION_ERROR,
    
     /**
     * Used with the server thread 
     * <p>Specifies that some kind of action (Login, Register etc..) was fine</p>
     */
    OK,
    
     /**
     * Used with the server thread 
     * <p>Specifies to the server to do nothing</p>
     */
    IDLE,
    
     /**
     * Used with the server thread 
     * <p>Specifies the user wishes to send a message through chat</p>
     */
    SEND_MESSAGE,
    
     /**
     * Used with the server thread 
     * <p>Specifies the user wishes to send a message through chat to killers
       only </p>
     */
    SEND_MESSAGE_KILLER,
    
     /**
     * Used with the server thread 
     * <p>Specifies the user to send a message to all the dead people in chat
       in case he is dead too (dead chat)</p>
     */
    SEND_MESSAGE_DEAD,
    
     /**
     * Used with the server thread 
     * <p>Specifies that a message from the server itself is sent</p>
     */
    SERVER_MESSAGE,
    
    /**
     * Used with the server thread 
     * <p>Checks if the queue to join the game is available</p>
     */
    CHECK_GAME,
    
     /**
     * Used with the server thread 
     * <p>Specifies the user is ready for a game an is in game queue</p>
     */
    READY,
    
     /**
     * Used with the server thread 
     * <p>Specifies that the server send the updates player list</p>
     */
    REFRESH_PLAYERS,
    
     /**
     * Used with the server thread 
     * <p>Specifies that the game has begun</p>
     */
    BEGIN,
    
     /**
     * Used with the server thread 
     * <p>Specifies the switch to the night phase in-game</p>
     */
    NIGHT,
    
     /**
     * Used with the server thread 
     * <p>Specifies the switch to the day phase in-game</p>
     */
    DAY,
    
     /**
     * Used with the server thread 
     * <p>Specifies that the game has ended</p>
     */
    END,
    
     /**
     * Used with the server thread 
     * <p>Specifies the switch to the voting phase in-game</p>
     */
    VOTE,
    
     /**
     * Used with the server thread 
     * <p>Specifies the the game ended in a draw</p>
     */
    DRAW,
    
     /**
     * Used with the server thread 
     * <p>Specifies that a user is to be executed after voting phase</p>
     */
    EXECUTE,
    
     /**
     * Used with the server thread 
     * <p>Specifies that a user is to be killed</p>
     */
    KILL,
    
     /**
     * Used with the server thread 
     * <p>Specifies that a user is to be healed</p>
     */
    HEAL,
    
     /**
     * Used with the server thread 
     * <p>Specifies that a user's identity will be revealed by the 
       {@code Snitch}</p>
     */
    DETECT,
    
    /**
     * Used with the server thread 
     * <p>Asks the server for top 10 list </p>
     */
    TOP10;
}
