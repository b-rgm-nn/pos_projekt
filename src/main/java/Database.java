
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * 
 * @author Matthias
 */
public class Database {
    private static Database instance;
    private Connection conn;
    
    /**
     * private constructor for Singleton implementation
     * @throws SQLException when connection to database is unsuccessful
     */
    private Database() throws SQLException {
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/projekt", "postgres", "bermac16");
    }

    /**
     * get the single instance of Database. create it if it doesn't exist
     * @return the only  instance of Database
     * @throws SQLException if connection to the Database was unsuccessful
     */
    public static synchronized Database getInstance() throws SQLException {
        if(instance == null) {
            instance = new Database();
        }
        return instance;
    }
}
