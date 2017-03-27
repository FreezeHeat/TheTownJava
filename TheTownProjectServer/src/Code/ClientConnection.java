package Code;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class creates a single connection for a client
 * @author Ben Gilad and Asaf Yeshayahu
 * @version %I%
 * @since 1.0
 */
public class ClientConnection {
    /**
     * Output stream for data
     */
    public ObjectOutputStream out;
    
    /**
     * Input stream for data
     */
    public ObjectInputStream in;
    
    /**
     * States if the user is online or not
     * <ul>
     * <li>true = online</li>
     * <li>false = offline</li>
     * </ul>
     */
    public Boolean online = false;
    
    /**
     * {@code Socket} used for Input and Output streams
     * @see Code.ClientConnection#in
     * @see Code.ClientConnection#out
     */
    private Socket connection;
    
    /**
     * Specifies the hostname to connect to (Server IP)
     */
    private String hostname = "127.0.0.1";
    
    /**
     * Port number, used with hostname <i>e.g. (127.0.0.1:5555)</i>
     * @see Code.ClientConnection#hostname
     */
    private int port = 55555;
    
    /**
     * Create a <b>Singleton</b> connection of the class
     */
    public static ClientConnection client = new ClientConnection();

    /**
     * Create a singleton connection
     */
    private ClientConnection() {
    }

    /**
     * Retry the connection to the server
     * @throws java.io.IOException
     */
    public void startConnection() throws IOException {
        if (online == false) {
            connection = new Socket(hostname, port);
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
            online = true;
        }
    }

    /**
     * Close all connections and alert the server of it
     */
    public void exit() {
        if (online == true) {
            try {
                online = false;
                out.writeObject(Commands.DISCONNECT);
                out.close();
                in.close();
                connection.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Finalize the object and also exit from the server;
     */
    @Override
    protected void finalize() {
        exit();
    }
}
