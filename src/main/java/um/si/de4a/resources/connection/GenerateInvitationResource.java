package um.si.de4a.resources.connection;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.DIDConn;
import um.si.de4a.util.CustomDE4ALogFormatter;
import um.si.de4a.util.DE4ALogger;

import javax.ws.rs.*;
import java.io.IOException;
import java.util.List;
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
            logRecordInfo.setMessage("Received input eIDAS user data.");
            Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01001"};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);

        } catch (ParseException e) {
            logRecordSevere.setMessage("Error parsing input eIDAS data.");
            Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1001"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);

            e.printStackTrace();
        }

        JSONObject invitation = null;
        if(jsonUserID != null) {
            String userID = "";
            try {
                userID = jsonUserID.get("userId").toString();
            }
            catch(Exception ex){
                logRecordSevere.setMessage("Error parsing input parameters.");
                Object[] params = new Object[]{"Authority Agent DT", "Evidence Portal DO", "1005"};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            // DONE: call Aries /connections/create-invitation : JSON
            AriesUtil aries = new AriesUtil();
            invitation = aries.generateInvitation();

            if (invitation != null) {
                try {
                    invitationJson = invitation.get("invitation").toString();
                }
                catch(Exception ex){
                    logRecordSevere.setMessage("Error parsing received JSON response.");
                    Object[] params = new Object[]{"Authority Agent DT", "Evidence Portal DO", "1005"};
                    logRecordSevere.setParameters(params);
                    logger.log(logRecordSevere);
                }
                JSONObject jsonObjectInv = (JSONObject) jsonParser.parse(invitationJson);

                long currentTime = System.currentTimeMillis();

                // DONE: saveDIDConn(userID, invitationID, invitationJSON, status: invitation_generated): boolean
                DBUtil dbUtil = new DBUtil();
                try {
                    System.out.println("GENERATE-INVITATION: Current DIDConn: " + dbUtil.getCurrentDIDConnStatus(userID));

                    if(dbUtil.getCurrentDIDConnStatus(userID) != null){ // there is an existing DIDConn
                        System.out.println("GENERATE-INVITATION: Current DIDConn Invitation ID: " + dbUtil.getCurrentDIDConnStatus(userID).getInvitationId());

                        List<DIDConn> didConnList = dbUtil.getDIDConnStatuses(userID);
                        for (DIDConn didConn: didConnList){
                            if(didConn.getTimeDeleted() == -1){
                                dbUtil.updateOldDIDConnections(userID, didConn.getMyDID(), didConn.getTheirDID(), didConn.getConnectionId(), didConn.getStatus());
                            }
                        }
                        //dbStored = dbUtil.saveDIDConn(userID, jsonObjectInv.get("@id").toString(), invitation.toString(), currentTime);
                    }

                    dbStored = dbUtil.saveDIDConn(userID, jsonObjectInv.get("@id").toString(), invitation.toString(), currentTime);
                    logRecordInfo.setMessage("Stored current state in the Authority Agent DT database.");
                    Object[] params = new Object[]{"Authority Agent DT", "Evidence Portal", "01006"};
                    logRecordInfo.setParameters(params);
                    logger.log(logRecordInfo);
                } catch (Exception ex) {
                    logRecordSevere.setMessage( "Error storing current state in internal database.");
                    Object[] params = new Object[]{"Authority Agent DT", "Evidence Portal", "1006"};
                    logRecordSevere.setParameters(params);
                    logger.log(logRecordSevere);
                    //System.out.println("[GENERATE-INVITATION] Exception: " + ex.getMessage());
                }
            }
        }
        return invitation.toString();
    }
}
