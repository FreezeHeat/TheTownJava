/*
 * This work is not in the public domain. 
 * This work is copyright © Ben Gilad & Asaf Yeshayahu 2015-2016
 * All rights reserved.
 */

package Code;

import GUI.FormGame;
import GUI.FormLogin;
import audio.SoundJLayer;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.swing.JFrame;

/**
 * Main Class of <h2>TheTownProject</h2>
 * @author Ben Gilad and Asaf Yeshayahu
 * @version %I%
 * @since 1.0
 */
public class Main {

    /**
     * Main program, runs the client side of <b>The Town Project</b>
     * @param args arguments sent by command-line or any other means
     */
    
    /**
     * The MP3 Player that plays the background music
     * @see audio.SoundJLayer The mp3 player
     */
    public static SoundJLayer player = new SoundJLayer();
    
    /**
     * Main execution of the program <b>The town project</b>
     * @param args arguments to be sent (not in use here)
     */
    public static void main(String[] args){
//        DB database = DB.getInstance();
//        database.getUsernames().add(new AbstractUser("Ben", "Gilad"));
//        database.getUsernames().add(new AbstractUser("Asaf", "Yesha"));

        FormLogin login = new FormLogin();
        player.play();
//          FormGame form = new FormGame(null, new AbstractUser("Yossi", "Hazan"));
//        BasicClientTest client = new BasicClientTest();
    }
    
}
