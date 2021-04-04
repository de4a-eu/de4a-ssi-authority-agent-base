package um.si.de4a.resources.connection;


import um.si.de4a.db.DBUtil;
import um.si.de4a.db.DIDConn;
import um.si.de4a.db.DIDConnStatusEnum;
import um.si.de4a.db.VCStatusEnum;

import javax.ws.rs.*;
import java.net.MalformedURLException;
import java.util.List;

@Path("/did-conn-status")
public class DIDConnStatusResource {

    @GET
    @Consumes("text/plain")
    @Produces("text/plain")
    @Path("{userId}")
    public int fetch(@PathParam("userId") String userID) throws MalformedURLException {
        int connectionStatusCode = 0;
        DIDConn userDidConn = null;

        // DONE: call database getDIDConn (userID): invitationID, connectionID, status
        DBUtil dbUtil = new DBUtil();
        try {
            List<DIDConn> userDIDConnList = dbUtil.getDIDConn(userID);
            userDidConn = userDIDConnList.get(userDIDConnList.size()-1); //TODO: which user's DID invitation to get? - lastTimeChanged
            System.out.println("[DID-CONN-STATUS] Last status change: " + userDidConn.getTimeUpdated());
        }
        catch(Exception ex){
            System.out.println("[DID-CONN-STATUS] Exception: " + ex.getMessage());
        }

        if(userDidConn != null) {
            // DONE: if (invitationID == null): return -1 (Invitation has never been generated)
            if (userDidConn.getInvitationId() == null)
                connectionStatusCode = -1;
            else {
                // DONE: case "status == connection_established": return 1
                if (userDidConn.getStatus() == DIDConnStatusEnum.CONNECTION_ESTABLISHED) {
                    connectionStatusCode = 1;
                }
                // case "status == invitation_generated":
                else if (userDidConn.getStatus() == DIDConnStatusEnum.INVITATION_GENERATED) {
                    // TODO: call Aries /connections: [connection]

                    // DONE: if (connectionID == null): return 0 (Invitation has been generated but not yet accepted)
                    if (userDidConn.getConnectionId() == null) {
                        connectionStatusCode = 0;
                    }
                    else{
                        // TODO: call Aries /connections/{connectionID}/accept-request: null/empty

                        // DONE: call database updateDIDConnection(userID, myDID, theirDID, connectionID, status: connection_established): boolean
                        dbUtil.updateDIDConnection(userDidConn.getUserId(), "mydid", "theirdid", "conn1", DIDConnStatusEnum.CONNECTION_ESTABLISHED);
                        connectionStatusCode = 1;// return 1 (Connection has been established)
                    }

                }
            }
        }

        return connectionStatusCode;
    }
}
