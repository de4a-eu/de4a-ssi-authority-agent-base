package um.si.de4a.resources.vc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.model.xml.Credential;
import um.si.de4a.util.XMLtoJSONConverter;

import javax.ws.rs.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

@Path("/generate-vc")
public class GenerateVCResource {
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public boolean generateVC(String vcData) {
        boolean result = true;
        JSONObject jsonVC = null;
        JAXBContext context;

        JSONParser jsonParser = new JSONParser();
        try {
            jsonVC = (JSONObject) jsonParser.parse(vcData);
        } catch (ParseException e) {
            e.printStackTrace();
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