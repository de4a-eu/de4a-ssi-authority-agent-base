package um.si.de4a.resources.connection;


import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import um.si.de4a.db.DBUtil;
import um.si.de4a.model.json.Invitation;
import um.si.de4a.util.ObjectToJSONConverter;

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
    @Path("{userId}")
    public String createInvitation(@PathParam("userId") String userID) throws MalformedURLException {
        String jsonInvitation = "";
        boolean dbStored= false;

        // TODO: call Aries /connections/create-invitation : JSON
        Invitation invitation = new Invitation("id1", "endpoint1",
                new String[]{"key1", "key2"}, "de4a-invitation", "invitation");
        long currentTime = System.currentTimeMillis();
        invitation.setStatusChangedTime(currentTime);
        try {
            jsonInvitation = ObjectToJSONConverter.getJsonObject(invitation);
        }catch (Exception ex){
            System.out.println("[GENERATE-INVITATION] Exception: " + ex.getMessage());
        }

        // DONE: saveDIDConn(userID, invitationID, invitationJSON, status: invitation_generated): boolean
        DBUtil dbUtil = new DBUtil();
        try {
            dbStored = dbUtil.saveDIDConn(userID, "inv1", jsonInvitation, currentTime);
        }catch (Exception ex){
            System.out.println("[GENERATE-INVITATION] Exception: " + ex.getMessage());
        }

        return jsonInvitation;
    }
}
