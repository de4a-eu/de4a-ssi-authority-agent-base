package um.si.de4a.resources.connection;


import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import um.si.de4a.db.DBUtil;

import javax.ws.rs.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.net.MalformedURLException;

@Path("/generate-invitation")
public class GenerateInvitationResource {

    @POST
    @Consumes("text/plain")
    @Produces("text/plain")
    //@Produces("application/json")
    public boolean createInvitation(@QueryParam("userId")String userID) throws MalformedURLException {
        // JSONObject jsonInvitation = new JSONObject();
        boolean result = false;
        // call Aries /connections/create-invitation : JSON

        // saveDIDConn(userID, invitationID, invitationJSON, status: invitation_generated): boolean
        DBUtil dbUtil = new DBUtil();
        result = dbUtil.saveDIDConn(userID, "inv1", "json1");


        return result;
    }
}
