package um.si.de4a.resources;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import um.si.de4a.util.XMLConverter;

import javax.ws.rs.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

@Path("/update-did")
public class UpdateDIDResource {

    @PUT
    @Consumes("text/plain")
    @Produces("text/plain")
    public boolean updateDID(@QueryParam("subject")String subject) {
        return true;
    }
}
