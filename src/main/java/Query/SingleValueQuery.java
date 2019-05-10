package Query;


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
import java.util.LinkedList;
import java.util.List;


public class SingleValueQuery extends Query{
    private String company;

    public SingleValueQuery(MessageResponse messageResponse) throws UnknownQueryException {
        super(messageResponse);
        parseCompany();
    }
    
    /**
     * query the open, close, high and low values for the specified company and timeframe
     * @return list of values, ordered: open, high, low, close
     * @throws NoDataFoundException if the specified company has no values during the timeframe
     * @throws SQLException 
     */
    public List<Double> queryValues() throws NoDataFoundException, SQLException {
        String query = "SELECT *"
                    + "FROM data "
                    + "WHERE symbol = ? "
                    + "     AND date BETWEEN ? AND ?;";
        PreparedStatement statement = Database.getInstance().prepareStatement(query);
        statement.setString(1, company);
        statement.setDate(2, Date.valueOf(startDate));
        statement.setDate(3, Date.valueOf(endDate));
        
        ResultSet resultSet = statement.executeQuery();
        if(!resultSet.first()) {
            throw new NoDataFoundException();
        }
        List<Double> result = new LinkedList<>();
        result.add(resultSet.getDouble("open"));
        result.add(resultSet.getDouble("high"));
        result.add(resultSet.getDouble("low"));
        result.add(resultSet.getDouble("close"));
        statement.close();
        return result;
    }
    
    private void parseCompany() throws UnknownQueryException {
        for (RuntimeEntity entity : response.getEntities()) {
            if(entity.getEntity().equals(Entity.company.getName())) {
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
