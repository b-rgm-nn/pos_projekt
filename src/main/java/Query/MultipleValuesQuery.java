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

public class MultipleValuesQuery extends Query {

    private String company;

    public MultipleValuesQuery(MessageResponse messageResponse) throws UnknownQueryException {
        super(messageResponse);
        parseCompany();
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
