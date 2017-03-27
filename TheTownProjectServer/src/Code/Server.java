package Code;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import static resources.LocalizationUtil.localizedResourceBundle;

/**
 * Main server class
 * <p>Involves all input and output to the server and the management of threads</p>
 * @author Ben Gilad and Asaf Yeshayahu
 * @version %I%
 * @see Code.ServerThread The Thread used for each connection
 * @since 1.0
 */
public class Server {

    /**
     * The main server socket, used to accept connections
     */
    private ServerSocket server;
    
    /**
     * The socket used for the server connection
     */
    private Socket connection;
    
    /**
     * Holds all the clients which connections were accepted
     */
    private ArrayList<ServerThread> clients = new ArrayList<>();
    
    /**
     * The listening port used for the server
     */
    private int port;
    
    /**
     * Max connections allowed to the server
     */
    private int numClients;
    
    /**
     * The minimum players to start a game
     */
    final int MIN_PLAYERS = 5;
    
    /**
     * States if a game already started or not
     * <ul>
     * <li> false = didn't start </li>
     * <li> true = started </li>
     * </ul>
     */
    private Boolean gameStarted = false;
    
     /**
     * Used for flow control of Threads
     * <p> States if a thread finished it's job or not </p>
     * <ul>
     * <li> false = didn't finish </li>
     * <li> true = finished </li>
     * </ul>
     */
    private Boolean threadFinished = false;
    
    /**
     * Used to time Threads by specifying seconds
     */
    private Timer timer = new Timer();
    
    /**
     * The 'Game Start' default delay time
     */
    private final int startDelay = 35 * 1000;
    
    /**
     * The 'Day Phase' default delay time
     */
    private final int dayDelay = 40 * 1000;
    
    /**
     * The 'Night Phase' default delay time
     */
    private final int nightDelay = 30 * 1000;
    
    /**
     * A {@code Thread} used for the day cycle / phase
     */
    private dayCycle dayThread;
    
    /**
     * A {@code Thread} used for the night cycle / phase
     */
    private nightCycle nightThread;
    
    /**
     * States the current phase of the game
     * @see Code.Commands
     */
    private Commands cycle;
    
    /**
     * A {@code Game} class, Creates a new game instance for the server
     */
    private Game game = new Game();
    
    /**
     * An {@code ArrayList} that holds the current players of the game
     */
    ArrayList<User> players = new ArrayList<User>();

    //2 Citizens, 1 Killer, 1 Snitch, 1 Healer
    /**
     * The 'Roles Bank', holds roles currently available for the game in order:
     * <p> {@code Citizen, Killer, Snitch, Healer} </p>
     * @see code.Citizen
     * @see code.Killer
     * @see code.Snitch
     * @see code.Healer
     */
    int[] rolesBank = new int[]{2, 1, 1, 1};

    /**
     * Used to synchronize {@code TimerTask} instances for the game phase
     */
    CountDownLatch latch;
    /**
     * Start the server, listen to clients and handle each with a thread
     *
     * @param port port to run on
     * @param numClients number of clients to accept
     */
    public void startServer(int port, int numClients){
        try {
            this.port = port;
            this.numClients = numClients;
            server = new ServerSocket(port, numClients);
            System.out.println("Listening for a connection on port "
                    + port + " limited for " + numClients + " clients");
            
            while(true){
                // listen for connections
                listenForConnection();
                clients.add(new ServerThread(this, connection));
                clients.get(clients.size() - 1).start();
                System.out.println("Client count: " + clients.size());
            }
        }catch (EOFException eofex) {
            eofex.printStackTrace();
            System.out.println("EOF - Connection failed");
            for(ServerThread t : clients){
                this.removeClient(t);
            }
        }catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("IO - Connection failed");
            for(ServerThread t : clients){
                this.removeClient(t);
            }
        }
    }

    /**
     * Listen for a connection
     *
     * @throws IOException
     */
    private void listenForConnection() throws IOException {
        connection = server.accept();
        System.out.println("Connection established with "
                + connection.getInetAddress().getHostAddress());
    }

    /**
     * Remove a client from the list
     *
     * @param client the client to be removed
     */
    public synchronized void removeClient(ServerThread client) {
        boolean isAlive = client.user.IsAlive();
        players.remove(client.user);
        Iterator i = clients.iterator();
        while (i.hasNext()) {
            if (i.next() == client) {
                i.remove();
                break;
            }
        }
        System.out.println("Removed client thread number: " + client.getName());
        System.out.println("Gamestarted = " + gameStarted + " Game Thread = " + game.isAlive());
        if(gameStarted == true && players.size() < 5 && isAlive == true){
            game.stop();
            game = null;
            broadcast(Commands.CONNECTION_ERROR);
            gameStarted = false;
            initGame();
        }else if(gameStarted == true){
            
            //update everyone that the user has left
            broadcast(players, Commands.REFRESH_PLAYERS);
        }
    }

    /**
     * Login to the server
     *
     * @param details the user's details
     * @return User as built by the database
     */
    public synchronized User login(User details) {
        try {
            User user = DB.database.login(
                    details.getUsername(),
                    details.getPassword());
            System.out.println("DB Login DONE");
            return user;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Register a new user
     *
     * @param details the user's details
     * @return whether the registration was successful or not
     */
    public synchronized Boolean register(User details) {
        try {
            System.out.println("DB Register");
            return DB.database.register(
                    details.getUsername(),
                    details.getPassword());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Checks the details sent with the server and database for errors
     *
     * @param x The {@code ServerThread} to run the method on
     * @param details User's details
     * @param type type of check, either login or register
     * @return result of the check (OK, ALREADY_EXISTS, etc..)
     * @see ServerThread
     */
    public synchronized Commands checkUser(
            ServerThread x, User details, Commands type) {
        Commands result = null;

        switch (type) {
            case LOGIN:
                result = loginCheck(x, details);
                System.out.println("CHECK login");
                break;
            case REGISTER:
                result = registerCheck(details);
                System.out.println("CHECK register");
                break;
        }
        return result;

    }

    /**
     * Check if the user already exists in the database
     *
     * @param details the user's details
     * @return result of the check
     * @throws SQLException
     */
    private synchronized Commands registerCheck(User details) {
        try {
            if (DB.database.register(
                    details.getUsername(),
                    details.getPassword()) == false) {
                return Commands.ALREADY_EXISTS;
            } else {
                return Commands.REGISTER;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Check if the user is already connected, or if the details are wrong
     *
     * @param details user's details
     * @return result of the check
     */
    private synchronized Commands loginCheck(ServerThread client, User details) {
        Commands result = Commands.LOGIN;

        //check if the user is already connected in the list of clients
        for (ServerThread x : clients) {
            if (x != client && x.user != null && x.user.equals(details)) {
                result = Commands.ALREADY_CONNECTED;
            }
        }
        return result;
    }

    /**
     * Adds a user to the list of players
     *
     * @param player the thread of a player who wishes to join
     */
    public synchronized void addPlayer(ServerThread player) {
        Roles role = getRandomRole();

        switch (role) {
            case CITIZEN:
                player.user = new Citizen(player.user);
                break;
            case KILLER:
                player.user = new Killer(player.user);
                break;
            case SNITCH:
                player.user = new Snitch(player.user);
                break;
            case HEALER:
                player.user = new Healer(player.user);
                break;
        }
        System.out.println(player.user.getUsername() + " got role " + role);

        //add the player to the list and set the player as alive
        player.user.setIsAlive(true);
        players.add(player.user);

        System.out.println(player.user.getUsername() + " added to list of players");

        //inform everyone (except the this player) that a new player has joined
        broadcast(
                player.user.getUsername() + " "
                + localizedResourceBundle.getString("FormGame.joined"),
                Commands.SERVER_MESSAGE);

        //send the list of players to everyone who are ingame
        broadcast(players, Commands.REFRESH_PLAYERS);

        //if the game hasn't started, and players are above the minimum
        if (players.size() >= MIN_PLAYERS && gameStarted == false) {
            gameStarted = true;
            game.start();
        }
    }

    /**
     * Determines game phases and gameplay
     */
    private class Game extends Thread {

        @Override
        public void run() {
            //Initial game phase
            broadcast(
                    localizedResourceBundle.getString("FormGame.gameStart"),
                    Commands.SERVER_MESSAGE);
            System.out.println("*** Game started ****");

            //send to all that the night cycle is starting (used once)
            TimerTask start = new TimerTask() {
                @Override
                public synchronized void run() {
                    cycle = Commands.NIGHT;
                    
                    //notify that thread has finished
                    latch.countDown();
                }
            };
            

            //start the cycle with begin
            cycle = Commands.BEGIN;

            //Day / Night / Begin cycles
            while (gameStarted == true) {
                System.out.println("--- PHASE: " + cycle + " ---");
                
                //set latch for synchronizing timer tasks
                latch = new CountDownLatch(1);
                switch (cycle) {
                    case DAY:
                        announceDay();
                        timer.schedule(dayThread = new dayCycle(), dayDelay);
                        break;
                    case NIGHT:
                        announceNight();
                        timer.schedule(nightThread = new nightCycle(), nightDelay);
                        break;
                    case BEGIN:
                        announceStart();
                        timer.schedule(start, startDelay);
                        break;
                }
                
                //synchronize with timer
                try {
                    latch.await();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }

                System.out.println("--- PHASE ENDED ---");

                //method to check if game end conditions are met
                isEnd();
            }
            
            // start a new instance for the next game
            initGame();
        }
    }

    /**
     * Chat between killers only
     *
     * @param msg the message
     */
    public synchronized void chatKillers(String msg) {
        for (ServerThread client : clients) {
            try {
                if (client.inGame == true && client.user instanceof Killer) {
                    client.transferObject(Commands.SEND_MESSAGE_KILLER);
                    client.transferObject(msg);
                    System.out.println("CHAT-KILLERS: " + msg);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Chat between dead only
     *
     * @param msg the message
     */
    public synchronized void chatDead(String msg) {
        for (ServerThread client : clients) {
            try {
                if (client.inGame == true && client.user.IsAlive() == false) {
                    client.transferObject(Commands.SEND_MESSAGE_DEAD);
                    client.transferObject(msg);
                    System.out.println("CHAT-DEAD: " + msg);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Broadcast an object to all clients in-game
     * <p><b>NOTE: it calls to the overloaded method {@code Broadcast}</b></p>
     * @param object the object to be sent
     * @see #broadcast(java.lang.Object, Code.Commands, Code.ServerThread) Mentioned in <b>NOTE</b>
     */
    public synchronized void broadcast(Object object) {
        
        //call for the method (which is synchronized, in order to avoid double
        //boradcasts to the users (which can corrupt the protocol.
        broadcast(object, null, null);
    }

    /**
     * Broadcast an object to all clients in-game except the originator
     * <p><b>NOTE: it calls to the overloaded method {@code Broadcast}</b></p>
     * @param object the object to be sent
     * @param origin the originator, to which the broadcast won't be sent
     * @see #broadcast(java.lang.Object, Code.Commands, Code.ServerThread) Mentioned in <b>NOTE</b>
     */
    public synchronized void broadcast(Object object, ServerThread origin) {
        
        //call for the method (which is synchronized, in order to avoid double
        //boradcasts to the users (which can corrupt the protocol.
        broadcast(object, null, origin);
    }

    /**
     * Broadcast an object and then a command to all clients in-game
     * <p><b>NOTE: it calls to the overloaded method {@code Broadcast}</b></p>
     * @param object object to be sent
     * @param command command to be sent
     * @see #broadcast(java.lang.Object, Code.Commands, Code.ServerThread) Mentioned in <b>NOTE</b>
     */
    public synchronized void broadcast(Object object, Commands command) {
        
        //call for the method (which is synchronized, in order to avoid double
        //boradcasts to the users (which can corrupt the protocol.
        broadcast(object, command, null);
    }

    /**
     * Broadcast an object and then a command to all clients in-game, except the
     * originator / with the originator
     *
     * @param object object to be sent
     * @param command command to be sent
     * @param origin the originator, to which the broadcast won't be sent
     */
    public synchronized void broadcast(
            Object object, Commands command, ServerThread origin) {
        
        //In case the broadcast is meant for all except the originator
        if(origin != null && command != null){
            for (ServerThread client : clients) {
                try {
                    if (client != origin && client.inGame == true) {
                        client.transferObject(command);
                        client.transferObject(object);
                        System.out.println(
                                "BROADCAST: " + object.getClass()
                                + " " + object + " " + command
                                + " Originator: " + origin.user.getUsername()
                        );
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }else if(origin == null && command != null){
            
            //In case the broadcast is meant for all (with object && command)
            for (ServerThread client : clients) {
                try {
                    if (client.inGame == true) {
                        client.transferObject(command);
                        client.transferObject(object);
                        System.out.println(
                                "BROADCAST: " + object.getClass()
                                + " " + object + " " + command);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
        }
        }else{
            
            // it the broadcast was meant for all (object only)
            for (ServerThread client : clients) {
                try {
                    if (client.inGame == true) {
                        client.transferObject(object);
                        System.out.println(
                                "BROADCAST: " + object.getClass()
                                + " " + object
                        );
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Get an available random role from the roles bank
     *
     * @return the random role
     */
    private Roles getRandomRole() {
        Roles[] roles = {Roles.CITIZEN, Roles.KILLER, Roles.SNITCH, Roles.HEALER};
        Random rand = new Random();
        int number;

        while (true) {
            number = rand.nextInt(roles.length);

            //if the role generated is available in the bank
            if (rolesBank[number] > 0) {
                rolesBank[number]--;
                System.out.println("SERVER: Generated random role");
                return roles[number];
            }
        }
    }

    /**
     * Refresh each <code>ServerThread's</code> <code>User</code> value, with
     * values from the list of players in the <code>Server</code> class
     */
    private synchronized void refreshPlayers() {
        for (ServerThread x : clients) {
            if (x.inGame == true) {
                x.user = players.get(players.indexOf(x.user));
            }
        }
        System.out.println("SERVER: Refreshed Players List");
    }

    /**
     * Announce the first stage of the game
     */
    private synchronized void announceStart() {
        //announce day cycle
        broadcast(
                localizedResourceBundle.getString("FormGame.PhaseDayWelcome"),
                Commands.SERVER_MESSAGE
        );
        broadcast(Commands.BEGIN);
    }

    /**
     * Announce day-cycle
     */
    private synchronized void announceDay() {
        broadcast(
                localizedResourceBundle.getString("FormGame.PhaseDay"),
                Commands.SERVER_MESSAGE
        );
        broadcast(Commands.DAY);
    }

    /**
     * Announce night-cycle
     */
    private synchronized void announceNight() {
        broadcast(
                localizedResourceBundle.getString("FormGame.PhaseNight"),
                Commands.SERVER_MESSAGE
        );
        broadcast(Commands.NIGHT);
    }

    private synchronized void clearTargets() {
        for (ServerThread x : clients) {
            if (x.inGame == true) {
                x.target = null;
            }
        }
    }

    /**
     * Check for the end-game conditions and execute procedures if they apply
     */
    private synchronized void isEnd() {
        int killers = 0;
        int citizens = 0;

        for (User x : players) {
            if (x.IsAlive() == true) {
                if (x instanceof Killer) {
                    killers++;
                } else {
                    citizens++;
                }
            }
        }

        //set apropriate send message (if the game ended)
        boolean endMessage;
        if (killers == 0) {
            endMessage = true;
            System.out.println("SERVER: Citizens Won" + " K\\C "
                    + killers + "\\" + citizens);
        } else if (citizens <= 1) {
            endMessage = false;
            System.out.println("SERVER: Killers Won" + " K\\C "
                    + killers + "\\" + citizens);
        } else {
            System.out.println("PHASE PLAYER COUNT:  K\\C "
                    + killers + "\\" + citizens);
            return;
        }

        //update the database with stats
        for (ServerThread x : clients) {
            if (x.inGame == true) {
                try {

                    //If the citizens won then all the killers lose and vice-versa
                    if (citizens <= 1) {
                        if (x.user instanceof Killer) {
                            x.user.setWon(x.user.getWon() + 1);
                        } else {
                            x.user.setLost(x.user.getLost() + 1);
                        }
                    } else {
                        if (x.user instanceof Killer) {
                            x.user.setLost(x.user.getLost() + 1);
                        } else {
                            x.user.setWon(x.user.getWon() + 1);
                        }
                    }

                    //update each player's stats to the database
                    DB.database.updatePlayerStats(x.user);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }

        //Announce the game has ended
        broadcast(
                (endMessage),
                Commands.END
        );

        //update the client's user
        for (ServerThread x : clients) {
            if (x.inGame == true) {
                try {
                    x.transferObject(x.user);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        //makes sure to exit the game() loop
        gameStarted = false;
    }

    /**
     * A timed task (every 60 sec) to tally all the votes from each player AKA
     * the day cycle of the game
     */
    public class dayCycle extends TimerTask {

        @Override
        public synchronized void run() {
            int max = 0;
            int runnerUp = 0;
            int index = 0;
            int i = 0;
            int[] votes = new int[players.size()];
            //Get the first and second placed number of votes
            for (ServerThread x : clients) {
                if (x.inGame == true && x.target != null) {
                    i = players.indexOf(x.target);
                    votes[i]++;
                    System.out.println(x.target.getUsername() + " votes: " + votes[i]);

                    if (max <= votes[i]) {
                        runnerUp = max;
                        max = votes[i];
                        index = i;
                        System.out.println("Most voted: " + players.get(index).getUsername());
                    }
                }
            }
            
            System.out.println("max = " + max + " runnerup = " + runnerUp);
            
            //Votes Draw
            if (max == runnerUp) {
                broadcast(
                        localizedResourceBundle.getString("FormGame.draw"),
                        Commands.SERVER_MESSAGE);
            }
            
            //Execute the player
            else {
                players.get(index).setIsAlive(false);
                refreshPlayers();
                broadcast(
                        clients.get(index).user.getUsername() + " "
                        + localizedResourceBundle.getString("FormGame.announceExecuted"),
                        Commands.SERVER_MESSAGE);
                broadcast(players, Commands.REFRESH_PLAYERS);
            }

            //set the game cycle to the next cycle
            cycle = Commands.NIGHT;

            //Clear all te targets and set them to null
            clearTargets();
                
            //notify that thread has finished
            latch.countDown();
        }
    }

    /**
     * execute the players' actions and determine the result of the night cycle.
     */
    public class nightCycle extends TimerTask {

        @Override
        public synchronized void run() {
            User victim = null;
            String result = null;

            try {
                
                //Set the kills first (later ammended if needed by healers)
                for (ServerThread x : clients) {
                    if (x.user instanceof Killer && x.target != null) {
                        players.get(players.indexOf(x.target)).setIsAlive(false);
                        x.user.setKills(x.user.getKills() + 1);
                        System.out.println("KILLER " + x.user.getUsername()
                                + " killed " + x.target.getUsername());

                        //for the sake of announcing who died in the phase
                        victim = x.target;
                    }
                }
                
                // making sure to prevent null pointer exceptions
                if(victim != null){
                
                    //Healers turn
                    for (ServerThread x : clients) {
                        if (x.user.IsAlive() == true) {
                            if (x.user instanceof Healer && x.target != null) {
                                players.get(players.indexOf(x.target)).setIsAlive(true);
                                x.user.setHeales(x.user.getHeales() + 1);

                                //if the victim isn't dead, don't announce it.
                                if (x.target.equals(victim)) {
                                    victim = null;
                                }
                                System.out.println("HEALER " + x.user.getUsername()
                                        + " healed " + x.target.getUsername());
                            }
                        }
                    }

                    //Snitches turn
                    for (ServerThread x : clients) {
                        if (x.user.IsAlive() == true) {
                            if (x.user instanceof Snitch && x.target != null) {
                                if (players.get(players.indexOf(x.target)) instanceof Killer) {
                                    x.transferObject(Commands.SERVER_MESSAGE);
                                    x.transferObject(
                                            x.target.getUsername() + " "
                                            + localizedResourceBundle.getString(
                                                    "FormGame.isKiller"));
                                } else {
                                    x.transferObject(Commands.SERVER_MESSAGE);
                                    x.transferObject(
                                            x.target.getUsername() + " "
                                            + localizedResourceBundle.getString(
                                                    "FormGame.isNotKiller"));
                                }
                                System.out.println("SNITCH " + x.user.getUsername()
                                        + " snitched " + x.target.getUsername());
                            }
                        }
                    }
                }

                //update the list of players
                refreshPlayers();

                //send the list of players to everyone who are ingame
                broadcast(players, Commands.REFRESH_PLAYERS);

                //set the game cycle to the next cycle
                cycle = Commands.DAY;

                //announce the result
                if (victim != null) {
                    broadcast(
                            victim.getUsername() + " "
                            + localizedResourceBundle.getString("FormGame.announceDead"),
                            Commands.SERVER_MESSAGE);
                }

                //Clear all te targets and set them to null
                clearTargets();

                //notify that thread has finished
                latch.countDown();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Re-initiates the game in case it was over
     */
    private synchronized void initGame(){
            game = new Game();
            rolesBank = new int[]{2, 1, 1, 1};
            timer.purge();
            timer.cancel();
            timer = null;
            timer = new Timer();
    }

    /**
     * Finalizing the server instance
     * <p><b>NOTE:</b> Unlike any object, the finalization of the server requires
     * the Server instance to close</p>
     * @see java.net.ServerSocket#close()
     */
    @Override
    protected synchronized void finalize() {
        try {
            server.close();
        } catch (IOException e) {
            System.out.println("Could not close socket");
            System.exit(-1);
        }
    }

}
