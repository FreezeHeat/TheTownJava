package Code;

import java.util.ArrayList;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The project's database(Singleton), includes all users
 * @author Ben Gilad and Asaf Yeshayahu
 * @version %I%
 * @since 1.0
 */
public class DB {

    /**
     * Holds the connection details for the <b>MySQL server</b>
     */
    private Connection connectionSQL;
    
    /**
     * A <b>Singleton</b> instance of the database
     */
    public static DB database = new DB();

    /**
     * Builds the database from MySQL server, it's private to ensure the
     * <b>Singleton</b> design
     */
    private DB() {
        try {
            //Checking connection to database server
            Class.forName(DB_Utils.sqlDriver);
            connectionSQL = DriverManager.getConnection(
                    DB_Utils.mySqlUrl,
                    DB_Utils.username,
                    DB_Utils.password);
            connectionSQL.close();
        } catch (SQLException ex) {
            System.out.println("Connection to MySQL database failed");
        } catch (ClassNotFoundException ex) {
            System.out.println("MySQL Driver failed");
        }
    }

    /**
     * Login to the server using a username and password
     *
     * @param username input username
     * @param password input password
     * @return "Login Successful" / "Login Failed" based on the result
     * @throws java.sql.SQLException
     */
    public User login(String username, String password)
            throws SQLException {
        connectionSQL = DriverManager.getConnection(
                DB_Utils.mySqlUrl,
                DB_Utils.username,
                DB_Utils.password);

        PreparedStatement query = connectionSQL.prepareStatement(
                "SELECT username, password FROM Users;"
        );

        PreparedStatement queryStats = connectionSQL.prepareStatement(
                "SELECT won, lost, kills, heals FROM Stats "
                + "WHERE username = ?;"
        );

        ResultSet result = query.executeQuery();

        while (result.next()) {
            String sqlUsername = result.getString("username");
            String sqlPassword = result.getString("password");
            // if user was found
            if (sqlUsername.equals(username) && sqlPassword.equals(password)) {
                User user = new User(username, password);
                //set the user's stats from the database
                queryStats.setString(1, username);
                result = queryStats.executeQuery();
                while (result.next()) {
                    user.setWon(result.getLong("won"));
                    user.setLost(result.getLong("lost"));
                    user.setKills(result.getLong("kills"));
                    user.setHeales(result.getLong("heals"));
                }
                connectionSQL.close();
                return user;
            }
        }
        //if user wasn't found
        connectionSQL.close();
        return null;
    }

    /**
     * Register a user on the DB
     *
     * @param username the user's name
     * @param password the user's password
     * @return result of the process successful or not
     * @throws SQLException
     */
    public boolean register(String username, String password)
            throws SQLException {

        if (doesUserExist(username) == false) {
            connectionSQL = DriverManager.getConnection(
                    DB_Utils.mySqlUrl,
                    DB_Utils.username,
                    DB_Utils.password);

            PreparedStatement query = connectionSQL.prepareStatement(
                    "INSERT INTO Users (username, password) "
                    + "VALUES (?, ?) ;"
            );
            query.setString(1, username);
            query.setString(2, password);

            PreparedStatement queryStats = connectionSQL.prepareStatement(
                    "INSERT INTO Stats "
                    + "(username, won, lost, kills, heals) "
                    + "VALUES (?, 0, 0, 0, 0) ;"
            );
            queryStats.setString(1, username);

            query.execute();
            queryStats.execute();

            connectionSQL.close();
            return true;
        }
        return false;
    }

    /**
     * Check if the user already exists in the DB
     *
     * @param username the user
     * @return result of the query
     * @throws SQLException
     */
    public boolean doesUserExist(String username) throws SQLException {
        connectionSQL = DriverManager.getConnection(
                DB_Utils.mySqlUrl,
                DB_Utils.username,
                DB_Utils.password);

        PreparedStatement query = connectionSQL.prepareStatement(
                "SELECT username FROM Users WHERE username LIKE ?;");
        query.setString(1, username);

        ResultSet result = query.executeQuery();

        // if there's a result, that means there's such a user...
        if (result.next()) {
            connectionSQL.close();
            return true;
        }

        //if user wasn't found
        connectionSQL.close();
        return false;
    }

    /**
     * Update a player's stats
     *
     * @param user the user
     * @throws SQLException
     */
    public void updatePlayerStats(User user) throws SQLException {
        connectionSQL = DriverManager.getConnection(
                DB_Utils.mySqlUrl,
                DB_Utils.username,
                DB_Utils.password);

        PreparedStatement query = connectionSQL.prepareStatement(
                "UPDATE Stats SET "
                + "won = ?, "
                + "lost = ?, "
                + "kills = ?, "
                + "heals = ? "
                + "WHERE username LIKE ? ;"
        );
        query.setLong(1, user.getWon());
        query.setLong(2, user.getLost());
        query.setLong(3, user.getKills());
        query.setLong(4, user.getHeales());
        query.setString(5, user.getUsername());

        query.execute();

        connectionSQL.close();
    }
    
    /**
     * Returns from the database the TOP 10 players, ordered by Wins, Kills and
     * then Heals (In this order)
     * @return {@code ArrayList} of 10 {@code User} objects, in order, 0 is first
     * @throws SQLException 
     */
    public ArrayList<User> getTop10()
            throws SQLException {
        connectionSQL = DriverManager.getConnection(
                DB_Utils.mySqlUrl,
                DB_Utils.username,
                DB_Utils.password);

        PreparedStatement queryStats = connectionSQL.prepareStatement(
                "SELECT username, won, lost, kills, heals FROM Stats "
                + "ORDER BY won DESC, kills DESC, heals DESC LIMIT 10;"
        );
        
        //counter for top 10 users
        int i = 10; 
        
        //holds the users from the query
        ArrayList<User> users = new ArrayList<User>();
        
        //execute the query
        ResultSet result = queryStats.executeQuery();
        
        //holds the temporary user
        User user = null;
        
        // add each user in the list
        while (result.next()) {
            user = new User();
            user.setUsername(result.getString("username"));
            user.setWon(result.getLong("won"));
            user.setLost(result.getLong("lost"));
            user.setKills(result.getLong("kills"));
            user.setHeales(result.getLong("heals"));
            users.add(user);
        }
        
        connectionSQL.close();
        return users;      
    }
}
