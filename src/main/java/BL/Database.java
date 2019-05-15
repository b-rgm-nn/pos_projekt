package BL;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;


/**
 * 
 * @author Matthias
 */
public class Database {
    private static Database instance;
    private Connection conn;
    
    /**
     * private constructor for Singleton implementation
     * reads connection details from db_login.txt
     * @throws SQLException when connection to database is unsuccessful
     */
    private Database() throws SQLException {
        File login = new File("db_login.txt");
        String dbname = "";
        String username = "";
        String pw = "";
        
        try(BufferedReader br = new BufferedReader(new FileReader(login))) {
            String line = "";
            while((line = br.readLine()) != null) {
                if(line.startsWith("username")) {
                    username = line.split("=")[1].trim();
                }
                if(line.startsWith("pw")) {
                    pw = line.split("=")[1].trim();
                }
                if(line.startsWith("dbname")) {
                    dbname = line.split("=")[1].trim();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Could not read login data from db_login.txt");
            System.exit(0);
        }
        
        conn = DriverManager.getConnection("jdbc:postgresql://localhost/" + dbname, username, pw);
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
    
    /**
     * create a scrollable prepared statement
     * @param sql the sql to execute
     * @return the Prepared Statement
     * @throws SQLException when conneciton fails 
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    }
    
    /**
     * Execute an sql query and return the scrollable ResultSet
     * @param sql the query to execute
     * @return scrollable ResultSet
     * @throws SQLException when conneciton fails 
     */
    public ResultSet query(String sql) throws SQLException {
        Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet result = statement.executeQuery(sql);
        result.first();
        return result;
    }
    
    /**
     * Execute a statement that alters data
     * @param sql the statement to execute
     * @throws SQLException when conneciton fails  
     */
    public void update(String sql) throws SQLException {
        Statement statement = conn.createStatement();
        statement.executeUpdate(sql);
        statement.close();
    }
}
