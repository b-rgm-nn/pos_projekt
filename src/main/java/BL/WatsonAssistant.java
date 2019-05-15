package BL;


import Exceptions.UnknownQueryException;
import Enum.Intent;
import Query.MultipleValuesQuery;
import Query.SingleValueQuery;
import Query.Query;
import com.ibm.cloud.sdk.core.service.security.IamOptions;
import com.ibm.watson.assistant.v1.model.MessageInput;
import com.ibm.watson.assistant.v1.model.MessageResponse;
import com.ibm.watson.assistant.v1.Assistant;
import com.ibm.watson.assistant.v1.model.MessageOptions;
import com.ibm.watson.assistant.v1.model.RuntimeIntent;
import java.util.LinkedList;

/**
 * provides the interface between the program and IBM Watson
 * @author Matthias
 */
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

    /**
     * Query IBM Watson for a sentence, determine the intent and extract
     * data like the company
     * @param message the sentence that should be parsed
     * @return a Query object of the appropriate type
     * @throws UnknownQueryException if the returned intent is not recognized
     */
    public Query query(String message) throws UnknownQueryException {
        MessageInput input = new MessageInput();
        input.setText(message);

        MessageOptions options = new MessageOptions.Builder()
                .workspaceId(workspaceId)
                .input(input)   
                .build();
        
        MessageResponse response = assistant.
                message(options).execute().getResult();
        

        RuntimeIntent mostConfidentIntent = null;
        for (RuntimeIntent intent : response.getIntents()) {
            if(mostConfidentIntent == null || intent.getConfidence() > mostConfidentIntent.getConfidence()){
                mostConfidentIntent = intent;
            }
        }
        if(mostConfidentIntent == null)
            throw new UnknownQueryException("No intent found");
        if(mostConfidentIntent.getIntent().equals(Intent.query_single_value.getName())) {
            return new SingleValueQuery(response);
        }
        if(mostConfidentIntent.getIntent().equals(Intent.query_multiple_values.getName())) {
            return new MultipleValuesQuery(response);
        }
        throw new UnknownQueryException("Unknown Intent " + mostConfidentIntent.getIntent());
    }

}
