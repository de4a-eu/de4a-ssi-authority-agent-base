package um.si.de4a.resources.vc;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.model.json.VerifiableCredential;
import um.si.de4a.model.xml.HigherEducationDiploma;
import um.si.de4a.util.XMLtoJSONAdapter;

import javax.ws.rs.*;
import java.io.IOException;

@Path("/generate-vc")
public class GenerateVCResource {
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public VerifiableCredential generateVC(String vcData) throws IOException, ParseException, java.text.ParseException {

        VerifiableCredential evidenceVC = null;
        JSONObject jsonObject = null;

        JSONParser jsonParser = new JSONParser();
        try {
            jsonObject = (JSONObject) jsonParser.parse(vcData);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(jsonObject != null) {

            HigherEducationDiploma diploma = XMLtoJSONAdapter.convertXMLToPOJO(jsonObject.get("evidence").toString());

            if (diploma != null) {
                evidenceVC = XMLtoJSONAdapter.convertPOJOtoJSON(diploma, jsonObject.get("myDID").toString());
            }
        }

        return evidenceVC;
    }

}