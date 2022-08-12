package um.si.de4a.resources.connection;


import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import um.si.de4a.AppConfig;
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

    private AppConfig appConfig = null;

    @GET
    @Consumes("application/json")
    @Produces("application/json")
    @Path("{userId}")
    public int fetch(@PathParam("userId") String userID) throws IOException, ParseException {
        Logger logger = DE4ALogger.getLogger();
        LogRecord logRecordInfo = new LogRecord(Level.INFO, "");
        LogRecord logRecordSevere = new LogRecord(Level.SEVERE, "");

        String alias = "";
        appConfig = new AppConfig();
        try {
            alias = appConfig.getProperties().getProperty("alias");
        }
        catch (Exception ex){
            logRecordSevere.setMessage( "Configuration error occurred on Authority Agent.");
            Object[] params = new Object[]{"AAE09", alias};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

        int connectionStatusCode = 0;
        DIDConn userDidConn = null;
        AriesUtil ariesUtil = new AriesUtil();

        // DONE: call database getDIDConn (userID): invitationID, connectionID, status
        DBUtil dbUtil = new DBUtil();
        try {
            userDidConn =  dbUtil.getCurrentDIDConnStatus(userID);
            logRecordInfo.setMessage("DID-CONN-STATUS: Received user DIDConn status data.");
            Object[] params = new Object[]{"AAI14", alias};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);
        }
        catch(Exception ex){
            logRecordSevere.setMessage("Error accessing data on Authority Agent internal database: [DID-CONN-STATUS] " + ex.getMessage() + ".");
            Object[] params = new Object[]{"AAE04", alias};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
            //System.out.println("[DID-CONN-STATUS] Exception: " + ex.getMessage() + ".");
        }

        if(userDidConn != null) {
            //System.out.println("DID-CONN-STATUS: Current user DIDConn connection ID: " + userDidConn.getConnectionId());
            // DONE: case "status == connection_established": return 1
            if (userDidConn.getStatus() == DIDConnStatusEnum.CONNECTION_ESTABLISHED) {
                logRecordInfo.setMessage("DID-CONN-STATUS: DID Connection has been established for invitation ID " + userDidConn.getInvitationId() + ".");
                Object[] params = new Object[]{"AAI29", alias};
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
                                    dbUtil.updateDIDConnectionStatus(userDidConn.getUserId(), conn.get("MyDID").toString(),
                                            conn.get("TheirDID").toString(), connectionID, DIDConnStatusEnum.CONNECTION_ESTABLISHED);
                                    logRecordInfo.setMessage("DID-CONN-STATUS: Stored current state in Authority Agent internal database.");
                                    Object[] params = new Object[]{"AAI13", alias};
                                    logRecordInfo.setParameters(params);
                                    logger.log(logRecordInfo);
                                }
                                catch(Exception ex){
                                    logRecordSevere.setMessage( "Error saving data on Authority Agent internal database: [DID-CONN-STATUS] " + ex.getMessage() + ".");
                                    Object[] params = new Object[]{"AAE04", alias};
                                    logRecordSevere.setParameters(params);
                                    logger.log(logRecordSevere);
                                }

                                connectionStatusCode = 1;// return 1 (Connection has been established)

                                logRecordInfo.setMessage("DID-CONN-STATUS: DID Connection has been established for invitation ID " + userDidConn.getInvitationId() + ".");
                                Object[] params = new Object[]{"AAI29", alias};
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
