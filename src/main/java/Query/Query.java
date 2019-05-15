package Query;


import BL.LocalDateComparator;
import Enum.Entity;
import Exceptions.UnknownQueryException;
import com.ibm.watson.assistant.v1.model.MessageResponse;
import com.ibm.watson.assistant.v1.model.RuntimeEntity;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;


public class Query implements Serializable {
    protected static DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE;
    protected LocalDate startDate, endDate;
    protected String queryText;

    public Query(MessageResponse messageResponse) throws UnknownQueryException {
        this.queryText = messageResponse.getInput().text();
        parseTimeRange(messageResponse);
    }
    
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
