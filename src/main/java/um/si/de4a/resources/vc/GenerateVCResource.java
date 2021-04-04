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
    @Path("{userId}/{evidence}")
    public boolean generateVC(@PathParam("userId") String userID, @PathParam("evidence") String evidence) {
        boolean result = false;

        JAXBContext context;

        try {
            context = JAXBContext.newInstance(Credential.class);
            Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
            Credential modelEvidence = (Credential) jaxbUnmarshaller.unmarshal(new StringReader(evidence));

            // TODO: convert to JSON schema, adjust model classes
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