package Query;

import BL.Value;
import BL.Database;
import Enum.Entity;
import Exceptions.NoDataFoundException;
import Exceptions.UnknownQueryException;
import com.ibm.watson.assistant.v1.model.MessageResponse;
import com.ibm.watson.assistant.v1.model.RuntimeEntity;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SingleValueQuery extends Query {

    private String company;

    public SingleValueQuery(MessageResponse messageResponse) throws UnknownQueryException {
        super(messageResponse);
        parseCompany();
    }

    /**
     * query the open, close, high and low values for the specified company and
     * timeframe
     *
     * @return list of values, ordered: open, high, low, close
     * @throws NoDataFoundException if the specified company has no values
     * during the timeframe
     * @throws SQLException
     */
    public Value queryValue() throws NoDataFoundException, SQLException {
        String highLowQuery = "SELECT MAX(high), MIN(low) "
                + "FROM data "
                + "WHERE symbol = ? "
                + "     AND date BETWEEN ? AND ? ";
        PreparedStatement highLowStatement = Database.getInstance().prepareStatement(highLowQuery);
        highLowStatement.setString(1, company);
        highLowStatement.setDate(2, Date.valueOf(startDate));
        highLowStatement.setDate(3, Date.valueOf(endDate));

        ResultSet highLowResultSet = highLowStatement.executeQuery();
        if (!highLowResultSet.first()) {
            throw new NoDataFoundException();
        }

        String openCloseQuery = "SELECT open, close "
                + "FROM data "
                + "WHERE symbol = ? "
                + "     AND date BETWEEN ? AND ? "
                + "ORDER BY date";
        PreparedStatement openCloseStatement = Database.getInstance().prepareStatement(openCloseQuery);
        openCloseStatement.setString(1, company);
        openCloseStatement.setDate(2, Date.valueOf(startDate));
        openCloseStatement.setDate(3, Date.valueOf(endDate));

        ResultSet openCloseResultSet = openCloseStatement.executeQuery();
        openCloseResultSet.first();
        double open = openCloseResultSet.getDouble("open");
        openCloseResultSet.last();
        double close = openCloseResultSet.getDouble("close");
        return new Value(startDate,
                highLowResultSet.getDouble(2),
                highLowResultSet.getDouble(1),
                open,
                close);
    }

    public List<Value> queryValues() throws SQLException {
        String query = "SELECT *"
                + "FROM data "
                + "WHERE symbol = ? "
                + "     AND date BETWEEN ? AND ?;";
        PreparedStatement statement = Database.getInstance().prepareStatement(query);
        statement.setString(1, company);
        statement.setDate(2, Date.valueOf(startDate));
        statement.setDate(3, Date.valueOf(endDate));

        List<Value> values = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();
        do {
            values.add(new Value(startDate,
                    resultSet.getDouble("low"),
                    resultSet.getDouble("high"),
                    resultSet.getDouble("open"),
                    resultSet.getDouble("close")));
        } while (resultSet.next());
        return values;
    }

    private void parseCompany() throws UnknownQueryException {
        for (RuntimeEntity entity : response.getEntities()) {
            if (entity.getEntity().equals(Entity.company.getName())) {
                company = entity.getValue();
                return;
            }
        }
        throw new UnknownQueryException("company not recognized");
    }

    public String getCompany() {
        return company;
    }
}
