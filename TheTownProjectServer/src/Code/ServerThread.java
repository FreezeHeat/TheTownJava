package Code;

import Code.Commands;
import Code.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static resources.LocalizationUtil.localizedResourceBundle;

/**
 * A Thread for each client
 * <p>used for each connection from the server, involves requests to the main
 * server and manages the player's actions</p>
 * @author Ben Gilad and Asaf Yeshayahu
 * @version %I%
 * @see Code.Server The main server
 * @since 1.0
 */
public class ServerThread extends Thread {

    /**
     * The thread socket connection
     */
    private Socket connection;
    
    /**
     * The output stream from the {@code Socket}
     */
    private ObjectOutputStream out;
    
    /**
     * The input stream from the {@code Socket}
     */
    private ObjectInputStream in;
    
    /**
     * The instance of the main {@code Server}, used to call for methods within
     * the main server class.
     * @see Code.Server
     */
    private Server server;
    
    /**
     * The current user that is serviced by this Thread
     * @see Code.User
     */
    User user;
    
    /**
     * The current user's <b>Target</b> of actions, as stated by the client
     * @see Code.User
     */
    User target;
    
    /**
     * The current user's <b>Action</b>
     * @see Code.Commands Actions that can be done by the user
     */
    Commands action;
    
    /**
     * Whether the user's status is online or offline
     * <ul>
     * <li>true = online</li>
     * <li>false = offline</li>
     * </ul>
     */
    Boolean online;
    
    /**
     * Whether the user is in a game or not
     * <ul>
     * <li>true = in-game</li>
     * <li>false = in lobby</li>
     * </ul>
     */
    Boolean inGame;

    /**
     * Initiate the thread with access to the server and socket
     *
     * @param server Main server class (Multi-Threaded)
     * @param connection Socket connection
     * @throws IOException
     */
    public ServerThread(Server server, Socket connection) throws IOException{
            this.server = server;
            this.connection = connection;
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            online = true;
            inGame = false;
    }

    /**
     * Server logic and protocol for each client
     */
    @Override
    public void run() {
        Commands input = Commands.IDLE;

        System.out.println("Now serving client");
        do {
            try {
                input = (Commands) in.readObject();
                System.out.println("**********\nInput: " + input);
                serveClient(input);
                System.out.println("Done\n**********");
            } catch (ClassNotFoundException cnfex) {
                cnfex.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                break;
            }
        } while (online == true);

        try {
            //close all connections if it's lost
            closeCrap();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets the input from the user executes the correct action
     *
     * @param input the input from the user
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public void serveClient(Commands input)
            throws IOException, ClassNotFoundException {
        switch (input) {
            case LOGIN:
                login(input);
                break;
            case REGISTER:
                register(input);
                break;
            case TOP10:
                getTop10();
                break;
            case CHECK_GAME:
                checkGame();
                break;
            case READY:
                joinGame();
                break;
            case SEND_MESSAGE:
                chat();
                break;
            case SEND_MESSAGE_KILLER:
                chatKillers();
                break;
            case SEND_MESSAGE_DEAD:
                chatDead();
                break;
            case VOTE:
                vote();
                break;
            case KILL:
            case HEAL:
            case DETECT:

                //set action based on the player's class
                setAction();
                break;
            case DISCONNECT:
                disconnect();
                break;
        }
    }

    /**
     * Close all connections and remove this thread
     *
     * @throws IOException
     */
    public void closeCrap() throws IOException {
        server.removeClient(this);
        System.out.println(user.getUsername() + " Disconnected");
        in.close();
        out.close();
        connection.close();
    }

    /**
     * Login handler
     *
     * @param input Login command
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void login(Commands input) throws IOException {
        try {
            Commands result = Commands.LOGIN;

            //login through the database
            user = (User) in.readObject();
            user = DB.database.login(
                    user.getUsername(),
                    user.getPassword());

            //if there's a user, check if already connected
            if (user != null) {
                result = server.checkUser(this, user, input);
            } //if there's no such user
            else {
                result = Commands.WRONG_DETAILS;
            }

            switch (result) {
                case LOGIN:
                    out.writeObject(result);
                    out.writeObject(user);
                    break;
                case WRONG_DETAILS:
                    out.writeObject(result);
                    user = null;
                    break;
                case ALREADY_CONNECTED:
                    out.writeObject(result);
                    user = null;
                    break;
            }
            System.out.println("LOGIN result: " + result);
        } catch (IOException ex) {
            ex.printStackTrace();
            out.writeObject(Commands.CONNECTION_ERROR);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        out.flush();
    }

    /**
     * Register handler
     *
     * @param input Register command
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void register(Commands input) throws IOException {
        try {
            User details = (User) in.readObject();
            Commands result = server.checkUser(this, details, input);
            switch (result) {
                case REGISTER:
                    out.writeObject(result);
                    user = server.login(details);
                    out.writeObject(user);
                    break;
                case ALREADY_EXISTS:
                    out.writeObject(result);
                    break;
            }
            System.out.println("REGISTER result: " + result);
        } catch (IOException ex) {
            ex.printStackTrace();
            out.writeObject(Commands.CONNECTION_ERROR);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        out.flush();
    }
    
    /**
     * Requests a top 10 list of users from the database and sends it back to the
     * client
     * @throws IOException 
     * @see DB#getTop10() The getTop10 method from the Database(Which is in use here)
     */
    public void getTop10() throws IOException{
        try {
            ArrayList<User> users = DB.database.getTop10();
            System.out.println("GET TOP 10: " + users);
            out.writeObject(users);
            System.out.println("SENT OUT TOP 10");
        } catch (IOException ex) {
            ex.printStackTrace();
            out.writeObject(Commands.CONNECTION_ERROR);
            System.out.println("ERROR TOP 10");
        } catch (SQLException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        out.flush();
    }

    /**
     * Transfer an object from the server to the client
     *
     * @param object the object to be sent
     * @throws IOException
     */
    public synchronized void transferObject(Object object) throws IOException {

        //Java uses object references when sending objects through the stream
        //in some cases, an old reference will be sent, therefore reset() method
        //resets the references and lets the modified object to be sent over
        out.reset();
        out.writeObject(object);
        System.out.println("SENT OBJECT: " + object.getClass());
        out.flush();
    }

    /**
     * Checks if the queue for the game is full or not
     * <p>returns the answer through {@code Socket}</p>
     * @throws java.io.IOException
     */
    public void checkGame() throws IOException{
        out.writeObject(server.players.size() >= server.MIN_PLAYERS);
    }
    
    /**
     * Adds the player to the list of players
     */
    public void joinGame() {
        inGame = true;
        System.out.println(user.getUsername() + " requests to JOIN GAME");
        server.addPlayer(this);
    }

    /**
     * send a message to all other players in-game
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public void chat() throws IOException, ClassNotFoundException {
        String msg = (String) in.readObject();
        server.broadcast(msg, Commands.SEND_MESSAGE);
    }

    /**
     * send a message to all other killers in-game
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void chatKillers() throws IOException, ClassNotFoundException {
        String msg = (String) in.readObject();
        server.chatKillers(msg);
    }

    /**
     * send a message to all the dead people in-game
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void chatDead() throws IOException, ClassNotFoundException {
        String msg = (String) in.readObject();
        server.chatDead(msg);
    }

    /**
     * Vote for a player, Renounce vote, or change it. then announce it to all
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void vote() throws IOException, ClassNotFoundException {
        User u = (User) in.readObject();

        //if voted for no one, or if vote was renounced before
        if (target == null) {
            target = u;
            server.broadcast(
                    "***" + user.getUsername() + " "
                    + localizedResourceBundle.getString("FormGame.voted") + " "
                    + target.getUsername() + "***",
                    Commands.SERVER_MESSAGE);
            System.out.println(user.getUsername() + " voted against "
                    + u.getUsername());
        } //in case user wants to renמounce his vote
        else if (target.equals(u)) {
            target = null;
            server.broadcast(
                    "***" + user.getUsername() + " "
                    + localizedResourceBundle.getString("FormGame.noVote") + "***",
                    Commands.SERVER_MESSAGE);
            System.out.println(user.getUsername() + " renounced his vote");
        } //in case the user changed his vote
        else {
            target = u;
            server.broadcast(
                    "***" + user.getUsername() + " "
                    + localizedResourceBundle.getString("FormGame.changeVote")
                    + " " + target.getUsername() + "***",
                    Commands.SERVER_MESSAGE);
            System.out.println(user.getUsername() + " changed his vote to "
                    + u.getUsername());
        }
    }

    /**
     * Set the action of the user based on his class
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void setAction() throws IOException, ClassNotFoundException {
        User u = (User) in.readObject();

        //in case user wants to renמounce his vote
        if (target != null && target.equals(u)) {
            target = null;
            action = null;
            transferObject(Commands.SERVER_MESSAGE);
            transferObject(
                    localizedResourceBundle.getString("FormGame.cancelAction"));
            System.out.println(user.getUsername() + " canceled his action");
        } else {

            //in case the user changed his vote
            target = u;
            getActionByRole();
            transferObject(Commands.SERVER_MESSAGE);
            transferObject(
                    localizedResourceBundle.getString("FormGame.setAction")
                    + " " + target.getUsername());
            System.out.println(user.getUsername() + " ACTION: " + action);
        }
    }

    /**
     * Set action based on the instanced class
     */
    private void getActionByRole() {
        if (user instanceof Killer) {
            action = Commands.KILL;
        } else if (user instanceof Healer) {
            action = Commands.HEAL;
        } else if (user instanceof Snitch) {
            action = Commands.DETECT;
        }
    }

    /**
     * User wished to disconnect
     *
     * @throws IOException
     */
    private void disconnect() throws IOException {
        System.out.println(user.getUsername() + " DISCONNECT request");
        online = false;

        if (inGame == true) {
            inGame = false;
            server.players.remove(user);

            //send to all but this player, that this player left the game
            server.broadcast(
                    user.getUsername() + " left the game",
                    Commands.SERVER_MESSAGE,
                    this);
        }
    }
}
