package um.si.de4a.resources.connection;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;

import javax.ws.rs.*;
import java.io.IOException;

@Path("/generate-invitation")
public class GenerateInvitationResource {

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public String createInvitation(String user) throws IOException, ParseException {
        boolean dbStored= false;
        String invitationJson = "";
        JSONObject jsonUserID = null;

        JSONParser jsonParser = new JSONParser();
        try {
            jsonUserID = (JSONObject) jsonParser.parse(user);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(jsonUserID != null) {
            System.out.println("user ID: " + jsonUserID.get("userId"));

            String userID = jsonUserID.get("userId").toString();

            // DONE: call Aries /connections/create-invitation : JSON
            AriesUtil aries = new AriesUtil();
            JSONObject invitation = aries.generateInvitation();

            if (invitation != null) {
                invitationJson = invitation.toJSONString();
                long currentTime = System.currentTimeMillis();

                // DONE: saveDIDConn(userID, invitationID, invitationJSON, status: invitation_generated): boolean
                DBUtil dbUtil = new DBUtil();
                try {
                    dbStored = dbUtil.saveDIDConn(userID, invitation.get("@id").toString(), invitation.toString(), currentTime);
                } catch (Exception ex) {
                    System.out.println("[GENERATE-INVITATION] Exception: " + ex.getMessage());
                }
            }
        }
        return invitationJson;
    }
}
