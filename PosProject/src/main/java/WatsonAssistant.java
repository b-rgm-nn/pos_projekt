
import com.ibm.cloud.sdk.core.service.security.IamOptions;
import com.ibm.watson.assistant.v1.model.MessageInput;
import com.ibm.watson.assistant.v1.model.MessageResponse;
import com.ibm.watson.assistant.v1.Assistant;
import com.ibm.watson.assistant.v1.model.MessageOptions;
import com.ibm.watson.assistant.v1.model.RuntimeEntity;
import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedList;

public class WatsonAssistant {

    private final IamOptions options = new IamOptions.Builder()
            .apiKey("tiz42FgJ8n9gRvLs7KVu2r0ek-wSh_Eubat8qg7nETps")
            .build();

    private final Assistant assistant = new Assistant("2019-05-07", options);
    
    private String workspaceId = "537c832a-c2fc-4ce9-9277-58286accdba4";
    
    private LinkedList<MessageResponse> responses = new LinkedList<>();

    public WatsonAssistant() {
        assistant.setEndPoint("https://gateway-lon.watsonplatform.net/assistant/api");
    }

    public MessageResponse query(String message) {
        MessageInput input = new MessageInput();
        input.setText(message);

        MessageOptions options = new MessageOptions.Builder()
                .workspaceId(workspaceId)
                .input(input)   
                .build();
        
        MessageResponse response = assistant.message(options).execute().getResult();
        return  response;
    }

    public void getQueryTimeRange(MessageResponse response) throws UnknownQueryException{
        LinkedList<LocalDate> dates = new LinkedList<>();
        
        for (RuntimeEntity entity : response.getEntities()) {
            if(entity.getEntity().equals(Entity.date.getName())) {
                dates.add(LocalDate.parse(entity.getValue()));
            }
        }
        
        if(dates.size() > 2) throw new UnknownQueryException();
        
        dates.sort(new LocalDateComparator());
        
//        return dates.get(0).until(dates.get(1));
    }
}
