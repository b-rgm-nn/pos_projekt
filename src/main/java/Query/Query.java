package Query;


import BL.Database;
import BL.LocalDateComparator;
import Enum.Entity;
import Exceptions.UnknownQueryException;
import com.ibm.watson.assistant.v1.model.MessageResponse;
import com.ibm.watson.assistant.v1.model.RuntimeEntity;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

/**
 * Represents a query that was sent to IBM Watson
 * @author Matthias
 */
public class Query implements Serializable {
    protected static DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE;

    protected LocalDate startDate, endDate;
    protected String queryText;

    /**
     * parse the messageResponse Watson returned
     * @param messageResponse the messageResponse Watson returned
     * @throws UnknownQueryException when there's the wrong number of dates
     */
    public Query(MessageResponse messageResponse) throws UnknownQueryException {
        this.queryText = messageResponse.getInput().text();
        parseTimeRange(messageResponse);
    }
    
    /**
     * make sure there's one or two dates in the query and parse them
     * @param response the Watson response
     * @throws UnknownQueryException if no dates or more than 2 dates
     */
    private void parseTimeRange(MessageResponse response) throws UnknownQueryException {
        LinkedList<LocalDate> dates = new LinkedList<>();
        
        for (RuntimeEntity entity : response.getEntities()) {
            if(entity.getEntity().equals(Entity.date.getName())) {
                dates.add(LocalDate.parse(entity.getValue()));
            }
        }
        
        if(dates.size() < 1 || dates.size() > 2)
            throw new UnknownQueryException("wrong number of dates");
        
        dates.sort(new LocalDateComparator());
        
        startDate = dates.get(0);
        endDate = dates.size() == 1 ? startDate : dates.get(1);
    }

    /**
     * get the company name that corresponds to a stock symbol
     * @param companySymbol the symbol of the company
     * @return the name of the company
     * @throws SQLException when something goes wrong
     */
    public static String companyName(String companySymbol) throws SQLException {
        String query = "SELECT name "
                + "FROM companies "
                + "WHERE UPPER(symbol) = UPPER(?);";
        PreparedStatement statement = Database.getInstance().prepareStatement(query);
        statement.setString(1, companySymbol);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.first()) {
            return resultSet.getString("name");
        }
        throw new SQLException();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getQueryText() {
        return queryText;
    }

    @Override
    public String toString() {
        return queryText;
    }
}
