package um.si.de4a.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import um.si.de4a.model.json.Invitation;

public class ObjectToJSONConverter {

    public static String getJsonObject(Invitation invitation){
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(invitation);
            System.out.println("[JSON Invitation] " + jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
