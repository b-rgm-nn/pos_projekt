package parser;

import BL.Database;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class CSV_Parser {

    private File file = new File("sandp500\\all_stocks_5yr.csv");
    private File companies = new File("sandp500\\companynames.csv");

    private long index;
    private long rows = 1;

    /**
     * parse the stock data from the csv and write it into the database
     *
     * @throws SQLException
     */
    public void parseStocks() throws SQLException {
        Database database = Database.getInstance();
        database.update("DELETE FROM data WHERE 1=1");

        Path path = Paths.get(file.getAbsolutePath());
        try {
            rows = Files.lines(path).count();
        } catch (IOException ex) {
            rows = 1;
        }

        index = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
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
                            "INSERT INTO data (date, open, high,  low, close, volume, symbol) "
                            + "VALUES ('%s'  , %.2f, %.2f, %.2f, %.2f , %d    , '%s');",
                            date, open, high, low, close, volume, name);
                    database.update(sql);
                    index++;
                } catch (NumberFormatException ex) {
                    System.out.print("numberformatex, skipping: ");
                    System.out.println(line);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        index = -1;
    }

    /**
     * parse the company name translation from csv to the database
     * @throws SQLException 
     */
    public void parseCompanyNames() throws SQLException {
        Database database = Database.getInstance();
        database.update("DELETE FROM companies WHERE 1=1");

        try (BufferedReader br = new BufferedReader(new FileReader(companies))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
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

    /**
     * create the companies and data table if they don't exist yet
     * @throws SQLException 
     */
    public void createTables() throws SQLException {
        String createSQL = "CREATE TABLE IF NOT EXISTS companies ("
                + "symbol VARCHAR(5),"
                + "name VARCHAR(255),"
                + "sector VARCHAR(255)"
                + ");";

        Database.getInstance().update(createSQL);

        createSQL = "CREATE TABLE IF NOT EXISTS data ("
                + "date DATE,"
                + "open REAL,"
                + "high REAL,"
                + "low REAL,"
                + "close REAL,"
                + "volume BIGINT,"
                + "symbol VARCHAR(5)"
                + ");";
        Database.getInstance().update(createSQL);
    }

    /**
     * while parsing the stock data, this will return the progress in %
     * when parsing has completed it will return a value < 0
     * @return 
     */
    public double getProgress() {
        return ((double) index) / ((double) rows) * 100;
    }
}
