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

/**
 * a query where the user requested the development of a companies stock
 * @author Matthias
 */
public class MultipleValuesQuery extends Query {

    private String company;

    public MultipleValuesQuery(MessageResponse messageResponse) throws UnknownQueryException {
        super(messageResponse);
        parseCompany(messageResponse);
    }

    /**
     * get the values that match the query
     * @return an unordered list of all the values that lie between the two
     * specified dates
     * @throws SQLException when conneciton fails  
     */
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
        resultSet.first();
        if (!resultSet.isFirst()) {
            return values;
        }
        do {
            values.add(new Value(resultSet.getDate("date").toLocalDate(),
                    resultSet.getDouble("low"),
                    resultSet.getDouble("high"),
                    resultSet.getDouble("open"),
                    resultSet.getDouble("close")));
            resultSet.next();
        } while (resultSet.next());
        return values;
    }

    /**
     * parse the company that has been requested by the user
     * @param response
     * @throws UnknownQueryException 
     */
    private void parseCompany(MessageResponse response) throws UnknownQueryException {
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
