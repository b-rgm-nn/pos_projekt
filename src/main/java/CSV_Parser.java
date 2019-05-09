


import BL.Database;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CSV_Parser {
    private File file = new File("C:\\Users\\Matthias\\OneDrive - HTBLA Kaindorf\\Schule\\3CHIF\\POS\\pos_projekt\\sandp500\\all_stocks_5yr.csv");
    
    private void parse() throws SQLException {
        Database database = Database.getInstance();
        createTables();
        database.update("DELETE FROM data WHERE 1=1");
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while((line = br.readLine()) != null) {
                try {
                    String[] data = line.split(",");
                    String date = data[0];
                    double open = Double.parseDouble(data[1]);
                    double high = Double.parseDouble(data[2]);
                    double low = Double.parseDouble(data[3]);
                    double close = Double.parseDouble(data[4]);
                    int volume = Integer.parseInt(data[5]);
                    String name = data[6];
                    String sql = String.format(
                            "INSERT INTO data (date, open, high,  low, close, volume, name) "
                                    + "VALUES ('%s'  , %.2f, %.2f, %.2f, %.2f , %d    , '%s');",
                                               date, open, high,  low, close, volume, name);
                    database.update(sql);
                } catch(NumberFormatException ex) {
                    System.out.print("numberformatex: ");
                    System.out.println(line);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void createTables() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS data ("
                + "date DATE,"
                + "open REAL,"
                + "high REAL,"
                + "low REAL,"
                + "close REAL,"
                + "volume BIGINT,"
                + "name VARCHAR(5)"
                + ");";
        Database.getInstance().update(query);
    }
    
    public static void main(String[] args) {
        CSV_Parser parser = new CSV_Parser();
        try {
            parser.parse();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
