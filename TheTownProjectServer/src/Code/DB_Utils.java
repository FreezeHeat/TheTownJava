package Code;

/**
 * This class holds information about the database, such as connection strings,
 * drivers etc..
 * @author Ben Gilad and Asaf Yeshayahu
 * @version %I%
 * @since 1.0
 */
public class DB_Utils {
    
    /**
     * The MySQL driver to be used
     */
    public static final String sqlDriver = "com.mysql.jdbc.Driver";
    
    /**
     * The MySQL database's URL
     */
    public static final String mySqlUrl = 
            "jdbc:mysql://localhost:3306/thetown?zeroDateTimeBehavior=convertToNull";
    
    /**
     * The MySQL database's username
     */
    public static final String username = "root";
    
    /**
     * The MySQL database's password
     */
    public static final String password = "root";
}
