package um.si.de4a.resources.connection;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.util.CustomDE4ALogFormatter;
import um.si.de4a.util.DE4ALogger;

import javax.ws.rs.*;
import java.io.IOException;
import java.util.logging.*;

@Path("/generate-invitation")
public class GenerateInvitationResource {

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public String createInvitation(String user) throws IOException, ParseException {

        Logger logger = DE4ALogger.getLogger();
        LogRecord logRecordInfo = new LogRecord(Level.INFO, "");
        LogRecord logRecordSevere = new LogRecord(Level.SEVERE, "");

        boolean dbStored= false;
        String invitationJson = "";
        JSONObject jsonUserID = null;

        JSONParser jsonParser = new JSONParser();
        try {
            jsonUserID = (JSONObject) jsonParser.parse(user);
            logRecordInfo.setMessage("GENERATE-INVITATION: Received input eIDAS user data.");
            Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "0201"};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);

        } catch (ParseException e) {
            logRecordSevere.setMessage("GENERATE-INVITATION: Object conversion error on Authority Agent DT.");
            Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "20005"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);

            e.printStackTrace();
        }

        JSONObject invitation = null;
        if(jsonUserID != null) {
            String userID = "";
            try {
                userID = jsonUserID.get("userId").toString();
                logRecordInfo.setMessage("GENERATE-INVITATION: Received input userId data.");
                Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "0202"};
                logRecordInfo.setParameters(params);
                logger.log(logRecordInfo);

            }
            catch(Exception ex){
                logRecordSevere.setMessage("GENERATE-INVITATION: Object conversion error on Authority Agent DT.");
                Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "20005"};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            // DONE: call Aries /connections/create-invitation : JSON
            AriesUtil aries = new AriesUtil();
            invitation = aries.generateInvitation();

            if (invitation != null) {
                try {
                    invitationJson = invitation.get("invitation").toString();
                    logRecordInfo.setMessage("GENERATE-INVITATION: Processing the JSON response received from /generate-invitation.");
                    Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "0102"};
                    logRecordInfo.setParameters(params);
                    logger.log(logRecordInfo);
                }
                catch(Exception ex){
                    logRecordSevere.setMessage("GENERATE-INVITATION: Object conversion error on Authority Agent DT.");
                    Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "20005"};
                    logRecordSevere.setParameters(params);
                    logger.log(logRecordSevere);
                }
                JSONObject jsonObjectInv = (JSONObject) jsonParser.parse(invitationJson);

                long currentTime = System.currentTimeMillis();

                // DONE: saveDIDConn(userID, invitationID, invitationJSON, status: invitation_generated): boolean
                DBUtil dbUtil = new DBUtil();
                try {
                    dbStored = dbUtil.saveDIDConn(userID, jsonObjectInv.get("@id").toString(), invitation.toString(), currentTime);
                    logRecordInfo.setMessage("GENERATE-INVITATION: Stored current state in the Authority Agent DT internal database.");
                    Object[] params = new Object[]{"Authority Agent DT", "Evidence Portal", "0103"};
                    logRecordInfo.setParameters(params);
                    logger.log(logRecordInfo);
                } catch (Exception ex) {
                    logRecordSevere.setMessage( "GENERATE-INVITATION: Error saving data on Authority Agent DT.");
                    Object[] params = new Object[]{"Authority Agent DT", "Evidence Portal DO", "20006"};
                    logRecordSevere.setParameters(params);
                    logger.log(logRecordSevere);
                    //System.out.println("[GENERATE-INVITATION] Exception: " + ex.getMessage());
                }
            }
        }
        return invitation.toString();
    }
}
