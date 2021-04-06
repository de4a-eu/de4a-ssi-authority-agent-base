package um.si.de4a.resources.connection;


import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;

import javax.ws.rs.*;
import java.io.IOException;

@Path("/generate-invitation")
public class GenerateInvitationResource {

    @POST
    @Consumes("text/plain")
    @Produces("text/plain")
    public String createInvitation(@QueryParam("userId") String userID) throws IOException, ParseException {
        boolean dbStored= false;

        // DONE: call Aries /connections/create-invitation : JSON
        AriesUtil aries = new AriesUtil();
        JSONObject invitation = aries.generateInvitation();

        long currentTime = System.currentTimeMillis();

        // DONE: saveDIDConn(userID, invitationID, invitationJSON, status: invitation_generated): boolean
        DBUtil dbUtil = new DBUtil();
        try {
            dbStored = dbUtil.saveDIDConn(userID, invitation.get("@id").toString(), invitation.toJSONString(), currentTime);
        }catch (Exception ex){
            System.out.println("[GENERATE-INVITATION] Exception: " + ex.getMessage());
        }

        return invitation.toJSONString();
    }
}
