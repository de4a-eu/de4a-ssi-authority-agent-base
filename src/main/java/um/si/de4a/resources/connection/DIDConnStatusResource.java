package um.si.de4a.resources.connection;


import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.DIDConn;
import um.si.de4a.db.DIDConnStatusEnum;
import um.si.de4a.util.DE4ALogger;

import javax.ws.rs.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Path("/did-conn-status")
public class DIDConnStatusResource {

    @GET
    @Consumes("application/json")
    @Produces("application/json")
    @Path("{userId}")
    public int fetch(@PathParam("userId") String userID) throws IOException, ParseException {
        Logger logger = DE4ALogger.getLogger();
        LogRecord logRecordInfo = new LogRecord(Level.INFO, "");
        LogRecord logRecordSevere = new LogRecord(Level.SEVERE, "");

        int connectionStatusCode = 0;
        DIDConn userDidConn = null;
        AriesUtil ariesUtil = new AriesUtil();

        // DONE: call database getDIDConn (userID): invitationID, connectionID, status
        DBUtil dbUtil = new DBUtil();
        try {
            userDidConn =  dbUtil.getDIDConnStatus(userID);
        }
        catch(Exception ex){
            logRecordSevere.setMessage("Error accessing data on Authority Agent DT.");
            Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1010"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
            //System.out.println("[DID-CONN-STATUS] Exception: " + ex.getMessage());
        }

        if(userDidConn != null) {
            // DONE: case "status == connection_established": return 1
            if (userDidConn.getStatus() == DIDConnStatusEnum.CONNECTION_ESTABLISHED) {
                logRecordInfo.setMessage("DID Connection has been established.");
                Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1001"};
                logRecordInfo.setParameters(params);
                logger.log(logRecordInfo);

                connectionStatusCode = 1; // return 1 (connection has been established)
            }
            // case "status == invitation_generated":
            else if (userDidConn.getStatus() == DIDConnStatusEnum.INVITATION_GENERATED) {
                // DONE: call Aries /connections: [connection]
                ArrayList<JSONObject> connections = ariesUtil.getConnections();
                if(connections.size() > 0){
                    for (JSONObject conn: connections){
                        //System.out.println("[DID-CONN-STATUS] Connection: " + conn.toString());
                        String connectionID = conn.get("ConnectionID").toString();

                        if (connectionID.equals("")) {
                            connectionStatusCode = 0; // return 0 (Invitation has been generated but not yet accepted)
                        } else {
                            String invitationID = conn.get("InvitationID").toString();
                            //System.out.println("[DID-CONN-STATUS] InvitationID: " + invitationID);

                            if(invitationID.equals(userDidConn.getInvitationId())) {
                                try {
                                    dbUtil.updateDIDConnection(userDidConn.getUserId(), conn.get("MyDID").toString(),
                                            conn.get("TheirDID").toString(), connectionID, DIDConnStatusEnum.CONNECTION_ESTABLISHED);
                                    logRecordInfo.setMessage("Stored current state in the Authority Agent DT database.");
                                    Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01006"};
                                    logRecordInfo.setParameters(params);
                                    logger.log(logRecordInfo);
                                }
                                catch(Exception ex){
                                    logRecordSevere.setMessage("Error saving data on Authority Agent DT.");
                                    Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1010"};
                                    logRecordSevere.setParameters(params);
                                    logger.log(logRecordSevere);
                                }

                                connectionStatusCode = 1;// return 1 (Connection has been established)

                                logRecordInfo.setMessage("DID Connection has been established.");
                                Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01009"};
                                logRecordInfo.setParameters(params);
                                logger.log(logRecordInfo);
                            }
                        }
                    }
                }
            }
        }
        else
            return -1; // return -1 (Invitation has never been generated)

        return connectionStatusCode;
    }
}
