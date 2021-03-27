package um.si.de4a.resources.connection;


import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.ws.rs.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

@Path("/generate-invitation")
public class GenerateInvitationResource {

    @POST
    @Consumes("text/plain")
    @Produces("application/json")
    public JSONObject createInvitation(@QueryParam("userID")String userID) {
        JSONObject jsonInvitation = new JSONObject();

        // call Aries /connections/create-invitation : JSON

        // saveDIDConn(userID, invitationID, invitationJSON, status: invitation_generated): boolean

        return jsonInvitation;
    }
}
