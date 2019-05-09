


import BL.Database;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;

public class CSV_Parser {
    private File file = new File("C:\\Users\\Matthias\\OneDrive - HTBLA Kaindorf\\Schule\\3CHIF\\POS\\pos_projekt\\sandp500\\all_stocks_5yr.csv");
    private File companies = new File("C:\\Users\\Matthias\\OneDrive - HTBLA Kaindorf\\Schule\\3CHIF\\POS\\pos_projekt\\sandp500\\companynames.csv");
    
    private void parseStocks() throws SQLException {
        Database database = Database.getInstance();

        String createSQL = "CREATE TABLE IF NOT EXISTS data ("
                + "date DATE,"
                + "open REAL,"
                + "high REAL,"
                + "low REAL,"
                + "close REAL,"
                + "volume BIGINT,"
                + "symbol VARCHAR(5)"
                + ");";
        Database.getInstance().update(createSQL);

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
    
    private void parseCompanyNames() throws SQLException {
        Database database = Database.getInstance();
        String createSQL = "CREATE TABLE IF NOT EXISTS companies ("
                + "symbol VARCHAR(5),"
                + "name VARCHAR(255),"
                + "sector VARCHAR(255)"
                + ");";
        
        database.update(createSQL);
        database.update("DELETE FROM companies WHERE 1=1");
        
        try (BufferedReader br = new BufferedReader(new FileReader(companies))) {
            String line = br.readLine();
            while((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String symbol = data[0].replace("'", "");
                String name = data[1].replace("'", "");
                String sector = data[2].replace("'", "");
                String sql = String.format(
                        "INSERT INTO companies (symbol, name, sector) "
                                + "VALUES ('%s', '%s', '%s');",
                                           symbol, name, sector);
                database.update(sql);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws Exception {
        CSV_Parser parser = new CSV_Parser();
        parser.parseCompanyNames();
        if(true) {
            throw new Exception("Data has already been parsed");
        }
        try {
            parser.parseStocks();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
