package um.si.de4a.resources;


import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import um.si.de4a.util.XMLConverter;

import javax.ws.rs.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

@Path("/generate-invitation")
public class GenerateInvitationResource {

    @POST
    @Consumes("text/plain")
    @Produces("text/plain")
    public String createInvitation(@QueryParam("did")String did) {

        return "";
    }
}
