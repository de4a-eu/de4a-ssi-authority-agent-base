package um.si.de4a.resources.vc;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    @Consumes("application/xml")
    @Produces("text/plain")
    public boolean generateVC(@QueryParam("userId")String userID, @QueryParam("evidence") String evidence) {
        boolean result = false;

        JAXBContext context;

        try {
            context = JAXBContext.newInstance(Credential.class);
            Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
            Credential modelEvidence = (Credential) jaxbUnmarshaller.unmarshal(new StringReader(evidence));

            String jsonVerCredential = XMLtoJSONConverter.toJSONObject(modelEvidence);
            System.out.println("[INPUT EVIDENCE] " + jsonVerCredential);
            result = true;
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }

}