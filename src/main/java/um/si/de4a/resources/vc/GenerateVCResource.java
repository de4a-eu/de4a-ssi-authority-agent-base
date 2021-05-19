package um.si.de4a.resources.vc;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.DIDConn;
import um.si.de4a.model.xml.HigherEducationDiploma;
import um.si.de4a.util.XMLtoJSONAdapter;

import javax.ws.rs.*;
import javax.xml.bind.JAXBContext;
import java.io.IOException;
import java.net.MalformedURLException;

@Path("/generate-vc")
public class GenerateVCResource {
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public boolean generateVC(String vcData) throws IOException {
        boolean result = false;

        AriesUtil ariesUtil = new AriesUtil();

        String evidenceString = "";
        JSONObject jsonObject = null;

        JSONParser jsonParser = new JSONParser();
        try {
            jsonObject = (JSONObject) jsonParser.parse(vcData);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(jsonObject != null) {

            System.out.println("evidence: " + jsonObject.get("evidence"));
            HigherEducationDiploma diploma = XMLtoJSONAdapter.convertXMLToPOJO(jsonObject.get("evidence").toString());

            if (diploma != null) {
                String outputJson = XMLtoJSONAdapter.convertPOJOtoJSON(diploma, jsonObject.get("myDID").toString());
                System.out.println("VC: " + outputJson);
                result = true;
            }
        }
        /*try {
            context = JAXBContext.newInstance(Credential.class);
            Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
            Credential modelEvidence = (Credential) jaxbUnmarshaller.unmarshal(new StringReader(jsonVC.get("evidence").toString()));

            // TODO: convert to JSON schema, adjust model classes
            String jsonVerCredential = XMLtoJSONConverter.toJSONObject(modelEvidence);
            System.out.println("[INPUT EVIDENCE] " + jsonVerCredential);
            result = true;
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }*/

        return result;
    }

}